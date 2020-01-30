/** PPR Property Price Engine   **/

package com.company.priceengine;

import com.company.exception.GoogleLimitException;
import com.company.priceengine.exception.EngineShuttingDownException;
import com.company.priceengine.managers.PropertyPersistanceManager;
import com.company.priceengine.managers.WorkAllocationManager;
import com.company.propertyprice.dao.PropertyDAO;
import com.company.propertyprice.model.Address;
import com.company.propertyprice.model.GeoCode;
import com.company.propertyprice.model.IProperty;
import com.company.propertyprice.model.PPRSearchVariables;
import com.company.propertyprice.model.PropertyElement;
import com.company.propertyprice.model.PropertySale;
import com.company.propertyprice.model.PropertySaleError;
import com.company.utils.DateUtils;
import com.company.utils.StringUtils;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.gae.GAEUtils;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Node;

public class PPRPriceEngine extends PriceEngine {

	private final Logger logger = Logger.getLogger(PPRPriceEngine.class
			.getName());

	public static final String START_PAGE = "http://www.propertypriceregister.ie";
	public static final String PPR_LISTING = "p";
	public static final String DEFAULT_PAGE = "http://www.propertypriceregister.ie/website/npsra/PPR/npsra-ppr.nsf/";

	protected GoogleGeocodeFacade geocodeFacade = null;
	protected int counter = 0;
	
	private PPRSearchVariables searchCriteria = null;
	private WorkAllocationManager workAllocationManager = null;
	private PropertyPersistanceManager propertyPersistanceManager = null;

	public PPRPriceEngine() {

		super(PPR_LISTING);

		try {

			geocodeFacade = GoogleGeocodeFacade.getInstance();
			workAllocationManager = new WorkAllocationManager();
			propertyPersistanceManager = new PropertyPersistanceManager();
			recursive = false;

			webClient = new WebClient(BrowserVersion.FIREFOX_17);
			webClient.setThrowExceptionOnFailingStatusCode(false);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.closeAllWindows();
			//webClient.getOptions().setJavaScriptEnabled(true);
			webClient.setUseInsecureSSL(true);
			webClient.setJavaScriptEnabled(false);

		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (GAEUtils.isGaeMode()) {
			webClient.setWebConnection(new UrlFetchWebConnection(webClient));
		}

		logger.log(Level.INFO, "PPRPriceEngine()");
	}

	public void writeProductInfo(IProperty product) {

		super.writeProductInfo(product);
	}

	public List<String> findPageLinks(String pageLink, List searchLinks) {

		List<String> pageLinks = null;
		pageLinks = new ArrayList<String>();

		// NB: Only master servers should findPageLinks
		/*
		 * if (!Configuration.getInstance().getServerConfig().isMaster()) {
		 * return null; } else { disableProcessingPhase = true; // Disable the
		 * processing phase, as slaves should process the data. }
		 */

		// int batchSize =
		// Configuration.getInstance().getServerConfig().getBatchSize();
		int batchSize = 800;

		try {

			webClient.closeAllWindows();

			logger.info("Searching for properties for batch of size ["
					+ batchSize + "]");

			// Search through all counties for all years
			for (String year : searchCriteria.getYears()) {
				for (String county : searchCriteria.getCounties()) {

					// First search for the required property
					HtmlPage page = searchProperty(pageLink, year, county);

					Thread.sleep(2000L);
					HtmlElement htmlElement = page.getDocumentElement();
					List<HtmlElement> htmlElements = (List<HtmlElement>) htmlElement
							.getByXPath("//a [contains(@href,'eStampUNID')]");

					for (HtmlElement element : htmlElements) {

						String link = DEFAULT_PAGE
								+ element.getAttribute("href");
						if (!previousPageLinks.contains(link)) {
                            Thread.sleep(1000L);
							// Check the database if this property has already
							// been processed.
							List<PropertySale> sales = PropertyDAO
									.getInstance().findByUrl(
											StringUtils.extractUUID(link));
                            
                            // FIXME: Check why all UUIDs are not being added to DB, is there a bug??
							if (sales.isEmpty()) {
								pageLinks.add(link);
								pageLinkCounter++;
								logger.log(Level.INFO, (new StringBuilder())
										.append("Found Sub Menu Link No: [" + pageLinkCounter + "] link: [")
										.append(link).append("]").toString());
							} /*else {   // Hook to modify existing sales entries during processing
								logger.log(Level.INFO, (new StringBuilder())
										.append("Property already exists in DB : [")
										.append(link).append("]").toString());
								//processExistingSalesHook(sales.get(0));
							}*/

						}
						if (pageLinkCounter >= batchSize)
							break;
					}
					if (pageLinkCounter >= batchSize)
						break;
				}
				if (pageLinkCounter >= batchSize)
					break;
			}

		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.toString());
			ex.printStackTrace();
		}

		// The master now assigns the links to available slave servers to
		// process
		// workAllocationManager.allocate(pageLinks);

		// Don't return links to be processed when the server is master
		// --> Master gathers links
		// --> Slaves process the links
		return pageLinks;
	}

	public HtmlPage searchProperty(String urlString, String year, String county) {

		HtmlPage returnPage = null;

		// NB: Only allow master to search for new properties
		/*
		 * if (!Configuration.getInstance().isMaster()) return null;
		 */

		try {
			webClient.closeAllWindows();
			HtmlPage page = (HtmlPage) webClient.getPage(urlString);
			Thread.sleep(2000L);
			HtmlElement htmlElement = page.getDocumentElement();
			List<HtmlElement> list = (List<HtmlElement>) htmlElement
					.getByXPath("//a [contains(@href,'OpenForm')]");
			HtmlElement element = (HtmlElement) list.get(0);
			HtmlPage searchPage = element.click();

			Thread.sleep(2000L);

			HtmlElement searchHtmlElement = searchPage.getDocumentElement();

			// Set the search criteria

			// County
			List<HtmlElement> countyList = (List<HtmlElement>) searchHtmlElement
					.getByXPath("//select [contains(@id,'County')]");
			HtmlSelect countyElement = (HtmlSelect) countyList.get(0);
			countyElement.setSelectedAttribute(county, true);

			// Year
			List<HtmlElement> yearList = (List<HtmlElement>) searchHtmlElement
					.getByXPath("//select [contains(@id,'Year')]");
			HtmlSelect yearElement = (HtmlSelect) yearList.get(0);
			yearElement.setSelectedAttribute(year, true);

			// Start Month
			List<HtmlElement> startMonthList = (List<HtmlElement>) searchHtmlElement
					.getByXPath("//select [contains(@id,'StartMonth')]");
			HtmlSelect startMonthElement = (HtmlSelect) startMonthList.get(0);
			startMonthElement.setSelectedAttribute("01 - January", true);

			// End Month
			List<HtmlElement> endMonthList = (List<HtmlElement>) searchHtmlElement
					.getByXPath("//select [contains(@id,'EndMonth')]");
			HtmlSelect endMonthElement = (HtmlSelect) endMonthList.get(0);
			endMonthElement.setSelectedAttribute("12 - December", true);

			// Submit Button
			List<HtmlElement> submitMonthList = (List<HtmlElement>) searchHtmlElement
					.getByXPath("//input [contains(@type,'submit')]");
			HtmlElement submitMonthElement = (HtmlElement) submitMonthList
					.get(0);
			returnPage = submitMonthElement.click();

			// logger.log(Level.SEVERE, "The average house price is : [" +
			// calculateAveragePrice(returnPage) + "]");

		} catch (Exception ex) {
			logger.log(Level.SEVERE,
					(new StringBuilder()).append("searchProperty error : ")
							.append(ex.toString()).toString());
			ex.printStackTrace();
		}

		return returnPage;
	}

	public List findPropertyElements(String pageLink, String id) {

		List productElements = null;
		productElements = new ArrayList();

		try {

			webClient.closeAllWindows();

			HtmlPage page = (HtmlPage) webClient.getPage(pageLink);
			Thread.sleep(2000L);

			logger.log(Level.WARNING, "Retrieved page link [" + page.asXml()
					+ "]");

			HtmlElement htmlElement = page.getDocumentElement();
			List<HtmlElement> list = (List<HtmlElement>) htmlElement
					.getByXPath("//div [contains(@id,'userContent')]");

			for (HtmlElement element : list) {
				PropertyElement productElement = null;
				productElement = new PropertyElement(element.asXml());
				productElement.setPageLink(pageLink);
				productElements.add(productElement);
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

	public IProperty extractPropertyElement(PropertyElement propertyElement)
			throws EngineShuttingDownException {

		IProperty property = null;

		try {

			// Address Info
			Node address1Node = getProductItemNode(propertyElement,
					"//table [@id='AddInfo']//td").item(0);
			String address1String = address1Node.getTextContent().trim();

			Node address2Node = getProductItemNode(propertyElement,
					"//table [@id='AddInfo']//td").item(1);
			String address2String = address2Node.getTextContent().trim();

			Node address3Node = getProductItemNode(propertyElement,
					"//table [@id='AddInfo']//td").item(2);
			String address3String = address3Node.getTextContent().trim();

			Node address4Node = getProductItemNode(propertyElement,
					"//table [@id='AddInfo']//td").item(3);
			String address4String = address4Node.getTextContent().trim();

			Node address5Node = getProductItemNode(propertyElement,
					"//table [@id='AddInfo']//td").item(4);
			String address5String = address5Node.getTextContent().trim();

			// Sale Info
			Node dateOfSaleNode = getProductItemNode(propertyElement,
					"//table [@id='SaleInfo']//td").item(1);
			String dateOfSaleString = dateOfSaleNode.getTextContent().trim();

			Node priceNode = getProductItemNode(propertyElement,
					"//table [@id='SaleInfo']//td").item(3);
			String priceString = priceNode.getTextContent().trim();
			double value = new Double(priceString.substring(1).replace(",", "")
					.replace("*", "")).doubleValue();

			Node marketPriceNode = getProductItemNode(propertyElement,
					"//table [@id='SaleInfo']//td").item(5);
			String marketPriceString = marketPriceNode.getTextContent().trim();
			boolean marketPriceFlag = "No".equals(marketPriceString);

			Node vatExclusiveNode = getProductItemNode(propertyElement,
					"//table [@id='SaleInfo']//td").item(7);
			String vatExclusiveString = vatExclusiveNode.getTextContent()
					.trim();
			boolean vatExclusiveFlag = "Yes".equals(vatExclusiveString);

			Node propertySizeNode = getProductItemNode(propertyElement,
					"//table [@id='SaleInfo']//td").item(9);
			String propertySizeString = propertySizeNode.getTextContent()
					.trim();

			// Save the property
			PropertySale sale = new PropertySale();
			Address address = new Address();
			sale.setPrice(value);
			sale.setDateOfSale(DateUtils.getDate(dateOfSaleString));
			sale.setFullMarketPrice(marketPriceFlag);
			sale.setPprUrl(StringUtils.extractUUID(propertyElement
					.getPageLink()));
			sale.setVatExclusive(vatExclusiveFlag);
			sale.setPropertySize(propertySizeString);
			sale.setDateCreated(new Date());
			sale.setDateUpdated(new Date());

			address.setAddressLine1(removeTrailingChar(address1String));
			address.setAddressLine2(removeTrailingChar(address2String));
			address.setAddressLine3(removeTrailingChar(address3String));
			address.setAddressLine4(removeTrailingChar(address4String));
			address.setAddressLine5(removeTrailingChar(address5String));
			address.setDateCreated(new Date());
			address.setDateUpdated(new Date());
			sale.setAddress(address);

			// Lookup the geo settings
			GeoCode geoCode = geocodeFacade.getGeocode(address);
			address.setGeocode(geoCode);

			// Store the sale for later processing
			counter++;
			propertyPersistanceManager.persist(sale);

			logger.log(Level.INFO,
					"Geo settings for address [" + address.getAddressLine1()
							+ "] are : lat [" + geoCode.getLatitude()
							+ "] : long [" + geoCode.getLongitude() + "]");

		} catch (GoogleLimitException glex) {

			logger.log(Level.SEVERE, glex.toString());
			glex.printStackTrace();
			throw new EngineShuttingDownException(glex.toString());
		} catch (Exception ex) {

			logger.log(Level.SEVERE, ex.toString());
		}

		return property;
	}

	private double calculateAveragePrice(HtmlPage page) {

		double averagePrice = 250000;
		double currentTotal = 0;
		int numberProperties = 0;
		double value = 0;

		HtmlElement htmlElement = page.getDocumentElement();
		List<HtmlElement> propertyList = (List<HtmlElement>) htmlElement
				.getByXPath("//table [contains(@class,'resultsTable')]/tbody/tr/td");

		ListIterator iterator = propertyList.listIterator();

		while (iterator.hasNext()) {
			HtmlElement propertyElement = (HtmlElement) iterator.next();
			String nodeValue = propertyElement.asText();

			// Check if this is the price <td>
			if ("€".equals(nodeValue.substring(0, 1))) {
				value = new Double(nodeValue.substring(1).replace(",", "")
						.replace("*", "")).doubleValue();

			}

			// Narrow in on certain locations
			if (propertyElement.hasChildNodes()/*
												 * elements != null &&
												 * !elements.isEmpty() &&
												 * nodeValue
												 * .contains("ABERCORN")
												 */) {

				// DomNodeList<HtmlElement> elements =
				// propertyElement.getFirstElementChild().getElementsByTagName("A");
				// logger.log(Level.SEVERE, "size : [" + elements.size() + "]");

				// Should we exclude this price as an anomaly (TODO: Use
				// 'Log-Distribution')
				// For the moment exclude anything greater than 4 times the
				// average
				if (value < (1000000)) {
					currentTotal = currentTotal + value;
					numberProperties++;
					averagePrice = currentTotal / numberProperties;
					logger.log(Level.SEVERE, "Property : [" + nodeValue
							+ "] value [" + value + "]");

					// Save the property
					PropertySale sale = new PropertySale();
					Address address = new Address();
					sale.setPrice(value);
					sale.setDateOfSale(new Date());
					address.setAddressLine1(nodeValue);
					sale.setAddress(address);

					PropertyDAO.getInstance().attachDirty(sale);
				}
			}
		}

		logger.log(Level.SEVERE, "The total number of properties : ["
				+ numberProperties + "]");

		logger.log(Level.SEVERE, "Average House Price : [" + averagePrice + "]");

		return averagePrice;
	}

	@Override
	public void findProductsOnNextScreen(String s, String s1, int i) {

	}

	private String removeTrailingChar(String str) {

		return str.substring(0, str.length() - 1);
	}

	/**
	 * @return the searchCriteriaList
	 */
	public PPRSearchVariables getSearchCriteriaList() {
		return searchCriteria;
	}

	/**
	 * @param searchCriteriaList
	 *            the searchCriteriaList to set
	 */
	public void setSearchCriteria(PPRSearchVariables searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public void tmpCheckBlankFields(PropertySale existingSale) {
		String currentPageLink = existingSale.getPprUrl();
		List<PropertyElement> propertyElements = findPropertyElements(
				currentPageLink, productString);
		for (PropertyElement propertyElement : propertyElements) {

			try {
				IProperty property = tmpExtractPropertyElement(propertyElement,
						existingSale);
			} catch (EngineShuttingDownException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public IProperty tmpExtractPropertyElement(PropertyElement propertyElement,
			PropertySale existingSale) throws EngineShuttingDownException {

		IProperty property = null;

		try {

			Address existingAddress = existingSale.getAddress();

			// Address Info
			Node address1Node = getProductItemNode(propertyElement,
					"//table [@id='AddInfo']//td").item(0);
			String address1String = address1Node.getTextContent().trim();

			Node address2Node = getProductItemNode(propertyElement,
					"//table [@id='AddInfo']//td").item(1);
			String address2String = address2Node.getTextContent().trim();

			Node address3Node = getProductItemNode(propertyElement,
					"//table [@id='AddInfo']//td").item(2);
			String address3String = address3Node.getTextContent().trim();

			Node address4Node = getProductItemNode(propertyElement,
					"//table [@id='AddInfo']//td").item(3);
			String address4String = address4Node.getTextContent().trim();

			Node address5Node = getProductItemNode(propertyElement,
					"//table [@id='AddInfo']//td").item(4);
			String address5String = address5Node.getTextContent().trim();

			// Sale Info
			Node dateOfSaleNode = getProductItemNode(propertyElement,
					"//table [@id='SaleInfo']//td").item(1);
			String dateOfSaleString = dateOfSaleNode.getTextContent().trim();

			Node priceNode = getProductItemNode(propertyElement,
					"//table [@id='SaleInfo']//td").item(3);
			String priceString = priceNode.getTextContent().trim();
			double value = new Double(priceString.substring(1).replace(",", "")
					.replace("*", "")).doubleValue();

			Node marketPriceNode = getProductItemNode(propertyElement,
					"//table [@id='SaleInfo']//td").item(5);
			String marketPriceString = marketPriceNode.getTextContent().trim();
			boolean marketPriceFlag = "No".equals(marketPriceString);

			Node vatExclusiveNode = getProductItemNode(propertyElement,
					"//table [@id='SaleInfo']//td").item(7);
			String vatExclusiveString = vatExclusiveNode.getTextContent()
					.trim();
			boolean vatExclusiveFlag = "Yes".equals(vatExclusiveString);

			Node propertySizeNode = getProductItemNode(propertyElement,
					"//table [@id='SaleInfo']//td").item(9);
			String propertySizeString = propertySizeNode.getTextContent()
					.trim();

			// Save the property
			PropertySale sale = new PropertySale();
			Address address = new Address();
			sale.setPrice(value);
			sale.setDateOfSale(DateUtils.getDate(dateOfSaleString));
			sale.setFullMarketPrice(marketPriceFlag);
			sale.setPprUrl(propertyElement.getPageLink());
			sale.setVatExclusive(vatExclusiveFlag);
			sale.setPropertySize(propertySizeString);
			sale.setDateCreated(new Date());
			sale.setDateUpdated(new Date());

			address.setAddressLine1(removeTrailingChar(address1String));
			address.setAddressLine2(removeTrailingChar(address2String));
			address.setAddressLine3(removeTrailingChar(address3String));
			address.setAddressLine4(removeTrailingChar(address4String));
			address.setAddressLine5(removeTrailingChar(address5String));
			address.setDateCreated(new Date());
			address.setDateUpdated(new Date());
			sale.setAddress(address);

			// Lookup the geo settings
			// GeoCode geoCode = mapsFacade.getGeocode(address);
			// address.setGeocode(geoCode);

			// logger.log(Level.INFO, "Geo settings for address [" +
			// address.getAddressLine1() + "] are : lat [" +
			// geoCode.getLatitude() + "] : long [" + geoCode.getLongitude() +
			// "]");

			// TODO: Need to handle updates to properties on the
			// PropertyPriceRegister.ie site (I'm not sure how a property price
			// can be updated though ????)
			// These currently show up as unique constraint violation exceptions

			// Only save property if Geocode returned correctly :::: Keep a
			// record of (0.0,0.0)'s to record failed geocodes.
			if (((existingAddress.getAddressLine1() == null || ""
					.equals(existingAddress.getAddressLine1())) && (address
					.getAddressLine1() != null && !"".equals(address
					.getAddressLine1())))
					|| ((existingAddress.getAddressLine2() == null || ""
							.equals(existingAddress.getAddressLine2())) && (address
							.getAddressLine2() != null && !"".equals(address
							.getAddressLine2())))
					|| ((existingAddress.getAddressLine3() == null || ""
							.equals(existingAddress.getAddressLine3())) && (address
							.getAddressLine3() != null && !"".equals(address
							.getAddressLine3())))
					|| ((existingAddress.getAddressLine4() == null || ""
							.equals(existingAddress.getAddressLine4())) && (address
							.getAddressLine4() != null && !"".equals(address
							.getAddressLine4())))) {

				// Save the geo error to investigate later
				PropertySaleError propertySaleError = new PropertySaleError();
				propertySaleError.setIssue("BLA");
				propertySaleError.setPprUrl(propertyElement.getPageLink());
				propertySaleError.setDateCreated(new Date());
				propertySaleError.setDateUpdated(new Date());
				PropertyDAO.getInstance().attachDirty(propertySaleError);
			}

		} catch (Exception ex) {

			logger.log(Level.SEVERE, ex.toString());
		}

		return property;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.company.priceengine.PriceEngine#findNextLink(java.lang.String,
	 * java.lang.String, int)
	 */
	@Override
	protected String findNextLink(String originalPageLink, String pageLink,
			int pageIndex) {

		return null;
	}
	
    @Override
    protected int getProductCount() {
    	return counter;   	
    }
    
	/**
	 * Hook for performing work on existing sales.
	 * 
	 * @param sale
	 */
	public void processExistingSalesHook(PropertySale sale) {
		
		// Set the gridId property on existing sales
		GeoCode geocode = sale.getAddress().getGeocode();
		
		if (geocode.getGridId() == 0) {
			int gridId = geocodeFacade.getGridId(geocode);
			geocode.setGridId(gridId);
			
			// Update the sale;
			PropertyDAO.getInstance().update(sale);
		}
	}
}
