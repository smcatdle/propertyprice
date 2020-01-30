package com.company.priceengine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.company.utils.SystemConstants;
import com.google.gson.Gson;

/**
 * Servlet implementation class RetrieveGraphDataServlet
 */
public class RetrieveDaftInfoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final static Logger logger = Logger
	    .getLogger(RetrieveDaftInfoServlet.class.getName());

    
    // The number of properties loaded today
    public static int propertiesAdded = 0;
    
    // Has the cache been setup
    public static boolean batchComplete = false;
    
    // The last server startup time
    public static Date lastRestartTime = null;
    
    
    private Gson gson = null;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public RetrieveDaftInfoServlet() {
	super();
	gson = new Gson();
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
	    long startTime = System.currentTimeMillis();
	    PrintWriter writer = null;
	    writer = response.getWriter();
	    
		DaftPriceEngine engine = new DaftPriceEngine();
		ArrayList<String> searchLinks = new ArrayList<String>();
		
		propertiesAdded = engine.start(DaftPriceEngine.START_PAGE, searchLinks);
		
		logger.log(Level.INFO, "Completed Batch");
		System.gc();

	    writer.print("ok");
	    writer.close();
	    response.setContentType(SystemConstants.UTF_8);

	    long endTime = System.currentTimeMillis();
	    logger.info("RetrieveDaftInfoServlet time ["
		    + (endTime - startTime) + "] milliseconds.");
	    
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
	 
    }

}
