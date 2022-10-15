package base;

import com.aventstack.extentreports.gherkin.model.Scenario;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.Assert;
import utilities.ScreenShotUtil;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class Base {
	public static Scenario scenario;

	private static String caseID;
	public static String platform;
	private static long start_time;

	public static void setStartTime() {
		start_time = System.currentTimeMillis();
	}
//
	public static String getRunTime() {
		long run_time = System.currentTimeMillis() - start_time;
		long currentMS = run_time % 1000;
		long totalSeconds = run_time / 1000;
		long currentSecond = totalSeconds % 60;
		long totalMinutes = totalSeconds / 60;
		long currentMinute = totalMinutes % 60;

		return currentMinute + "m" + currentSecond + "."
				+ currentMS + "s";
	}

	public static String getCaseID() {
		return caseID;
	}

	public static void setCaseID(String caseID) {
		Base.caseID = caseID;
	}

	public static boolean checkIfServerIsRunnning(int port) {
		boolean isServerRunning = false;
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(port);

			serverSocket.close();
		} catch (IOException e) {
			isServerRunning = true;
		} finally {
			serverSocket = null;
		}
		return isServerRunning;
	}

	public static void setChromeCapabilities() {
		
		ChromeOptions options = new ChromeOptions();

		options.addArguments("--ignore-certificate-errors");
//		options.addArguments("--headless");

		String downloadFilepath = System.getProperty("user.dir") +File.separator +"resource"+ File.separator +"download";
		HashMap<String, Object> chromePrefs = new HashMap<>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", downloadFilepath);
		options.setExperimentalOption("prefs", chromePrefs);
		String driverPath = System.getProperty("user.dir") + File.separator + "resource" + File.separator + "driver" + File.separator + "chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", driverPath);
		DriverContext.setDriver (new ChromeDriver(options));
	}

	public static void setEdgeCapabilities() {

		EdgeOptions options = new EdgeOptions();

		String downloadFilepath = System.getProperty("user.dir") +File.separator +"resource"+ File.separator +"download";
		HashMap<String, Object> chromePrefs = new HashMap<>();
		String driverPath = System.getProperty("user.dir") + File.separator + "resource" + File.separator + "driver" + File.separator + "msedgedriver.exe";
		System.setProperty("webdriver.edge.driver", driverPath);
		DriverContext.setDriver (new EdgeDriver(options));
	}


	public static void delay(long delaySec) {
		try {
			Thread.sleep(delaySec * 1000);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	public static void renameReportFolder() {
		String oldPath = System.getProperty("user.dir") + File.separator + "output" + File.separator + "report.html";
		String newPath = System.getProperty("user.dir") + File.separator + "output" + File.separator + "screenshots" + File.separator + caseID + File.separator + "report.html";
		File newFile = new File(newPath);
		new File(oldPath).renameTo(newFile);
		System.out.println("report renamed to " + caseID + "_report.html");
	}

	public static void checkDownloadFile() throws IOException {
		File currentDirFile = new File(".");
		String excelFilePath = currentDirFile.getAbsolutePath().substring(0, currentDirFile.getAbsolutePath().length() - currentDirFile.getCanonicalPath().length()) + "resource" + File.separator + "download";
		Date currentDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
		String currentDTM = sdf.format(currentDate);
		File directory = new File(excelFilePath);
		File[] files = directory.listFiles();
		assert files != null;
		for (File f : files) {
			Date filesLastModifiedDTM = new Date(f.lastModified());
			if (f.getName().contains(currentDTM) && (currentDate.getTime()-filesLastModifiedDTM.getTime())/1000/60>2) {
				System.out.println("File last modified time compare to current time: "+(currentDate.getTime()-filesLastModifiedDTM.getTime())/1000/60 +"mins");
				f.delete();
				System.out.println("Download file deleted");
			}
		}
		if (isFileDownloaded(excelFilePath, currentDTM)) {
			System.out.println("Download successfully");
			Base.delay(5);
			ScreenShotUtil.addScreenShot();
		} else {
			System.out.println("Download failed");
			Assert.fail();
		}
	}

	public static String hkidGenerator() {
//		StringBuilder hkid = new StringBuilder();
//		Random rnd = new Random();
//		char randomAlphabet = (char) ('A' + rnd.nextInt(26));
//		hkid.append(randomAlphabet);
//		for (int i=0;i<7;i++){
//			int randomDigit = rnd.nextInt(10);
//			hkid.append(randomDigit);
//		}
//		return hkid.toString();
		ChromeOptions opt = new ChromeOptions();
		opt.addArguments("headless");
		WebDriver driver = new ChromeDriver(opt);
		driver.get("https://pinkylam.me/playground/hkid/");
		return driver.findElement(By.xpath("//*[@id='randomHKID']")).getText();
	}

	public static boolean isFileDownloaded(String downloadPath, String fileName) {
		boolean flag = false;
		File dir = new File(downloadPath);
		File[] dir_contents = dir.listFiles();

		assert dir_contents != null;
		for (File dir_content : dir_contents) {
			if (dir_content.getName().contains(fileName))
				flag = true;
		}
		return flag;
	}
	public static String hkMobileNumberGenerator() {
		StringBuilder hkMobileNumber = new StringBuilder();
		Random rnd = new Random();
//		char randomAlphabet = (char) ('A' + rnd.nextInt(26));
//		hkid.append(randomAlphabet);
		for (int i=0;i<8;i++){
			int randomDigit = rnd.nextInt(10);
			hkMobileNumber.append(randomDigit);
		}
		return hkMobileNumber.toString();
	}
	public static String emailAddressGenerator() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 10) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr + "@NETVIGATOR.COM";
	}
	public static String agentCodeGenerator(String agentCode) {
		String prefix = agentCode.replaceAll("\\d","");
		String number = agentCode.split(prefix)[1];
		int i=Integer.parseInt(number);
		i++;
		if ((int) (Math.log10(i) + 1) < 4){
			String formatted = String.format("%04d", i);
			agentCode = prefix + formatted;
		} else {
			agentCode = prefix + i;
		}
		return agentCode;
	}

	public static boolean checkAlertPresent(WebDriver driver){
		try
		{
			driver.switchTo().alert();
			return true;
		}
		catch (NoAlertPresentException Ex)
		{
			return false;
		}
	}
}