/**
 * 
 */
package com.company.priceengine;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.company.utils.network.HttpUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

/**
 * @author smcardle
 *
 */
public class GoogleDriveFacade {
	  
	public static final String GOOGLE_SERVICE_ACCCOUNT_EMAIL = "192375477162-a14tb50gvlkpt6ln8r88lp0che70sr30@developer.gserviceaccount.com";
	public static final String GOOGLE_SERVICE_ACCCOUNT_KEY_FILE = "2cd7624c3a7b942145a1acd2748e90f9a02e06d8-privatekey.p12";
	public static final String MIME_TYPE_IMAGE_JPEG = "image/jpeg";
	
    private final Logger logger = Logger.getLogger(GoogleDriveFacade.class.getName());
    
    private static GoogleDriveFacade instance = null;
    private Drive service = null;
    private static GoogleCredential credential = null;
    
    private GoogleDriveFacade() {
    	try {
			service = getDriveService();
		} catch (GeneralSecurityException e) {
			  
			e.printStackTrace();
		} catch (IOException e) {
			  
			e.printStackTrace();
		} catch (URISyntaxException e) {
			  
			e.printStackTrace();
		}
    }
    
    public static GoogleDriveFacade getInstance() {
    	
    	if (instance == null) {
    		instance = new GoogleDriveFacade();
    	}
    	
    	return instance;
    }
    
    public String insertFile(String fileName) {
    	
	    try {

		    //Insert a file  
		    File body = new File();
		    body.setTitle(fileName);
		    body.setDescription(fileName);
		    body.setMimeType(MIME_TYPE_IMAGE_JPEG);
		    
		    java.io.File fileContent = new java.io.File(fileName);
		    FileContent mediaContent = new FileContent(MIME_TYPE_IMAGE_JPEG, fileContent);
	
		    File file = service.files().insert(body, mediaContent).execute();
		    logger.log(Level.INFO,"File DownloadUrl : " + file.getDownloadUrl());
		    
		    return file.getId();
		    
	    } catch (Exception ex) {
	    	logger.log(Level.SEVERE, ex.getMessage());
	    }
	    
	    return null;
    }


	  /**
	   * Retrieve all the files.
	   * 
	   * @return a list of files.
	   */
	  public List<File> getFiles() {
	      try {
	        FileList list =
	            service.files().list()
	                .execute();
	        List<File> files = list.getItems();
	        
	        for (File f :files) {
	        	logger.log(Level.INFO, "The fileId is : [" + f.getId() + "]");
	        	logger.log(Level.INFO, "The downloadUrl is : [" + f.getDownloadUrl() + "]");
	        }

	        return files;
	      } catch (IOException e) {
	        // An error occurred.
	        e.printStackTrace();
	        return null;
	      }
	  }
	  
	  /**
	   * Get a file.
	   * 
	   * @param fileId The fileId.
	   * @return InputStream containing the file's content if successful,
	   *         {@code null} otherwise.
	   */
	  public void getFile(String fileId) {
	      try {
	    	    
	        File file = service.files().get(fileId)
	                .execute();

	        String downloadUrl = file.getDownloadUrl();
	        
	        String accessToken = credential.getAccessToken();
	        java.io.File f = new java.io.File(fileId + ".jpg");
	        //OutputStream stream = new FileOutputStream(f);
	        HttpUtils.httpRequest(downloadUrl, HttpUtils.METHOD_GET, null, accessToken);
	        
	      } catch (IOException e) {
	        // An error occurred.
	        e.printStackTrace();
	      }
	  }
	  
	  /**
	   * Delete a file.
	   * 
	   * @param fileId The fileId.
	   */
	  public void deleteFile(String fileId) {
	      try {
	            service.files().delete(fileId)
	                .execute();
	            
	      } catch (IOException e) {
	        // An error occurred.
	        e.printStackTrace();
	      }
	  }
	  
	  /**
	   * Delete all files.
	   * 
	   */
	  public void deleteAllFiles() {
		  
	      try {
	    	  
		        FileList list =
		            service.files().list()
		                .execute();
		        List<File> files = list.getItems();
		        
		        for (File f : files) {
		        	logger.log(Level.INFO, "Deleting file with fileId is : [" + f.getId() + "]");
		            service.files().delete(f.getId()).execute();
		        }
	            
	      } catch (IOException e) {
	        // An error occurred.
	        e.printStackTrace();
	      }
	  }
	  
	  /**
	   * Build and returns a Drive service object authorized with the service accounts.
	   *
	   * @return Drive service object that is ready to make requests.
	   */
	  private static Drive getDriveService() throws GeneralSecurityException,
	      IOException, URISyntaxException {
	    HttpTransport httpTransport = new NetHttpTransport();
	    JacksonFactory jsonFactory = new JacksonFactory();
	    credential = new GoogleCredential.Builder()
	        .setTransport(httpTransport)
	        .setJsonFactory(jsonFactory)
	        .setServiceAccountId(GOOGLE_SERVICE_ACCCOUNT_EMAIL)
	        .setServiceAccountScopes(Arrays.asList(DriveScopes.DRIVE))
	        .setServiceAccountPrivateKeyFromP12File(
	            new java.io.File(GOOGLE_SERVICE_ACCCOUNT_KEY_FILE))
	        .build();
	    Drive service = new Drive.Builder(httpTransport, jsonFactory, null)
	        .setHttpRequestInitializer(credential).build();
	    return service;
	  }

	/**
	 * @return the credential
	 */
	public static GoogleCredential getCredential() {
		return credential;
	}

	/**
	 * @param credential the credential to set
	 */
	public static void setCredential(GoogleCredential credential) {
		GoogleDriveFacade.credential = credential;
	}
	  
	  
}
