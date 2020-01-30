/** Property Price Engine   **/

package com.company.priceengine;

import com.company.priceengine.exception.EngineShuttingDownException;
import com.company.propertyprice.model.*;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXException;


public abstract class PriceEngine implements IPriceEngine {

    
    protected static final String AMP = "&";
    protected static final String AMP_SAFE = "&amp;";
    protected static final String IMAGE_END = "/IDShot_90x90.jpg";
    protected static final String VIEW_MODE_PARAM = "viewMode";
    protected static final String ACTION_PARAM = "action";
    protected static final String HTTP_STRING = "http://";
    protected static final String NEXT_PAGE_STRING = "&Nao=";
    protected static final String NEXT_PAGE_LAST_SCREEN = "Refine your search";
    protected static final String PRODUCT_STRING = "new TESCO.sites.UI.entities.Product";
    protected static final String PRODUCT_LIST_SCREEN = "next";
    protected static final String TESCO_DEPARTMENT_LINK = "www.tesco.ie/groceries/department/default.aspx?";
    protected static final String TESCO_PRODUCT_LINK = "www.tesco.ie/groceries/product/browse/default.aspx?";
    protected static final String OUTPUT_FILE = "TescoProducts.xml";
    private final Logger logger = Logger.getLogger(PriceEngine.class.getName());
    private Gson gson;
    protected Set previousPageLinks;
    private Set previousProducts;
    protected int pageLinkCounter;
    private int counter;
    protected int pageIncrement;
    protected File outputFile;
    protected String outputFileName;
    private BufferedOutputStream outputStream;
    protected boolean recursive;
    protected PrintWriter writer;
    protected String nextPageLastScreen;
    protected String nextPageString;
    protected String productListScreen;
    protected String productString;
    protected WebClient webClient;
    protected String initialGroceryPage;
    protected XPath xpath;
    protected DocumentBuilder xmlDocumentBuilder;
    protected String chain;
    protected String initialPageLink = null;
    protected boolean disableProcessingPhase;
    
    
    
    public PriceEngine(String chain) {
    	
        gson = new Gson();
        previousPageLinks = new HashSet();
        previousProducts = new HashSet();
        pageLinkCounter = 0;
        counter = 0;
        pageIncrement = 20;
        outputFile = null;
        outputFileName = null;
        outputStream = null;
        writer = null;
        nextPageLastScreen = null;
        nextPageString = null;
        productListScreen = null;
        productString = null;
        webClient = null;
        initialGroceryPage = null;
        xpath = null;
        xmlDocumentBuilder = null;
        this.chain = null;
        LogFactory.getFactory();
        logger.log(Level.INFO, "PriceEngine()");
        nextPageLastScreen = "Refine your search";
        nextPageString = "&Nao=";
        productListScreen = "next";
        productString = "new TESCO.sites.UI.entities.Product";
        this.chain = chain;
        recursive = true;
        
        try {
        	
            XPathFactory factory = XPathFactory.newInstance();
            xpath = factory.newXPath();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            xmlDocumentBuilder = documentBuilderFactory.newDocumentBuilder();
             
        } catch(ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void createOutputFile() {
    	
        try {
        	
            outputFile = new File(outputFileName);
            outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
            writer = new PrintWriter(outputStream);
            writer.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            writer.print("<A>");
            
        } catch(FileNotFoundException ex) {
            logger.log(Level.SEVERE, ex.toString());
        }
    }

    public void closeOutputFile() {
    	
        try {
            writer.print("</A>");
            writer.close();
            outputStream.close();
            
        } catch(FileNotFoundException ex) {
            logger.log(Level.SEVERE, ex.toString());
        } catch(IOException ioex) {
            logger.log(Level.SEVERE, ioex.toString());
        }
    }

    public String retrieveInitialHTMLPage(String urlString) {
        return retrieveHTMLPage(urlString);
    }

    public String retrieveHTMLPage(String urlString) {
    	
	HttpURLConnection uc = null;
        String pageHTML = null;
        String line = null;
        URL url = null;
        
        try {
            url = new URL(urlString);
            uc = (HttpURLConnection)url.openConnection();
            uc.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            for(pageHTML = ""; (line = in.readLine()) != null; pageHTML = (new StringBuilder()).append(pageHTML).append(line).toString());
            uc.disconnect();
            in.close();
            in = null;
            uc = null;
            url = null;
            
        } catch(IOException e) {
            logger.log(Level.SEVERE, e.toString());
            e.printStackTrace();
        } finally {
            uc.disconnect();
        }
        
        return pageHTML;
    }

    public List<String> findPageLinks(String pageLink, List searchLinks) { 
    	
	List<String> pageLinks = null;
        String newPageLink = null;
        String htmlPage = retrieveHTMLPage(pageLink);
        pageLinks = new ArrayList<String>();

        for (String link : (List<String>)searchLinks) {
        	
            String splitHtmlPage[] = htmlPage.split(link);
            if(splitHtmlPage.length > 1) {
            	
                for(int i = 1; i < splitHtmlPage.length; i++) {
                	
                    String currentSplitHtmlPage = splitHtmlPage[i];
                    if(currentSplitHtmlPage.substring(1, 2).equals("N")) {
                    	
                        String extractedLink = splitHtmlPage[i].substring(1, splitHtmlPage[i].indexOf("\"")).replace("&amp;", "&");
                        String extractedLinkActionRemoved[] = extractedLink.split("action");
                        String extractedLinkViewModeRemoved[] = extractedLinkActionRemoved[0].split("viewMode");
                        newPageLink = (new StringBuilder()).append("http://").append(link).append(extractedLinkViewModeRemoved[0]).toString();
                        String formattedPageLink = newPageLink;
                        
                        if(!previousPageLinks.contains(formattedPageLink)) {
                            pageLinks.add(formattedPageLink);
                            pageLinkCounter++;
                        }
                        
                        extractedLink = null;
                        extractedLinkActionRemoved = null;
                        extractedLinkViewModeRemoved = null;
                        newPageLink = null;
                        formattedPageLink = null;
                    }
                    currentSplitHtmlPage = null;
                }

                splitHtmlPage = null;
            }
        }
        
        htmlPage = null;
        return pageLinks;
    }

    public List findPropertyElements(String pageLink, String id) {
    	
        List htmlElements = null;
        htmlElements = new ArrayList();
        String htmlPage = retrieveHTMLPage(pageLink);
        String splitHtmlPage[] = htmlPage.split(id);
        if(splitHtmlPage.length > 1) {
            for(int i = 1; i < splitHtmlPage.length; i++) {
            	
                String productAsString = splitHtmlPage[i].substring(0, splitHtmlPage[i].indexOf(");"));
                PropertyElement product = new PropertyElement(productAsString);
                if(htmlPage.contains("promo")) {
                    product.setHtmlPage(htmlPage);
                    product.setPageLink(pageLink);
                    htmlElements.add(product);
                }
                productAsString = null;
            }

        }
        
        htmlPage = null;
        splitHtmlPage = null;
        return htmlElements;
    }

    public int start(String initialPage, List searchLinks) {
    	
    	try {
	        String pageText = null;
	        //pageText = retrieveInitialHTMLPage(initialPage);
	        findProducts(initialPage, searchLinks);
	        webClient.closeAllWindows();
	        
	        
    	} catch (EngineShuttingDownException esdex) {
    		logger.log(Level.SEVERE, "A shutdown exception has been thrown : " + esdex.getMessage());
    	} 
        
        logger.log(Level.INFO, "The engine has completed.");
        
        return getProductCount();

    }

    public void findProducts(String pageLink, List searchLinks) throws EngineShuttingDownException {
        
    	try {
    		
            webClient.closeAllWindows();
            List<String> pageLinks = null;
            String processingLink = null;

	    // Search for links if not set set in searchLinks
	    if (searchLinks == null || searchLinks.size() == 0) {
		HtmlPage page = (HtmlPage) webClient.getPage("http://www.daft.ie/dublin-city/houses-for-sale/?s[area_type]=on&s[advanced]=1");
		logger.info("Finding page links");
		pageLinks = findPageLinks(pageLink, searchLinks);
	    } else {   // NB: Use the searchLinks provided (If we don't hit this initial page, performance is very poor
		webClient.closeAllWindows();
		HtmlPage page = (HtmlPage) webClient.getPage("http://www.daft.ie/dublin-city/houses-for-sale/?s[area_type]=on&s[advanced]=1");
		Thread.sleep(2000L);
		pageLinks = searchLinks;
	    }

	    logger.info("Found [" + pageLinks.size() + "] links");
	    
	    // Has processing of the links been disabled (e.g. for scenarios where only slaves server should process links).
	    //if (disableProcessingPhase) return;	    
	    
            for (String link : pageLinks) {
            	logger.info("Current Memory : Free [" + Runtime.getRuntime().freeMemory() + "] Total [" + Runtime.getRuntime().totalMemory() + "]");
            	int pageLinksCounter = 0;
            	
            	processingLink = link;
            	
                // Is there a 'next' link with more properties?
                while (!previousPageLinks.contains(processingLink) || hasNextPage(processingLink)) {
                	
                    if (previousPageLinks.contains(processingLink)) {
                	processingLink = findNextLink(pageLink, processingLink,0);
                    } 
                    
                    previousPageLinks.add(processingLink);
                    //String htmlPage = retrieveHTMLPage(processingLink);
                    logger.log(Level.WARNING, (new StringBuilder()).append("processingLink : No: [" + pageLinksCounter + "] link : [").append(processingLink).append("]").toString());
                    
                    // Extract the product
                    List<PropertyElement> propertyElements = findPropertyElements(processingLink, productString);
                    for (PropertyElement propertyElement : propertyElements) {
                    	
                        IProperty property = extractPropertyElement(propertyElement);
                        if(property != null && !previousProducts.contains(property.getPropertyId())) {
                        	previousProducts.add(property.getPropertyId());
                        }
                        
                        property = null;
                    }
                    System.gc();
                }
                
                pageLinksCounter++;
            }
            
        /*} catch (EngineShuttingDownException esdex) {
        	throw esdex;*/
        } catch(Exception ex) {
            logger.log(Level.SEVERE, ex.toString());
            ex.printStackTrace();
        }
    }

    public boolean hasNextPage(String htmlPage) {
        return htmlPage.contains(productListScreen);
    }

    public IProperty extractPropertyElement(PropertyElement productElement) throws EngineShuttingDownException {
    	
        return null;
    }

    public void writeProductInfo(IProperty product) {
    	
        initialiseProductOutput(product);
        writeProductOutput(product);
        finalizeProductOutput(product);
    }

    public void initialiseProductOutput(IProperty product) {
        counter++;
        writer.print("<X>");
    }

    public void writeProductOutput(IProperty product) {
    }

    public void finalizeProductOutput(IProperty product) {
        writer.print("</X>");
    }


    public void addPromotion(IProperty iproduct, String s) {
    	
    }

    public String replaceSpecialHtmlChars(String text) {
    	
        String replacedText = null;
        replacedText = text;
        replacedText.replace("&", "&amp;");
        return replacedText;
    }

    protected NodeList getProductItemNode(PropertyElement productElement, String xpathString) {
    	
        XPathExpression expression = null;
        NodeList nodes = null;
        String htmlString = productElement.getProductAsString();
        
        try {
        	
            ByteArrayInputStream input = new ByteArrayInputStream(htmlString.getBytes("UTF-8"));
            org.w3c.dom.Document doc = xmlDocumentBuilder.parse(input);
            expression = xpath.compile(xpathString);
            Object object = expression.evaluate(doc, XPathConstants.NODESET);
            nodes = (NodeList)object;
            input = null;
            doc = null;
            expression = null;
            
        } catch(XPathExpressionException e) {
            e.printStackTrace();
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch(SAXException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        return nodes;
    }

    protected int getProductCount() {
    	return 0;   	
    }
    
    protected String getNodeAttribute(Node node, String attributeName)  {
    	
        String attributeValue = null;
        NamedNodeMap attributes = node.getAttributes();
        int count = 0;
        
        while (attributes != null && attributes.getLength() > count) {
            Node attributeNode = attributes.item(count);
            if(attributeNode.getNodeName().equals(attributeName)) {
                attributeValue = attributeNode.getNodeValue();
            }
            
            count++;
        } 
        
        attributes = null;
        return attributeValue;
    }

    public static double round(double valueToRound, int numberOfDecimalPlaces) {
    	
        double multipicationFactor = Math.pow(10D, numberOfDecimalPlaces);
        double interestedInZeroDPs = valueToRound * multipicationFactor;
        return (double)Math.round(interestedInZeroDPs) / multipicationFactor;
    }
    
    protected abstract String findNextLink(String originalPageLink,
	    String pageLink, int pageIndex);
    
}
