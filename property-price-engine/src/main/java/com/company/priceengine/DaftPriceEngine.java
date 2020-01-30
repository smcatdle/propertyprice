/** PPR Property Price Engine   **/

package com.company.priceengine;

import com.company.propertyprice.dao.DaftPropertyDAO;
import com.company.propertyprice.model.Address;
import com.company.propertyprice.model.Property;
import com.company.propertyprice.model.PropertyElement;
//import com.company.propertyprice.dao.DaftPropertyDAO;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.gae.GAEUtils;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.WebClientUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.exception.ConstraintViolationException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DaftPriceEngine extends PriceEngine {

    //protected static final String START_PAGE = "http://www.daft.ie/dublin-city/houses-for-sale/?s%5Barea_type%5D=on&s%5Badvanced%5D=1&offset=10";
	protected static final String START_PAGE = "http://www.daft.ie/ireland/property-for-sale/?s[advanced]=1&s[sort_by]=date&s[sort_type]=d&searchSource=sale&offset=";
	protected static SimpleDateFormat dateFormatter = null;
    protected static String dateFormat = "dd/MM/yyyy";
    public static final String DAFT_LISTING = "d";
    public static final String DEFAULT_PAGE = "http://www.daft.ie";
    public int pageCounter = 0;

    private final Logger logger = Logger.getLogger(DaftPriceEngine.class
	    .getName());

    // protected GoogleDriveFacade facade = null;
    protected int counter = 0;

    public DaftPriceEngine() {

	super(DAFT_LISTING);

	try {

	    System.getProperties().put(
		    "org.apache.commons.logging.simplelog.defaultlog", "trace");

	    dateFormatter = new SimpleDateFormat(dateFormat);
	    // facade = GoogleDriveFacade.getInstance();

	    webClient = new WebClient(BrowserVersion.FIREFOX_17);
	    webClient.setThrowExceptionOnFailingStatusCode(false);
	    webClient.getOptions().setThrowExceptionOnScriptError(false);
	    webClient.getOptions().setJavaScriptEnabled(false);
	    webClient.setUseInsecureSSL(true);
	    // webClient.setAjaxController(new
	    // NicelyResynchronizingAjaxController());

	    if (GAEUtils.isGaeMode()) {
		webClient
			.setWebConnection(new UrlFetchWebConnection(webClient));
	    }
	} catch (FailingHttpStatusCodeException e) {
	      
	    e.printStackTrace();
	} catch (GeneralSecurityException e) {
	      
	    e.printStackTrace();
	}

	logger.log(Level.INFO, "DaftPriceEngine()");
    }

    public String retrieveInitialHTMLPage(String urlString) {

	System.getProperties().put(
		"org.apache.commons.logging.simplelog.defaultlog", "trace");

	try {
	    webClient.closeAllWindows();
	    HtmlPage page1 = (HtmlPage) webClient.getPage(urlString);
	    return page1.asXml();

	} catch (MalformedURLException muex) {
	    logger.severe("Exception : " + muex);
	} catch (IOException ioex) {
	    logger.severe("Exception : " + ioex);
	}

	return null;
    }

    public void writeProductInfo(Property product) {

	super.writeProductInfo(product);
	Property pprProduct = null;
	pprProduct = (Property) product;
    }

    public List<String> findPageLinks(String pageLink, List searchLinks) {

	List<String> pageLinks = null;
	pageLinks = new ArrayList<String>();

	pageLinks.add(pageLink);

	return pageLinks;
    }

    public boolean hasNextPage(String htmlLink) {

	HtmlPage page;
	try {
	    webClient = new WebClient(BrowserVersion.FIREFOX_17);
	    webClient.setThrowExceptionOnFailingStatusCode(false);
	    webClient.getOptions().setThrowExceptionOnScriptError(false);
	    webClient.getOptions().setJavaScriptEnabled(false);
	    webClient.setUseInsecureSSL(true);
	    page = (HtmlPage) webClient.getPage(htmlLink);

	    Thread.sleep(1000L);

	    // logger.log(Level.SEVERE, "Page" + page.asXml());
	    // Check if there is a 'next' link
	    HtmlElement htmlElement = page.getDocumentElement();
	    List<HtmlElement> htmlElements = (List<HtmlElement>) htmlElement
		    .getByXPath("//li [contains(@class,'next_page')]/a");
	    HtmlElement nextElement = htmlElements.get(0);
	    String text = nextElement.getTextContent().trim();

	    if ("Next".equals(text)) {
		logger.log(Level.INFO, "Has Next Link [" + nextElement + "]");
		return true;
	    }

	} catch (FailingHttpStatusCodeException e) {
	      
	    e.printStackTrace();
	} catch (MalformedURLException e) {
	      
	    e.printStackTrace();
	} catch (IOException e) {
	      
	    e.printStackTrace();
	} catch (InterruptedException e) {
	      
	    e.printStackTrace();
	} catch (Exception e) {
	      
	    e.printStackTrace();
	}

	return false;
    }

    public String findNextLink(String originalPageLink, String pageLink,
	    int pageIndex) {

	    String nextLink = null;
	    
	try {

	    pageCounter = pageCounter + 10;
	    nextLink = START_PAGE + pageCounter;

	    logger.log(Level.INFO, "Next Link [" + nextLink + "]");

	} catch (Exception ex) {
	    logger.log(Level.SEVERE, ex.toString());
	}

	return nextLink;
    }

    public List findPropertyElements(String pageLink, String id) {

	List productElements = null;
	productElements = new ArrayList();

	try {

	    webClient.closeAllWindows();

	    HtmlPage page = (HtmlPage) webClient.getPage(pageLink);
	    Thread.sleep(2000L);
	    HtmlElement htmlElement = page.getDocumentElement();
	    List<HtmlElement> elements = (List<HtmlElement>) htmlElement
		    .getByXPath("//tbody/tr/td/div [contains(@class,'box')]");

	    for (HtmlElement element : elements) {
		PropertyElement propertyElement = null;
		propertyElement = new PropertyElement(element.asXml());
		propertyElement.setPageLink(pageLink);
		productElements.add(propertyElement);
	    }

	} catch (MalformedURLException e) {
	    logger.log(Level.SEVERE, e.toString());
	    e.printStackTrace();
	} catch (FailingHttpStatusCodeException e) {
	    logger.log(Level.SEVERE, e.toString());
	    e.printStackTrace();
	} catch (IOException e) {
	    logger.log(Level.SEVERE, e.toString());
	    e.printStackTrace();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	return productElements;
    }

    public Property extractPropertyElement(PropertyElement propertyElement) {

	Property property = null;
	String propertyIdString = "";
	double price = 0;
	Address address = null;
	String imageUrlString = "";
	boolean propertyImageAvailable = false;
	property = new Property();

	try {

	    // Check the box type (don't process 'sponsored' boxes).
	    NodeList boxTypeNode = getProductItemNode(propertyElement,
		    "//div [contains(@class,'sponsored')]");
	    if (boxTypeNode == null || boxTypeNode.getLength() == 0) {

		// Property Info
		Node peopertyIdNode = getProductItemNode(propertyElement,
			"//a/img [contains(@id,'pp_')]").item(0);
		if (peopertyIdNode != null) {
		    propertyIdString = getNodeAttribute(peopertyIdNode, "id")
			    .replace("pp_", "");
		}

		Node priceNode = getProductItemNode(propertyElement,
			"//strong [@class='price']").item(0);
		if (priceNode != null) {
		    String priceString = priceNode.getTextContent().trim();
		    int indexEuroSymbol = priceString.indexOf("€");
		    try {
			price = new Double(priceString
				.substring(indexEuroSymbol + 1)
				.replace(",", "").replace("*", ""))
				.doubleValue();
		    } catch (Exception ex) {

		    }
		}

		Node addressNode = getProductItemNode(propertyElement,
			"//div [@class='search_result_title_box'] /h2/a").item(0);
		if (addressNode != null) {
		    String addressString = addressNode.getTextContent().trim();
		    int dashDelimiterIndex = addressString.indexOf("-");
		    String addressText = addressString.substring(0,
			    dashDelimiterIndex - 2);
		    address = parseAdressString(addressText);
		}

		Node imageNode = getProductItemNode(propertyElement,
			"//img [contains(@id,'pp_')]").item(0);
		if (imageNode != null) {
		    imageUrlString = DEFAULT_PAGE
			    + getNodeAttribute(imageNode, "src");
		    propertyImageAvailable = !imageUrlString
			    .contains("daft_no_photo");
		}

		// Goto the property page to get extra info
		gotoFullPage(addressNode, property);

		NodeList infoNodes = getProductItemNode(propertyElement,
			"//div/ul [contains(@class,'info')]/li");
		if (infoNodes != null) {
		    parseDaftInfoNodes(infoNodes, property);
		}

		// Save the property
		property.setPropertyId(new Integer(propertyIdString).intValue());
		// property.setImageUrl(imageUrlString);
		property.setPrice(price);
		property.setAddress(address);
		property.setDateCreated(new Date());
		property.setDateUpdated(new Date());

		logger.log(Level.INFO, "property [" + property.getPropertyId()
			+ "]");

		/*
		 * if (propertyImageAvailable) {
		 * 
		 * // Download the image url for the property java.io.File f =
		 * new java.io.File(propertyIdString + ".jpg"); OutputStream
		 * stream = new FileOutputStream(f);
		 * HttpUtils.httpRequest(imageUrlString, HttpUtils.METHOD_GET,
		 * null, null); String fileId = facade.insertFile("test.jpg");
		 * 
		 * // Record the fileId retrieved from Google Drive
		 * property.setFileId(fileId); }
		 */

		try {
		    DaftPropertyDAO.getInstance().attachDirty(property);
			logger.log(Level.INFO, "Saved Property [" + property.getPropertyId()
					+ "] counter [" + counter + "]");
			counter++;
		} catch (ConstraintViolationException ex) {

		    // Update the 'date_updated' field if the property already
		    // exists
		    DaftPropertyDAO.getInstance().update(property);
			logger.log(Level.INFO, "Updated Property [" + property.getPropertyId()
					+ "] counter [" + counter + "]");
			counter++;
		}

	    }

	} catch (Exception ex) {
	    logger.log(Level.SEVERE, ex.toString());
	}

	return property;
    }

    // Parse the daft nodes into property info
    private void parseDaftInfoNodes(NodeList nodes, Property property) {

	try {

	    for (int i = 0; i < nodes.getLength(); i++) {

		Node node = nodes.item(i);
		String text = node.getTextContent();
		String formattedText = text.replace("|", "").replace("\n", "");
		formattedText.trim();

		// Check what type of property info
		if (formattedText.contains("House")
			|| formattedText.contains("Apartment")
			|| formattedText.contains("Bungalow")
			|| formattedText.contains("Duplex")
			|| formattedText.contains("Townhouse")) {
		    String dwellingType = formattedText.replace("for sale", "");
		    property.setDwellingType(dwellingType.trim());

		} else if (formattedText.contains("Beds")) {
		    String beds = formattedText.replace(" ", "");
		    property.setBeds(new Integer(beds.replace("Bed", "")
			    .replace("s", "")).intValue());
		} else if (formattedText.contains("Bath")) {
		    String baths = formattedText.replace(" ", "");
		    property.setBaths(new Integer(baths.replace("Bath", "")
			    .replace("s", "")).intValue());
		}
	    }
	} catch (Exception ex) {

	}
    }

    private Address parseAdressString(String addressString) {

	Address address = new Address();

	// Parse the comma delimited address
	String[] addressSplit = addressString.replace("\n", "").split(",");

	for (int i = 0; i < addressSplit.length; i++) {

	    switch (i) {
	    case 0:
		address.setAddressLine1(addressSplit[0]);
		break;
	    case 1:
		address.setAddressLine2(addressSplit[1]);
		break;
	    case 2:
		address.setAddressLine3(addressSplit[2]);
		break;
	    case 3:
		address.setAddressLine4(addressSplit[3]);
		break;
	    case 4:
		address.setAddressLine5(addressSplit[4]);
		break;
	    }
	}

	address.setDateCreated(new Date());
	address.setDateUpdated(new Date());

	return address;
    }

    protected void gotoFullPage(Node addressNode, Property property) {

	String sizeNodeString = "";
	double size = 0;

	try {
	    String hrefString = DEFAULT_PAGE
		    + getNodeAttribute(addressNode, "href");

	    webClient.closeAllWindows();
	    HtmlPage page = (HtmlPage) webClient.getPage(hrefString);
	    
	    String url = getNodeAttribute(addressNode, "href");
	    property.setUrl(url);
	    //String[] urlSplit = url.split("/");
	    //if (urlSplit != null) property.setUrl(urlSplit[2]);

	    Thread.sleep(2000L);

	    HtmlElement htmlElement = page.getDocumentElement();

	    // Get the maps tab
	    HtmlElement mapElement = (HtmlElement) htmlElement.getByXPath(
		    "//span [contains(@id,'smi-map-link')]").get(0);
	    /*
	     * if (mapElement != null) { getMap(mapElement, property); }
	     */

	    /*
	     * HtmlElement descriptionElement = (HtmlElement) htmlElement
	     * .getByXPath("//div [contains(@id,'description')]").get(0); if
	     * (descriptionElement != null) { String descriptionElementString =
	     * descriptionElement.getTextContent().trim();
	     * property.setDescription(descriptionElementString); }
	     */

	    List<HtmlElement> list = (List<HtmlElement>) htmlElement
		    .getByXPath("//div [contains(@class,'description_block')]");

	    for (HtmlElement element : list) {
		if (element != null) {
		    sizeNodeString = element.getTextContent().trim();
		    if (sizeNodeString.contains("Sq")) {

			String[] sizeNodeSplit1 = sizeNodeString.split(":");
			String[] sizeNodeSplit2 = sizeNodeSplit1[1].split("Sq");
			size = new Double(sizeNodeSplit2[0].trim());
			property.setSize(size);
		    }
		}
	    }

	} catch (InterruptedException e) {
	      
	    e.printStackTrace();
	} catch (FailingHttpStatusCodeException e) {
	      
	    e.printStackTrace();
	} catch (MalformedURLException e) {
	      
	    e.printStackTrace();
	} catch (Exception e) {
	      
	    e.printStackTrace();
	}
    }

    protected void getMap(HtmlElement element, Property property) {

	try {

	    webClient.closeAllWindows();
	    HtmlPage page = (HtmlPage) element.click();

	    Thread.sleep(2000L);

	    HtmlElement htmlElement = page.getDocumentElement();

	    List<HtmlElement> mapList = (List<HtmlElement>) htmlElement
		    .getByXPath("//a [contains(@href,'maps')]");
	    HtmlElement mapElement = (HtmlElement) mapList.get(0);
	    if (mapElement != null) {
		String mapNodeString = mapElement.getAttribute("href").trim();
		String[] mapNodeStringSplit1 = mapNodeString.split("/");
		String[] mapNodeStringSplit2 = mapNodeStringSplit1[4]
			.split(",");
		double lat = new Double(mapNodeStringSplit2[0]);
		double lng = new Double(mapNodeStringSplit2[1]);
	    }

	} catch (InterruptedException e) {
	      
	    e.printStackTrace();
	} catch (FailingHttpStatusCodeException e) {
	      
	    e.printStackTrace();
	} catch (MalformedURLException e) {
	      
	    e.printStackTrace();
	} catch (Exception e) {
	      
	    e.printStackTrace();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.company.priceengine.IPriceEngine#findProductsOnNextScreen(java.lang
     * .String, java.lang.String, int)
     */
    @Override
    public void findProductsOnNextScreen(String s, String s1, int i) {
	// TODO Auto-generated method stub

    }
    
    @Override
    protected int getProductCount() {
    	return counter;   	
    }
}
