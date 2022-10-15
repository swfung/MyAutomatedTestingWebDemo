package base;

import org.openqa.selenium.WebDriver;

public class DriverContext{
	private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

	public static synchronized void setDriver(WebDriver inputDriver)
	{
		driver.set(inputDriver);
	}
	public static synchronized WebDriver getDriver()
	{
		return driver.get();
	}
}