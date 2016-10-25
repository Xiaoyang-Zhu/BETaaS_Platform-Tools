/**

Copyright 2014 ATOS SPAIN S.A.

Licensed under the Apache License, Version 2.0 (the License);
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Authors :
Francisco Javier Nieto. Atos Research and Innovation, Atos SPAIN SA
@email francisco.nieto@atos.net 
Sergio García Villalonga. Atos Research and Innovation, Atos SPAIN SA
@email sergio.garciavillalonga@atos.net
**/

package eu.betaas.taas.taasvmmanager.configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;

import com.sun.jersey.core.osgi.OsgiRegistry;

import eu.betaas.taas.taasvmmanager.api.datamodel.Flavor;
import eu.betaas.taas.taasvmmanager.api.datamodel.Flavor.FlavorType;

public class TaaSVMMAnagerConfiguration {

	public static enum SystemArchitecture {INTEL, ARM};
	private static Logger logger = Logger.getLogger("betaas.taas");
	
	private static String gwId;
	
	/******** FlavorParameters ********/
	//TODO set correct parameters
	private static final int  CPUTINY      = 1;
	private static final int  CPUSMALL     = 1;
	private static final int  CPUSTANDARD  = 2;
	private static final long MEMTINY      = 384;
	private static final long MEMSMALL     = 512;
	private static final long MEMSTANDARD  = 1024;
	private static final long DISKTINY     = 0;
	private static final long DISKSMALL    = 0;
	private static final long DISKSTANDARD = 0;
	/**********************************/
	
	private static SystemArchitecture systemArchitecture;
	
	private static String vmmPath;
	private static String vmmArmPath;
	private static String customKernelPath;
	private static String customDtbPath;
	private static String baseImagesPath;
	private static String baseAppImagePath;
	private static String baseGatewayImagePath;
	private static String baseBigDataImagePath;
	private static String baseStorageImagePath;
	private static String instantiatedImagesPath;
	private static String baseImagesURL;
	
	private static String keystoneEndpoint;
	private static String novaEndpoint;
	private static String cinderEndpoint;
	private static String neutronEndpoint;
	private static String openStackTenant;
	private static String openStackUser;
	private static String openStackPass;
	
	/* Structure that contains the information about the clouds.
	 * The key corresponds to its unique URL and the values are
	 * stored as follows:
	 * 
	 * Key -> [type, user, password]
	 * 
	 **/
	private static HashMap<String, String[]> clouds = null;
	
	public static void setupService () {
		logger.info("[TaaSVMMAnagerConfiguration] Starting configuration loading...");
		if (System.getProperty("os.arch").equals("amd64")) {
			systemArchitecture = SystemArchitecture.INTEL;
		} else {
			systemArchitecture = SystemArchitecture.ARM;
		}
		
		logger.info("vmImagesPath    = " + vmmPath);
		logger.info("openStackHost   = " + keystoneEndpoint);
		logger.info("novaEndpoint    = " + novaEndpoint);
		logger.info("neutronEndpoint = " + neutronEndpoint);
		logger.info("openStackHost   = " + keystoneEndpoint);
		logger.info("openStackUser   = " + openStackUser);
		
		createPaths();
		//loadClouds();
		//downloadBaseImages();
		logger.info("[TaaSVMMAnagerConfiguration] Configuration loaded...");
	}
	
	private static void createPaths() {
		if (systemArchitecture == SystemArchitecture.ARM) {
			vmmArmPath = vmmPath + "/arm";
			customKernelPath = vmmArmPath + "/vexpress-zImage";
			customDtbPath    = vmmArmPath + "/vexpress-v2p-ca15-tc1.dtb";
		} else {
			customKernelPath = "";
			customDtbPath    = "";
		}
		
		baseImagesPath = vmmPath + "/base";
		baseAppImagePath = baseImagesPath + "/app.img";
		baseGatewayImagePath = baseImagesPath + "/gateway.img";
		baseBigDataImagePath = baseImagesPath + "/bigdata.img";
		baseStorageImagePath = baseImagesPath + "/storage.img";
		instantiatedImagesPath =  vmmPath + "/instantiated";
		
		logger.info("Cheking configuration directory structure.");
		
		File f = new File(vmmPath);
		if (!f.isDirectory()) {
			logger.warn("BETaaS VMManager images directory does not exist. Creating...");			
			if (f.mkdir()) {
				logger.info("BETaaS VMManager  image directory created successfully.");				
			}
		} else {
			logger.info("BETaaS VMManager base image directory exists.");			
		}
		
		f = new File(baseImagesPath);
		if (!f.isDirectory()) {
			logger.warn("BETaaS VMManager base images directory does not exist. Creating...");			
			if (f.mkdir()) {
				logger.info("BETaaS VMManager base image directory created successfully.");				
			}
		} else {
			logger.info("BETaaS VMManager base image directory exists.");			
		}
		
		f = new File(instantiatedImagesPath);
		if (!f.isDirectory()) {
			logger.warn("BETaaS VMManager instantiated images directory does not exist. Creating...");			
			if (f.mkdir()) {
				logger.info("BETaaS VMManager instantiated image directory created successfully.");				
			}
		} else {
			logger.info("BETaaS VMManager base image directory exists.");			
		}
		
		if (systemArchitecture == SystemArchitecture.ARM) {
			f = new File(vmmArmPath);
			if (!f.isDirectory()) {
				logger.warn("BETaaS VMManager custom ARM directory does not exist. Creating...");			
				if (f.mkdir()) {
					logger.info("BETaaS VMManager custom ARM directory created successfully.");				
				}
			} else {
				logger.info("BETaaS VMManager custom ARM directory exists.");			
			}
		}
	}
	
	private static void downloadBaseImages () throws Exception{
		URL website;
		ReadableByteChannel rbc;
		FileOutputStream fos;
		String arch = System.getProperty("os.arch");
		
		if (!arch.equals("amd64") || !arch.equals("arm") /*TODO check*/ ) {
			throw new Exception(
					"System architecture not supported for virtualization");
		}
		
		String finalImagesURL = baseImagesURL + "-" + arch;
		logger.info("Downloading default VM images.");		
		try {
			website = new URL(finalImagesURL);
			rbc = Channels.newChannel(website.openStream());
			fos = new FileOutputStream(baseImagesPath);
			logger.info("Downloading base image from " + baseImagesURL);			
			//new DownloadUpdater(baseComputeImageX86Path, baseImagesURL).start();
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		} catch (MalformedURLException e) {			
			logger.error("Error downloading images: bad URL.");			
			logger.error(e.getMessage());			
		} catch (IOException e) {			
			logger.error("Error downloading images: IO exception.");			
			logger.error(e.getMessage());			
		}
		logger.info("Completed!");
	}
	
	private static void loadClouds () {
		int   iter;
		String type;
		String url;
		String user;
		String pass;
		String[] cloudData;
		
		if (clouds == null) {
			clouds = new HashMap<String, String[]>();
			
			try {
				XMLConfiguration config = new XMLConfiguration("vmmanager.conf");
				
				iter = 0;
				while (config.getProperty("clouds.cloud(" + iter + ")") != null) {
					type = config.getString("clouds.cloud(" + iter + ")[@type]");
					url  = config.getString("clouds.cloud(" + iter + ").url");
					user = config.getString("clouds.cloud(" + iter + ").user");
					pass = config.getString("clouds.cloud(" + iter++ + ").password");
					
					cloudData = new String[3];
					cloudData[0] = type;
					cloudData[1] = user;
					cloudData[2] = pass;
					
					clouds.put(url, cloudData);
				}
			} catch (ConfigurationException e) {				
				logger.error(e.getMessage());
			}
		}
	}
	
	public void setGwId(String gwId) {
		TaaSVMMAnagerConfiguration.gwId = gwId;
	}

	public void setVmmPath(String vmImagesPath) {
		TaaSVMMAnagerConfiguration.vmmPath = vmImagesPath;
	}
	
	public void setKeystoneEndpoint(String keystoneEndpoint) {
		TaaSVMMAnagerConfiguration.keystoneEndpoint = keystoneEndpoint;
	}

	public void setNovaEndpoint(String novaEndpoint) {
		TaaSVMMAnagerConfiguration.novaEndpoint = novaEndpoint;
	}

	public void setCinderEndpoint(String cinderEndpoint) {
		TaaSVMMAnagerConfiguration.cinderEndpoint = cinderEndpoint;
	}

	public void setNeutronEndpoint(String neutronEndpoint) {
		TaaSVMMAnagerConfiguration.neutronEndpoint = neutronEndpoint;
	}

	public void setOpenStackTenant(String openStackTenant) {
		TaaSVMMAnagerConfiguration.openStackTenant = openStackTenant;
	}

	public void setOpenStackUser(String openStackUser) {
		TaaSVMMAnagerConfiguration.openStackUser = openStackUser;
	}

	public void setOpenStackPass(String openStackPass) {
		TaaSVMMAnagerConfiguration.openStackPass = openStackPass;
	}

	public static String getGwId() {
		return TaaSVMMAnagerConfiguration.gwId;
	}

	public static HashMap<String, String[]> getClouds () {
		return clouds;
	}

	public static String getVmmPath() {
		return TaaSVMMAnagerConfiguration.vmmPath;
	}

	public static String getBaseImagesPath() {
		return baseImagesPath;
	}

	public static String getBaseAppImagePath() {
		return baseAppImagePath;
	}

	public static String getBaseGatewayImagePath() {
		return baseGatewayImagePath;
	}

	public static String getBaseBigDataImagePath() {
		return baseBigDataImagePath;
	}
	
	public static String getBaseStorageImagePath() {
		return baseStorageImagePath;
	}

	public static String getInstantiatedImagesPath() {
		return instantiatedImagesPath;
	}

	public static String getBaseImagesURL() {
		return baseImagesURL;
	}
	
	public static String getKeystoneEndpoint() {
		return keystoneEndpoint;
	}

	public static String getCinderEndpoint() {
		return cinderEndpoint;
	}

	public static String getNovaEndpoint() {
		return novaEndpoint;
	}

	public static String getNeutronEndpoint() {
		return neutronEndpoint;
	}

	public static String getOpenStackTenant() {
		return openStackTenant;
	}
	
	public static String getOpenStackUser() {
		return openStackUser;
	}

	public static String getOpenStackPass() {
		return openStackPass;
	}

	public static SystemArchitecture getSystemArchitecture() {
		return systemArchitecture;
	}

	public static String getCustomKernelPath() {
		return customKernelPath;
	}

	public static String getCustomDtbPath() {
		return customDtbPath;
	}

	public static Flavor getFlavor(FlavorType type) {
		Flavor ret = null;
		
		switch (type) {
		case tiny:
			ret = new Flavor(type, CPUTINY, MEMTINY, DISKTINY);
			break;
		case small:
			ret = new Flavor(type, CPUSMALL, MEMSMALL, DISKSMALL);
			break;
		case standard:
			ret = new Flavor(type, CPUSTANDARD, MEMSTANDARD, DISKSTANDARD);
			break;
		}
		return ret;
	}
	
	private class DownloadUpdater extends Thread {
		private boolean completed = false;
		private long totalSize, actualSize;
		private String fileName, URL;
		private File file;
		
		public DownloadUpdater (String fileName, String uri) {
			this.fileName = fileName;
			logger.debug(fileName);
			
			URL url;
			URLConnection conn;
			totalSize = -1;

			try {
				url = new URL(uri);
				conn = url.openConnection();
				totalSize = conn.getContentLength();
				if(totalSize < 0) {
					logger.warn("Could not determine file size.");
				}
				conn.getInputStream().close();
			} catch(Exception e) {
				logger.error("Error getting image size.");
				logger.error(e.getMessage());
			}
		}
		
		@Override
		public synchronized void start() {
			super.start();
			
			if (totalSize > 0) {
				try {
					for (double progressPercentage = 0.0; progressPercentage < 1.0; progressPercentage += 0.01) {
						updateProgress();
						Thread.sleep(20);
					}
				} catch (InterruptedException e) {
					logger.error("Download interrupted!");
				}
			}
		}
		
		private void updateProgress () {
			file = new File(fileName);
			actualSize = file.getTotalSpace();
			completed  = actualSize >= totalSize;
			
			final int width = 50;
			float progress = actualSize / totalSize;

			logger.debug("\r(" + (int) progress + "%) [");
			int i;
			for (i = 0; i <= (int)(progress*width); i++) {
				logger.debug("#");
			}
			for (; i < width; i++) {
				logger.debug(" ");
			}
			logger.debug("]("+ actualSize +"/" + totalSize +")");
		}
	}
}
