package com.company.priceengine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.company.utils.DateUtils;
import com.company.utils.SystemConstants;

/**
 * Servlet implementation class RetrieveGraphDataServlet
 */
public class HealthCheckServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final static Logger logger = Logger
	    .getLogger(HealthCheckServlet.class.getName());
    
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HealthCheckServlet() {
	super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {

	PrintWriter writer = null;

	try {

	    writer = response.getWriter();

	    writer.print(" " + DateUtils.getLongString(RetrieveDaftInfoServlet.lastRestartTime) + " " + RetrieveDaftInfoServlet.batchComplete + " " + RetrieveDaftInfoServlet.propertiesAdded + " ");
		  

	} catch (Exception ex) {
	    ex.printStackTrace();
	    logger.log(Level.SEVERE, ex.getMessage());
	    writer.print("Error");
		
	} finally {
	    writer.close();
	    response.setContentType(SystemConstants.UTF_8);

	    writer = null;

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
