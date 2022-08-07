package resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Base {

	private WebDriver driver;
	private Properties prop;
	private Properties xpaths;

	public void initializeDriver() throws IOException {
		// baseData.properties file path
		String basePropertyPath = System.getProperty("user.dir") + File.separator
				+ "src\\main\\java\\resources\\baseData.properties";
		
		
		
		String fireFoxDriverPath = "C:\\reserved\\geckodriver-win64\\geckodriver.exe";
		FileInputStream file1 = new FileInputStream(basePropertyPath);
		prop = new Properties();
		prop.load(file1);
		
		String xpathPropertyPath = prop.getProperty("xpath_properties");
		FileInputStream file2 = new FileInputStream(xpathPropertyPath);
		xpaths = new Properties();
		xpaths.load(file2);

		String browserName = "chrome";
		String chromeDriverPath = prop.getProperty("chromeDriverPath");
		String chromeProfilePath = prop.getProperty("chromeProfilePath");

		if (browserName.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", chromeDriverPath);
			ChromeOptions option = new ChromeOptions();
			option.addArguments("user-data-dir=" + chromeProfilePath);
			driver = new ChromeDriver(option);
		} else if (browserName.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.driver", fireFoxDriverPath);
			driver = new FirefoxDriver();
		}

		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(20,TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(60,TimeUnit.SECONDS);
//		driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(20));
//		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));

	}

	public WebDriver getDriver() {
		return driver;
	}

	public Properties getBaseDataProperty() {
		return prop;
	}

	public Properties getXpathProperty() {
		return xpaths;
	}

	public WebElement getCustomerElement(Integer custID) {
		String xpath = String.format("//li[@id='context_id_%s']//a", custID);
		return driver.findElement(By.xpath(xpath));
	}

	public String getCustomerName(Integer custID) {
		String xpath = String.format("//li[@id='context_id_%s']//a", custID);
		return driver.findElement(By.xpath(xpath)).getText();
	}

}
