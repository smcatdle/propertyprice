/**
 * 
 */
package com.company.priceengine.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.company.propertyprice.dao.ServerConfigDAO;
import com.company.propertyprice.model.ServerConfig;
import com.company.propertyprice.model.ServerNode;


/**
 * @author smcardle
 *
 */
public class Configuration {

    private final Logger logger = Logger.getLogger(Configuration.class
	    .getName());
    
    
    public static final String MASTER = "M";
    public static final String SLAVE = "S";
    
    
    private static Configuration instance = null;
    
    private ServerConfig serverConfig = null;
    private List<String> masterNodes = null;
    private List<String> slaveNodes = null;
    private boolean isMaster = false;
    
    
    public static Configuration getInstance() {
    	
    	if (instance == null) {
    		instance = new Configuration();
    	}
    	
    	return instance;
    }
    
    private Configuration() {
	masterNodes = new ArrayList<String>();
	slaveNodes = new ArrayList<String>();
	
	/*String localIPAddress = "";
	try {
	    localIPAddress = InetAddress.getLocalHost().getHostAddress();
	} catch (UnknownHostException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	// Load the server configuration settings
	ServerConfig serverConfig = ServerConfigDAO.getInstance().findServerConfigById(0);
	List<ServerNode> masterNodes = ServerConfigDAO.getInstance().getServerNodes(MASTER);
	List<ServerNode> slaveNodes = ServerConfigDAO.getInstance().getServerNodes(SLAVE);
	
	List<String> masterNodesStrings = new ArrayList<String>();
	List<String> slaveNodesStrings = new ArrayList<String>();
	
	for (ServerNode serverNode : masterNodes) {
	    logger.info("Adding config setting masterNodes : " + serverNode.getServerName());
	    masterNodesStrings.add(serverNode.getServerName());
	}
	
	for (ServerNode serverNode : slaveNodes) {
	    logger.info("Adding config setting slaveNodes : " + serverNode.getServerName());
	    slaveNodesStrings.add(serverNode.getServerName());
	}
	
	setServerConfig(serverConfig);
	setMasterNodes(masterNodesStrings);
	setSlaves(slaveNodesStrings);
	
	if (localIPAddress.equals(serverConfig.getMasterIpAddress())) isMaster = true;
	
	logger.log(Level.INFO, "Master IP Address is : [" + serverConfig.getMasterIpAddress() + "] and local address is [" + localIPAddress + "]");*/
    }

    /**
     * @return the serverConfig
     */
    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    /**
     * @param serverConfig the serverConfig to set
     */
    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    /**
     * @return the masterNodes
     */
    public List<String> getMasterNodes() {
        return masterNodes;
    }

    /**
     * @param masterNodes the masterNodes to set
     */
    public void setMasterNodes(List<String> masterNodes) {
        this.masterNodes = masterNodes;
    }

    /**
     * @return the slaveNodes
     */
    public List<String> getSlaves() {
        return slaveNodes;
    }

    /**
     * @param slaveNodes the slaveNodes to set
     */
    public void setSlaves(List<String> slaveNodes) {
        this.slaveNodes = slaveNodes;
    }

    public boolean isMaster() {
	return isMaster;
    }
 
}
