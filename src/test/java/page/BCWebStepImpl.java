package page;

import base.Base;
import base.DriverContext;
import constant.ConstantFile;
import constant.Xpath_Locator_BC_Web;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import utilities.ExcelUtil;
import utilities.ScreenShotUtil;
import utilities.WebUtil;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Calendar;

import static base.Base.checkAlertPresent;
import static base.Base.delay;

public class BCWebStepImpl {

	WebDriver driver = DriverContext.getDriver();

	public void loginBCWeb() {
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(ConstantFile.TIMEOUT10, TimeUnit.SECONDS);
		driver.get(Xpath_Locator_BC_Web.mainPageURL);
		ScreenShotUtil.addScreenShot();
	}

	public void checkPage(String page) {
		switch (page) {
			case "BC home":
				Assert.assertTrue(WebUtil.waitElementVisible(Xpath_Locator_BC_Web.bcHomeHeader, ConstantFile.TIMEOUT10));
				break;
		}
		ScreenShotUtil.addScreenShot();
	}

	public void userInputs(String field) {
		switch (field) {
			case "Mobile Number":
				driver.findElement(By.xpath(Xpath_Locator_BC_Web.mobileInputField)).clear();
//				driver.findElement(By.xpath(Xpath_Locator_BC_Web.mobileInputField)).sendKeys(ConstantFile.USERID_PT);
				driver.findElement(By.xpath(Xpath_Locator_BC_Web.mobileInputField)).sendKeys(ExcelUtil.getValue("mobileNumber"));
				break;
			case "Password":
				driver.findElement(By.xpath(Xpath_Locator_BC_Web.pwInputField)).clear();
//				driver.findElement(By.xpath(Xpath_Locator_BC_Web.pwInputField)).sendKeys(ConstantFile.PWD_PT);
				driver.findElement(By.xpath(Xpath_Locator_BC_Web.pwInputField)).sendKeys(ExcelUtil.getValue("password"));
				break;
		}
		ScreenShotUtil.addScreenShot();
	}

	public void userClicks(String button) {
		switch (button) {
			case "Login/Register":
				driver.findElement(By.xpath(Xpath_Locator_BC_Web.loginRegBtn)).click();
				Base.delay(3);
				break;
			case "Login":
				driver.findElement(By.xpath(Xpath_Locator_BC_Web.loginBtn)).click();
				Base.delay(3);
				break;
		}
		ScreenShotUtil.addScreenShot();
	}

	public void userCanSee(String result) throws IOException, ParseException {
		switch (result) {
			case "套票及積分 & Logout":
				Assert.assertTrue(WebUtil.waitElementVisible(Xpath_Locator_BC_Web.ptsBtn, ConstantFile.TIMEOUT5));
				Assert.assertTrue(WebUtil.waitElementVisible(Xpath_Locator_BC_Web.logoutBtn, ConstantFile.TIMEOUT5));
				break;
		}
		ScreenShotUtil.addScreenShot();
	}
}

	
