/**
 * 
 */
package com.company.propertyprice.dao;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.company.geo.Coordinate;
import com.company.geo.GraphPoint;
import com.company.geo.ViewPort;
import com.company.propertyprice.model.mdo.PropertyMDO;

public class FastPropertyRetrieveDAO {

	private static final Log logger = LogFactory
			.getLog(FastPropertyRetrieveDAO.class);

	// FIXME: Store these settings in an xml file on server
	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_CONNECTION = "jdbc:mysql://53b6d7bd5973cab5da0000b1-propertyprod.rhcloud.com:51486/sales";
	private static final String DB_USER = "adminefhLDUF";
	private static final String DB_PASSWORD = "D2TRnm-tz2j4";

	/*
	 * private static final String DB_DRIVER = "com.mysql.jdbc.Driver"; private
	 * static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/sales";
	 * private static final String DB_USER = "root"; private static final String
	 * DB_PASSWORD = "";
	 */

	private static FastPropertyRetrieveDAO instance = null;
	private Connection dbConnection = null;

	public static FastPropertyRetrieveDAO getInstance() {

		if (instance == null) {
			instance = new FastPropertyRetrieveDAO();
		}

		return instance;
	}

	private FastPropertyRetrieveDAO() {
		dbConnection = getDBConnection();
	}

	public List<PropertyMDO> selectRecordsFromTable() throws SQLException {

		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		PropertyMDO propertyMDO = null;
		List<PropertyMDO> list = new ArrayList<PropertyMDO>();

		String selectSQL = "select a.address_line_1 as address1, a.address_line_2 as address2, a.address_line_3 as address3, a.address_line_4 as address4, a.address_line_5 as address5, p.price as price, p.date_of_sale as date from address a, property_sale p, geocode g where a.id = p.address_id and a.geocode_id = g.id and g.latitude >= 53.28046 and g.longitude >= -6.2278833 and g.latitude <= 53.300457 and g.longitude <= -6.20";

		try {

			long startTime = System.currentTimeMillis();
			preparedStatement = dbConnection.prepareStatement(selectSQL);

			// execute select SQL stetement
			rs = preparedStatement.executeQuery();

			while (rs.next()) {

				propertyMDO = new PropertyMDO();
				String address1 = rs.getString("address1");
				String address2 = rs.getString("address2");
				String address3 = rs.getString("address3");
				String address4 = rs.getString("address4");
				String address5 = rs.getString("address5");
				double price = rs.getDouble("price");
				Date date = rs.getDate("date");

				propertyMDO.setA1(address1);
				propertyMDO.setA2(address2);
				propertyMDO.setA3(address3);
				propertyMDO.setA4(address4);
				propertyMDO.setA5(address5);
				propertyMDO.setP(price);
				propertyMDO.setD(date);
				list.add(propertyMDO);

				logger.info("Result : [" + address1 + "," + address2 + ","
						+ address3 + "," + price + "]");

			}

			long endTime = System.currentTimeMillis();
			logger.info("FastPropertyRetrieveDAO is for [" + list.size()
					+ "] and time [" + (endTime - startTime)
					+ "] milliseconds.");

		} catch (SQLException e) {

			logger.error(e.getMessage());
			e.printStackTrace();

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
				preparedStatement = null;

			}
			if (rs != null) {
				rs.close();
				rs = null;
			}

		}

		return list;

	}

	public List<GraphPoint> retrieveGraphData() throws SQLException {

		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		GraphPoint graphPoint = null;
		List<GraphPoint> list = new ArrayList<GraphPoint>();

		String selectSQL = "select count(1) as quantity, sum(price)/count(1) as average, CONCAT(YEAR(date_of_sale), '-', MONTH(date_of_sale)) as sale_date, date_of_sale from property_sale, address a where  a.id = address_id and (address_line_4 like '%Dublin%' or address_line_5 like '%Dublin%' or address_line_3 like '%Dublin%') and price < 2000000 and full_market_price = 1 group by sale_date order by date_of_sale";
		try {

			long startTime = System.currentTimeMillis();
			preparedStatement = dbConnection.prepareStatement(selectSQL);

			// execute select SQL stetement
			rs = preparedStatement.executeQuery();

			while (rs.next()) {

				graphPoint = new GraphPoint();
				double average = rs.getDouble("average");
				double quantity = rs.getDouble("quantity");
				String saleDate = rs.getString("sale_date");

				graphPoint.setA(average);
				graphPoint.setQ(quantity);
				graphPoint.setD(saleDate);
				list.add(graphPoint);

				logger.info("Result : [" + saleDate + " : " + average + "]");

			}

			long endTime = System.currentTimeMillis();
			logger.info("FastPropertyRetrieveDAO:retrieveGraphData is for ["
					+ list.size() + "] and time [" + (endTime - startTime)
					+ "] milliseconds.");

		} catch (SQLException e) {

			logger.info(e.getMessage());
			e.printStackTrace();

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
				preparedStatement = null;
			}
			if (rs != null) {
				rs.close();
				rs = null;
			}

		}

		return list;

	}

	public Map<String, GraphPoint> retrieveGridGraphData(ViewPort viewPort)
			throws SQLException {

		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		GraphPoint graphPoint = null;
		Map<String, GraphPoint> map = new Hashtable<String, GraphPoint>();

		String selectSQL = "select count(1) as quantity, sum(price) as total,YEAR(date_of_sale) as sale_date, date_of_sale from property_sale, address a, geocode g where  a.id = address_id and a.geocode_id = g.id and g.latitude <= "
				+ viewPort.getTopLeftCoord().getLat()
				+ " and g.longitude >= "
				+ viewPort.getTopLeftCoord().getLng()
				+ " and g.latitude >= "
				+ viewPort.getBottomRightCoord().getLat()
				+ " and g.longitude <= "
				+ viewPort.getBottomRightCoord().getLng()
				+ " and price < 2000000 and full_market_price = 1 group by sale_date order by date_of_sale";
		try {

			long startTime = System.currentTimeMillis();
			preparedStatement = dbConnection.prepareStatement(selectSQL);

			// execute select SQL stetement
			rs = preparedStatement.executeQuery();

			while (rs.next()) {

				graphPoint = new GraphPoint();
				double total = rs.getDouble("total");
				double quantity = rs.getDouble("quantity");
				String saleDate = rs.getString("sale_date");

				graphPoint.setT(total);
				graphPoint.setQ(quantity);
				graphPoint.setD(saleDate);
				map.put(saleDate, graphPoint);

				logger.info("Result : [" + saleDate + " : " + total + "]");

			}

			long endTime = System.currentTimeMillis();
			logger.info("FastPropertyRetrieveDAO:retrieveGridGraphData is for ["
					+ map.size()
					+ "] and time ["
					+ (endTime - startTime)
					+ "] milliseconds.");

		} catch (SQLException e) {

			logger.error(e.getMessage());
			e.printStackTrace();

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
				preparedStatement = null;
			}
			if (rs != null) {
				rs.close();
				rs = null;
			}
		}

		return map;

	}

	private static Connection getDBConnection() {

		Connection dbConnection = null;

		try {

			Class.forName(DB_DRIVER);

		} catch (ClassNotFoundException e) {

			logger.error(e.getMessage());
			e.printStackTrace();

		}

		try {

			dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER,
					DB_PASSWORD);
			return dbConnection;

		} catch (SQLException e) {

			logger.error(e.getMessage());
			e.printStackTrace();

		}

		return dbConnection;

	}

	public void finalize() {

		try {
			if (dbConnection != null) {

				dbConnection.close();
				dbConnection = null;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<Integer> getGridsOutsideViewPort(Coordinate topLeft, Coordinate bottomRight) throws SQLException {

		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		GraphPoint graphPoint = null;
		List<Integer> list = new ArrayList<Integer>();
		
		String selectSQL = "select distinct(grid_id) as grid_id from geocode geocode where geocode.grid_id <> 0 and geocode.grid_id not in (select distinct(geocode.grid_id) from geocode geocode where geocode.latitude < " + topLeft.getLat() + " and geocode.longitude > " + topLeft.getLng() + " and geocode.latitude > " + bottomRight.getLat() + " and geocode.longitude < " + bottomRight.getLng() + ")";
		
		logger.info("getGridsOutsideViewPort with sql [" + selectSQL + "]");
		
		try {

			long startTime = System.currentTimeMillis();
			preparedStatement = dbConnection.prepareStatement(selectSQL);

			// execute select SQL stetement
			rs = preparedStatement.executeQuery();

			while (rs.next()) {

				int gridId = rs.getInt("grid_id");
				list.add(gridId);

				logger.info("Result : [" + gridId + "]");

			}

			long endTime = System.currentTimeMillis();
			logger.info("FastPropertyRetrieveDAO:getGridsOutsideViewPort is for ["
					+ list.size() + "] and time [" + (endTime - startTime)
					+ "] milliseconds.");

		} catch (Exception e) {

			logger.info(e.getMessage());
			e.printStackTrace();

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
				preparedStatement = null;
			}
			if (rs != null) {
				rs.close();
				rs = null;
			}

		}

		return list;

	}
}
