package test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.service.ExtentService;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@CucumberOptions(
//		monochrome = true,
//		plugin = { "pretty","com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:" },
//		plugin = { "pretty", "json:target/cucumber-reports/Cucumber.json" },
//		plugin = { "json:target/cucumber-reports/Cucumber.json" },
		plugin = {
				"pretty",
				"json:output/json-report/cucumber.json",
				"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
		},
		features = "src/feature",
		glue = { "stepdefinition" },
				tags = "@BC-Web-001"
)
public class TestRun extends AbstractTestNGCucumberTests {
	public static ITestContext itc;

	@BeforeSuite
	public void beforeSuite(ITestContext itc) throws Exception {
		this.itc = itc;
		ExtentReports extent = new ExtentReports();
		ExtentSparkReporter spark = new ExtentSparkReporter("");//System.getProperty("user.dir")+output/reportX.html
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss").format(itc.getStartDate());

		Properties props = System.getProperties();
		props.setProperty("extent.reporter.spark.start","true");
		props.setProperty("extent.reporter.spark.config","resource/extent-config.xml");
		props.setProperty("extent.reporter.spark.out", "output/"+timeStamp+"/sparkReport.html");
		props.setProperty("screenshot.dir", "output/"+timeStamp+"/screenshots/");
//		props.setProperty("extent.reporter.spark.out", "report/");
//		props.setProperty("screenshot.dir", "screenshots/");
		props.setProperty("screenshot.rel.path", "screenshots/");
//		props.setProperty("basefolder.name","output/Test");
//		props.setProperty("basefolder.datetimepattern","yyyy.MM.dd_HH.mm.ss");

		extent.attachReporter(spark);

	}

	@BeforeTest
	@Parameters("browser")
	public void beforeTest(String browser){
		Properties props = System.getProperties();
		props.setProperty("browser",browser);
	}

//	@Override
//	@DataProvider(parallel = true)
//	public Object[][] features() {
//		Object[][] a = super.features();
//		return a;
//	}
	@AfterSuite
	public void afterClass(ITestContext itc) throws Exception {
		String srcPath= ExtentService.getScreenshotFolderName().replace("/"+ExtentService.getScreenshotReportRelatvePath(),"");
		copyFolder(Paths.get("output/json-report/"),Paths.get(srcPath+ File.separator +"json-report"));
	}

	public void copyFolder(Path src, Path dest) throws IOException {
		try (Stream<Path> stream = Files.walk(src)) {
			stream.forEach(source -> copy(source, dest.resolve(src.relativize(source))));
		}
	}

	private void copy(Path source, Path dest) {
		try {
			Files.deleteIfExists(dest);
			Files.copy(source, dest, REPLACE_EXISTING);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
