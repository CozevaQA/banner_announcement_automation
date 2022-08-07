package pageObjects;

import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import resources.GenericFunctions;

public class LoginPage extends GenericFunctions{
	private WebDriver driver;
	private Properties prop;
	private Properties xpaths;
	
	public LoginPage(WebDriver driver, Properties prop, Properties xpaths) {
		// TODO Auto-generated constructor stub
		this.driver = driver;
		this.prop = prop;
		this.xpaths = xpaths;
	}
	
	
	public WebElement username() {
		return driver.findElement(By.xpath(xpaths.getProperty("username")));
	}
	
	public WebElement password() {
		return driver.findElement(By.xpath(xpaths.getProperty("password")));
	}
	
	public WebElement button() {
		return driver.findElement(By.xpath(xpaths.getProperty("login_button")));
	}
	
	public WebElement reason() {
		return driver.findElement(By.xpath(xpaths.getProperty("login_reason")));
	}
	
	public WebElement ajaxPreloader() {
		return driver.findElement(By.xpath(xpaths.getProperty("ajax_preloader")));
	}
}
