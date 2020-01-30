/**
 * 
 */
package com.company.priceengine.managers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.company.propertyprice.model.PropertySale;
import com.company.propertyprice.model.SalesWorkUnit;
import com.company.propertyprice.model.rest.PropertySalesWrapper;
import com.company.propertyprice.model.rest.SalesWorkUnitWrapper;
import com.company.utils.DateUtils;

/**
 * @author smcardle
 * 
 */
public class WorkAllocationManager {

    private final Logger logger = Logger.getLogger(WorkAllocationManager.class
	    .getName());

    private PropertyPersistanceManager propertyPersistanceManager = null;

    public WorkAllocationManager() {
	propertyPersistanceManager = new PropertyPersistanceManager();
    }

    public void allocate(List<String> pageLinks) {

	List<String> nodes = com.company.priceengine.config.Configuration
		.getInstance().getSlaves();
	long numberSlaveNodes = nodes.size();
	long numberWorkUnits = pageLinks.size();
	long numberWorkUnitsPerNode = Math.round(Math.floor(numberWorkUnits
		/ numberSlaveNodes));
	List<SalesWorkUnit> salesWorkUnits = null;
	long workUnitsCounter = 0;
	SalesWorkUnitWrapper salesWorkUnitWrapper = null;
	RestTemplate restTemplate = new RestTemplate();
	List<PropertySale> sales = null;

	// TODO: Loop through to find 'MappingJackson2HttpMessageConverter'
	MappingJackson2HttpMessageConverter converter = (MappingJackson2HttpMessageConverter) restTemplate
		.getMessageConverters().get(6);
	PropertySalesWrapper wrapper = null;

	converter.getObjectMapper().setDateFormat(
		new SimpleDateFormat(DateUtils.DATE_FORMAT));
	Map<String, String> vars = new HashMap<String, String>();
	vars.put("id", "JS01");

	logger.info("Allocating work units of [" + numberWorkUnitsPerNode
		+ "] to each slave node [" + numberSlaveNodes + "]...");

	// Allocate work to each slave node
	for (String node : nodes) {

	    try {
		logger.info("Allocating work to node : [" + node + "]");
		SalesWorkUnit salesWorkUnit = null;
		salesWorkUnits = new ArrayList<SalesWorkUnit>();

		logger.info("Concatanating work units [" + workUnitsCounter
			+ "-" + (workUnitsCounter + numberWorkUnitsPerNode)
			+ "] for slave node [" + node + "]");
		for (int i = 0; i < numberWorkUnitsPerNode; i++) {

		    salesWorkUnit = new SalesWorkUnit();
		    salesWorkUnit.setLink(pageLinks.get(new Long(
			    workUnitsCounter + i).intValue()));
		    salesWorkUnits.add(salesWorkUnit);
		}

		salesWorkUnitWrapper = new SalesWorkUnitWrapper();
		salesWorkUnitWrapper.setSalesWorkUnit(salesWorkUnits);

		// Send a REST service request to the slave
		logger.info("Sending REST service request to url : ["
			+ "http://" + node
			+ "/property-price-server/processSales" + "]");
		wrapper = restTemplate.postForObject("http://" + node
			+ "/property-price-server/processSales",
			salesWorkUnitWrapper, PropertySalesWrapper.class, vars);

		sales = wrapper.getSales();
		logger.info("Slave node [" + node + "] processed ["
			+ sales.size() + "] sales.");

		// TODO: Move this into a specific handler
		/*
		 * for (PropertySale sale : sales) {
		 * logger.info("Persisting sale with pprUrl : [" +
		 * sale.getPprUrl() + "]");
		 * propertyPersistanceManager.persist(sale); }
		 */

	    } catch (Exception ex) {
		ex.printStackTrace();
		logger.log(Level.SEVERE, ex.getMessage());
	    }
	    workUnitsCounter = workUnitsCounter + numberWorkUnitsPerNode;
	}

	logger.info("Completed allocation of work to [" + numberWorkUnits
		+ "] slave nodes.");

    }

}
