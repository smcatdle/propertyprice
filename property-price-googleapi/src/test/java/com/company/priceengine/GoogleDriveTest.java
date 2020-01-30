package com.company.priceengine;

import com.google.api.services.drive.model.File;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;


public class GoogleDriveTest {
	
    
    private final Logger logger = Logger.getLogger(GoogleDriveTest.class.getName());
    
    private GoogleDriveFacade facade =  null;
    public static String fileId = null;
    
    
    @Before
    public void setup() {
    	
    	try {
	    	facade = GoogleDriveFacade.getInstance();
    	} catch (Exception ex) {
    		logger.log(Level.SEVERE, ex.getMessage());
    	}
    }
    
	/*@Test
	public void deleteAllFiles() {
		
		facade.deleteAllFiles();
	    
	}
	
	@Test
	public void testInsertFile() {
		
		fileId = facade.insertFile("daft1.jpg");
		logger.log(Level.INFO, "Retrieved file with fileId : [" + fileId + "]");
	}*/

	@Test
	public void testGetFiles() {
		
		List<File> files = facade.getFiles();
	    
        for (File f :files) {
        	logger.log(Level.INFO, "The fileId is : [" + f.getId() + "]");
        	logger.log(Level.INFO, "The downloadUrl is : [" + f.getDownloadUrl() + "]");
        }
	}

	@Test
	public void testGetFile() {
		
		facade.getFile("0B-krDD1p_ygZcWNyelpKeTZRdGM");
	    
	}
  
}
