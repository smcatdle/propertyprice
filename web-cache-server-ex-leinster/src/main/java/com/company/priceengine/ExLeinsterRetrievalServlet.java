package com.company.priceengine;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.company.geo.Coordinate;
import com.company.geo.GraphPoint;
import com.company.geo.ViewPort;
import com.company.geo.ViewPortGridDivisor;
import com.company.propertyprice.dao.FastPropertyRetrieveDAO;
import com.company.propertyprice.dao.PropertyDAO;
import com.company.propertyprice.model.GridWrapper;
import com.company.propertyprice.model.PropertySale;
import com.company.propertyprice.model.mdo.PropertyMDO;
import com.company.utils.SystemConstants;
import com.google.gson.Gson;

/**
 * Servlet implementation class RetrieveGraphDataServlet
 */
public class ExLeinsterRetrievalServlet extends HttpServlet {

	private static final String CACHE_REQUEST_PARAM_KEY = "key";
	private static final String CACHE_REQUEST_PARAM_GRIDS = "keys";

	private static final long serialVersionUID = 1L;

	// The number of POST queries made
	public static int queries = 0;

	// The number of grids in cache
	public static int grids = 0;

	// Has the cache been setup
	public static boolean setupComplete = false;

	// The last server startup time
	public static Date lastRestartTime = null;

	private final static Logger logger = Logger
			.getLogger(ExLeinsterRetrievalServlet.class.getName());

	private Gson gson = null;
	private Map<String, String> cache = null;
	private ViewPortGridDivisor viewPortGridDivisor = null;
	private int gridCountTotal = 0;
	private boolean initialized = false;
	private Session session = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ExLeinsterRetrievalServlet() {
		super();
		gson = new Gson();
		cache = new HashMap<String, String>();
		viewPortGridDivisor = new ViewPortGridDivisor(ViewPort.VIEWPORT_IRELAND);
		gridCountTotal = 1000 * 1000;
		lastRestartTime = new Date();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	/*
	 * @Override public void destroy() { // TODO Auto-generated method stub
	 * super.destroy();
	 * 
	 * try { Integer threadLocalCount; GSONThreadLocalImmolater gsonImmolator =
	 * new GSONThreadLocalImmolater(); gsonImmolator.immolate();
	 * logger.log(Level.INFO, "gsonImmolator.immolate() completed: immolated ");
	 * } catch (Exception e) { logger.log(Level.SEVERE,
	 * "caught exception raised by gsonImmolator.immolate()", e); } finally { //
	 * do nothing } }
	 */

	public synchronized void initialize() {

		String jsonString = null;
		String key = null;
		ViewPort cellViewPort = null;
		Coordinate topLeft = null;
		Coordinate bottomRight = null;
		Map<String, GraphPoint> graphPoints = null;
		GridWrapper gW = null;
		int gridId = 0;

		try {

			initialized = true;

			// Get a hibernate session
			session = PropertyDAO.getInstance().getSession();

			// Load all the sales at servlet init.
			for (int rowId = 363; rowId < 596; rowId++) {
				
				// Give the server some time to garbage collect and prevent triggering cpu thresholds
				Thread.sleep(5000); 
				System.gc();
				
				for (int cellId = 313; cellId < 604; cellId++) {
					gridId = (rowId * 1000) + cellId;

					cellViewPort = viewPortGridDivisor.getCellViewPort(gridId);

					// Only load properties outside peak area
					if (!viewPortGridDivisor
							.checkIfGridWithinPeakArea(cellViewPort)) {

						topLeft = new Coordinate();
						bottomRight = new Coordinate();
						topLeft.setLat(cellViewPort.getTopLeftCoord().getLat());
						topLeft.setLng(cellViewPort.getTopLeftCoord().getLng());
						bottomRight.setLat(cellViewPort.getBottomRightCoord()
								.getLat());
						bottomRight.setLng(cellViewPort.getBottomRightCoord()
								.getLng());

						List<PropertyMDO> properties = getPropertiesWithinViewport(
								topLeft, bottomRight);

						if (properties != null && properties.size() > 0) {

							// Calculate the averages and quantity for each year
							graphPoints = FastPropertyRetrieveDAO.getInstance()
									.retrieveGridGraphData(cellViewPort);

							gW = new GridWrapper();
							gW.setG(graphPoints);
							gW.setP(properties);

							key = cellViewPort.getTopLeftCoord().getLat()
									+ "-"
									+ cellViewPort.getTopLeftCoord().getLng()
									+ "-"
									+ cellViewPort.getBottomRightCoord()
											.getLat()
									+ "-"
									+ cellViewPort.getBottomRightCoord()
											.getLng();

							jsonString = gson.toJson(gW);
							cache.put(key, jsonString);
							grids++;

							logger.log(Level.INFO, "Caching sale with key ["
									+ key + "] and sale [" + jsonString + "]");

							topLeft = null;
							bottomRight = null;
							cellViewPort = null;
							jsonString = null;
							key = null;
							gW = null;
							graphPoints = null;
							properties = null;
						}
					}

				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.log(Level.SEVERE, e.getMessage());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			session.close();
			session = null;
			topLeft = null;
			bottomRight = null;
			cellViewPort = null;
			jsonString = null;
			key = null;
			gson = null;
			System.gc();
			setupComplete = true;
		}

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter writer = null;
		String jsonString = null;

		try {

			if (!initialized)
				initialize();

			long startTime = System.currentTimeMillis();
			writer = null;
			writer = response.getWriter();
			jsonString = null;

			String key = request
					.getParameter(ExLeinsterRetrievalServlet.CACHE_REQUEST_PARAM_KEY);

			// Dont get properties until cache setup
			if (setupComplete)
				jsonString = cache.get(key);

			writer.print(jsonString);
			writer.close();
			response.setContentType(SystemConstants.UTF_8);

			long endTime = System.currentTimeMillis();
			logger.info("SaleRetrievalServlet key [" + key + "] jsonString ["
					+ jsonString + "] time [" + (endTime - startTime)
					+ "] milliseconds.");

			writer = null;
			jsonString = null;
			// System.gc();

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.log(Level.SEVERE, ex.getMessage());
		} finally {
			writer = null;
			jsonString = null;
			// System.gc();
		}
	}

	public List<PropertyMDO> getPropertiesWithinViewport(Coordinate topLeft,
			Coordinate bottomRight) {

		List<PropertySale> properties = null;
		List<PropertyMDO> convertedProperties = null;
		long startTime = System.currentTimeMillis();

		try {
			properties = PropertyDAO.getInstance()
					.getPropertiesWithinViewportWithSession(topLeft,
							bottomRight, session);

			if (properties.size() > 0)
				logger.log(Level.INFO, "getPropertiesWithinViewport size ["
						+ properties.size() + "]");

			// Convert from HibernateProxy classes before converting to JSON.
			convertedProperties = new ArrayList<PropertyMDO>();

			// TODO: Need to write custom type adaptors for hibernate, see
			// commented code below;
			for (PropertySale property : properties) {
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

				convertedProperties.add(p);

			}

			long endTime = System.currentTimeMillis();
			logger.log(Level.INFO, "Service time is : ["
					+ (endTime - startTime) + "] milliseconds.");

		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			ex.getStackTrace();
		} finally {
			properties = null;
		}

		return convertedProperties;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = null;
		String jsonString = null;
		String jsonStringTmp = null;
		String commaChar = "";
		String[] gridKeys = null;
		String[] jsonString1 = null;

		try {

			if (!initialized)
				initialize();

			// Increment the queries
			queries++;

			long startTime = System.currentTimeMillis();
			writer = null;
			writer = response.getWriter();
			jsonString = null;

			String requestParam = request
					.getParameter(ExLeinsterRetrievalServlet.CACHE_REQUEST_PARAM_GRIDS);

			gridKeys = requestParam.split(",");

			jsonString = "{\"grids\":[";
			for (int i = 0; i < gridKeys.length; i++) {

				try {
					jsonStringTmp = cache.get(gridKeys[i]);

					if (jsonStringTmp != null && !"".equals(jsonStringTmp)) {

						// Add comma if not first element of list
						if (i > 0)
							commaChar = ",";

						// To prevent high load issues with basic server
						// resources, parse the json string to get the graph
						// points
						jsonString1 = jsonStringTmp.split("graphPoints");
						if (jsonString1[1] != null)
							jsonString = jsonString
									+ commaChar
									+ "{\"graphPoints"
									+ jsonString1[1].substring(0,
											jsonString1[1].length() - 1) + "}";
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					logger.log(Level.SEVERE, ex.getMessage());
				}

			}
			jsonString = jsonString + "]}";

			writer.print(jsonString);
			writer.close();
			response.setContentType(SystemConstants.UTF_8);

			long endTime = System.currentTimeMillis();
			logger.info("SaleRetrievalServlet key [" + jsonString
					+ "] jsonString [" + jsonString + "] time ["
					+ (endTime - startTime) + "] milliseconds.");

			writer = null;
			jsonString = null;
			jsonString1 = null;
			jsonStringTmp = null;
			gridKeys = null;

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.log(Level.SEVERE, ex.getMessage());
		} finally {
			writer = null;
			jsonString = null;
			jsonStringTmp = null;
			gridKeys = null;
			session.close();
			session = null;
			jsonString = null;
			gson = null;
			System.gc();
		}
	}

}
