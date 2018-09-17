package com.chatbot.testcases;
 
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.chatbot.base.TestBase;
import com.chatbot.initiators.ChatbotInitiator;
import com.chatbot.util.ExcelReader;

 
/**
 * This class contains billing page test cases
 * 
 * @author ganapati.bhat
 *
 */
public class ChatbotVerificationDataProvider extends ChatbotInitiator{
	private static final Logger LOGGER = Logger.getLogger(ChatbotVerificationDataProvider.class);
	 
 static String executionstatus;
 
   String tcName;
 
	/**
	 * Data provider 
	 * @return
	 * @throws Exception
	 */
	@DataProvider(name="chatbotData")
	public Object[][] chatbotTestData() throws Exception{
		  TestBase.sheetname="chatbot";
		Object[][] result = new ExcelReader().getDataProviderData(excelpath, TestBase.sheetname);		 
		return result;
	}
	
	/**
	 * Verify chatbot verification
	 */
	@SuppressWarnings({ "resource", "static-access" })
	@Test(dataProvider="chatbotData")
	public void chatbotVerification(String... cbData) throws Exception{
		int testDataSize=cbData.length;
		ArrayList<String> answers = new ArrayList<String>();
 
		
		if(cbData[0].equalsIgnoreCase("y")) {			
			TestBase.testCasename= cbData[2];
			reporterTest=extent.createTest(testCasename);
			reporterTest.log(Status.INFO, "Test Case execution is Started: "+testCasename);
			LOGGER.info("***********************************************************");
			LOGGER.info( "Test Case execution is Started: "+testCasename);
			LOGGER.info("***********************************************************");
			
			String question = cbData[3];
			for(int i=4;i<testDataSize;i++) {
				 
				if(!cbData[i].equals("EMPTY")) {
					System.out.println("cbData[i]"+i+cbData[i]);
					answers.add(cbData[i]);
				}else {
					 
					break;
				}
			}
			
			int wrapperSize= getDriver().findElements(By.xpath("//div[@class='wc-message-wrapper list']")).size();
			System.out.println("wrapperSize "+wrapperSize);
			 reporterTest.log(Status.INFO, "Question is: "+question);
			 LOGGER.info(  "Question is: "+question);
			 new ExcelReader().enterQuestions(question);
  
			 int wrapperSizeAfter= getDriver().findElements(By.xpath("//div[@class='wc-message-wrapper list']")).size();
			System.out.println("wrapperSizeAfter "+wrapperSizeAfter);
			int actualResponse=wrapperSizeAfter-wrapperSize;
		 for(int k=actualResponse;k>0;k--) {
			String xpath= "//div[@class='wc-message-wrapper list'][last()-"+(k-1)+"]//p";
			try {
			  getDriver().findElement(By.xpath(xpath)).isDisplayed();
			  System.out.println("Bot Response is: \n"+getDriver().findElement(By.xpath(xpath)).getText());
			  reporterTest.log(Status.INFO, "Bot Response is: \n"+getDriver().findElement(By.xpath(xpath)).getText());
			  LOGGER.info("Bot Response is: \n"+getDriver().findElement(By.xpath(xpath)).getText());
			}catch(NoSuchElementException e) {
				 
			}
		 }
			
		
			 
		}
	
		
	}
	 
}
