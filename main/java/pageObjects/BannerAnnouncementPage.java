package pageObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import resources.GenericFunctions;

public class BannerAnnouncementPage extends GenericFunctions{
	private WebDriver driver;
	private Properties prop;
	private Properties xpaths;
	
	public BannerAnnouncementPage(WebDriver driver, Properties prop, Properties xpaths) {
		// TODO Auto-generated constructor stub
		this.driver = driver;
		this.prop = prop;
		this.xpaths = xpaths;
	}
	
	public WebElement accountIcon() {
		return driver.findElement(By.xpath(xpaths.getProperty("account_section")));
	}
	
	public WebElement bannerAnnouncementButton() {
		return driver.findElement(By.xpath(xpaths.getProperty("banner_announcement_button")));
	}
	
	public WebElement bannerAnnouncementLandingPage() {
		return driver.findElement(By.xpath(xpaths.getProperty("banner_announcement_landing_page")));
	}
	
	public WebElement bannerAnnouncementForm() {
		return driver.findElement(By.xpath(xpaths.getProperty("banner_announcement_form")));
	}
	
	public WebElement createBannerAnnouncement() {
		return driver.findElement(By.xpath(xpaths.getProperty("create_banner_announcement")));
	}
	
	public WebElement saveButton() {
		return driver.findElement(By.xpath(xpaths.getProperty("BA_creation_modal_save_button")));
	}
	
	public WebElement toastContainer() {
		return driver.findElement(By.xpath(xpaths.getProperty("toast_message_container")));
	}
	
	public List<WebElement> toastMessages() {
		return driver.findElements(By.xpath(xpaths.getProperty("toast_messages")));
	}
	
	public WebElement singleToastMessage() {
		return driver.findElement(By.xpath(xpaths.getProperty("single_toast_message")));
	}
	
	public WebElement toastContainerCloseIcon() {
		return driver.findElement(By.xpath(xpaths.getProperty("toast_message_container_close_icon")));
	}
	
	public WebElement selectBADropdown() {
		return driver.findElement(By.xpath(xpaths.getProperty("select_BA_dropdown")));
	}
	
	public WebElement bannerOption() {
		return driver.findElement(By.xpath(xpaths.getProperty("banner_option")));
	}
	
	public WebElement announcementOption() {
		return driver.findElement(By.xpath(xpaths.getProperty("announcement_option")));
	}
	
	public WebElement textField() {
		return driver.findElement(By.xpath(xpaths.getProperty("text_field")));
	}
	
	public WebElement linkField() {
		return driver.findElement(By.xpath(xpaths.getProperty("link_field")));
	}
	
	public WebElement attachmentField() {
		return driver.findElement(By.xpath(xpaths.getProperty("attachment_field")));
	}
	
	public WebElement deleteIconOnAttachmentField() {
		return driver.findElement(By.xpath(xpaths.getProperty("attachment_field_delete_icon")));
	}
	
	public WebElement toDateField() {
		return driver.findElement(By.xpath(xpaths.getProperty("to_date_field")));
	}
	
	public WebElement todays_date() {
		return driver.findElement(By.xpath(xpaths.getProperty("todays_date")));
	}
	
	public WebElement rolesField() {
		return driver.findElement(By.xpath(xpaths.getProperty("roles_field")));
	}
	
	public List<WebElement> roles() {
		return driver.findElements(By.xpath(xpaths.getProperty("role_elements")));
	}
	
	public WebElement customersField() {
		return driver.findElement(By.xpath(xpaths.getProperty("customers_field")));
	}
	
	public List<WebElement> customers() {
		return driver.findElements(By.xpath(xpaths.getProperty("customer_elements")));
	}
	
	public List<WebElement> getBannerAnnouncementRecord(){
		return driver.findElements(By.xpath(xpaths.getProperty("banner_announcement_rows")));
	}
	
	public WebElement logoutButton(){
		return driver.findElement(By.xpath(xpaths.getProperty("logout_button")));
	}
	
}
