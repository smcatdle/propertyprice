/**
 * 
 */
package com.company.priceengine;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.company.geo.Coordinate;
import com.company.propertyprice.model.Address;
import com.company.propertyprice.model.GeoCode;
import com.company.propertyprice.model.OSIAddressMatch;
import com.company.utils.SentenceComparison;
import com.company.utils.StringComparisonMetrics;
import com.company.utils.network.HttpUtils;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.UrlFetchWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.gae.GAEUtils;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author smcardle
 *
 */
public class OSIGeocodeFacade {

	private final Logger logger = Logger.getLogger(OSIGeocodeFacade.class
			.getName());

	public static final String START_PAGE = "http://maps.osi.ie/publicviewer/getGaz.aspx?v=1.2&a=txt&c=&t=";

	public static final String GET_COORDS_URL = "http://maps.osi.ie/publicviewer/getGaz.aspx?v=1.2&a=zft&ftf=";
	public static final String GET_COORDS_URL_TRAIL = "&fbu=";
	public static final String POST_CONVERT_ITM_TO_GPS = "http://www.osi.ie/calculators/converter_index.asp#results";

	protected DocumentBuilder xmlDocumentBuilder;
	protected WebClient webClient;

	public OSIGeocodeFacade() {

		webClient = new WebClient(BrowserVersion.FIREFOX_17);
		webClient.closeAllWindows();
		webClient.setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setJavaScriptEnabled(true);

		if (GAEUtils.isGaeMode()) {
			webClient.setWebConnection(new UrlFetchWebConnection(webClient));
		}

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		try {
			xmlDocumentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {

			e.printStackTrace();
		}

		logger.log(Level.INFO, "PPRPriceEngine()");
	}

	// FIXME: Need to add second lookup if 'House Number' failed, then get GPS
	// details of best match.
	// Try removing address_line_2
	public GeoCode osiSearch(Address address) {

		webClient.getOptions().setJavaScriptEnabled(true);
		long distance = 0;
		GeoCode geocode = new GeoCode();
		HtmlPage page;
		try {

			String addressString = address.concatAddress();
			String formattedAddress = addressString.replace("Ireland", "");
			String results = HttpUtils.httpRequest(
					START_PAGE + URLEncoder.encode(formattedAddress),
					HttpUtils.METHOD_GET, null, null);

			List<OSIAddressMatch> osiResults = parseOSIResults(results);
			String txtHouseNo = parseTxtHouseNo(results);

			OSIAddressMatch bestMatch = findBestOSIMatch(osiResults,
					formattedAddress);

			logger.log(Level.INFO, "The best matching osi result is : ["
					+ bestMatch.getAddressString() + "] with matching index ["
					+ bestMatch.getMatchingIndex() + "]");

			// Now lookup the xy coords
			webClient.closeAllWindows();

			String url = GET_COORDS_URL + bestMatch.getIndex()
					+ GET_COORDS_URL_TRAIL + txtHouseNo;
			logger.log(Level.INFO, "The matching OSI url is : [" + url
					+ "]");
			String osiLocationResult = HttpUtils.httpRequest(url,
					HttpUtils.METHOD_GET, null, null);
			formattedAddress = bestMatch.getAddressString();
			geocode.setPartialMatch(false);
			
			// Try again without house number if error matching house number
			if (osiLocationResult != null
					&& osiLocationResult.contains("Sorry")) {
				
				// Record that this only a partial match (i.e. no house number match).
				geocode.setPartialMatch(true);				
				
				url = GET_COORDS_URL + bestMatch.getIndex()
						+ GET_COORDS_URL_TRAIL;
				osiLocationResult = HttpUtils.httpRequest(url,
						HttpUtils.METHOD_GET, null, null);
				formattedAddress = bestMatch.getAddressString().replace(
						txtHouseNo, "");
				
			}

			// TODO: Try OSI search again with just address_line_1 and address_line_5 when consensus, then mark as partial match. 
			// This will narrow on exact street.
			
			
			logger.log(Level.INFO, "The best matching osi result is : ["
					+ osiLocationResult + "]");

			String[] osiLocationResultSplit = osiLocationResult.split("'");
			String[] xySplit = osiLocationResultSplit[1].split(",");
			String x = xySplit[0];
			String y = xySplit[1];
			Coordinate coordinate = convertToGPS(x, y);

			geocode.setFormattedAddress(formattedAddress);
			geocode.setLatitude(coordinate.getLat());
			geocode.setLongitude(coordinate.getLng());
			geocode.setId(new Integer(bestMatch.getId()).intValue());

			// Set Partial Match if less than 65%
			if (bestMatch.getMatchingIndex() < 0.65) {
				geocode.setPartialMatch(true);
			} 

		} catch (FailingHttpStatusCodeException e) {

			e.printStackTrace();
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return geocode;
	}

	/*
	 * txtResults[0]= new Data('T38019413','Meridian Court Spindrift Avenue
	 * Royal Canal Park Dublin'); txtResults[1]= new Data('T38019413','Discovery
	 * Spindrift Avenue Royal Canal Park Dublin'); txtResults[2]= new
	 * Data('T37348826','Watermint Court Royal Canal Avenue Royal Canal Park
	 * Dublin'); txtResults[3]= new Data('T37348826','Compass Court South Royal
	 * Canal Avenue Royal Canal Park Dublin'); txtResults[4]= new
	 * Data('T37348812','Watermint Court Pelletstown Avenue Royal Canal Park
	 * Dublin'); txtResults[5]= new Data('T37348817','Watermint Court Phelan
	 * Avenue Royal Canal Park Dublin'); txtResults[6]= new
	 * Data('T37348823','Clearwater Court North Camden Avenue Royal Canal Park
	 * Dublin'); txtResults[7]= new Data('T37348812','Cassian Court South
	 * Pelletstown Avenue Royal Canal Park Dublin'); txtResults[8]= new
	 * Data('T37348823','Compass Court West Camden Avenue Royal Canal Park
	 * Dublin'); txtResults[9]= new Data('T37348812','Clearwater Court South
	 * Pelletstown Avenue Royal Canal Park Dublin'); txtResults[10]= new
	 * Data('T37348817','Cassian Court East Phelan Avenue Royal Canal Park
	 * Dublin'); txtResults[11]= new Data('T37348812','Compass Court North
	 * Pelletstown Avenue Royal Canal Park Dublin');
	 */
	private List<OSIAddressMatch> parseOSIResults(String resultString) {
		ArrayList<OSIAddressMatch> osiResults = new ArrayList<OSIAddressMatch>();

		String[] splitResults = resultString.split("new Data");
		// exclude split 0 and last
		for (int i = 1; i < splitResults.length - 1; i++) {
			String resultStringRow = splitResults[i];

			String[] resultStringRecords = resultStringRow.split("'");
			String index = resultStringRecords[1];
			String addressString = resultStringRecords[3];

			OSIAddressMatch match = new OSIAddressMatch();
			match.setIndex(index);
			match.setAddressString(addressString);
			osiResults.add(match);
		}

		return osiResults;
	}

	private String parseTxtHouseNo(String resultString) {

		String[] houseNumberSplit = resultString.split("txtHouseNo");
		String[] txtHouseNoSplit = houseNumberSplit[1].split("\"");
		String txtHouseNo = txtHouseNoSplit[1];

		return txtHouseNo;
	}

	// TODO: Needs more work to match addresses like '4 Fulham Terrace', this
	// matches '4 Park Terrace'???
	private OSIAddressMatch findBestOSIMatch(List<OSIAddressMatch> osiResults,
			String address) {
		OSIAddressMatch bestMatch = new OSIAddressMatch();
		double highestMatchingIndex = 0;
		int id = 1;

		try {
			for (OSIAddressMatch match : osiResults) {
				String resultAddress = match.getAddressString().trim();
				String searchAddress = address.replace(",", " ");

				double matchingIndex = StringComparisonMetrics.compareStrings(
						resultAddress, searchAddress);
				boolean alternativeMatchingIndex = SentenceComparison
						.stringsSimilar(resultAddress, searchAddress);
				int wordMatchIndex = SentenceComparison
						.getMostMatchingWordsFromBeginning(resultAddress,
								searchAddress);

				matchingIndex = matchingIndex + (wordMatchIndex * .1);

				if (matchingIndex > highestMatchingIndex) {
					bestMatch = match;
					bestMatch.setId(Integer.toString(id++));
					bestMatch.setMatchingIndex(matchingIndex);
					highestMatchingIndex = matchingIndex;
				}
			}
		} catch (Exception ex) {
			logger.log(Level.INFO, ex.getMessage());
		}

		return bestMatch;
	}

	private Coordinate convertToGPS(String x, String y) {

		Coordinate coordinate = null;

		try {
			webClient.closeAllWindows();
			HtmlPage page = (HtmlPage) webClient
					.getPage(POST_CONVERT_ITM_TO_GPS);

			//Thread.sleep(2000L);

			HtmlElement htmlElement = page.getDocumentElement();

			// Easting
			List<HtmlElement> eastingList = (List<HtmlElement>) htmlElement
					.getByXPath("//input [contains(@name,'itmEast')]");
			HtmlInput eastingElement = (HtmlInput) eastingList.get(0);
			eastingElement.setValueAttribute(x);

			// Northing
			List<HtmlElement> northingList = (List<HtmlElement>) htmlElement
					.getByXPath("//input [contains(@name,'itmNorth')]");
			HtmlInput northingElement = (HtmlInput) northingList.get(0);
			northingElement.setValueAttribute(y);

			// Submit Button
			List<HtmlElement> submitList = (List<HtmlElement>) htmlElement
					.getByXPath("//input [contains(@name,'itmConvert')]");
			HtmlElement submitElement = (HtmlElement) submitList.get(0);
			HtmlPage returnPage = submitElement.click();

			htmlElement = returnPage.getDocumentElement();

			// Latitude Degrees
			List<HtmlElement> latDegreesList = (List<HtmlElement>) htmlElement
					.getByXPath("//input [contains(@name,'latDeg')]");
			HtmlInput latDegreesElement = (HtmlInput) latDegreesList.get(0);
			String latDegrees = latDegreesElement.getAttribute("value");

			// Latitude Minutes
			List<HtmlElement> latMinutesList = (List<HtmlElement>) htmlElement
					.getByXPath("//input [contains(@name,'latMin')]");
			HtmlInput latMinutesElement = (HtmlInput) latMinutesList.get(0);
			String latMinutes = latMinutesElement.getAttribute("value");

			// Latitude Seconds
			List<HtmlElement> latSecondsList = (List<HtmlElement>) htmlElement
					.getByXPath("//input [contains(@name,'latSec')]");
			HtmlInput latSecondsElement = (HtmlInput) latSecondsList.get(0);
			String latSeconds = latSecondsElement.getAttribute("value");

			// Longitude Degrees
			List<HtmlElement> lngDegreesList = (List<HtmlElement>) htmlElement
					.getByXPath("//input [contains(@name,'longDeg')]");
			HtmlInput lngDegreesElement = (HtmlInput) lngDegreesList.get(0);
			String lngDegrees = lngDegreesElement.getAttribute("value");

			// Longitude Minutes
			List<HtmlElement> lngMinutesList = (List<HtmlElement>) htmlElement
					.getByXPath("//input [contains(@name,'longMin')]");
			HtmlInput lngMinutesElement = (HtmlInput) lngMinutesList.get(0);
			String lngMinutes = lngMinutesElement.getAttribute("value");

			// Longitude Seconds
			List<HtmlElement> lngSecondsList = (List<HtmlElement>) htmlElement
					.getByXPath("//input [contains(@name,'longSec')]");
			HtmlInput lngSecondsElement = (HtmlInput) lngSecondsList.get(0);
			String lngSeconds = lngSecondsElement.getAttribute("value");

			// logger.log(Level.INFO, "The coversion result is : [" +
			// returnPage.asXml() + "]");

			/*
			 * 
			 * decimaldegrees=degrees+frac{minutes}{60}+frac{seconds}{3600}
			 */
			double latMinutesVal = (new Double(latMinutes).doubleValue() / 60.0);
			double latSecondsVal = (new Double(latSeconds).doubleValue() / 3600.0);
			double latDegreesVal = new Double(latDegrees).doubleValue();
			double latRadians = new Double(latDegrees).doubleValue()
					+ latMinutesVal + latSecondsVal;
			double lngMinutesVal = (new Double(lngMinutes).doubleValue() / 60.0);
			double lngSecondsVal = (new Double(lngSeconds).doubleValue() / 3600.0);
			double lngDegreesVal = new Double(lngDegrees).doubleValue();
			double lngRadians = lngDegreesVal - lngMinutesVal - lngSecondsVal;
			logger.log(Level.INFO, "The latitude is : [" + latRadians + "]");
			logger.log(Level.INFO, "The longitude is : [" + lngRadians + "]");

			coordinate = new Coordinate();
			coordinate.setLat(new Float(latRadians).floatValue());
			coordinate.setLng(new Float(lngRadians).floatValue());

		} catch (IOException e) {

			e.printStackTrace();
		}

		return coordinate;
	}
}
