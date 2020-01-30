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

import com.company.propertyprice.model.Address;
import com.company.propertyprice.model.GeoCode;
import com.company.propertyprice.model.rest.CrossCheckResult;
import com.company.utils.SystemConstants;
import com.google.gson.Gson;

/**
 * Servlet implementation class RetrieveLastCrossCheckServlet
 */
public class RetrieveLastCrossCheckServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final static Logger logger = Logger
			.getLogger(RetrieveLastCrossCheckServlet.class.getName());

	private Gson gson = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RetrieveLastCrossCheckServlet() {
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

		logger.log(Level.INFO, "Starting RetrieveLastCrossCheckServlet");

		long startTime = System.currentTimeMillis();
		PrintWriter writer = null;
		String responseString = null;
		CrossCheckResult crossCheckResult = null;
		CrossCheckResult[] crossCheckResults;

		try {
			writer = response.getWriter();
			
			if (CrossCheckAddressesServlet.batchComplete) {
			int crossCheckSize = CrossCheckAddressesServlet.addresses.size();
			
			crossCheckResults = new CrossCheckResult[crossCheckSize];
		    List<GeoCode> crossCheckAddresses = CrossCheckAddressesServlet.crossCheckAddresses;
		    List<Address> addresses = CrossCheckAddressesServlet.addresses;
		    String crossCheckAddress = null;
			
			for (int i=0; i<crossCheckSize; i++) {
				crossCheckResult = new CrossCheckResult();
				crossCheckResult.setOriginalAddress(addresses.get(i).concatAddress());
				crossCheckResult.setCurrentAddress(addresses.get(i).getGeocode().getFormattedAddress());
				crossCheckResult.setCrossCheckCode(crossCheckAddresses.get(i).getCrossCheckCode());
				
				crossCheckAddress = crossCheckAddresses.get(i).getFormattedAddress();
				if (crossCheckAddress != null) crossCheckResult.setSuggestedAddress(crossCheckAddress);
				
				crossCheckResults[i] = crossCheckResult;
			}
			
			} else {
				crossCheckResults = new CrossCheckResult[0];
			}
			
			responseString = gson.toJson(crossCheckResults);
		    writer.print(responseString);
		    
			writer.close();
			response.setContentType(SystemConstants.UTF_8);

			long endTime = System.currentTimeMillis();
			logger.info("RetrieveLastCrossCheckServlet time ["
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

	}

}
