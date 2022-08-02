package homePage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.math3.ode.AbstractFieldIntegrator;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.opentelemetry.exporter.logging.SystemOutLogExporter;
import pageObjects.BannerAnnouncementPage;
import pageObjects.CozevaPage;
import pageObjects.LoginPage;
import resources.Base;
import resources.CSVutility;
import resources.Excel;

public class HomePage extends Base {

	private WebDriver driver;
	private Properties prop;
	private Properties xpaths;
	private Actions action;
	private Excel excelData;
	private WebDriverWait wait;
	private HashMap<Integer, Object> CustomerIDToCustomerDetails;
	private HashMap<String, Object> roleAbbrToRoleDetails;
	private ArrayList<Integer> allCustomerID;
	private ArrayList<Integer> selectedCustomerID;
	private ArrayList<Integer> nonSelectedCustomerID;
	private ArrayList<String> selectedRoleAbbr;
	private HashMap<String, String> userData;
	private int testCaseID;

	@Test(priority = 0)
	public void Test0_initialization() throws IOException, InterruptedException {

		CustomerIDToCustomerDetails = new HashMap<Integer, Object>();
		roleAbbrToRoleDetails = new HashMap<String, Object>();
		allCustomerID = new ArrayList<Integer>();
		selectedCustomerID = new ArrayList<Integer>();
		nonSelectedCustomerID = new ArrayList<Integer>();
		selectedRoleAbbr = new ArrayList<String>();
		userData = new HashMap<String, String>();
		excelData = new Excel();
		testCaseID = 1;

		initializeDriver();

		driver = getDriver();
		prop = getBaseDataProperty();
		xpaths = getXpathProperty();
		action = new Actions(driver);
		wait = new WebDriverWait(driver, Duration.ofSeconds(60));

		Thread.sleep(500);
	}

	@Test(priority = 1)
	public void Test1_load_data() throws FileNotFoundException, IOException {
		CSVutility csv = new CSVutility();

		csv.readCSVwithHeader(prop.getProperty("customerDB_path"));
		CustomerIDToCustomerDetails = csv.getCustomerIDToCustomerDetails();
		allCustomerID = new ArrayList<Integer>();
		allCustomerID = (ArrayList<Integer>) csv.getAllCustomerID();
		csv.closeCSV();

		csv.readCSVwithHeader(prop.getProperty("roleDB_path"));
		roleAbbrToRoleDetails = csv.getRoleAbbrToRole();
		csv.closeCSV();

		csv.readCSVwithHeader(prop.getProperty("userInput_path"));
		userData = csv.loadUserData();
		csv.closeCSV();

		excelData.createNewExcel(prop.getProperty("excel_report_file_folder_location"),
				prop.getProperty("excel_report_file_name"));
		excelData.createNewSheet(prop.getProperty("excel_report_file_name"));
		List<String> headerRow = new ArrayList<String>(Arrays.asList("Test ID","Test Scenario", "Expected Result", "Status"));
		excelData.setHeader(headerRow);

		selectedCustomerID = new ArrayList<Integer>();
		String[] customerIDs = userData.get("customers").split(",");
		for (String str : customerIDs) {
			Integer ID = Integer.parseInt(str.trim());
			selectedCustomerID.add(ID);
		}

		selectedRoleAbbr = new ArrayList<String>();
		String[] roleAbbr = userData.get("roles").split(",");
		for (String str : roleAbbr) {
			str = str.trim().toLowerCase();
			selectedRoleAbbr.add(str);
		}

//		System.out.println("CustomerIDToCustomerDetails: " + CustomerIDToCustomerDetails);
//		System.out.println("roleAbbrToRoleDetails: " + roleAbbrToRoleDetails);
		System.out.println("userData: " + userData);
		System.out.println("selectedCustomerID: " + selectedCustomerID);
//		System.out.println("allCustomerID: " + allCustomerID);
//		System.out.println("nonSelectedCustomerID: " + nonSelectedCustomerID);
		System.out.println("selectedRoleAbbr: " + selectedRoleAbbr+"\n\n");
	}

	@Test(priority = 2)
	public void Test2_login_to_registry() throws InterruptedException {
		try {
			driver.get(prop.getProperty("cozevaLoginPage"));

			LoginPage login = new LoginPage(driver, prop, xpaths);

			login.username().sendKeys(new String(Base64.getDecoder().decode(prop.getProperty("username"))));
			login.password().sendKeys(new String(Base64.getDecoder().decode(prop.getProperty("password"))));
			login.button().click();

			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpaths.getProperty("login_reason"))));

			login.reason().sendKeys(prop.getProperty("login_reason"));
			login.button().click();

			List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),"To verify login successful or not",
					"Login should be committed successfully", "PASSED"));
			excelData.insertRow(testList);
		} catch (Exception e) {
			List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),"To verify login successful or not",
					"Login should be committed successfully", "FAILED"));
			excelData.insertRow(testList);
		}

		Thread.sleep(500);
	}

	@Test(priority = 3)
	public void Test3_open_BA_creation_page() throws InterruptedException {
		try {
			BannerAnnouncementPage BAPage = new BannerAnnouncementPage(driver, prop, xpaths);

			BAPage.accountIcon().click();
			BAPage.bannerAnnouncementButton().click();
			BAPage.createBannerAnnouncement().click();

			Assert.assertTrue(BAPage.bannerAnnouncementForm().isDisplayed());

			try {
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
				wait.until(ExpectedConditions.invisibilityOf(driver.findElement(
						By.xpath("//div[@id='modal_dialog_add_banner_form']//div[@class='ajax_preloader']"))));
				Thread.sleep(500);
			} catch (NoSuchElementException noElem) {
				Thread.sleep(1500);
			}

			List<String> testList = new ArrayList<String>(
					Arrays.asList(Integer.toString(testCaseID++),"To verify banner and announcement page is opening properly or not",
							"Banner and announcement page should be opened properly", "PASSED"));
			excelData.insertRow(testList);
		} catch (Exception e) {
			List<String> testList = new ArrayList<String>(
					Arrays.asList(Integer.toString(testCaseID++),"To verify banner and announcement page is opening properly or not",
							"Banner and announcement page should be opened properly", "FAILED"));
			excelData.insertRow(testList);
		}

		Thread.sleep(500);
	}

	@Test(priority = 4)
	public void Test4_toast_message() throws InterruptedException {
		try {
			List<String> toastMessgagesDB = new ArrayList<String>();
			toastMessgagesDB.add("stopSelect Banner/Announcement field is required.");
			toastMessgagesDB.add("stopText field is required.");
			toastMessgagesDB.add("stopTo field is required.");

			BannerAnnouncementPage BAPage = new BannerAnnouncementPage(driver, prop, xpaths);

			action.moveToElement(BAPage.saveButton()).click().build().perform();
			Thread.sleep(200);
			// BAPage.saveButton()

			Assert.assertTrue(BAPage.toastContainer().isDisplayed());
			Thread.sleep(200);
			List<WebElement> toastMessages = BAPage.toastMessages();

			for (int i = 0; i < toastMessages.size(); i++) {
				Assert.assertEquals(toastMessages.get(i).getText(), toastMessgagesDB.get(i));
			}

			BAPage.toastContainerCloseIcon().click();

			List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
					"To verify 3 toast messages are appearing after directly hitting save button properly or not",
					"3 toast messages should be appearing properly", "PASSED"));
			excelData.insertRow(testList);
		} catch (Exception e) {
			List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
					"To verify 3 toast messages are appearing after directly hitting save button properly or not",
					"3 toast messages should be appearing properly", "FAILED"));
			excelData.insertRow(testList);
		}

		Thread.sleep(500);
	}

	@Test(priority = 5)
	public void Test5_select_banner_or_announcement() throws Exception {
		try {
			List<String> toastMessgagesDB = new ArrayList<String>();
			toastMessgagesDB.add("stopText field is required.");
			toastMessgagesDB.add("stopTo field is required.");

			BannerAnnouncementPage BAPage = new BannerAnnouncementPage(driver, prop, xpaths);

			BAPage.selectBADropdown().click();
			Thread.sleep(200);
			String category = userData.get("Category");
			if (category.startsWith("b") || category.startsWith("B")) {
				BAPage.bannerOption().click();
			} else if (category.startsWith("a") || category.startsWith("A")) {
				BAPage.announcementOption().click();
			} else {
				throw new Exception("Category type not found.");
			}
			Thread.sleep(200);
			action.moveToElement(BAPage.saveButton()).click().build().perform();
			Thread.sleep(200);
			Assert.assertTrue(BAPage.toastContainer().isDisplayed());
			List<WebElement> toastMessages = BAPage.toastMessages();

			for (int i = 0; i < toastMessages.size(); i++) {
				Assert.assertEquals(toastMessages.get(i).getText(), toastMessgagesDB.get(i));
			}

			BAPage.toastContainerCloseIcon().click();
			
			//inserting passed test case
			List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
					"To verify 2 toast messages are appearing after selecting banner/announcement and directly hitting save button properly or not",
					"2 toast messages should be appearing properly", "PASSED"));
			excelData.insertRow(testList);
		}
		catch(Exception e) {
			//inserting failed test case
			List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
					"To verify 2 toast messages are appearing after selecting banner/announcement and directly hitting save button properly or not",
					"2 toast messages should be appearing properly", "FAILED"));
			excelData.insertRow(testList);
		}
		

		Thread.sleep(500);
	}

	@Test(priority = 6)
	public void Test6_text_field() throws InterruptedException {
		try {
			String toastMessage = "To field is required.";

			BannerAnnouncementPage BAPage = new BannerAnnouncementPage(driver, prop, xpaths);

			String textFieldData = userData.get("text");

			BAPage.textField().sendKeys(textFieldData);
			Thread.sleep(200);
			action.moveToElement(BAPage.saveButton()).click().build().perform();
			Thread.sleep(200);
			driver.switchTo().alert().accept();
			Thread.sleep(200);
			Assert.assertTrue(BAPage.toastContainer().isDisplayed());
			WebElement singkeToastMessage = BAPage.singleToastMessage();

			Assert.assertEquals(singkeToastMessage.getText(), toastMessage);
			Thread.sleep(200);
			BAPage.toastContainerCloseIcon().click();
			
			List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
					"To verify 'To field is required.' toast message is appearing after filling up text field ",
					"'To field is required.' toast message should be appeared properly", "PASSED"));
			excelData.insertRow(testList);
		}
		catch(Exception e) {
			List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
					"To verify 'To field is required.' toast message is appearing after filling up text field ",
					"'To field is required.' toast message should be appeared properly", "FAILED"));
			excelData.insertRow(testList);
		}
		

		Thread.sleep(500);
	}

	@Test(priority = 7)
	public void Test7_link_field() throws InterruptedException {
		try {
			String toastMessage = "To field is required.";

			BannerAnnouncementPage BAPage = new BannerAnnouncementPage(driver, prop, xpaths);

			String linkFieldData = userData.get("link");

			BAPage.linkField().sendKeys(linkFieldData);
			Thread.sleep(200);
			action.moveToElement(BAPage.saveButton()).click().build().perform();
			Thread.sleep(200);
			driver.switchTo().alert().accept();
			Thread.sleep(200);
			Assert.assertTrue(BAPage.toastContainer().isDisplayed());
			WebElement singleToastMessage = BAPage.singleToastMessage();

			Assert.assertEquals(singleToastMessage.getText(), toastMessage);
			Thread.sleep(200);
			BAPage.toastContainerCloseIcon().click();
			
			List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
					"To verify 'To field is required.' toast message is appearing after filling up link field ",
					"'To field is required.' toast message should be appeared properly", "PASSED"));
			excelData.insertRow(testList);
		}
		catch(Exception e) {
			List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
					"To verify 'To field is required.' toast message is appearing after filling up link field ",
					"'To field is required.' toast message should be appeared properly", "FAILED"));
			excelData.insertRow(testList);
		}

		Thread.sleep(500);
	}

	@Test(priority = 8)
	public void Test8_attachment_field() throws InterruptedException {
		try {
			String toastMessage = "You are not allowed to upload more than 1 file(s).";

			BannerAnnouncementPage BAPage = new BannerAnnouncementPage(driver, prop, xpaths);

			String file1 = userData.get("attachment1");
			String file2 = userData.get("attachment2");

			BAPage.attachmentField().sendKeys(file1);
			Thread.sleep(200);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
			wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath(xpaths.getProperty("attachment_field_delete_icon"))));
			Thread.sleep(200);
			BAPage.attachmentField().sendKeys(file2);
			Thread.sleep(200);
			Assert.assertTrue(BAPage.toastContainer().isDisplayed());
			WebElement singleToastMessage = BAPage.singleToastMessage();

			Assert.assertEquals(singleToastMessage.getText(), toastMessage);
			Thread.sleep(200);
			BAPage.toastContainerCloseIcon().click();
			
			List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
					"To verify 'You are not allowed to upload more than 1 file(s).' toast message is appearing after uploading more than one file ",
					"'You are not allowed to upload more than 1 file(s).' toast message should be appeared properly", "PASSED"));
			excelData.insertRow(testList);
		}
		catch(Exception e) {
			List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
					"To verify 'You are not allowed to upload more than 1 file(s).' toast message is appearing after uploading more than one file ",
					"'You are not allowed to upload more than 1 file(s).' toast message should be appeared properly", "FAILED"));
			excelData.insertRow(testList);
		}
		
		Thread.sleep(500);
	}

	@Test(priority = 9)
	public void Test9_select_date_field() throws InterruptedException {
		try {
			BannerAnnouncementPage BAPage = new BannerAnnouncementPage(driver, prop, xpaths);

			BAPage.toDateField().click();
			Thread.sleep(200);
			BAPage.todays_date().click();
			
			List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
					"To verify date field are selected properly ",
					"Date field should be selected properly", "PASSED"));
			excelData.insertRow(testList);
		}
		catch(Exception e) {
			List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
					"To verify date field are selected properly ",
					"Date field should be selected properly", "FAILED"));
			excelData.insertRow(testList);
		}

		Thread.sleep(500);
	}

	@Test(priority = 10)
	public void Test10_select_roles() throws InterruptedException {
		try {
			BannerAnnouncementPage BAPage = new BannerAnnouncementPage(driver, prop, xpaths);

			ArrayList<String> roleNameAtBannerModal = new ArrayList<String>();
			for (String roleAbbr : selectedRoleAbbr) {
				ArrayList<String> roleList = (ArrayList<String>) roleAbbrToRoleDetails.get(roleAbbr);
				roleNameAtBannerModal.add(roleList.get(0).trim().toLowerCase());
			}

			// System.out.println("rolesData:" + rolesData);
			BAPage.rolesField().click();
			Thread.sleep(500);
			List<WebElement> roles = BAPage.roles();

			for (WebElement elem : roles) {
				String roleName = elem.getText().trim().toLowerCase();

				// System.out.println("Role name: " + roleName);
				if (roleNameAtBannerModal.contains(roleName)) {
					continue;
				} else {
					action.moveToElement(elem).click().build().perform();
					// elem.click();
				}

				Thread.sleep(100);
			}
			
			List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
					"To verify all the roles as described by user are getting selected properly ",
					"All the users as described by user should be selected properly", "PASSED"));
			excelData.insertRow(testList);
		}
		catch(Exception e) {
			List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
					"To verify all the roles as described by user are getting selected properly ",
					"All the users as described by user should be selected properly", "FAILED"));
			excelData.insertRow(testList);
		}

		Thread.sleep(500);
	}

	@Test(priority = 11)
	public void Test11_select_customers() throws InterruptedException {
		try {
			BannerAnnouncementPage BAPage = new BannerAnnouncementPage(driver, prop, xpaths);

			ArrayList<String> customerNameAtBannerModal = new ArrayList<String>();
			for (Integer custID : selectedCustomerID) {
				ArrayList<String> custList = (ArrayList<String>) CustomerIDToCustomerDetails.get(custID);
				customerNameAtBannerModal.add(custList.get(1).trim().toLowerCase());
			}

			// System.out.println("customersData: " + customersData);
			BAPage.customersField().click();
			Thread.sleep(500);
			List<WebElement> customers = BAPage.customers();

			for (int i = 1; i < customers.size(); i++) {
				WebElement elem = customers.get(i);
				String customerName = elem.getText().trim().toLowerCase();

				// System.out.println("Customer name: " + customerName);
				if (customerNameAtBannerModal.contains(customerName)) {
					continue;
				} else {
					action.moveToElement(elem).click().build().perform();
					// elem.click();
				}

				Thread.sleep(100);
			}
			
			List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
					"To verify all the customers as described by user are getting selected properly ",
					"All the customers as described by user should be selected properly", "PASSED"));
			excelData.insertRow(testList);
		}
		catch(Exception e) {
			List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
					"To verify all the customers as described by user are getting selected properly ",
					"All the customers as described by user should be selected properly", "FAILED"));
			excelData.insertRow(testList);
		}

		Thread.sleep(500);
	}

	@Test(priority = 12)
	public void Test12_saveFinally() throws InterruptedException {
		try {
			BannerAnnouncementPage BAPage = new BannerAnnouncementPage(driver, prop, xpaths);
			action.moveToElement(BAPage.saveButton()).click().build().perform();
			Thread.sleep(200);
			wait.until(ExpectedConditions.invisibilityOf(BAPage.bannerAnnouncementForm()));
			
			List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
					"To verify the modal is finally getting saved properly or not.",
					"The modal is finally getting saved", "PASSED"));
			excelData.insertRow(testList);
		}
		catch(Exception e) {
			List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
					"To verify the modal is finally getting saved properly or not.",
					"The modal is finally getting saved", "FAILED"));
			excelData.insertRow(testList);
		}
		
		Thread.sleep(500);
	}

	@Test(priority = 13)
	public void Test13_check_correctness_from_cs_role() throws Exception {

		CozevaPage cozevaPage = new CozevaPage(driver, prop, xpaths);

		String category = userData.get("Category");
		String textData = userData.get("text");

		textData = textData.replace("{", "").replace("}", "");
		textData = textData.replace("[", "").replace("]", "");
		if (selectedRoleAbbr.contains("cs")) {
			for (Integer custID : selectedCustomerID) {
				try {
					WebElement customerElem = getCustomerElement(custID);
					cozevaPage.customerDropdown().click();
					Thread.sleep(100);
					customerElem.click();
					Thread.sleep(100);
					cozevaPage.applyButton().click();
//					cozevaPage.ajaxPreloaderWait(driver);
					Thread.sleep(1000);
				} catch (NoSuchElementException NoE) {
					System.out.println("Customer not found: " + custID);
					continue;
				} catch (Exception E) {
					System.out.println("Exception occurred(Probably apply button is skipped): " + custID);
				}
				
				String expectedResult = String.format("The desired banner or announcement should be visible for the customer ID: %s", custID);
				try {
					if (category.startsWith("a") || category.startsWith("A")) {
						Assert.assertTrue(cozevaPage.announcementContainer().isDisplayed());
						List<String> announcementStrings = cozevaPage.announcements().stream().map(e -> e.getText())
								.collect(Collectors.toList());
						Assert.assertTrue(announcementStrings.contains(textData));
					} else if (category.startsWith("b") || category.startsWith("B")) {
						Assert.assertTrue(cozevaPage.bannerContainer().isDisplayed());
						List<String> bannerStrings = cozevaPage.banners().stream().map(e -> e.getText())
								.collect(Collectors.toList());
						Assert.assertTrue(bannerStrings.contains(textData));
					}
					
					
					List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
							"To verify if the desired banner or announcement is properly visible or not",
							expectedResult, "PASSED"));
					excelData.insertRow(testList);
				}
				catch(Exception e) {
					List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
							"To verify if the desired banner or announcement is properly visible or not",
							expectedResult, "FAILED"));
					excelData.insertRow(testList);
				}
				

			}
			
			// Checking unavailability of banner or announcement

			nonSelectedCustomerID = new ArrayList<Integer>(allCustomerID);
			for (Integer id : selectedCustomerID) {
				nonSelectedCustomerID.remove(id);
			}

			Set<Integer> randomCustIndex = new HashSet<Integer>();
			for (int i = 0; i < 5; i++) {
				Integer index = (int) (Math.random() * nonSelectedCustomerID.size());
				randomCustIndex.add(index);
			}

			for (Integer randomIndex : randomCustIndex) {
				Integer custID = nonSelectedCustomerID.get(randomIndex);
//				System.out.println("Randomly picked id: "+custID);
				
				try {
					WebElement customerElem = getCustomerElement(custID);
					cozevaPage.customerDropdown().click();
					Thread.sleep(100);
					customerElem.click();
					Thread.sleep(100);
					cozevaPage.applyButton().click();
//					cozevaPage.ajaxPreloaderWait(driver);
					Thread.sleep(1000);
				} catch (NoSuchElementException NoE) {
					System.out.println("Customer not found: " + custID);
					continue;
				} catch (Exception E) {
					System.out.println("Exception occurred(Probably apply button is skipped): " + custID);
				}
				
				String expectedResult = String.format("The desired banner or announcement should not be visible for the customer ID: %s", custID);
				try {
					if (category.startsWith("a") || category.startsWith("A")) {
						// Assert.assertTrue(cozevaPage.announcementContainer().isDisplayed());
						List<String> announcementStrings = cozevaPage.announcements().stream().map(e -> e.getText())
								.collect(Collectors.toList());
						// Assert.assertTrue(announcementStrings.contains(textData));
						Assert.assertFalse(announcementStrings.contains(textData));
					} else if (category.startsWith("b") || category.startsWith("B")) {
						// Assert.assertTrue(cozevaPage.bannerContainer().isDisplayed());
						List<String> bannerStrings = cozevaPage.banners().stream().map(e -> e.getText())
								.collect(Collectors.toList());
						// Assert.assertTrue(bannerStrings.contains(textData));
						Assert.assertFalse(bannerStrings.contains(textData));
					}
					
					List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
							"To verify the desired banner or announcement is not visible.",
							expectedResult, "PASSED"));
					excelData.insertRow(testList);
				}
				catch(Exception e) {
					List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
							"To verify the desired banner or announcement is not visible.",
							expectedResult, "FAILED"));
					excelData.insertRow(testList);
				}
				
			}
		}

		
	}

	@Test(priority = 14)
	public void Test14_check_correctness_from_other_roles() throws Exception {
		List<String> roleNames = new ArrayList<String>();
		List<String> customerNames = new ArrayList<String>();
		selectedRoleAbbr.remove("cs");
		
		if(selectedRoleAbbr.size()==0) {
			return;
		}

		for (Integer custID : selectedCustomerID) {
			ArrayList<String> list = (ArrayList<String>) CustomerIDToCustomerDetails.get(custID);
			if (list.get(0).equals("1")) {
				continue;
			}
			String name = list.get(2);
			customerNames.add(name);
		}

		for (String abbr : selectedRoleAbbr) {
			ArrayList<String> list = (ArrayList<String>) roleAbbrToRoleDetails.get(abbr);
			String name = list.get(1);
			roleNames.add(name);
		}

		roleNames = roleNames.stream().map(e -> e.trim()).collect(Collectors.toList());
		customerNames = customerNames.stream().map(e -> e.trim()).collect(Collectors.toList());

		CozevaPage cozevaPage = new CozevaPage(driver, prop, xpaths);

		String category = userData.get("Category");
		String textData = userData.get("text");

		textData = textData.replace("{", "").replace("}", "");
		textData = textData.replace("[", "").replace("]", "");
		// System.out.println("textData:"+textData);

		cozevaPage.accountIcon().click();
		Thread.sleep(100);
		cozevaPage.usersOption().click();
		Thread.sleep(100);
		cozevaPage.ajaxPreloaderWait(driver);
		Thread.sleep(100);

		for (String custName : customerNames) {

			cozevaPage.userListFilter().click();
			Thread.sleep(100);
			cozevaPage.customerField().click();
			Thread.sleep(200);
			try {
				action.moveToElement(cozevaPage.getSpecificCustomer(custName, 0)).click().build().perform();
				// cozevaPage.getSpecificCustomer(customer,0).click();
			} catch (NoSuchElementException noE) {
				action.moveToElement(cozevaPage.getSpecificCustomer(custName, 1)).click().build().perform();
				// cozevaPage.getSpecificCustomer(customer,1).click();
			}
			cozevaPage.userFilterApplyButton().click();
			cozevaPage.ajaxPreloaderWait(driver);

			for (String roleName : roleNames) {

				cozevaPage.userListFilter().click();
				Thread.sleep(100);
				cozevaPage.roleField().click();
				Thread.sleep(200);
				try {
					// cozevaPage.getSpecificRole(role,0).click();
					action.moveToElement(cozevaPage.getSpecificRole(roleName, 0)).click().build().perform();
				} catch (NoSuchElementException noE) {
					// cozevaPage.getSpecificRole(role,1).click();
					action.moveToElement(cozevaPage.getSpecificRole(roleName, 1)).click().build().perform();
				}
				cozevaPage.userFilterApplyButton().click();
				cozevaPage.ajaxPreloaderWait(driver);

				int userIndex = 0;
				WebElement userCheckbox = cozevaPage.getUser(userIndex++);
				while (userCheckbox != null) {
					action.moveToElement(userCheckbox).click().build().perform();
					cozevaPage.kebabIcon().click();
					Thread.sleep(50);
					cozevaPage.masquerade().click();

					if (cozevaPage.isToastContainerPresent()) {
						cozevaPage.toastContainerCloseIcon().click();
						action.moveToElement(userCheckbox).click().build().perform();
						userCheckbox = cozevaPage.getUser(userIndex++);
						continue;
					}
					break;
				}

				if (userCheckbox == null) {
					System.out.println("No such user found for: " + custName + " - " + roleName);
					continue;
				}
				/*
				 * action.moveToElement(userCheckbox).click().build().perform();
				 * cozevaPage.kebabIcon().click(); cozevaPage.masquerade().click();
				 */

				cozevaPage.masqueradeReasonField().sendKeys(prop.getProperty("login_reason").trim());
				cozevaPage.masqueradeSignatureField().sendKeys(prop.getProperty("signature").trim());
				cozevaPage.goButton().click();

				if (cozevaPage.isToastContainerPresent()) {
					cozevaPage.toastContainerCloseIcon().click();
				}

				if (cozevaPage.isTOCPresent() != null) {
					cozevaPage.skipButton().click();
				}

				// Banner/Announcement verification code
				String expectedResult = String.format("The desired banner or announcement should be visible for the customer '%s' and role '%s'.", custName,roleName);
				try {
					if (category.startsWith("a") || category.startsWith("A")) {
						Assert.assertTrue(cozevaPage.announcementContainer().isDisplayed());
						List<String> announcementStrings = cozevaPage.announcements().stream().map(e -> e.getText())
								.collect(Collectors.toList());
//						System.out.println(announcementStrings);
//						Assert.assertTrue(announcementStrings.contains(textData));
					} else if (category.startsWith("b") || category.startsWith("B")) {
						Assert.assertTrue(cozevaPage.bannerContainer().isDisplayed());
						List<String> bannerStrings = cozevaPage.banners().stream().map(e -> e.getText())
								.collect(Collectors.toList());
//						System.out.println(bannerStrings);
						Assert.assertTrue(bannerStrings.contains(textData));
					}
					
					List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
							"To verify if the desired banner or announcement is properly visible or not",
							expectedResult, "PASSED"));
					excelData.insertRow(testList);
				}
				catch(Exception e) {
					List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
							"To verify if the desired banner or announcement is properly visible or not",
							expectedResult, "FAILED"));
					excelData.insertRow(testList);
				}
				

				// wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpaths.getProperty("toast_message_container"))));

				// action.moveToElement(cozevaPage.switchBack()).click().build().perform();
				// action.moveToElement(cozevaPage.switchBackAgain()).click().build().perform();
				cozevaPage.switchBack().click();
				cozevaPage.switchBackAgain().click();
				cozevaPage.ajaxPreloaderWait(driver);
				
				if (cozevaPage.isToastContainerPresent()) {
					cozevaPage.toastContainerCloseIcon().click();
				}
				// wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpaths.getProperty("toast_message_container"))));
			}
		}

		Thread.sleep(500);
	}

	@Test(priority = 14)
	public void delete_banner_announcement() throws InterruptedException {
		String deleteSuccessMsg = "Deleted Successfully!";
		try {
			BannerAnnouncementPage BAPage = new BannerAnnouncementPage(driver, prop, xpaths);
			final String text = userData.get("text").replace("{", "").replace("}", "").replace("[", "").replace("]", "");
			// text = text.replace("{", "").replace("}", "");
			// text = text.replace("[", "").replace("]", "");

			BAPage.accountIcon().click();
			Thread.sleep(150);
			BAPage.bannerAnnouncementButton().click();
			Thread.sleep(150);
			List<WebElement> bannerRecordElements = BAPage.getBannerAnnouncementRecord();

			bannerRecordElements = bannerRecordElements.stream().map(e -> e.findElement(By.xpath(".//td[4]")))
					.collect(Collectors.toList());
			bannerRecordElements = bannerRecordElements.stream().filter(e -> e.getText().equalsIgnoreCase(text))
					.collect(Collectors.toList());

			for (WebElement elem : bannerRecordElements) {
//				System.out.println(elem.getText());
				elem = elem.findElement(By.xpath("./.."));
				elem = elem.findElement(By.xpath(".//a//span[text()='delete']"));
				elem.click();
				Thread.sleep(150);
				driver.switchTo().alert().accept();
				Thread.sleep(150);
				Assert.assertTrue(BAPage.toastContainer().isDisplayed());
				WebElement singleToastMessage = BAPage.singleToastMessage();
				Assert.assertEquals(singleToastMessage.getText(), deleteSuccessMsg);
				Thread.sleep(200);
				BAPage.toastContainerCloseIcon().click();
				Thread.sleep(200);
				Assert.assertTrue(BAPage.bannerAnnouncementLandingPage().isDisplayed());
				Thread.sleep(200);
			}
			
			List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
					"To verify if the created banner or announcement has been deleted properly or not",
					"Created banner or announcement should be deleted", "PASSED"));
			excelData.insertRow(testList);
		}
		catch(Exception e) {
			List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
					"To verify if the created banner or announcement has been deleted properly or not",
					"Created banner or announcement should be deleted", "FAILED"));
			excelData.insertRow(testList);
		}
		

		Thread.sleep(500);
	}

	@Test(priority = 15)
	public void logout_and_close() throws InterruptedException {
		BannerAnnouncementPage BAPage = new BannerAnnouncementPage(driver, prop, xpaths);
		try {
			BAPage.accountIcon().click();
			Thread.sleep(150);
			BAPage.logoutButton().click();
			Thread.sleep(150);
			driver.close();
			
			List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
					"To verify if COZEVA logout successfully committed or not",
					"COZEVA logout should be committed successfully", "PASSED"));
			excelData.insertRow(testList);
		}
		catch(Exception e) {
			List<String> testList = new ArrayList<String>(Arrays.asList(Integer.toString(testCaseID++),
					"To verify if the created banner or announcement has been deleted properly or not",
					"COZEVA logout should be committed successfully", "FAILED"));
			excelData.insertRow(testList);
		}
		
		excelData.generateFile();

		Thread.sleep(500);
	}
}
