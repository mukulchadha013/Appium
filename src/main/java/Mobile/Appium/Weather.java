package Mobile.Appium;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Weather extends BaseSteps{
	
	WebElement element = null;
	private String sBaseUrl = "https://api.forecast.io";

	@SuppressWarnings("resource")
	public String getTemperatureFromApi(String sCityDimensions) throws MalformedURLException, IOException{
		
		String sTemperature = "";
		String sApiKey = getJsonValue("data.json", "api_key");
		String sCoordinates = getJsonValue("data.json", sCityDimensions);
		String sCompleteUrl = sBaseUrl + "/forecast/" + sApiKey + "/" + sCoordinates;		
		String sResponse = readJsonFromUrl(sCompleteUrl);
		JsonParser parser = new JsonParser();		
		JsonObject simpleJsonObject = parser.parse(sResponse.toString()).getAsJsonObject();	
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = gson.toJson(simpleJsonObject);
		FileWriter file = new FileWriter("test-output\\cityTemperature.json");		
		file.write(prettyJson);
		file.flush();		
		sTemperature = (String) simpleJsonObject.get("currently").getAsJsonObject().get("temperature").getAsString();
		Log.info(" Temperature from Rest API: " + sTemperature);
		return sTemperature;
	}

	public void clickOnCity(WebDriver driver, String sCity) throws InterruptedException{
		element = getWebElement(driver, sCity + "_city");
		clickElement(element, "'" + sCity + " City'.");
	}

	public String getCityTemperatureFromApp(WebDriver driver, String sCity) throws InterruptedException{
		String sTemperatureFromApp = "";
		element = getWebElement(driver, "City_Temperature_textview");
		elementShouldBePresent(element, "'" + sCity + " City' Temperature ");
		sTemperatureFromApp = getElementText(element, "'" + sCity + " City'");
		sTemperatureFromApp = sTemperatureFromApp.substring(0, sTemperatureFromApp.length() - 1);
		Log.info(" Temperature from APP: " + sTemperatureFromApp);
		return sTemperatureFromApp;
	}
}
