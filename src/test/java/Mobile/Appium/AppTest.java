package Mobile.Appium;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
//import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.apache.log4j.xml.DOMConfigurator;
import Mobile.Appium.Log;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;



public class AppTest 
{
	public WebDriver driver;
	public WebElement element;
	int iTimeOut = 1500;
	private String sUsername = "", sPassword = "";

	@SuppressWarnings("rawtypes")
	@BeforeClass
	public void setUp() throws MalformedURLException, IOException {
		deleteLogFile("logfile.log");
		createLogFile("logfile.log");

		DOMConfigurator.configure("log4j.xml");
		Log.setUp();
		// Created object of DesiredCapabilities class.
		DesiredCapabilities capabilities = new DesiredCapabilities();

		// Set android deviceName desired capability. Set your device name.
		capabilities.setCapability("deviceName", "my_android");
		//capabilities.setCapability("deviceName", "Lenovo K50a40");

		// Set BROWSER_NAME desired capability. It's Android in our case here.
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "Android");

		// Set android VERSION desired capability. Set your mobile device's OS version.
		capabilities.setCapability(CapabilityType.VERSION, "5.1.1");
		//capabilities.setCapability(CapabilityType.VERSION, "6.0");

		// Set android platformName desired capability. It's Android in our case here.
		capabilities.setCapability("platformName", "Android");

		// Set android appPackage desired capability. It is
		// com.android.calculator2 for calculator application.
		// Set your application's appPackage if you are using any other app.
		capabilities.setCapability("appPackage", "com.example.loginscreen");

		// Set android appActivity desired capability. It is
		// com.android.calculator2.Calculator for calculator application.
		// Set your application's appPackage if you are using any other app.
		capabilities.setCapability("appActivity", "com.example.loginscreen.MainActivity");

		// Created object of RemoteWebDriver will all set capabilities.
		// Set appium server address and port number in URL string.
		// It will launch calculator app in android device.
		driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		Log.info("SetUp done..");
	}	

	@Test
	public void Login_With_Wrong_Password() throws InterruptedException {
		Log.startTestCase("Login_With_Wrong_Password");
		sUsername = "admin";
		sPassword = "mukul";

		// Enter User Name.
		element = checkUsername();
		if (element == null){
			pressBack();			
			element = checkUsername();
		}
		insertText(element, sUsername);	


		element = checkPassword();
		if (element == null){
			pressBack();			
			element = checkPassword();
		}
		insertText(element, sPassword);
		
		
		element = checkLogin();
		if (element == null){
			pressBack();			
			element = checkLogin();
		}		
		
		clickElement(element, "Login");
		
		element = checkAttemptField();
		if (element == null){
			pressBack();			
			element = checkAttemptField();
		}

		String sValue = getText(element, "Attempt Field");

		verifyText("2", sValue, "Attempts Count");
		
		Log.endTestCase("Login_With_Wrong_UserName");

	}

	@AfterClass
	public void End() throws InterruptedException {
		element = checkCancel();
		if (element == null){
			pressBack();			
			element = checkCancel();
		}
		clickElement(element, "Cancel");
		driver.quit(); 
	}

	void deleteLogFile(String sFilePath){
		File file = new File(sFilePath);
		if(file.delete()) { 
			System.out.println(file.getName() + " is deleted!");
		} else {
			System.out.println("Delete operation is failed.");
		}
	}

	void createLogFile(String sFilePath) throws IOException{
		File file = new File(sFilePath);
		if(file.createNewFile()) { 
			System.out.println(file.getName() + " is created!");
		} else {
			System.out.println("Create operation is failed.");
		}
	}

	void pressKey(int iKey) throws InterruptedException{
		HashMap<String, Integer> keycode = new HashMap<String, Integer>();
		keycode.put("keycode", iKey);
		((JavascriptExecutor)driver).executeScript("mobile: keyevent", keycode);
		Thread.sleep(iTimeOut);
	}

	void pressBack() throws InterruptedException{
		((AndroidDriver<?>) driver).sendKeyEvent(AndroidKeyCode.BACK);		
		Log.info("Clicked on Back Button");		
		Thread.sleep(iTimeOut);
	}

	WebElement checkElementExists(WebDriver driver, String sXpath, String sMsg){
		WebElement element = null;
		if (driver.findElements( By.xpath(sXpath) ).size() != 0){
			element = driver.findElement( By.xpath(sXpath) );
			Log.info(sMsg + " exists.");
		}else{
			Log.error(sMsg + " not exists.");
		}
		return element;
	}

	WebElement checkCancel(){
		element = checkElementExists(driver, "//android.widget.Button[contains(@resource-id,'id/button2') and @text='Cancel']", "Cancel");
		return element;
	}

	WebElement checkPassword(){
		element = checkElementExists(driver, "//android.widget.EditText[contains(@resource-id,'id/editText2')]", "Password");
		return element;
	}

	WebElement checkUsername(){
		element = checkElementExists(driver, "//android.widget.EditText[@resource-id='com.example.loginscreen:id/editText']", "Username");
		return element;
	}

	WebElement checkLogin(){
		element = checkElementExists(driver, "//android.widget.Button[contains(@resource-id,'id/button') and @text='login']", "Login");
		return element;
	}

	WebElement checkAttemptField(){
		element = checkElementExists(driver, "//android.widget.TextView[contains(@resource-id,'id/textView3')]", "Attempt Field");
		return element;
	}

	void verifyText(String sActual, String sExpected, String sMsg){
		Assert.assertEquals(sActual.trim(), sExpected.trim(), sMsg + " are not equal.");
		Log.info(sMsg + " are equal.");
	}

	String getText(WebElement element, String sMsg) throws InterruptedException{
		String sReturnValue = "";
		if (element != null){
			sReturnValue = element.getText();
			Log.info( "Text Fetched from: " + sMsg);
			Thread.sleep(iTimeOut);
		}else{
			Log.error("Text not Fetched from: " + sMsg + " as element is null.");
			Assert.assertNotNull(element, "Text not Fetched from: " + sMsg + " as element is null.");
		}
		return sReturnValue;
	}

	void clickElement(WebElement element, String sMsg) throws InterruptedException{
		if (element != null){
			element.click();
			Log.info(sMsg + " clicked.");
			Thread.sleep(iTimeOut);
		}else{
			Log.error(sMsg + " not clicked as element is null.");
			Assert.assertNotNull(element, sMsg + " not clicked as element is null.");
		}
	}

	void insertText(WebElement element, String sText) throws InterruptedException{
		if (element != null){
			element.clear();
			element.sendKeys(sText);
			Log.info(sText + " entered.");
			((AndroidDriver<?>) driver).sendKeyEvent(AndroidKeyCode.KEYCODE_ENTER);
			Thread.sleep(iTimeOut);
		}else{
			Log.error(sText + " not entered as element is null");
			Assert.assertNotNull(element, sText + " not entered as element is null");
		}
	}

}
