package resources;

import java.time.Duration;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GenericFunctions {
	public void switchTab(WebDriver driver, int tab_no) {
		try {
			ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());

			driver.switchTo().window(tabs2.get(tab_no - 1));
			// driver.close();
			// driver.switchTo().window(tabs2.get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void invisibilityWait(WebDriver driver, WebElement loader_elem) throws InterruptedException {
		Thread.sleep(500);
		try {
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.invisibilityOf(loader_elem));
			Thread.sleep(500);
		} catch (NoSuchElementException noElem) {
			Thread.sleep(1500);
		}
	}

	public void ajaxPreloaderWait(WebDriver driver) throws InterruptedException {
		Thread.sleep(500);
		try {
//			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='ajax_preloader']")));
			Thread.sleep(500);
		} catch (NoSuchElementException noElem) {
			Thread.sleep(1500);
		}
	}

	public void wait_to_load(WebDriver driver) {
		String loader_element_classname = "sm_download_cssload_loader_wrap";
		WebDriverWait w = new WebDriverWait(driver, 60);
		w.until(ExpectedConditions.invisibilityOfElementLocated(By.className(loader_element_classname)));
	}
}
