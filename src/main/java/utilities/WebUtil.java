package utilities;

import base.DriverContext;
import constant.ConstantFile;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class WebUtil {
	
	public static synchronized void openUrl(String url) {
		WebDriver driver = DriverContext.getDriver();
		driver.get(url);
	}
	
	
	public static synchronized boolean waitElementVisible(String xPath, int timeout) {
		WebDriver driver = DriverContext.getDriver();
		boolean visible = false;
		int i = 0;
		while(!visible){
			i++;
			WebUtil.waitForSeconds(2);

			try {
				visible = (new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath))) != null);
			}catch (Exception e){
			}

			if(visible || i == 8){
				break;
			}
		}
		return visible;
	}
	
	public static synchronized void clickElement(String xPath) {
        WebDriver driver = DriverContext.getDriver();
        driver.findElement(By.xpath(xPath)).click();
    }

	public static synchronized void clickElementByLinkText(String linkText) {
		WebDriver driver = DriverContext.getDriver();
		driver.findElement(By.linkText(linkText)).click();
	}

    public static synchronized void setListValue(List<String> list, String value) {
		WebDriver driver = DriverContext.getDriver();
        boolean flag = false;
        for(String elementName : list){
            try{
				driver.findElement(By.xpath(elementName)).clear();
                WebUtil.setValue(elementName, value);
                flag = true;
				break;
            }catch (Exception e){
            }

        }

		if(!flag){
			throw new RuntimeException("Fail to set value");
		}
    }
	
	public static synchronized void setValue(String xPath, String value) {
		WebDriver driver = DriverContext.getDriver();
		driver.findElement(By.xpath(xPath)).clear();
		driver.findElement(By.xpath(xPath)).sendKeys(value);
	}
	
	public static synchronized void selectByLabel(String xPath, String value, boolean exactMatch) {
		WebDriver driver = DriverContext.getDriver();
		Select select = new Select(driver.findElement(By.xpath(xPath)));
		select.selectByVisibleText(value);
	}
	
	public static synchronized void selectByValue(String xPath, String value, boolean exactMatch) {
		WebDriver driver = DriverContext.getDriver();
		Select select = new Select(driver.findElement(By.xpath(xPath)));
		select.selectByValue(value);
	}
	
	public static synchronized boolean isElementVisible(By by,int timeout){
		WebDriverWait wait = new WebDriverWait(DriverContext.getDriver(),timeout);
		try{
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
   public static synchronized void waitForSeconds(double waitTime){

        try {
            Thread.sleep((long) waitTime * 1000);
        } catch (InterruptedException e) {
//            throw new TestingException("Hit error during wait", e);
        }
    }
    
    public static synchronized void clickUsingJS(String xpath) {
		WebDriver driver = DriverContext.getDriver();
		WebElement element = driver.findElement(By.xpath(xpath));
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click()", element);
	}
    
	public static String getRandomName(){
		Random random=new Random();
		StringBuffer sb = new StringBuffer();

		for(int i=0; i<6; i++){
			long result=0;
			result=Math.round(Math.random()*25+65);
			sb.append(String.valueOf((char)result));
		}

		return sb.toString();
	}
	
	public static synchronized void removeReadOnly(String id){
		WebDriver driver = DriverContext.getDriver();
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		String command = "document.getElementById(\"" + id + "\").removeAttribute(\"readonly\")";
		executor.executeScript(command);
	}
	
	public static synchronized void sendKeysByJS(String id, String text){
		WebDriver driver = DriverContext.getDriver();
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		String command = "document.getElementById(\"" + id + "\").value=\""+ text + "\"";
		executor.executeScript(command);
	}
	
	public static synchronized boolean isNullOrEmpty(String value){
		return (null == value || "".equals(value));
	}
	
	public static synchronized void waitElementClickable(String xpath, int timeout){
		WebDriverWait driverWait = new WebDriverWait(DriverContext.getDriver(), timeout);
		driverWait.until(ExpectedConditions.elementToBeClickable (By.xpath(xpath)));
	}

	public static synchronized BigDecimal toBigDecimal(String value){
		System.out.println("toBigDecimal value = " + value);

		if(value.indexOf(",") != -1){
			value = value.replace(",", "");
		}

		if(isNullOrEmpty(value)){
			System.out.println("toBigDecimal value is Null or empty, default as 0");
			return new BigDecimal(0);
		}else{
			System.out.println("toBigDecimal value NOT Null or empty");
			return new BigDecimal(value);
		}
	}

	public static synchronized String calculateDOB(){
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.YEAR, -18);

		return sdf.format(calendar.getTime());
	}

	public static synchronized void moveToElement(String xpath){
		WebDriver driver = DriverContext.getDriver();
		Actions actions = new Actions(driver);
		actions.moveToElement(driver.findElement(By.xpath(xpath))).perform();
	}

	public static synchronized boolean isSameValue(BigDecimal bigDecimal1, BigDecimal bigDecimal2){
		return (bigDecimal1.compareTo(bigDecimal2) == 0);
	}

	public static synchronized void handleCheckBox(String expectedValue, String statusFlagXpath, String clickableXpath){
		WebDriver driver = DriverContext.getDriver();

		if(WebUtil.isElementVisible(By.xpath(clickableXpath), ConstantFile.TIMEOUT5)) {
			if ("UNCHECK".equalsIgnoreCase(expectedValue)) {
				if ("0".equalsIgnoreCase(driver.findElement(By.xpath(statusFlagXpath)).getAttribute("value"))) {
					WebUtil.clickElement(clickableXpath);
				}
			}

			if ("CHECK".equalsIgnoreCase(expectedValue)) {
				if (!"0".equalsIgnoreCase(driver.findElement(By.xpath(statusFlagXpath)).getAttribute("value"))) {
					WebUtil.clickElement(clickableXpath);
				}
			}
		}
	}
}
	
