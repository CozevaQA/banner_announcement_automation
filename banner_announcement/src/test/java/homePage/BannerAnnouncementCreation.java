package homePage;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import pageObjects.BannerAnnouncementPage;
import resources.Base;

public class BannerAnnouncementCreation extends Base{
	
	BannerAnnouncementPage bannAnn;
	
	@Test
	public void Test() {
		WebDriver driver = getDriver();
		Properties prop = getBaseDataProperty();
		Properties xpaths = getXpathProperty();
		
		bannAnn = new BannerAnnouncementPage(driver,prop,xpaths);
		
	}
	
	@Test
	public void Test1() {
		bannAnn.accountIcon().click();
	}
	
	@Test
	public void Test2() {
		bannAnn.bannerAnnouncementButton().click();
	}
	
	@Test
	public void Test3() {
		bannAnn.createBannerAnnouncement().click();
	}
}
