package com.company.priceengine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.company.propertyprice.model.PropertySale;
import com.company.propertyprice.model.mdo.PropertyMDO;
import com.company.propertyprice.services.GeoPropertyService;
import com.company.utils.SystemConstants;
import com.google.gson.Gson;

/**
 * Servlet implementation class GetSaleByUUIDServlet
 */
public class GetSaleByUUIDServlet extends HttpServlet {

    private static final String UUID_PARAM = "uuid";

    private static final long serialVersionUID = 1L;

    private final static Logger logger = Logger
	    .getLogger(GetSaleByUUIDServlet.class.getName());

    
    // The number of queries today
    public static int queries = 0;

    // The last server startup time
    public static Date lastRestartTime = null;
    
    
    private Gson gson = null;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetSaleByUUIDServlet() {
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

	    String UUID = request.getParameter(GetSaleByUUIDServlet.UUID_PARAM);

	    List<PropertySale> properties = GeoPropertyService.getInstance()
		    .getPropertyByUUID(UUID);
	    PropertySale property = properties.get(0);

	    logger.info("Sale id is : ["
		    + property.getAddress().getAddressLine1() + "]");

	    // Convert to MDO object
	    PropertyMDO p = new PropertyMDO();
	    p.setId(property.getId());
	    p.setA1(property.getAddress().getAddressLine1());
	    p.setA2(property.getAddress().getAddressLine2());
	    p.setA3(property.getAddress().getAddressLine3());
	    p.setA4(property.getAddress().getAddressLine4());
	    p.setA5(property.getAddress().getAddressLine5());
	    p.setL(property.getAddress().getGeocode().getLatitude());
	    p.setO(property.getAddress().getGeocode().getLongitude());
	    p.setD(property.getDateOfSale());
	    p.setF(property.isFullMarketPrice());
	    p.setP(property.getPrice());
	    p.setS(property.getPropertySize());
	    p.setV(property.isVatExclusive());

	    String responseString = gson.toJson(p);

	    writer.print(responseString);
	    writer.close();
	    response.setContentType(SystemConstants.UTF_8);

	    long endTime = System.currentTimeMillis();
	    logger.info("GetSaleByUUIDServlet time [" + (endTime - startTime)
		    + "] milliseconds.");

	    queries++;
	    
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
