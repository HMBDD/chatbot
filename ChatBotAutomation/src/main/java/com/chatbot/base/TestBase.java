package com.chatbot.base;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.chatbot.util.ExcelReader;
 
 

public class TestBase {
	 
	public  static WebDriver driver;
 
	public static long id ;
	 
	public static Properties prop ;
	public static String executionType;
	public static String browserValue;
	public static String isGrid;
	public static long pageTimeOut;
	public static String url;
	public static String executeOnvalue;
	public static String device;
	public static String bsFile;
	String a;
 

	private static final Logger logger = Logger.getLogger(TestBase.class);
	public static String excelpath=System.getProperty("user.dir")+"\\src\\test\\resources\\testDataAndInitialSetup\\CBTestData_InitialSetup.xlsx";
	public static String sheetname=null;
	public static String testCasename=null;
	public static ExtentHtmlReporter htmlReporter;
	protected static ExtentReports extent;
	protected static ExtentTest reporterTest;
	 static DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
	 static Date date = new Date();
	 public static String sReportFolderpath="Execution_"+dateFormat.format(date);
	 public static String path=System.getProperty("user.dir")+"//Reports//"+TestBase.sReportFolderpath;
	public static WebDriver getDriver()  {
		 
		 
		return driver;
	}

	@BeforeSuite
	public   void setUpDriver() throws Exception  {
		File file= new File(path);
		file.mkdir();
		 String filepath=System.getProperty("user.dir")+"//Reports//"+TestBase.sReportFolderpath+"//ExecutionReport.html";
			File file1= new File(filepath);
			file1.createNewFile();
		htmlReporter = new ExtentHtmlReporter(filepath);
		 extent = new ExtentReports ();		
		 htmlReporter.loadXMLConfig(new File(System.getProperty("user.dir")+"//extent-config.xml"));
		 extent.attachReporter(htmlReporter);
		 extent.setSystemInfo("user", System.getProperty("user.name"));
		 extent.setSystemInfo("os", "Windows 10");
		 extent.setSystemInfo("browser", System.getProperty("browser"));
		
	       
	 
	 
			executionType=System.getProperty("executeOn");
			browserValue=System.getProperty("browser");
			isGrid=System.getProperty("remoteExecution");			 
			pageTimeOut=Long.parseLong(System.getProperty("pageWaitAndWaitTimeOut"));
		 
			browserValue=browserValue.toLowerCase();	
			String browser = null;
			if(browserValue.contains("chrome")) {
				browser="chrome";
			}
			if(browserValue.contains("firefox")) {
				browser="firefox";
			}
			if(browserValue.contains("ie")) {
				browser="ie";
			}
			
			if(executionType.equalsIgnoreCase("local")) {
			switch (browser) {
			case "chrome":
			 
				System.out.println("chromedriver location "+System.getProperty("user.dir")+"\\drivers\\chromedriver.exe");
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\drivers\\chromedriver.exe");
				 
				ChromeOptions options = new ChromeOptions();
				options.addArguments("chrome.switches", "--disable-extensions");
				DesiredCapabilities capabilitiesChrome = DesiredCapabilities.chrome();
	 				HashMap<String, Object> chromePrefs = new HashMap<String, Object>(); 
				 
			 if (isGrid.equalsIgnoreCase("true")) {
					capabilitiesChrome.setBrowserName("chrome");
					try {
						driver = new RemoteWebDriver(new URL(System.getProperty("gridhub")), capabilitiesChrome);
					} catch (MalformedURLException e) {
						
						e.printStackTrace();
					}
					System.out.println("Launching grid for Chrome browser.");
				} else {
					 
					chromePrefs.put("credentials_enable_service", false);
					chromePrefs.put("profile.password_manager_enabled", false);
					options.setExperimentalOption("prefs", chromePrefs);
					capabilitiesChrome.setCapability(ChromeOptions.CAPABILITY, options); 
					driver = new ChromeDriver();				 
					driver.manage().window().maximize();
					driver.manage().timeouts().implicitlyWait(pageTimeOut, TimeUnit.SECONDS);
					System.out.println("Going to launch Chrome driver!");

				}
				 
				break;

			case "firefox":
				System.out.println("  Firefox driver!");
				System.setProperty("webdriver.gecko.driver", ".\\drivers\\geckodriver.exe");
						
			 
				if (isGrid.equalsIgnoreCase("true")) {
					 DesiredCapabilities capabilities = DesiredCapabilities.firefox();	
					capabilities.setBrowserName("firefox");
					try {
						driver = new RemoteWebDriver(new URL(System.getProperty("gridhub")), capabilities);
					} catch (MalformedURLException e) {
						
						e.printStackTrace();
					}
				}
				driver = new FirefoxDriver();
				 
				System.out.println("Going to launch Firefox driver!");
				
				 
				
				break;

			case "ie":
				System.out.println("internet explorer");
				System.setProperty("webdriver.ie.driver", ".\\drivers\\IEDriverServer.exe");
				 
				DesiredCapabilities capabilitiesIE = DesiredCapabilities.internetExplorer();
				capabilitiesIE.setPlatform(org.openqa.selenium.Platform.WINDOWS);
				if (isGrid.equalsIgnoreCase("true")) {
					System.out.println("Inside remote::" + System.getProperty("gridhub"));
					capabilitiesIE.setPlatform(Platform.WINDOWS);
					capabilitiesIE.setCapability(CapabilityType.BROWSER_NAME, "internet explorer");
					capabilitiesIE.setCapability(CapabilityType.VERSION, "11");
					capabilitiesIE.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
					capabilitiesIE.setJavascriptEnabled(true);
					capabilitiesIE.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
					try {
						driver = new RemoteWebDriver(new URL(System.getProperty("gridhub")), capabilitiesIE);
					} catch (MalformedURLException e) {
						
						e.printStackTrace();
					}
					System.out.println("Launching grid for IE browser.");
				} else {
					capabilitiesIE.setCapability(
							InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
					driver = new InternetExplorerDriver();
					id= Thread.currentThread().getId();
					driver.manage().window().maximize();
					driver.manage().timeouts().implicitlyWait(pageTimeOut, TimeUnit.SECONDS);
					System.out.println("Going to launch IE driver!");
					 
					
				}
			 
				break;

			default:
				new RuntimeException("Unsupported browser type");
			}
	 
			
		} 
			
			logger.info("***********************************************************************************");
			logger.info("*** Start of Suite Execution: ");
			logger.info("***********************************************************************************");
			driver.manage().deleteAllCookies();
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			naviagateToChatBot();
 		 
	 }
	
	 
	
	public void getUrl(String url) {
		logger.info("Navigating to "+url);
		driver.get(url);
	}
	
	
	public void naviagateToChatBot() {
		try {
			url= new ExcelReader().getBaseURL(excelpath, "InitialNavigation");
			System.out.println("url "+url);
			 extent.setSystemInfo("Application URL",url);
		} catch (IOException e) {		 
			e.printStackTrace();
		}
		driver.get(url);
		try {
			new ExcelReader().navigateToChatBotFromExcel(excelpath, "InitialNavigation");
		} catch (IOException e) {
			 
			e.printStackTrace();
		}
 
		
	}
	
	 
	
	public TestBase assertTrue(boolean value,String message) {
		Assert.assertTrue(value,message);
		return this;
	}
	
	public TestBase assertEquals(String actual,String expected,String message) {
		Assert.assertEquals(actual, expected, message);
		return this;
	}
	
	public TestBase assertEquals(String actual,String expected) {
		Assert.assertEquals(actual, expected);
		return this;
	}
	
	public TestBase assertFalse(boolean value,String message) {
		Assert.assertFalse(value,message);
		return this;
	}
	
	public TestBase waitForVisible(WebElement element){
		
		WebDriverWait wait= new WebDriverWait(driver,new Long("120"));
		wait.until(ExpectedConditions.visibilityOf(element));
		
		
		new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOf(element));
		return this;
	}
	
	
	public TestBase waitForNotVisible(WebElement element) throws Exception{
		new WebDriverWait(driver, 30).until(ExpectedConditions.not(ExpectedConditions.visibilityOf(element)));
		return this;
	}
	

	public TestBase waitForVisible(WebElement element,long timeOutInSeconds){
		new WebDriverWait(driver, timeOutInSeconds).until(ExpectedConditions.visibilityOf(element));
		return this;
	}
	public TestBase Wait(int timeInSec) throws Exception {
		Thread.sleep(timeInSec*1000);
		return this;
	}
	
	public String getTitle(){

		return driver.getTitle();
	}
	
	public TestBase mouseOver(WebElement element) throws Exception{
		new Actions(driver).moveToElement(element).build().perform();
		return this;
	}
	public TestBase clearAndSetText(WebElement element,String value){
		 
		 
			waitForVisible(element);
			element.clear();
			element.sendKeys(value);
 
		return this;
	}
	
	public TestBase clearAndSetText(WebElement element,String value,String message){
		logger.info("Looking for the webelement "+message);
		 
			waitForVisible(element);
			element.clear();
			element.sendKeys(value);
	 
		return this;
	}
	 

	public boolean isElementPresent(WebElement element){
		try {
			element.getTagName();
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
	}
	
	public boolean isVisible(WebElement element){
		return element.isDisplayed();
	}
 
		public void clickElement(WebElement w, String s){
			logger.info("Looking for webElement: "+s);			
			w.click();	
		}

		public void setDropdownValue(WebElement w, String s, String wname)
		{
			logger.info("Looking for the webelement "+wname);
			
			Select oSelect=new Select(w);
			oSelect.selectByVisibleText(s); 
			 
		}
		
		public String getValue(WebElement w, String wname)
		{
			logger.info("Looking for the webelement "+wname);
			verifyVisibility(w,wname);
			logger.info("The value of the "+wname+" is: "+w.getAttribute("value"));
			return w.getAttribute("value");
		}
		
		public String getText(WebElement w, String wname)
		{
			logger.info("Looking for the webelement "+wname);
			verifyVisibility(w,wname);
			logger.info("The value of the "+wname+" is: "+w.getText());
			return w.getText();
		}
		
	 
		public void verifyVisibility(WebElement w, String s){
		 
			 	
				
				  w.isDisplayed();
				 Assert.assertTrue(true);
			 
			 
		}
		
		 
				public void verifyNotVisibility(WebElement w, String s){
					 
					 
						 assertFalse(w.isDisplayed(),"The "+s+" is  displayed");
					 
					
				}
		public String getDropdownValue(WebElement w, String wname)
		{
			logger.info("Looking for the webelement "+wname);
			Select oSelect=new Select(w);
			this.a=oSelect.getFirstSelectedOption().getAttribute("value");
			return this.a;
		}
		
		public String getDropdownText(WebElement w, String s){
			logger.info("Looking for the webelement "+s);
			
			Select oSelect=new Select(w);
			return oSelect.getFirstSelectedOption().getText();
		}
		
		
		//Mouse Hover Element
		public void mouseHover(WebElement w,String wname)
		{
			try{
				waitForVisible(w);
				mouseOver(w);
				assertTrue(w.isDisplayed(),"The "+wname+" is not displayed");
 
			}catch(Exception e) {
				logger.info(e.toString());
			} 
			
	 
		}
		
		
 
		
		
	 
		
		public void clickEnter(){
			logger.info("Pressing Enter Key");
			Robot r=null;
			try {
				 r=new Robot();
			} catch (AWTException e) {
				logger.info(e);
			}
			r.keyPress(KeyEvent.VK_ENTER);
			r.keyRelease(KeyEvent.VK_ENTER);
		}
		
}
