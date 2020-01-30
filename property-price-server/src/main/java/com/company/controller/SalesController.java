package com.company.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;

import com.company.priceengine.PPRPriceEngine;
import com.company.priceengine.config.Configuration;
import com.company.propertyprice.model.PropertySale;
import com.company.propertyprice.model.SalesWorkUnit;
import com.company.propertyprice.model.rest.PropertySalesWrapper;
import com.company.propertyprice.model.rest.SalesWorkUnitWrapper;
import com.company.propertyprice.services.SalesService;

//@RestController
public class SalesController {

    private final Logger logger = Logger.getLogger(SalesController.class
	    .getName());

    /*@RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody
    String handleSalesUpload(@RequestBody PropertySalesWrapper salesWrapper) {

	logger.info("handleSalesUpload called");

	// NB: Only allow slaves to upload sales
	/if (Configuration.getInstance().isMaster())
	    //return null;
	
	
	List<PropertySale> sales = salesWrapper.getSales();

	logger.info("handleSalesUpload uploading [" + sales.size() + "] sales");
	logger.info("sale1 with adress ["
		+ sales.get(0).getAddress().getAddressLine1() + "]");

	if (sales != null) {
	    try {

		SalesService service = SalesService.getInstance();
		service.uploadSales(sales);

		return "You successfully uploaded " + sales.size() + "";

	    } catch (Exception e) {
		return "You failed to upload " + sales.size() + " => "
			+ e.getMessage();
	    }
	} else {
	    return "You failed to upload " + sales.size()
		    + " because the file was empty.";
	}
    }*/

    @RequestMapping(value = "/processSales", method = RequestMethod.POST)
    public @ResponseBody
    PropertySalesWrapper handleProcessSales(
	    @RequestBody SalesWorkUnitWrapper salesWorkUnitWrapper) {

	logger.info("handleProcessSales called");
	
	// NB: Only allow slaves to process sales
	/*if (Configuration.getInstance().isMaster())
	    return new PropertySalesWrapper();*/

	List<SalesWorkUnit> salesWorkUnits = salesWorkUnitWrapper
		.getSalesWorkUnit();

	logger.info("handleProcessSales uploading [" + salesWorkUnits.size()
		+ "] work units");
	logger.info("work unit with link [" + salesWorkUnits.get(0).getLink()
		+ "]");

	if (salesWorkUnits != null) {
	    try {

		/*
		 * SalesService service = SalesService.getInstance();
		 * service.processSales(salesWorkUnits);
		 */

		/*List<String> searchLinks = new ArrayList<String>();

		for (SalesWorkUnit salesWorkUnit : salesWorkUnits) {
		    searchLinks.add(salesWorkUnit.getLink());
		    logger.info("Added link [" + salesWorkUnit.getLink()
			    + "] to process engine");
		}

		PPRPriceEngine engine = new PPRPriceEngine();
		engine.start(PPRPriceEngine.START_PAGE, searchLinks);
		List<PropertySale> processedSales = engine
			.retrieveProcessedSales();

		logger.log(Level.INFO, "Completed Batch ");
		logger.log(Level.INFO, "Processed Sales are : ["
			+ processedSales + "]");*/

		PropertySalesWrapper wrapper = new PropertySalesWrapper();
		wrapper.setSales(null);
		return wrapper;

	    } catch (Exception e) {
		return null;
	    }

	} else {
	    return null;
	}
    }

}
