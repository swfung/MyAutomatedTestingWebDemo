package stepdefinition;

import base.Base;
import base.DriverContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utilities.ExcelUtil;
import utilities.ScreenShotUtil;

import java.io.IOException;
import java.util.Collection;

public class Hook extends Base {

	@Before
	public void beforeScenario(Scenario scenario) throws IOException {
		ScreenShotUtil.setScenario(scenario);
		Collection<String> tags = scenario.getSourceTagNames();

		for(String tag: tags){
			System.out.println(tag);
		}
		Base.setCaseID(scenario.getName());
		 System.out.println("This will run before the Scenario");
		System.out.println("-----------------------------------");
		System.out.println("Starting - " + scenario.getName());
		System.out.println("-----------------------------------");

		if(System.getProperty("browser").equals("Chrome")){
			setChromeCapabilities();
		}else if(System.getProperty("browser").equals("Edge")){
			setEdgeCapabilities();
		}
		ExcelUtil.readExcel(scenario.getName());
		scenario.log("Using "+System.getProperty("browser")+" browser");
		scenario.attach("[Using "+System.getProperty("browser")+" browser]","text/plain","");
	}

//	@AfterStep
//	public void afterStep(Scenario scenario) {
//		System.out.println("AFTER STEP");
////		TakesScreenshot ts = (TakesScreenshot) DriverContext.getDriver();
////		byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
////		scenario.attach(screenshot, "image/png","image");
//
//	}

	@After
	public void afterScenario(Scenario scenario) {

		if("FAILED".equals(scenario.getStatus().toString())){
			ScreenShotUtil.saveScreenShotForStep("error");
			scenario.log("Error occurs");
		}
//		DriverContext.getDriver().quit();

		DriverContext.getDriver().close();
//		DriverContext.setDriver(null);
	}


}
