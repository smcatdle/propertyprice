/**
 * 
 */
package com.company.propertyprice.loadtest;

import static org.junit.Assert.fail;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

/**
 * @author smcardle
 *
 *
 */
public class InvokeServerThreadTest {

	private final Logger logger = Logger.getLogger(InvokeServerThreadTest.class.getName());
	
	@Test
	public void testInvokeProxyServer() {
		
		boolean passedTest = false;
		Result threadResult = new Result("");
		Result thread1Result = new Result("");
		Result thread2Result = new Result("");
		Result thread3Result = new Result("");
		Result thread4Result = new Result("");
		Result thread5Result = new Result("");
		Result thread6Result = new Result("");
		Result thread7Result = new Result("");
		Result thread8Result = new Result("");
		
		try {
			
			InvokeServerThread thread = null;
			InvokeServerThread thread1 = null;
			InvokeServerThread thread2 = null;
			InvokeServerThread thread3 = null;
			InvokeServerThread thread4 = null;
			InvokeServerThread thread5 = null;
			InvokeServerThread thread6 = null;
			InvokeServerThread thread7 = null;
			InvokeServerThread thread8 = null;
			
			thread = new InvokeServerThread(threadResult);
			long startTime = System.currentTimeMillis();
			thread.start();
			
			/*thread1 = new InvokeServerThread(thread1Result);
			thread1.start();
			
			thread2 = new InvokeServerThread(thread2Result);
			thread2.start();
			
			thread3 = new InvokeServerThread(thread3Result);
			thread3.start();
			
			thread4 = new InvokeServerThread(thread4Result);
			thread4.start();
			
			thread5 = new InvokeServerThread(thread5Result);
			thread5.start();
			
			thread6 = new InvokeServerThread(thread6Result);
			thread6.start();
			
			thread7 = new InvokeServerThread(thread7Result);
			thread7.start();
			
			thread8 = new InvokeServerThread(thread8Result);
			thread8.start();*/
			
			thread.join();
			/*thread1.join();
			thread2.join();
			thread3.join();
			thread4.join();
			thread5.join();
			thread6.join();
			thread7.join();
			thread8.join();*/

			long endTime = System.currentTimeMillis();
			       
			logger.log(Level.INFO, "PASSED TEST in [" + (endTime - startTime) + "] milliseconds.");
			
			passedTest = true;
			
		} catch (Exception ex) {
			passedTest = false;
			logger.log(Level.SEVERE, "Error executing thread : " + ex);
		}
		
		if (!passedTest) fail("The InvokeServerThreadTest failed.");
	}
	
}
