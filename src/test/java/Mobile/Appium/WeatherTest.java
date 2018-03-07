package Mobile.Appium;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.apache.log4j.xml.DOMConfigurator;

import Mobile.Appium.Log;

import io.appium.java_client.android.AndroidDriver;


public class WeatherTest 
{
	public WebDriver driver;
	public WebElement element;
	int iWaitSeconds = 15;
	WebDriverWait wait = null;
	BaseSteps base = null;	

	@SuppressWarnings("rawtypes")
	@BeforeClass
	public void setUp() throws MalformedURLException, IOException {
		Log.deleteLogFile("logfile.log");
		Log.createLogFile("logfile.log");

		DOMConfigurator.configure("log4j.xml");
		Log.setUp();
		
		// Created object of DesiredCapabilities class.
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", "Nexus_5X_API_24");
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "Android");
		capabilities.setCapability(CapabilityType.VERSION, "7.0");
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("appPackage", "com.daniloprado.weather");
		capabilities.setCapability("appActivity", "com.daniloprado.weather.view.main.MainActivity");	
		
		driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		driver.manage().timeouts().implicitlyWait(iWaitSeconds, TimeUnit.SECONDS);
		wait = new WebDriverWait(driver, iWaitSeconds);
		base = new BaseSteps(wait);
		Log.info("SetUp done..");		
	}	

	@Test(enabled=true)
	public void checkDefaultCityList() throws InterruptedException {		
		
		element = base.getWebElement(driver, "Dublin_city");
		base.elementShouldBePresent(element, "'Dublin city' ");
		element = null;
		
		element = base.getWebElement(driver, "London_city");
		base.elementShouldBePresent(element, "'London city' ");
		element = null;
		
		element = base.getWebElement(driver, "NewYork_city");
		base.elementShouldBePresent(element, "'NewYork city' ");
		element = null;
		
		element = base.getWebElement(driver, "Barcelona_city");
		base.elementShouldBePresent(element, "'Barcelona city' ");
		element = null;		
	}
	
	@BeforeMethod
	 public void beforeTestMethod(Method testMethod){
		Log.startTestCase(testMethod.getName());
	 }
	
	@AfterMethod
	 public void afterTestMethod(Method testMethod){
		Log.endTestCase(testMethod.getName());
	 }
	
	@Test(enabled=true)
	public void checkWeathertemperature() throws InterruptedException, MalformedURLException, IOException {	
		Weather weather = new Weather();
		String sTemperatureFromAPI = "", sTemperatureFromApp = "";
		
		weather.clickOnCity(driver, "London");
		
		sTemperatureFromApp = weather.getCityTemperatureFromApp(driver, "London");
		
		sTemperatureFromAPI = weather.getTemperatureFromApi("London_dimensions");
		
		base.verifyText(sTemperatureFromApp, sTemperatureFromAPI, "App Temperature: " + sTemperatureFromApp + " & Live API Temperature: " + sTemperatureFromAPI);	
		
		weather = null;
	}

	@AfterClass
	public void End() throws InterruptedException {		
		driver.quit(); 
		base = null;		
	}	

}
