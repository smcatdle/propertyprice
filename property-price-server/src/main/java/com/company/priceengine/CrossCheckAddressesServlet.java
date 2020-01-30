package com.company.priceengine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.company.geo.Coordinate;
import com.company.propertyprice.dao.PropertyDAO;
import com.company.propertyprice.model.Address;
import com.company.propertyprice.model.GeoCode;
import com.company.propertyprice.model.PropertySale;
import com.company.utils.SystemConstants;
import com.google.gson.Gson;

/**
 * Servlet implementation class RetrieveGraphDataServlet
 */
public class CrossCheckAddressesServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final static Logger logger = Logger
			.getLogger(CrossCheckAddressesServlet.class.getName());

	// The number of addresses corrected today
	public static int addressesCorrected = 0;

	// Has the cache been setup
	public static boolean batchComplete = false;

	// The last server startup time
	public static Date lastRestartTime = null;

	public static List<GeoCode> crossCheckAddresses = null;
	public static List<Address> addresses = null;

	private Gson gson = null;
	private CrossCheckFacade crossCheckFacade = null;
	private String responseString = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CrossCheckAddressesServlet() {
		super();
		gson = new Gson();
		crossCheckFacade = new CrossCheckFacade();
		lastRestartTime = new Date();
		crossCheckAddresses = new ArrayList<GeoCode>();
		addresses = new ArrayList<Address>();
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
			Address address = null;
			GeoCode geocode = null;
			int codeMatchResult = 0;

			String key = request.getParameter("key");
			String[] keySplit = key.split(",");

			float topLeftLat = Float.parseFloat(keySplit[0]);
			float topLeftLng = Float.parseFloat(keySplit[1]);
			float bottomRightLat = Float.parseFloat(keySplit[2]);
			float bottomRightLng = Float.parseFloat(keySplit[3]);

			Coordinate topLeft = new Coordinate();
			topLeft.setLat(topLeftLat);
			topLeft.setLng(topLeftLng);
			Coordinate bottomRight = new Coordinate();
			bottomRight.setLat(bottomRightLat);
			bottomRight.setLng(bottomRightLng);

			crossCheckAddresses.clear();
			addresses.clear();
			addressesCorrected = 0;

			// 1. Only Cross Check geocodes of suspect addresses for the moment
			// (type 'L' or "N').
			// 2. We also need to identify addresses that dont have a consensus
			// 3. Possible match on MyHome if address string stripped and words
			// like 'Floor', 'Apt', 'House' are removed.
			List<PropertySale> sales = PropertyDAO.getInstance()
					.findBadAddressProperties(topLeft, bottomRight);

			logger.info("Found [" + sales.size() + "] in cross check");
			for (PropertySale sale : sales) {

				address = sale.getAddress();

				try {
					geocode = crossCheckFacade.crossCheck(address, sale.getPrice());

					crossCheckAddresses.add(geocode);
					addresses.add(sale.getAddress());

					updateGeoCode(sale, null, codeMatchResult);

					addressesCorrected++;
					logger.info("CrossCheck:: updated DB");

				} catch (Exception ex) {
					logger.info("CrossCheck:: Error cross checking address for ["
							+ sale.getAddress().getAddressLine1()
							+ "]  "
							+ ex.getMessage());
				}

			}

			writer.print(responseString);
			writer.close();
			response.setContentType(SystemConstants.UTF_8);

			long endTime = System.currentTimeMillis();
			logger.info("CrossCheckAddressesServlet time ["
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
		// TODO Auto-generated method stub
	}

	private void updateGeoCode(PropertySale sale, GeoCode newGeocode,
			int crossCheckCode) {

		GeoCode geocode = sale.getAddress().getGeocode();
		geocode.setCrossCheckCode(crossCheckCode);
		sale.setDateUpdated(new Date());
		geocode.setDateUpdated(new Date());

		geocode.setLatitudeBck(geocode.getLatitude());
		geocode.setLongitudeBck(geocode.getLongitude());
		geocode.setFormattedAddressBck(geocode.getFormattedAddress());
		geocode.setGeocodeBckType(GeoCode.GEO_SYSTEM_TYPE_GOOGLE);

		geocode.setLatitude(newGeocode.getLatitude());
		geocode.setLongitude(newGeocode.getLongitude());
		geocode.setFormattedAddress(newGeocode.getFormattedAddress());
		geocode.setGeocodeCurType(GeoCode.GEO_SYSTEM_TYPE_MY_HOME);

		PropertyDAO.getInstance().update(sale);
		logger.info("PropertyDAO:: updated DB [" + crossCheckCode + "]");
	}

}
