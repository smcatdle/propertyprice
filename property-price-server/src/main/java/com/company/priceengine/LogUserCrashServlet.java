package com.company.priceengine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.company.propertyprice.managers.UserCrashManager;
import com.company.propertyprice.model.UserCrash;
import com.company.utils.SystemConstants;
import com.google.gson.Gson;

/**
 * Servlet implementation class RetrieveGraphDataServlet
 */
public class LogUserCrashServlet extends HttpServlet {

    private static final String USER_CRASH_STRING = "userCrash";

    private static final long serialVersionUID = 1L;

    
    // The number of queries today
    public static int crashes = 0;

    // The last server startup time
    public static Date lastRestartTime = null;
    
    
    private Gson gson = null;

    private final static Logger logger = Logger
	    .getLogger(LogUserCrashServlet.class.getName());

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogUserCrashServlet() {
	super();
	gson = new Gson();
	lastRestartTime = new Date();
    }

    public void init() throws ServletException {

    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {


	try {
	    long startTime = System.currentTimeMillis();
	   
	    
	    PrintWriter writer = null;
	    writer = response.getWriter();
	    String userCrashString = getBody(request);
	    
	    logger.info("LogUserCrashServlet userCrash param : [" + userCrashString + "]");

	    UserCrash userCrash = gson.fromJson(userCrashString,
		    UserCrash.class);

	    UserCrashManager userCrashManager = new UserCrashManager();
	    userCrashManager.loqCrash(userCrash);

	    writer.print("true");
	    writer.close();
	    response.setContentType(SystemConstants.UTF_8);

	    long endTime = System.currentTimeMillis();
	    logger.info("LogUserCrashServlet time [" + (endTime - startTime)
		    + "] milliseconds.");

	    crashes++;
	    
	    System.gc();

	} catch (Exception ex) {
	    ex.printStackTrace();
	    logger.log(Level.SEVERE, ex.getMessage());
	}
    }

    
    public String getBody(HttpServletRequest request) throws IOException {

	    String body = null;
	    StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader bufferedReader = null;

	    try {
	        InputStream inputStream = request.getInputStream();
	        if (inputStream != null) {
	            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	            char[] charBuffer = new char[128];
	            int bytesRead = -1;
	            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	                stringBuilder.append(charBuffer, 0, bytesRead);
	            }
	        } else {
	            stringBuilder.append("");
	        }
	    } catch (IOException ex) {
	        throw ex;
	    } finally {
	        if (bufferedReader != null) {
	            try {
	                bufferedReader.close();
	            } catch (IOException ex) {
	                throw ex;
	            }
	        }
	    }

	    body = stringBuilder.toString();
	    return body;
	}
    
}
