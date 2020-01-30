package com.company.priceengine;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.company.exception.GoogleLimitException;
import com.company.geo.Coordinate;
import com.company.geo.ViewPort;
import com.company.geo.ViewPortGridDivisor;
import com.company.propertyprice.model.Address;
import com.company.propertyprice.model.GeoCode;

public class GoogleGeocodeFacade {

	private static final Logger logger = Logger
			.getLogger(GoogleGeocodeFacade.class.getName());

	// URL prefix to the geocoder
	private static final String GEOCODER_REQUEST_PREFIX_FOR_XML = "https://maps.google.com/maps/api/geocode/xml";
	private static final String GEOCODER_ACCESS_KEY = ""; // "AIzaSyBMjcPIPGluEkG3tk7JiZ0g34wASHBSsr4";
	private static final int GOOGLE_LIMIT = 2490;

	private static GoogleGeocodeFacade instance = null;
	private static int limitCount = 0;
	private ViewPortGridDivisor viewPortGridDivisor = null;

	private GoogleGeocodeFacade() {
		viewPortGridDivisor = new ViewPortGridDivisor(ViewPort.VIEWPORT_IRELAND);
	}

	public static GoogleGeocodeFacade getInstance() {

		if (instance == null) {
			instance = new GoogleGeocodeFacade();
		}

		return instance;
	}

	public GeoCode getGeocode(final Address address)
			throws GoogleLimitException {

		// First check the Google limits have not been reached
		if (limitCount >= GOOGLE_LIMIT)
			throw new GoogleLimitException();

		GeoCode geocode = null;
		GeoCode geocodeBackup = null;
		Address addressCopy = address.deepCopy();
		GeoCode geocodeCopy = null;
		Document doc1 = null;

		try {

			doc1 = callGoogleGeoCodeAPI(addressCopy);
			geocode = extractGeocodeFromDocument(doc1);

			// If the location_type is approximate and type is 'locality', try
			// again removing address_line_2
			if (!GeoCode.GEO_STATUS_CODE_OVER_QUERY_LIMIT.equals(geocode
					.getStatus())
					&& ("".equals(geocode.getLocationType())
							|| GeoCode.GEO_LOCATION_TYPE_CODE_APPROXIMATE
									.equals(geocode.getLocationType()) || GeoCode.GEO_LOCATION_TYPE_CODE_GEOMETRIC_CENTER
								.equals(geocode.getLocationType()))) {

				// Keep a copy of the first geocode in case no improvement.
				geocodeCopy = geocode;

				/*
				 * addressCopy.setAddressLine1(removeLeftOfDigits(addressCopy
				 * .getAddressLine1()));
				 * 
				 * // Sleep to prevent 'QUERY_OVER_LIMIT' by calls in quick //
				 * succession. Thread.sleep(1000);
				 * 
				 * doc1 = callGoogleGeoCodeAPI(addressCopy); geocode =
				 * extractGeocodeFromDocument(doc1);
				 * 
				 * if (!GeoCode.GEO_STATUS_CODE_OVER_QUERY_LIMIT.equals(geocode
				 * .getStatus()) && ("".equals(geocode.getLocationType()) ||
				 * GeoCode.GEO_LOCATION_TYPE_CODE_APPROXIMATE
				 * .equals(geocode.getLocationType()) ||
				 * GeoCode.GEO_LOCATION_TYPE_CODE_GEOMETRIC_CENTER
				 * .equals(geocode.getLocationType()))) {
				 */

				addressCopy.setAddressLine2("");

				// Sleep to prevent 'QUERY_OVER_LIMIT' by calls in quick
				// succession.
				Thread.sleep(1000);

				// TODO: Record each formatted result and use string comparison to use best one.
				
				doc1 = callGoogleGeoCodeAPI(addressCopy);
				geocode = extractGeocodeFromDocument(doc1);

				// If the location_type is approximate and type is still
				// 'locality', try removing address_line_3
				if ("".equals(geocode.getLocationType())
						|| GeoCode.GEO_LOCATION_TYPE_CODE_APPROXIMATE
								.equals(geocode.getLocationType())
						|| GeoCode.GEO_LOCATION_TYPE_CODE_GEOMETRIC_CENTER
								.equals(geocode.getLocationType())) {
					addressCopy.setAddressLine3("");

					// Sleep to prevent 'QUERY_OVER_LIMIT' by calls in quick
					// succession.
					Thread.sleep(1000);

					doc1 = callGoogleGeoCodeAPI(addressCopy);
					geocode = extractGeocodeFromDocument(doc1);

					// If the location_type is approximate and type is still
					// 'locality', try removing address_line_3
					if ("".equals(geocode.getLocationType())
							|| GeoCode.GEO_LOCATION_TYPE_CODE_APPROXIMATE
									.equals(geocode.getLocationType())
							|| GeoCode.GEO_LOCATION_TYPE_CODE_GEOMETRIC_CENTER
									.equals(geocode.getLocationType())) {
						addressCopy.setAddressLine3("");

						// Sleep to prevent 'QUERY_OVER_LIMIT' by calls in
						// quick succession.
						Thread.sleep(1000);

						// Backup last geocode result
						geocodeBackup = geocode;
						
						addressCopy = address.deepCopy();
						addressCopy.setAddressLine1("");
						doc1 = callGoogleGeoCodeAPI(addressCopy);
						geocode = extractGeocodeFromDocument(doc1);
						
						if ("".equals(geocode.getLocationType())
								|| GeoCode.GEO_LOCATION_TYPE_CODE_APPROXIMATE
										.equals(geocode.getLocationType())
								|| GeoCode.GEO_LOCATION_TYPE_CODE_GEOMETRIC_CENTER
										.equals(geocode.getLocationType())) {
							
							// This result is no better so revert back to previous geocode result
							geocode = geocodeBackup;
						}
					}
				}
			}

			// }

		} catch (Exception e) {

			logger.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
		}

		// No improvement, so use copy.
		if (GeoCode.GEO_LOCATION_TYPE_CODE_APPROXIMATE.equals(geocode
				.getLocationType())
				|| GeoCode.GEO_LOCATION_TYPE_CODE_GEOMETRIC_CENTER
						.equals(geocode.getLocationType())) {
			geocode = geocodeCopy;
		}

		geocode.setGridId(getGridId(geocode));
		
		logger.log(Level.INFO, "The latitude is : [" + geocode.getLatitude()
				+ "] longitude [" + geocode.getLongitude() + "] gridId [" + geocode + "]");
		
		return geocode;
	}

	public boolean checkIfProblematicAddress(Address address,
			Document geocoderResultDocument) {

		XPath xpath = XPathFactory.newInstance().newXPath();
		NodeList resultNodeList = null;

		try {

			// Check if the address is approximate
			resultNodeList = (NodeList) xpath
					.evaluate(
							"/GeocodeResponse/result[1]/geometry/location_type [text() = 'APPROXIMATE']",
							geocoderResultDocument, XPathConstants.NODESET);
			if (resultNodeList.getLength() > 0) {

				logger.log(Level.INFO, "Found [" + resultNodeList.getLength()
						+ "] approximations");
				return true;
			}

			// Check locality looks right
			/*
			 * resultNodeList = (NodeList)
			 * xpath.evaluate("/GeocodeResponse/result", geocoderResultDocument,
			 * XPathConstants.NODESET);
			 * 
			 * logger.log(Level.INFO, "Found [" + resultNodeList.getLength() +
			 * "] results");
			 * 
			 * if (resultNodeList.getLength() > 0) {
			 * 
			 * for (int i = 0; i < resultNodeList.getLength() ; i++) { NodeList
			 * nodes = (NodeList) xpath.evaluate("/address_component",
			 * resultNodeList.item(i), XPathConstants.NODESET);
			 * 
			 * for (int j = 0; j < nodes.getLength() ; j++) { Node node =
			 * nodes.item(j); String locality =
			 * ((NodeList)xpath.evaluate("/address_component", node,
			 * XPathConstants.NODESET)).item(0).getTextContent(); if
			 * (!address.getAddressLine2().contains(locality) ||
			 * !address.getAddressLine3().contains(locality)) { return true; } }
			 * } }
			 */

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public GeoCode extractGeocodeFromDocument(Document doc) {

		GeoCode geocode = new GeoCode();

		try {

			String status = "";
			String locationType = "";
			boolean partialMatch = false;
			int results = 0;
			String type = "";

			// prepare XPath
			XPath xpath = XPathFactory.newInstance().newXPath();

			// extract the result
			NodeList resultNodeList = null;

			// Formatted Address
			NodeList formattedAddressNodeList = (NodeList) xpath.evaluate(
					"/GeocodeResponse/result[1]/formatted_address", doc,
					XPathConstants.NODESET);
			String formattedAddressString = (formattedAddressNodeList
					.getLength() > 0) ? formattedAddressNodeList.item(0)
					.getTextContent() : "";

			// Status
			NodeList statusNodeList = (NodeList) xpath.evaluate(
					"/GeocodeResponse/status", doc, XPathConstants.NODESET);
			String statusString = (statusNodeList.getLength() > 0) ? statusNodeList
					.item(0).getTextContent() : "";

			if (GeoCode.GEO_STATUS_OK.equals(statusString)) {
				status = GeoCode.GEO_STATUS_CODE_OK;
			} else if (GeoCode.GEO_STATUS_INVALID_REQUEST.equals(statusString)) {
				status = GeoCode.GEO_STATUS_CODE_INVALID_REQUEST;
			} else if (GeoCode.GEO_STATUS_OVER_QUERY_LIMIT.equals(statusString)) {
				status = GeoCode.GEO_STATUS_CODE_OVER_QUERY_LIMIT;
			} else if (GeoCode.GEO_STATUS_REQUEST_DENIED.equals(statusString)) {
				status = GeoCode.GEO_STATUS_CODE_REQUEST_DENIED;
			} else if (GeoCode.GEO_STATUS_ZERO_RESULTS.equals(statusString)) {
				status = GeoCode.GEO_STATUS_CODE_ZERO_RESULTS;
			} else if (GeoCode.GEO_STATUS_UNKNOWN_ERROR.equals(statusString)) {
				status = GeoCode.GEO_STATUS_CODE_UNKNOWN_ERROR;
			}

			// Location Type
			NodeList locationTypeNodeList = (NodeList) xpath.evaluate(
					"/GeocodeResponse/result[1]/geometry/location_type", doc,
					XPathConstants.NODESET);
			String locationTypeString = (locationTypeNodeList.getLength() > 0) ? locationTypeNodeList
					.item(0).getTextContent() : "";

			if (GeoCode.GEO_LOCATION_TYPE_APPROXIMATE
					.equals(locationTypeString)) {
				locationType = GeoCode.GEO_LOCATION_TYPE_CODE_APPROXIMATE;
			} else if (GeoCode.GEO_LOCATION_TYPE_GEOMETRIC_CENTER
					.equals(locationTypeString)) {
				locationType = GeoCode.GEO_LOCATION_TYPE_CODE_GEOMETRIC_CENTER;
			} else if (GeoCode.GEO_LOCATION_TYPE_RANGE_INTERPOLATED
					.equals(locationTypeString)) {
				locationType = GeoCode.GEO_LOCATION_TYPE_CODE_RANGE_INTERPOLATED;
			} else if (GeoCode.GEO_LOCATION_TYPE_ROOFTOP
					.equals(locationTypeString)) {
				locationType = GeoCode.GEO_LOCATION_TYPE_CODE_ROOFTOP;
			}

			// Partial Match
			NodeList partialMatchNodeList = (NodeList) xpath
					.evaluate(
							"/GeocodeResponse/result[1]/partial_match [text() = 'true']",
							doc, XPathConstants.NODESET);
			partialMatch = (partialMatchNodeList.getLength() > 0) ? true
					: false;

			// Results
			NodeList resultsNodeList = (NodeList) xpath.evaluate(
					"/GeocodeResponse/result", doc, XPathConstants.NODESET);
			results = (resultsNodeList.getLength() > 0) ? resultsNodeList
					.getLength() : 0;

			// Type
			NodeList typeNodeList = (NodeList) xpath.evaluate(
					"/GeocodeResponse/result[1]/type", doc,
					XPathConstants.NODESET);
			String typeString = (typeNodeList.getLength() > 0) ? typeNodeList
					.item(0).getTextContent() : "";

			if (GeoCode.GEO_TYPE_STREET_ADDRESS.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_STREET_ADDRESS;
			} else if (GeoCode.GEO_TYPE_ROUTE.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_ROUTE;
			} else if (GeoCode.GEO_TYPE_INTERSECTION.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_INTERSECTION;
			} else if (GeoCode.GEO_TYPE_POLITICAL.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_POLITICAL;
			} else if (GeoCode.GEO_TYPE_COUNTRY.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_COUNTRY;
			} else if (GeoCode.GEO_TYPE_ADMINISTRATIVE_AREA_LEVEL_1
					.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_ADMINISTRATIVE_AREA_LEVEL_1;
			} else if (GeoCode.GEO_TYPE_ADMINISTRATIVE_AREA_LEVEL_2
					.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_ADMINISTRATIVE_AREA_LEVEL_2;
			} else if (GeoCode.GEO_TYPE_ADMINISTRATIVE_AREA_LEVEL_3
					.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_ADMINISTRATIVE_AREA_LEVEL_3;
			} else if (GeoCode.GEO_TYPE_ADMINISTRATIVE_AREA_LEVEL_4
					.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_ADMINISTRATIVE_AREA_LEVEL_4;
			} else if (GeoCode.GEO_TYPE_ADMINISTRATIVE_AREA_LEVEL_5
					.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_ADMINISTRATIVE_AREA_LEVEL_5;
			} else if (GeoCode.GEO_TYPE_COLLOQUIAL_AREA.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_COLLOQUIAL_AREA;
			} else if (GeoCode.GEO_TYPE_LOCALITY.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_LOCALITY;
			} else if (GeoCode.GEO_TYPE_SUBLOCALITY.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_SUBLOCALITY;
			} else if (GeoCode.GEO_TYPE_SUBLOCALITY_LEVEL_1.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_SUBLOCALITY_LEVEL_1;
			} else if (GeoCode.GEO_TYPE_SUBLOCALITY_LEVEL_2.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_SUBLOCALITY_LEVEL_2;
			} else if (GeoCode.GEO_TYPE_SUBLOCALITY_LEVEL_3.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_SUBLOCALITY_LEVEL_3;
			} else if (GeoCode.GEO_TYPE_SUBLOCALITY_LEVEL_4.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_SUBLOCALITY_LEVEL_4;
			} else if (GeoCode.GEO_TYPE_SUBLOCALITY_LEVEL_5.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_SUBLOCALITY_LEVEL_5;
			} else if (GeoCode.GEO_TYPE_NEIGHBORHOOD.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_NEIGHBORHOOD;
			} else if (GeoCode.GEO_TYPE_PREMISE.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_PREMISE;
			} else if (GeoCode.GEO_TYPE_SUBPREMISE.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_SUBPREMISE;
			} else if (GeoCode.GEO_TYPE_POSTAL_CODE.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_POSTAL_CODE;
			} else if (GeoCode.GEO_TYPE_NATURAL_FEATURE.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_NATURAL_FEATURE;
			} else if (GeoCode.GEO_TYPE_AIRPORT.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_AIRPORT;
			} else if (GeoCode.GEO_TYPE_PARK.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_PARK;
			} else if (GeoCode.GEO_TYPE_POINT_OF_INTEREST.equals(typeString)) {
				type = GeoCode.GEO_TYPE_CODE_POINT_OF_INTEREST;
			}

			resultNodeList = (NodeList) xpath.evaluate(
					"/GeocodeResponse/result[1]/geometry/location/*", doc,
					XPathConstants.NODESET);
			float lat = 0;
			float lng = 0;
			for (int i = 0; i < resultNodeList.getLength(); ++i) {
				Node node = resultNodeList.item(i);
				if ("lat".equals(node.getNodeName()))
					lat = Float.parseFloat(node.getTextContent());
				if ("lng".equals(node.getNodeName()))
					lng = Float.parseFloat(node.getTextContent());

			}

			geocode.setLatitude(lat);
			geocode.setLongitude(lng);
			geocode.setFormattedAddress(formattedAddressString);
			geocode.setStatus(status);
			geocode.setLocationType(locationType);
			geocode.setPartialMatch(partialMatch);
			geocode.setResults(results);
			geocode.setType(type);
			geocode.setDateCreated(new Date());
			geocode.setDateUpdated(new Date());

		} catch (XPathExpressionException xpeex) {
			logger.info(xpeex.getMessage());
			xpeex.printStackTrace();
		}

		return geocode;
	}

	public Document callGoogleGeoCodeAPI(Address address) {

		Document geocoderResultDocument = null;
		HttpURLConnection conn = null;

		try {

			// Reformat address before processing
			Address formattedAddress = GoogleGeocodeAddressHelper
					.reformatAddress(address);

			String addressText = formattedAddress.concatAddress();
			URL url = null;
			String urlString = GEOCODER_REQUEST_PREFIX_FOR_XML + "?address="
					+ URLEncoder.encode(addressText, "UTF-8")
					+ "&sensor=false&key=" + GEOCODER_ACCESS_KEY;

			url = new URL(urlString);
			logger.log(Level.INFO, "GeoCode urlString [" + urlString + "]");
			// prepare an HTTP connection to the geocoder
			conn = (HttpURLConnection) url.openConnection();

			// open the connection and get results as InputSource.
			conn.connect();
			InputSource geocoderResultInputSource = new InputSource(
					conn.getInputStream());
			// logger.log(Level.INFO,
			// IOUtils.convertFromStream(geocoderResultInputSource.getByteStream()));

			// read result and parse into XML Document
			geocoderResultDocument = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(geocoderResultInputSource);
		} catch (MalformedURLException mue) {
			logger.info(mue.getMessage());
			mue.printStackTrace();
		} catch (IOException ioex) {
			logger.info(ioex.getMessage());
			ioex.printStackTrace();
		} catch (ParserConfigurationException pce) {
			logger.info(pce.getMessage());
			pce.printStackTrace();
		} catch (SAXException saex) {
			logger.info(saex.getMessage());
			saex.printStackTrace();
		} finally {
			conn.disconnect();
			limitCount++;
		}

		return geocoderResultDocument;

	}

	public static void main(String[] args) {

		try {
			Address address = new Address();
			address.setAddressLine1("102 Ryevale lawns");
			address.setAddressLine2("Leixlip");
			address.setAddressLine3("Co Kildare");
			address.setAddressLine3("");
			address.setAddressLine3("Kildare");

			GoogleGeocodeFacade facade = GoogleGeocodeFacade.getInstance();
			GeoCode geocode = facade.getGeocode(address);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
		}
	}

	public int getGridId(GeoCode geocode) {
		Coordinate coord = new Coordinate((float)geocode.getLatitude(), (float)geocode.getLongitude());
		return viewPortGridDivisor.getGridReferenceId(coord);

	}
	
	public String removeLeftOfDigits(String string) {
		String strippedString = "";

		String[] stringSplit = string.split("[0-9]");

		if (stringSplit != null && stringSplit.length > 0)
			strippedString = stringSplit[stringSplit.length - 1];

		return strippedString;

	}
}
