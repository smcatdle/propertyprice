package com.company.priceengine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.company.utils.DateUtils;
import com.company.utils.network.HttpUtils;

/**
 * Servlet implementation class NumberServlet
 */
public class HealthCheckServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static final String WEB_CACHE_LEINSTER_SERVER_ENDPOINT = "http://webcache20-property20.rhcloud.com/web-cache-server/HealthCheckServlet";
    public static final String WEB_CACHE_EX_LEINSTER_SERVER_ENDPOINT = "http://outsideleinstcache-dublin23.rhcloud.com/ex-leinster-cache/HealthCheckServlet";
    public static final String WEB_CACHE_BACKUP_SERVER_ENDPOINT = "http://property-cache21.rhcloud.com/web-cache-backup/HealthCheckServlet";
    public static final String DAFT_SERVER_ENDPOINT = "http://daft-propertystore.rhcloud.com/daft-price-server/HealthCheckServlet";

    private final static Logger logger = Logger
	    .getLogger(HealthCheckServlet.class.getName());

    private String[] endpoints = { WEB_CACHE_LEINSTER_SERVER_ENDPOINT,
	    WEB_CACHE_EX_LEINSTER_SERVER_ENDPOINT,
	    WEB_CACHE_BACKUP_SERVER_ENDPOINT, DAFT_SERVER_ENDPOINT };

    private String[] endpointNames = { "Cache-Leinster",
	    "Cache-Ex-Leinster",
	    "Cache-Backup", "Daft" };
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HealthCheckServlet() {
	super();
    }

    public void init() throws ServletException {

    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {

	logger.log(Level.INFO, "Starting HealthCheckServlet");

	outputHealthCheckStats(response);

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {

    }

    private List<String> refreshHealthCheckStats() {

	List<String> healthStats = new ArrayList<String>();
	String result = null;

	for (String endpoint : endpoints) {
	    
	    try {
		result = HttpUtils.httpRequest(endpoint, HttpUtils.METHOD_GET,
			null, null);

		healthStats.add(result);

	    } catch (Exception ex) {
		ex.printStackTrace();
		logger.log(Level.SEVERE, ex.getMessage());
		healthStats.add("Error");
	    }
	}

	return healthStats;
    }

    private void outputHealthCheckStats(HttpServletResponse response) {

	String htmlResponseString = "<!DOCTYPE html>" +
						"<html>" +
						"<head>" + 
						"<script src=\"http://ajax.googleapis.com/ajax/libs/angularjs/1.2.26/angular.min.js\"></script>" + 
						"</head><BODY><H1>Irish Property Price Health Status</H1><TABLE>";
	PrintWriter writer;
	int counter = 0;

	response.setContentType("text/html");

	try {

	    List<String> healthStats = refreshHealthCheckStats();

	    // Add the stats for this server
	    htmlResponseString = htmlResponseString + "<TR><TD>Sales</TD><TD>" + " " + DateUtils.getLongString(PPREngineBatchServlet.lastRestartTime) + " " + PPREngineBatchServlet.batchComplete + " " + PPREngineBatchServlet.propertiesAdded + " "  + "</TD></TR>";
	    htmlResponseString = htmlResponseString + "<TR><TD>  CrossCheck</TD><TD>" + " " + DateUtils.getLongString(CrossCheckAddressesServlet.lastRestartTime) + " " + CrossCheckAddressesServlet.batchComplete + " " + CrossCheckAddressesServlet.addressesCorrected + " "  + "</TD></TR>";		   
	    htmlResponseString = htmlResponseString + "<TR><TD>  UUID Lookup</TD><TD>" + " " + DateUtils.getLongString(GetSaleByUUIDServlet.lastRestartTime) + " " + GetSaleByUUIDServlet.queries + " "  + "</TD></TR>";		   
	    htmlResponseString = htmlResponseString + "<TR><TD>  Crashes</TD><TD>" + " " + DateUtils.getLongString(LogUserCrashServlet.lastRestartTime) + " " + LogUserCrashServlet.crashes + " "  + "</TD></TR>";		   
	    
	    htmlResponseString = htmlResponseString + "<TR><TD></TD></TR><TR><TD>  Servers </TD></TR><TR><TD></TD></TR>";		   
	    
	    // Display each health stat
	    if (healthStats != null && !healthStats.isEmpty()) {
        	    for (String healthStat : healthStats) {
        
        		// Output html segment for endpoint
        		htmlResponseString = htmlResponseString + "<TR><TD>" + endpointNames[counter++]
        			+ "</TD><TD>" + healthStat + "</TD></TR>";
        
        	    }
	    }

	    // Finalize the htmlResponseString
	    htmlResponseString = htmlResponseString + "</TABLE></HTML></BODY>";

	    writer = response.getWriter();
	    writer.print(htmlResponseString);
	    writer.close();

	    System.gc();

	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    htmlResponseString = htmlResponseString
		    + "<TR><TD>Error assembling health check stats</TD></TR>";
	}

    }

    /*
     * private void refreshHealthCheckStats() {
     * 
     * // NB: Only allow master to run health checks if
     * (!Configuration.getInstance().isMaster()) return;
     * 
     * List<String> nodes = com.company.priceengine.config.Configuration
     * .getInstance().getSlaves(); List<SalesWorkUnit> salesWorkUnits = null;
     * RestTemplate restTemplate = new RestTemplate(); SalesWorkUnitWrapper
     * salesWorkUnitWrapper = null; List<PropertySale> sales = null;
     * List<NodeStatus> nodeStatusList = new ArrayList<NodeStatus>(); String
     * nodeName = null;
     * 
     * // TODO: Loop through to find 'MappingJackson2HttpMessageConverter'
     * MappingJackson2HttpMessageConverter converter =
     * (MappingJackson2HttpMessageConverter) restTemplate
     * .getMessageConverters().get(6); PropertySalesWrapper wrapper = null;
     * 
     * converter.getObjectMapper().setDateFormat( new
     * SimpleDateFormat(DateUtils.DATE_FORMAT)); Map<String, String> vars = new
     * HashMap<String, String>(); vars.put("id", "JS01");
     * 
     * // Check health status of each slave node for (String node : nodes) {
     * 
     * try { nodeName = parseNodeName(node);
     * 
     * SalesWorkUnit salesWorkUnit = null; salesWorkUnit = new SalesWorkUnit();
     * salesWorkUnit.setLink(HEALTH_CHECK_PPR_URL); salesWorkUnits = new
     * ArrayList<SalesWorkUnit>(); salesWorkUnits.add(salesWorkUnit);
     * salesWorkUnitWrapper = new SalesWorkUnitWrapper();
     * salesWorkUnitWrapper.setSalesWorkUnit(salesWorkUnits);
     * 
     * // Send a REST service request to the slave
     * logger.info("Sending health check request to node : [" + "http://" + node
     * + "/property-price-server/processSales" + "]"); wrapper =
     * restTemplate.postForObject("http://" + node +
     * "/property-price-server/processSales", salesWorkUnitWrapper,
     * PropertySalesWrapper.class, vars);
     * 
     * sales = wrapper.getSales(); logger.info("Slave node [" + node +
     * "] processed [" + sales.size() + "] sales.");
     * 
     * // Add the node status NodeStatus nodeStatus = new NodeStatus();
     * nodeStatus.setNodeName(nodeName); nodeStatus .setStatus(((sales != null
     * && sales.size() > 0) ? "OK" : "No Results"));
     * nodeStatusList.add(nodeStatus);
     * 
     * } catch (Exception ex) { ex.printStackTrace(); logger.log(Level.SEVERE,
     * ex.getMessage());
     * 
     * // Set an error node NodeStatus nodeStatus = new NodeStatus();
     * nodeStatus.setNodeName(nodeName); nodeStatus.setStatus(ex.getMessage());
     * nodeStatusList.add(nodeStatus); } }
     * 
     * healthCheckStats.setHealthCheckStats(nodeStatusList); }
     * 
     * private void outputHealthCheckStats(HttpServletResponse response) {
     * 
     * String htmlResponseString =
     * "<HTML><BODY><H1>Irish Property Price Health Status</H1><TABLE>";
     * PrintWriter writer;
     * 
     * // NB: Only allow master to run health checks if
     * (!Configuration.getInstance().isMaster()) return;
     * 
     * response.setContentType("text/html");
     * 
     * try {
     * 
     * // Allocate work to each slave node for (NodeStatus nodeStatus :
     * healthCheckStats.getHealthCheckStats()) {
     * 
     * // Output html segment for node htmlResponseString = htmlResponseString +
     * "<TR><TD>" + nodeStatus.getNodeName() + "</TD><TD>" +
     * nodeStatus.getStatus() + "</TD></TR>";
     * 
     * }
     * 
     * // Finalize the htmlResponseString htmlResponseString =
     * htmlResponseString + "</TABLE></HTML></BODY>";
     * 
     * writer = response.getWriter(); writer.print(htmlResponseString);
     * writer.close();
     * 
     * 
     * System.gc();
     * 
     * } catch (IOException e) { // TODO Auto-generated catch block
     * e.printStackTrace(); htmlResponseString = htmlResponseString +
     * "<TR><TD>Error assembling health check stats</TD></TR>"; }
     * 
     * }
     */

}
