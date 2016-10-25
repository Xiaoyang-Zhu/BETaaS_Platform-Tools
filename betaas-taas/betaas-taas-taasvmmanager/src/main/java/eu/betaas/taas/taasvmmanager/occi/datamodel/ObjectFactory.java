//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.10.08 at 05:41:25 PM CEST 
//


package eu.betaas.taas.taasvmmanager.occi.datamodel;

import javax.xml.bind.annotation.XmlRegistry;

import eu.betaas.taas.taasvmmanager.api.datamodel.InstanceType;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the eu.betaas.OCCI.iface package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: eu.betaas.OCCI.iface
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link User.Quota }
     * 
     */
    public User.Quota createUserQuota() {
        return new User.Quota();
    }

    /**
     * Create an instance of {@link Storage }
     * 
     */
    public Storage createStorage() {
        return new Storage();
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link Resource }
     * 
     */
    public Resource createResource() {
        return new Resource();
    }

    /**
     * Create an instance of {@link Link }
     * 
     */
    public Link createLink() {
        return new Link();
    }

    /**
     * Create an instance of {@link StorageCollection }
     * 
     */
    public StorageCollection createStorageCollection() {
        return new StorageCollection();
    }

    /**
     * Create an instance of {@link ComputeCollection }
     * 
     */
    public ComputeCollection createComputeCollection() {
        return new ComputeCollection();
    }

    /**
     * Create an instance of {@link UserCollection }
     * 
     */
    public UserCollection createUserCollection() {
        return new UserCollection();
    }

    /**
     * Create an instance of {@link User.Usage }
     * 
     */
    public User.Usage createUserUsage() {
        return new User.Usage();
    }

    /**
     * Create an instance of {@link InstanceType }
     * 
     */
    public InstanceType createInstanceType() {
        return new InstanceType();
    }

    /**
     * Create an instance of {@link Network }
     * 
     */
    public Network createNetwork() {
        return new Network();
    }

    /**
     * Create an instance of {@link Compute.Nic }
     * 
     */
    public Compute.Nic createComputeNic() {
        return new Compute.Nic();
    }

    /**
     * Create an instance of {@link InstanceTypeCollection }
     * 
     */
    public InstanceTypeCollection createInstanceTypeCollection() {
        return new InstanceTypeCollection();
    }

    /**
     * Create an instance of {@link Compute.Disk }
     * 
     */
    public Compute.Disk createComputeDisk() {
        return new Compute.Disk();
    }

    /**
     * Create an instance of {@link Compute.Context }
     * 
     */
    public Compute.Context createComputeContext() {
        return new Compute.Context();
    }

    /**
     * Create an instance of {@link Compute }
     * 
     */
    public Compute createCompute() {
        return new Compute();
    }

    /**
     * Create an instance of {@link NetworkCollection }
     * 
     */
    public NetworkCollection createNetworkCollection() {
        return new NetworkCollection();
    }

}
