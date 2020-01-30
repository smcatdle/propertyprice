package com.company.priceengine;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.company.geo.Coordinate;
import com.company.geo.ViewPort;
import com.company.propertyprice.managers.UserEventLogManager;
import com.company.propertyprice.model.PropertySale;
import com.company.propertyprice.model.UserEvent;
import com.company.propertyprice.services.GeoPropertyService;
import com.company.utils.SystemConstants;
import com.google.gson.Gson;

/**
 * Servlet implementation class NumberServlet
 */
public class PropertyServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String SERVICE = "service";

    private static final String SERVICE_GET_PROERTIES_WITHIN_VIEWPORT = "getPropertiesWithinViewport";

    private static final String SERVICE_GET_PROERTIES_BY_DATE_WITHIN_VIEWPORT = "getPropertiesByDateWithinViewport";

    private static final String SERVICE_TOP_LEFT_LAT = "topLeftX";

    private static final String SERVICE_TOP_LEFT_LONG = "topLeftY";

    private static final String SERVICE_BOTTOM_RIGHT_LAT = "bottomRightX";

    private static final String SERVICE_BOTTOM_RIGHT_LONG = "bottomRightY";

    private static final String SERVICE_PARAM_DATE_FROM = "dateFrom";

    private static final String SERVICE_PARAM_DATE_TO = "dateTo";

    private final static Logger logger = Logger.getLogger(PropertyServlet.class
	    .getName());

    private static SimpleDateFormat dateFormatter = null;
    private static String dateFormat = "dd/MM/yyyy";

    private Gson gson = null;
    private UserEventLogManager userEventLogManager = null;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PropertyServlet() {
	super();

	dateFormatter = new SimpleDateFormat(dateFormat);
	gson = new Gson();
	userEventLogManager = new UserEventLogManager();
    }

    public void init() throws ServletException {

	// Run todays DaftPriceEngine (A cron job will restart the servlet each
	// day.)
	/*
	 * DaftPriceEngine engine = new DaftPriceEngine(); ArrayList<String>
	 * searchLinks = new ArrayList<String>();
	 * 
	 * engine.start(DaftPriceEngine.START_PAGE, searchLinks);
	 */

    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {

	logger.info("Invoking Servlet PropertyServlet...");

	// TODO: Check for Android App credential?

	String message = null;
	String responseString = null;
	String service = null;
	PrintWriter writer = null;
	String parameter1 = null;
	String parameter2 = null;
	String parameter3 = null;
	String parameter4 = null;
	String parameter5 = null;
	String parameter6 = null;
	Coordinate topLeft = null;
	Coordinate bottomRight = null;
	Date dateFrom = null;
	Date dateTo = null;
	List<PropertySale> convertedProperties = null;

	parameter1 = request.getParameter(PropertyServlet.SERVICE_TOP_LEFT_LAT);
	parameter2 = request
		.getParameter(PropertyServlet.SERVICE_TOP_LEFT_LONG);
	parameter3 = request
		.getParameter(PropertyServlet.SERVICE_BOTTOM_RIGHT_LAT);
	parameter4 = request
		.getParameter(PropertyServlet.SERVICE_BOTTOM_RIGHT_LONG);

	topLeft = new Coordinate();
	bottomRight = new Coordinate();

	topLeft.setLat(Float.parseFloat(parameter1));
	topLeft.setLng(Float.parseFloat(parameter2));
	bottomRight.setLat(Float.parseFloat(parameter3));
	bottomRight.setLng(Float.parseFloat(parameter4));

	// Log the user event
	try {
	    UserEvent userEvent = new UserEvent();
	    userEvent.setIpAddress(request.getRemoteAddr().trim());
	    userEvent.setDataType(UserEvent.DATA_TYPE_JSON);
	    userEvent
		    .setEventType(UserEvent.EVENT_TYPE_GET_PROPERTIES_WITHIN_VIEWPORT);
	    userEvent.setData(gson.toJson(new ViewPort(topLeft, bottomRight)));
	    userEvent.setDateCreated(new Date());
	    userEventLogManager.loqEvent(userEvent);
	    logger.info("Logged user event.");
	} catch (Exception ex) {
	    logger.severe("Error : [" + ex.getMessage() + "]");
	    ex.printStackTrace();
	}

	try {
	    // DDOS Prevention (don't retrieve markers for large cells)
	    if ((Math.abs(bottomRight.getLat() - topLeft.getLat()) < .03)
		    && (Math.abs(topLeft.getLng() - bottomRight.getLng()) < .03)) {

		logger.info("Within params");
		service = request.getParameter(PropertyServlet.SERVICE);

		writer = response.getWriter();
		GeoPropertyService geoPropertyService = GeoPropertyService
			.getInstance();

		// A simple service to add two numbers.
		if (PropertyServlet.SERVICE_GET_PROERTIES_WITHIN_VIEWPORT
			.equals(service)) {
		    logger.info("Service");
		    if (parameter1 != null && parameter2 != null
			    && parameter3 != null && parameter4 != null) {
			logger.info("Within params");
			message = "Received viewport coordinates : topLeftLat ["
				+ topLeft.getLat()
				+ "] topLeftLong ["
				+ topLeft.getLng()
				+ "] bottomRightLat ["
				+ bottomRight.getLat()
				+ "] bottomRightLong ["
				+ bottomRight.getLng() + "]";
			long startTime = System.currentTimeMillis();
			convertedProperties = GeoPropertyService.getInstance()
				.getPropertiesWithinViewport(topLeft,
					bottomRight);

			long endTime = System.currentTimeMillis();
			logger.log(Level.INFO, "Servlet time is ["
				+ (endTime - startTime) + "] milliseconds.");
		    } else {
			message = "Invalid viewport coordinates : ";
		    }
		} else if (PropertyServlet.SERVICE_GET_PROERTIES_BY_DATE_WITHIN_VIEWPORT
			.equals(service)) {
		    parameter1 = request
			    .getParameter(PropertyServlet.SERVICE_TOP_LEFT_LAT);
		    parameter2 = request
			    .getParameter(PropertyServlet.SERVICE_TOP_LEFT_LONG);
		    parameter3 = request
			    .getParameter(PropertyServlet.SERVICE_BOTTOM_RIGHT_LAT);
		    parameter4 = request
			    .getParameter(PropertyServlet.SERVICE_BOTTOM_RIGHT_LONG);
		    parameter5 = request
			    .getParameter(PropertyServlet.SERVICE_PARAM_DATE_FROM);
		    parameter6 = request
			    .getParameter(PropertyServlet.SERVICE_PARAM_DATE_TO);

		    if (parameter1 != null && parameter2 != null
			    && parameter3 != null && parameter4 != null
			    && parameter5 != null && parameter6 != null) {
			topLeft.setLat(Float.parseFloat(parameter1));
			topLeft.setLng(Float.parseFloat(parameter2));
			bottomRight.setLat(Float.parseFloat(parameter3));
			bottomRight.setLng(Float.parseFloat(parameter4));

			try {
			    dateFrom = dateFormatter.parse(parameter5);
			    dateTo = dateFormatter.parse(parameter6);
			} catch (ParseException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}

			message = "Received viewport coordinates : topLeftLat ["
				+ topLeft.getLat()
				+ "] topLeftLong ["
				+ topLeft.getLng()
				+ "] bottomRightLat ["
				+ bottomRight.getLat()
				+ "] bottomRightLong ["
				+ bottomRight.getLng()
				+ "] dateFrom ["
				+ dateFrom + "] dateTo [" + dateTo + "]";

			convertedProperties = GeoPropertyService.getInstance()
				.getPropertiesByDateWithinViewport(topLeft,
					bottomRight, dateFrom, dateTo);

		    } else {
			message = "Invalid viewport coordinates : ";
		    }

		} else {
		    message = "service : " + service + " is not recognised";
		}

		logger.info("response : [" + message + "] from service : "
			+ service);
		if (convertedProperties != null
			&& convertedProperties.size() > 0)
		    logger.info("Retrieved [" + convertedProperties.size()
			    + "] sales");

		responseString = gson.toJson(convertedProperties);
		writer.print(responseString);
		writer.close();
		response.setContentType(SystemConstants.UTF_8);
		System.gc();

	    }
	} catch (Exception ex) {
	    logger.severe(ex.getMessage());
	    ex.printStackTrace();

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
