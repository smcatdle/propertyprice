package com.company.priceengine;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Servlet implementation class NumberServlet
 */
public class TestServlet extends HttpServlet {

    public static final String START_PAGE = "https://www.propertypriceregister.ie";

    private static final long serialVersionUID = 1L;

    private final static Logger logger = Logger.getLogger(TestServlet.class
	    .getName());

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestServlet() {
	super();
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

	    WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);
	    webClient.setThrowExceptionOnFailingStatusCode(false);
	    webClient.getOptions().setThrowExceptionOnScriptError(false);
	    webClient.setUseInsecureSSL(true);
	    //webClient.closeAllWindows();
	    //webClient.getOptions().setJavaScriptEnabled(true);


	    webClient.closeAllWindows();

	    HtmlPage page = (HtmlPage) webClient.getPage(START_PAGE);

	    logger.log(Level.INFO, page.asXml());

	} catch (FailingHttpStatusCodeException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (GeneralSecurityException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
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

}
