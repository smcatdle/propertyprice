package com.company.priceengine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.company.geo.GraphPoint;
import com.company.propertyprice.dao.FastPropertyRetrieveDAO;
import com.company.propertyprice.dao.PropertyDAO;
import com.company.propertyprice.model.PropertySale;
import com.company.utils.SystemConstants;
import com.google.gson.Gson;

/**
 * Servlet implementation class RetrieveGraphDataServlet
 */
public class RetrieveGraphDataServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final static Logger logger = Logger
	    .getLogger(RetrieveGraphDataServlet.class.getName());

    private Gson gson = null;
    private String responseString = null;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public RetrieveGraphDataServlet() {
	super();
	gson = new Gson();
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

	    if (responseString == null) {
		FastPropertyRetrieveDAO fastPropertyRetrieveDAO = FastPropertyRetrieveDAO
			.getInstance();
		List<GraphPoint> list = fastPropertyRetrieveDAO.retrieveGraphData();

		responseString = gson.toJson(list);
	    }

	    writer.print(responseString);
	    writer.close();
	    response.setContentType(SystemConstants.UTF_8);

	    long endTime = System.currentTimeMillis();
	    logger.info("RetrieveGraphDataServlet time ["
		    + (endTime - startTime) + "] milliseconds.");

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
