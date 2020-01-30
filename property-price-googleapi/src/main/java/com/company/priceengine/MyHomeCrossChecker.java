/**
 * 
 */
package com.company.priceengine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.company.propertyprice.model.Address;
import com.company.propertyprice.model.GeoCode;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.UrlFetchWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.gae.GAEUtils;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author smcardle
 *
 */
public class MyHomeCrossChecker {

	private final Logger logger = Logger.getLogger(MyHomeCrossChecker.class
			.getName());

	public static final String START_PAGE = "http://myhome.ie/priceregister?RegionID=&LocalityIDs=&MinYear=2010&MaxYear=2015&Keywords=";

	protected DocumentBuilder xmlDocumentBuilder;
	protected WebClient webClient;

	public MyHomeCrossChecker() {

		webClient = new WebClient(BrowserVersion.FIREFOX_17);
		webClient.setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.closeAllWindows();
		
		try {
			webClient.setUseInsecureSSL(true);
		} catch (GeneralSecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

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

	public GeoCode crossCheck(Address address, double price) {

		GeoCode geocode = null;

		HtmlPage page;
		try {
			webClient.getOptions().setJavaScriptEnabled(false);
			webClient.getOptions().setTimeout(4000);
			
			String addressString = address.concatAddress();
			String formattedAddress = addressString.replace("Ireland", "");
			webClient.closeAllWindows();
			page = (HtmlPage) webClient.getPage(START_PAGE
					+ URLEncoder.encode(formattedAddress));

			//Thread.sleep(2000);

			logger.log(Level.INFO, START_PAGE + formattedAddress);

			//Thread.sleep(2000L);
			HtmlElement htmlElement = page.getDocumentElement();
			List<HtmlElement> list = (List<HtmlElement>) htmlElement
					.getByXPath("//div  [contains(@class,'priceRegisterList')]//li");
			logger.log(Level.INFO, "Found [" + list.size() + "] matches.");
			if (list.size() == 0)
				logger.log(Level.INFO, "No cross check found for ["
						+ addressString + "]");

			HtmlElement inputElement = (HtmlElement) list.get(0);
			String link = null;

			// Loop through each match
			for (int i = 0; i < list.size(); i++) {

				try {

					inputElement.setNodeValue(address.concatAddress());

					List<HtmlElement> resultList = (List<HtmlElement>) htmlElement
							.getByXPath("//div [contains(@class,'details')]/a");
					HtmlAnchor resultElement = (HtmlAnchor) resultList.get(i);

					// FIXME: narrow on price and sale date
					link = "http://www.myhome.ie"
							+ resultElement.getHrefAttribute();
					logger.log(Level.INFO, "Using match [" + link + "].");

					webClient.closeAllWindows();
					page = (HtmlPage) webClient.getPage(link);

					geocode = parseLatLng(page.asXml());
					geocode.setFormattedAddress(link);
					logger.log(Level.INFO,
							"GeoCode is [" + geocode.getLatitude() + "] : ["
									+ geocode.getLongitude() + "]");

					// No need to continue looping if geocode found
					if (geocode.getLatitude() != 0
							&& geocode.getLongitude() != 0 && parsePrice(page.asXml()) == price)
						break;

				} catch (Exception ex) {
					logger.log(Level.SEVERE, "Error retrieving geocode for ["
							+ link + ']');
				}

			}

			return geocode;

		} catch (FailingHttpStatusCodeException e) {

			e.printStackTrace();
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} 
		 

		return geocode;
	}

	// ToDo: Move to utility class
	protected NodeList getProductItemNode(String htmlString, String xpathString) {

		XPathExpression expression = null;
		NodeList nodes = null;

		try {

			ByteArrayInputStream input = new ByteArrayInputStream(
					htmlString.getBytes("UTF-8"));
			org.w3c.dom.Document doc = xmlDocumentBuilder.parse(input);
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			expression = xpath.compile(xpathString);
			Object object = expression.evaluate(doc, XPathConstants.NODESET);
			nodes = (NodeList) object;
			input = null;
			doc = null;
			expression = null;

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return nodes;
	}

	private GeoCode parseLatLng(String xml) {

		String[] latSplit = xml.split("lat: \"");
		String[] lngSplit = xml.split("lng: \"");

		String[] latSplit1 = latSplit[1].split("\"");
		String[] lngSplit1 = lngSplit[1].split("\"");

		GeoCode geocode = new GeoCode();
		geocode.setLatitude(new Double(latSplit1[0]).doubleValue());
		geocode.setLongitude(new Double(lngSplit1[0]).doubleValue());

		return geocode;

	}
	
	private double parsePrice(String xml) {

		String[] priceSplit = xml.split("€");
		String[] priceSplit1 = priceSplit[1].split("<");

		double price = new Double(priceSplit1[0].replace(",", "").trim());
		return price;

	}
}
