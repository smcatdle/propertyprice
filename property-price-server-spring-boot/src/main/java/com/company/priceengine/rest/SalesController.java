package com.company.priceengine.rest;



import java.util.List;
import java.util.logging.Logger;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.company.propertyprice.model.PropertySale;
import com.company.propertyprice.model.SalesWorkUnit;
import com.company.propertyprice.model.rest.PropertySalesWrapper;
import com.company.propertyprice.model.rest.SalesWorkUnitWrapper;
import com.company.propertyprice.services.SalesService;




@RestController
public class SalesController {

    private final Logger logger = Logger.getLogger(SalesController.class.getName());


    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleSalesUpload(@RequestBody PropertySalesWrapper salesWrapper){
	
	logger.info("handleSalesUpload called");
	
	List<PropertySale> sales = salesWrapper.getSales();
	
	logger.info("handleSalesUpload uploading [" + sales.size() + "] sales"); 
	logger.info("sale1 with adress [" + sales.get(0).getAddress().getAddressLine1() + "]"); 
	
        if (sales != null) {
            try {
        	
        	SalesService service = SalesService.getInstance();
        	service.uploadSales(sales);
        	
                return "You successfully uploaded " + sales.size() + "";
                
            } catch (Exception e) {
                return "You failed to upload " + sales.size() + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + sales.size() + " because the file was empty.";
        }
    }

    @RequestMapping(value="/processSales", method=RequestMethod.POST)
    public @ResponseBody String handleProcessSales(@RequestBody SalesWorkUnitWrapper salesWorkUnitWrapper){
	
	logger.info("handleProcessSales called");
	
	List<SalesWorkUnit> salesWorkUnits = salesWorkUnitWrapper.getSalesWorkUnit();
	
	logger.info("handleProcessSales uploading [" + salesWorkUnits.size() + "] work units"); 
	logger.info("work unit with link [" + salesWorkUnits.get(0).getLink() + "]"); 
	
        if (salesWorkUnits != null) {
            try {
        	
        	SalesService service = SalesService.getInstance();
        	service.processSales(salesWorkUnits);
        	
                return "You successfully uploaded " + salesWorkUnits.size() + "";
                
            } catch (Exception e) {
                return "You failed to upload " + salesWorkUnits.size() + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload";
        }
    }
    
}
