package Mobile.Appium;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class BaseSteps {
	
	WebDriverWait wait = null;
	BaseSteps(){
		
	}
	BaseSteps(WebDriverWait wait){		
		this.wait = wait;
	}

	public WebElement getWebElement(WebDriver driver, String sElementName){
		WebElement element = null;
		String sXpath = "";
		//sXpath = elements.getElementLocator(sElementName);
		sXpath = getJsonValue("sample.json", sElementName);
		if (sXpath != ""){
			if (driver.findElements( By.xpath(sXpath) ).size() != 0){
				element = driver.findElement( By.xpath(sXpath) );				
			}else
			    Log.error("Unable to find element: " + sElementName);
		}
		return element;
	}
	
	public void clickElement(WebElement element, String sMsg) throws InterruptedException{
		if (element != null){
			element.click();
			Log.info("Element: " + sMsg + " clicked.");			
		}else{
			elementShouldBePresent(element, "Unable to click element: " + sMsg);
		}
	}
	
	public String getElementText(WebElement element, String sMsg) throws InterruptedException{
		String sReturnValue = "";
		if (element != null){
			sReturnValue = element.getText();
			Log.info( "Text " + sReturnValue + " Fetched for: " + sMsg);			
		}else{
			Log.error("Unable to fetch Text for: " + sMsg);			
		}
		return sReturnValue;
	}	
	
	public void waitForElement(WebDriverWait wait, String sXpath){
		WebElement myDynamicElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(sXpath)));	   
	}
	
	public String readJsonFromUrl(String sUrl) throws MalformedURLException,IOException{
		int responsecode = 0;
		String sReturnValue = "";
		//"https://api.forecast.io/forecast/b7c2400a6478c18bcb8aee0f4baad505/51.5074,0.1278"; //just a string

	    // Connect to the URL using java's native library
	    URL url = new URL(sUrl);	    
	    HttpURLConnection request = (HttpURLConnection) url.openConnection();
	    request.setRequestMethod("GET"); 
	    request.connect();
	    responsecode = request.getResponseCode(); 
	    if(responsecode == 200){
	    	StringBuilder sb = new StringBuilder();
	    	Scanner sc = new Scanner(url.openStream());
		    while(sc.hasNext())
		    {
		    	sb.append(sc.nextLine() + "\n");		    	
		    }		    
		    sc.close();		     
		    sReturnValue = sb.toString();
	    }else
	    	System.err.println("Invalid response code :" + responsecode);
	    return sReturnValue;
	}
	
	public String getJsonValue(String sFileName, String sKey){
		String sValue = "";
		Object object =  null;
		org.json.simple.JSONObject jsonObject = null;
		JSONParser parser = new JSONParser();
		try
		{
			File file = new File(sFileName);
			if(file.exists() && !file.isDirectory()) { 			    
				object = parser.parse(new FileReader(file));           //Parsing the json file 
				
				//convert Object to JSONObject
				jsonObject = (org.json.simple.JSONObject)object;
				
				if ((jsonObject).containsKey(sKey)){
					sValue = (String) jsonObject.get(sKey);
				    Log.info("fetched key: " + sKey + " & value: " + sValue + "  pair");
				}else
					Log.error("key: " + sKey + " does not exists in json file.");
			}else{
				Log.error("file not present");
				System.err.println("file not present");
			}
			
		}catch(FileNotFoundException fe)
		{
			fe.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}finally{
			object = null;
			jsonObject = null;        	
		}
		return sValue;
	}
	
	public void verifyText(String sActual, String sExpected, String sMsg){
		Assert.assertEquals(sActual.trim(), sExpected.trim(), sMsg + " are not equal.");
		Log.info(sMsg + " are equal.");
	}
	
	public void elementShouldBePresent(WebElement element, String sMsg){	
		Assert.assertNotNull(element, sMsg + " element is not Present.");
		Log.info(sMsg + " element is Present.");		
	}
	
}
