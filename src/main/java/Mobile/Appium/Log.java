package Mobile.Appium;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;

public class Log {

	//Initialize Log4j logs
	private static Logger Log = Logger.getLogger(Log.class.getName());//

	// This is to print log for the set up
	public static void setUp(){
		Date myDate = new Date();
		Log.info("****************************************************************************************");
		Log.info("############################  Time: " + myDate.toString() + "  ############################");
		Log.info("****************************************************************************************");
		Log.info("X");
		Log.info("X");		 
	}	 

	public static void startTestCase(String sTestCaseName){
		Log.info("X");
		Log.info("X");		
		Log.info("****************************************************************************************");
		Log.info("****************************************************************************************");
		Log.info("$$$$$$$$$$$$$$$$$$$$$  Starting Test:               " + sTestCaseName + "       $$$$$$$$$$$$$$$$$$$$$$$$$");
		Log.info("****************************************************************************************");
		Log.info("****************************************************************************************");	
		Log.info("X");
		Log.info("X");	
	}

	//This is to print log for the ending of the test case
	public static void endTestCase(String sTestCaseName){
		Log.info("X");
		Log.info("X");
		Log.info("****************************************************************************************");
		Log.info("$$$$$$$$$$$$$$$$$$$$$  Ending Test:               " + sTestCaseName + "       $$$$$$$$$$$$$$$$$$$$$$$$$");
		Log.info("****************************************************************************************");
		Log.info("XXXXXXXXXXXXXXXXXXXXXXX             " + "-E---N---D-" + "             XXXXXXXXXXXXXXXXXXXXXX");
		Log.info("X");
		Log.info("X");

	}

	// Need to create these methods, so that they can be called
	public static void info(String message) {
		Log.info("--> " +message);
	}

	public static void warn(String message) {
		Log.warn("--> " +message);
	}

	public static void error(String message) {
		Log.error("-->> " +message);
	}

	public static void fatal(String message) {
		Log.fatal("--> " +message);
	}

	public static void debug(String message) {
		Log.debug("--> " +message);
	}

	public static void deleteLogFile(String sFilePath){
		File file = new File(sFilePath);
		if(file.delete()) { 
			System.out.println(file.getName() + " is deleted!");
		} else {
			System.out.println("Delete operation is failed.");
		}
	}

	public static void createLogFile(String sFilePath) throws IOException{
		File file = new File(sFilePath);
		if(file.createNewFile()) { 
			System.out.println(file.getName() + " is created!");
		} else {
			System.out.println("Create operation is failed.");
		}
	}

}