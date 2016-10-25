/*
 Copyright 2014-2015 Hewlett-Packard Development Company, L.P.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package eu.betaas.taas.bigdatamanager.tester.service.impl;


import java.util.List;

import org.apache.log4j.Logger;

import eu.betaas.service.bigdatamanager.applicationdataservice.IDataManagerADService;
import eu.betaas.service.bigdatamanager.service.datatask.data.TaskInfo;



public class TesterService  {
	
	private Logger logger;
	
	private IDataManagerADService service;


	public void setLogger(String conf) {
		// TODO Auto-generated method stub
		logger = Logger.getLogger(conf);
		
		
	}
	
	public void  clearTables(){
		
		logger.info("Clearing table content from a previous run data");
		
	}
	
	public void setService(IDataManagerADService service){
		this.service=service;
	}
	
	
	public void setup(){
		this.setLogger("betaas.taas");
		logger.info("Ready to get available tasks");
		String result;
		long ts1 = System.currentTimeMillis();
		logger.info("TEST 6.1.X Task list requesting");
		List<TaskInfo> list = service.getTaskList("");
		long ts2 = System.currentTimeMillis();
		logger.info("TEST 6.1.X Task list processed in "+(ts2-ts1));
		
		logger.debug("Got a list of "+list.size());
		
		for (int i=0;i<list.size();i++){
			logger.info("Got task for "+list.get(i).getTaskname());
			logger.info("Got task for "+list.get(i).getDescription());
		}
		
		logger.info("################################### Running dummy task");
		result = service.taskData("");
		logger.info("Returned a result "+result);
		logger.info("Trying to run the database task");
		logger.info("TEST 6.1.X Requesting DB Task");
		ts1 = System.currentTimeMillis();
		result = service.taskData("DatabaseRecentdataTask",null);
		ts2 = System.currentTimeMillis();
		logger.info("TEST 6.1.X Task over SQL db processed in in "+(ts2-ts1));

		logger.info("################################### Trying to run the traffic data task");
		//result = service.taskData("PrestoQueryTask",null);
		ts1 = System.currentTimeMillis();
		result = service.taskData("TrafficQueryTask",null);
		ts2 = System.currentTimeMillis();
		logger.info("TEST 6.1.X Task over Analytic db processed in in "+(ts2-ts1)+result);
		ts1 = System.currentTimeMillis();
		result = service.taskData("PrestoQueryTask",null);
		ts2 = System.currentTimeMillis();
		logger.info("TEST 6.1.X Task over Presto Analytic db processed in in "+(ts2-ts1)+result);
	}
	
	
	
	
}
