/**
 * 
 */
package com.company.controller;


import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final static Logger logger = Logger
    	    .getLogger(DashboardController.class.getName());
    

    @RequestMapping(method = RequestMethod.GET)
    public String get() {
    	logger.log(Level.INFO, "DashboardController called");
    	return "test";
    }

    @RequestMapping(value="/{address}", method = RequestMethod.GET)
    public String getForDay(Model model) {
        return null;
    }

}
