package pageObjects;

import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import resources.GenericFunctions;

public class CozevaPage extends GenericFunctions{
	private WebDriver driver;
	private Properties prop;
	private Properties xpaths;
	
	public CozevaPage(WebDriver driver, Properties prop, Properties xpaths) {
		// TODO Auto-generated constructor stub
		this.driver = driver;
		this.prop = prop;
		this.xpaths = xpaths;
	}
	
	public WebElement customerDropdown() {
		return driver.findElement(By.xpath(xpaths.getProperty("customer_dropdown")));
	}
	
	public List<WebElement> getCustomers() {
		return driver.findElements(By.xpath(xpaths.getProperty("customer_names")));
	}
	
	public WebElement getCustomer(String name) {
		String custSPXpath = String.format(xpaths.getProperty("customer_names")+"[@title='%s']", name);
		return driver.findElement(By.xpath(custSPXpath));
	}
	
	public WebElement applyButton() {
		return driver.findElement(By.xpath(xpaths.getProperty("apply_button")));
	}
	
	public List<WebElement> getBanners() {
		return driver.findElements(By.xpath(xpaths.getProperty("banners")));
	}
	
	public WebElement announcementContainer() {
		return driver.findElement(By.xpath(xpaths.getProperty("announcement_container")));
	}
	
	public WebElement specificAnnouncement(String announcementName) {
		String specificXpath = String.format(xpaths.getProperty("specific_announcement"), announcementName);
		return driver.findElement(By.xpath(specificXpath));
	}
	
	public List<WebElement> announcements() {
		return driver.findElements(By.xpath(xpaths.getProperty("announcements")));
	}
	
	public WebElement bannerContainer() {
		return driver.findElement(By.xpath(xpaths.getProperty("banner_container")));
	}
	
	public WebElement specificBanner(String bannerName) {
		String specificXpath = String.format(xpaths.getProperty("specific_banner"), bannerName);
		return driver.findElement(By.xpath(specificXpath));
	}
	
	public List<WebElement> banners() {
		return driver.findElements(By.xpath(xpaths.getProperty("banners")));
	}
	
	public WebElement accountIcon() {
		return driver.findElement(By.xpath(xpaths.getProperty("account_icon")));
	}
	
	public WebElement usersOption() {
		return driver.findElement(By.xpath(xpaths.getProperty("users_option")));
	}
	
	public WebElement userListFilter() {
		return driver.findElement(By.xpath(xpaths.getProperty("user_list_filter")));
	}
	
	public WebElement customerField() {
		return driver.findElement(By.xpath(xpaths.getProperty("customer_field")));
	}
	
	public WebElement getSpecificCustomer(String customerName,int space) {
		String custXpath=null;
		if(space==1) {
			custXpath = String.format(xpaths.getProperty("specific_customer"), customerName+" ");
		}
		else {
			custXpath = String.format(xpaths.getProperty("specific_customer"), customerName);
		}
		
//		System.out.println("custXpath: "+custXpath);
		return driver.findElement(By.xpath(custXpath));
	}
	
	public WebElement roleField() {
		return driver.findElement(By.xpath(xpaths.getProperty("role_field")));
	}
	
	public WebElement getSpecificRole(String roleName,int space) {
		String roleXpath=null;
		if(space==1) {
			roleXpath = String.format(xpaths.getProperty("specific_role"), roleName+" ");
		}
		else {
			roleXpath = String.format(xpaths.getProperty("specific_role"), roleName);
		}
		
//		System.out.println("roleXpath: "+roleXpath);
		return driver.findElement(By.xpath(roleXpath));
	}
	
	public WebElement userFilterApplyButton() {
		return driver.findElement(By.xpath(xpaths.getProperty("apply_button_of_users_filter")));
	}
	
	public WebElement getUser(int userIndex) {
		List<WebElement> userList = driver.findElements(By.xpath(xpaths.getProperty("user_checkboxes")));
		if(userList.size()==0) {
			return null;
		}
		
		try {
			return driver.findElements(By.xpath(xpaths.getProperty("user_checkboxes"))).get(userIndex);
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public WebElement kebabIcon() {
		return driver.findElement(By.xpath(xpaths.getProperty("kebab_icon")));
	}
	
	public WebElement masquerade() {
		return driver.findElement(By.xpath(xpaths.getProperty("masquerade")));
	}
	
	public WebElement masqueradeReasonField() {
		return driver.findElement(By.xpath(xpaths.getProperty("masquerade_reason_field")));
	}
	
	public WebElement masqueradeSignatureField() {
		return driver.findElement(By.xpath(xpaths.getProperty("masquerade_signature_field")));
	}
	
	public WebElement goButton() {
		return driver.findElement(By.xpath(xpaths.getProperty("masquerade_go_button")));
	}
	
	public WebElement switchBack() {
		return driver.findElement(By.xpath(xpaths.getProperty("switch_back")));
	}
	
	public WebElement switchBackAgain() {
		return driver.findElement(By.xpath(xpaths.getProperty("switch_back_again")));
	}
	
	public WebElement isTOCPresent() {
		try {
			return driver.findElement(By.xpath(xpaths.getProperty("TOC_page")));
		}
		catch(NoSuchElementException noE) {
			return null;
		}
	}
	
	
	public WebElement skipButton() {
		return driver.findElement(By.xpath(xpaths.getProperty("skip_button")));
	}
	
	public Boolean isToastContainerPresent() {
		try {
			driver.findElement(By.xpath(xpaths.getProperty("toast_message_container")));
			return true;
		}
		catch(NoSuchElementException noE) {
			return false;
		}
	}
	
	public WebElement toastContainerCloseIcon() {
		return driver.findElement(By.xpath(xpaths.getProperty("toast_message_container_close_icon")));
	}
}
