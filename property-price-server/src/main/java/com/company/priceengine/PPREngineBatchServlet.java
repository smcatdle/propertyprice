package com.company.priceengine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.company.propertyprice.model.PPRSearchVariables;

/**
 * Servlet implementation class NumberServlet
 */
public class PPREngineBatchServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final static Logger logger = Logger
	    .getLogger(PPREngineBatchServlet.class.getName());

    
    // The number of properties loaded today
    public static int propertiesAdded = 0;

    // Has the cache been setup
    public static boolean batchComplete = false;

    // The last server startup time
    public static Date lastRestartTime = null;

    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PPREngineBatchServlet() {
	super();
	lastRestartTime = new Date();
    }

    public void init() throws ServletException {

    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {

	try {
	    // NB: Only allow master to start batch runs
	    /*
	     * if (!Configuration.getInstance().isMaster()) {
	     * logger.log(Level.INFO, "Exiting as not master"); return; }
	     */

	    logger.log(Level.INFO, "Starting PPREngineBatch");

	    // Run todays PPRPriceEngine (A cron job will restart the batch each
	    // day.)
	    PPRPriceEngine pPRPriceEngine = new PPRPriceEngine();
	    ArrayList<String> searchLinks = new ArrayList<String>();

	    List<PPRSearchVariables> pprSearchCriteriaList = new ArrayList<PPRSearchVariables>();
	    PPRSearchVariables pprSearchVariables = new PPRSearchVariables();
	    List<String> counties = new ArrayList();
	    List<String> years = new ArrayList();

	    counties.add("Dublin");
	    counties.add("Carlow");
	    counties.add("Cavan");
	    counties.add("Clare");
	    counties.add("Cork");
	    counties.add("Donegal");
	    counties.add("Galway");
	    counties.add("Kerry");
	    counties.add("Kildare");
	    counties.add("Kilkenny");
	    
	    counties.add("Laois");
	    counties.add("Leitrim");
	    counties.add("Limerick");
	    counties.add("Longford");
	    counties.add("Louth");
	    counties.add("Mayo");
	    counties.add("Meath");
	    counties.add("Monaghan");
	    counties.add("Offaly");
	    counties.add("Roscommon");
	    counties.add("Sligo");
	    
	    counties.add("Tipperary");
	    counties.add("Waterford");
	    counties.add("Westmeath");
	    counties.add("Wexford");
	    counties.add("Wicklow");

	    years.add("2015");
	    years.add("2014");
	    years.add("2013");
	    years.add("2012");
	    years.add("2011");
	    years.add("2010");

	    pprSearchVariables.setCounties(counties);
	    pprSearchVariables.setYears(years);
	    pPRPriceEngine.setSearchCriteria(pprSearchVariables);
	    propertiesAdded = pPRPriceEngine.start(PPRPriceEngine.START_PAGE, searchLinks);

	    logger.log(Level.INFO, "Completed Batch");
	    batchComplete = true;
	    
	    System.gc();

	} catch (Exception ex) {
	    ex.printStackTrace();
	    logger.log(Level.SEVERE, ex.getMessage());
	}
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
	// TODO Auto-generated method stub
    }

}
