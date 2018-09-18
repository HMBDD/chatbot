package com.chatbot.initiators;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.chatbot.base.TestBase;

public class ChatbotInitiator extends TestBase {
	public static String pagevalue;
	public static HashMap<String, String> taxData = null;
	public static HashMap<String, String> nsVerificationData = null;

	public static String screenshotFolderpath = System.getProperty("user.dir") + "\\Screenshots\\";
	public static String screenshotFoldername = "Execution_"
			+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	private static final Logger LOGGER = Logger.getLogger(ChatbotInitiator.class);

	@AfterMethod(alwaysRun = true)
	public void afterTestMethod(ITestResult result) throws IOException {
		captureScreenShotOnTestFailure(result);
		LOGGER.info("***********************************************************");
		LOGGER.info(" End of Execution ");
		LOGGER.info("***********************************************************");
		 reporterTest.log(Status.INFO, "Test Case execution is completed");
	}

	@AfterSuite
	public void afterTestSuite() {
		 extent.flush();
		closeDriver();
		LOGGER.info("***********************************************************************************");
		LOGGER.info("*** End of Suite Execution: ");
		LOGGER.info("***********************************************************************************");
		
		
		File srcfile = new File(TestBase.targetReportPath);
		File[] listOfFiles = srcfile.listFiles();
		//getting   file names from   folder
	/*	 for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		    	  
		        System.out.println("File " + listOfFiles[i].getName());
		      } else if (listOfFiles[i].isDirectory()) {
		        System.out.println("Directory " + listOfFiles[i].getName());
		        
		      }*/
		      
		      File dstfile = new File(TestBase.path);
				try {
					FileUtils.copyDirectory(srcfile, dstfile);
				} catch (IOException e) {
					 
					e.printStackTrace();
				}
				 
				System.out.println("Report movement is completed. Path is: "+TestBase.path);
		
	 
	}

	public void captureScreenShotOnTestFailure(ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.FAILURE)
        {
			File path = new File(TestBase.targetReportPath + "//Screenshots//");
			if (!path.exists()) {
				try {
					path.mkdir();
				} catch (Exception e) {
					e.printStackTrace();
				}

				LOGGER.info("Path is " + path);
			}
			File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			String screenShotFileName = result.getMethod().getMethodName() + "_" + System.currentTimeMillis() + ".jpg";
			
			String screenShotPath = TestBase.targetReportPath + "//Screenshots//"+screenShotFileName;
			LOGGER.info("Screenshot path " + screenShotPath);
			try {
				FileUtils.copyFile(screenshotFile, new File(screenShotPath));
			} catch (IOException e) {

				e.printStackTrace();
			}
		
			reporterTest.log(Status.FAIL, MarkupHelper.createLabel(result.getName()+" Test case FAILED due to below issues:", ExtentColor.RED));
			reporterTest.fail(result.getThrowable());
			reporterTest.fail("Snapshot below: " + reporterTest.addScreenCaptureFromPath("."+"\\Screenshots\\"+screenShotFileName));
        }
        else if(result.getStatus() == ITestResult.SUCCESS)
        {
        	reporterTest.log(Status.PASS, MarkupHelper.createLabel(result.getName()+" Test Case PASSED", ExtentColor.GREEN));
        }
        else
        {
        	reporterTest.log(Status.SKIP, MarkupHelper.createLabel(result.getName()+" Test Case SKIPPED", ExtentColor.ORANGE));
        	reporterTest.skip(result.getThrowable());
        }
	 
		FileInputStream inputStream = new FileInputStream(new File(excelpath));		 
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		XSSFSheet firstSheet = workbook.getSheet(TestBase.sheetname);
		System.out.println("Sheet name selected is  "+TestBase.sheetname);
		int totalRowCount = getTotalRowCount(firstSheet);
		System.out.println("Total row count "+totalRowCount);
		switch (result.getStatus()) {
		case ITestResult.FAILURE:	
			for(int i=1;i<=totalRowCount;i++) {
				String tcname= firstSheet.getRow(i).getCell(2).getStringCellValue();
				 System.out.println("tcname in excel is "+tcname);
				 if(tcname.equalsIgnoreCase(TestBase.testCasename)) {
					 String dateValue=new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
					 firstSheet.getRow(i).getCell(1).setCellValue("FAILED_"+dateValue);
						break;
					}
			}
			break;
		case ITestResult.SUCCESS:
			for(int i=1;i<=totalRowCount;i++) {
				String tcname= firstSheet.getRow(i).getCell(2).getStringCellValue();
				 System.out.println("tcname in excel is "+tcname);
				 if(tcname.equalsIgnoreCase(TestBase.testCasename)) {
					 String dateValue=new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
					 firstSheet.getRow(i).getCell(1).setCellValue("PASSED_"+dateValue);
						break;
					}
			}
			break;
			case ITestResult.SKIP:
				for(int i=1;i<=totalRowCount;i++) {
					String tcname= firstSheet.getRow(i).getCell(2).getStringCellValue();
					 System.out.println("tcname in excel is "+tcname);
					 if(tcname.equalsIgnoreCase(TestBase.testCasename)) {
						 String dateValue=new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
						 firstSheet.getRow(i).getCell(1).setCellValue("SKIPPED_"+dateValue);
							break;
						}
				}
			break;

		default:
			break;
		}
		 FileOutputStream outputStream = new FileOutputStream(excelpath);
		 inputStream.close();
		 workbook.write(outputStream);
         workbook.close();
         outputStream.close();

	}

	public void closeDriver() {
		if (getDriver() != null) {
			try {
				LOGGER.info("Trying to Stop WebDriver");
				getDriver().quit();
				LOGGER.info("WebDriver Stopped");
			} catch (Exception e) {
				LOGGER.error("Error in stopping WebDriver " + e.getMessage());
			}
		}
	}
	
	/**
	 * to get total row count which contains data
	 * 
	 * @param sheetTD
	 * @return
	 */
	private static int getTotalRowCount(XSSFSheet sheetTD) {
		int totalRowCount = sheetTD.getLastRowNum();
 
		boolean isRowEmpty = false;
	
		int r = totalRowCount;
		int totalloopCountRow = sheetTD.getRow(totalRowCount).getPhysicalNumberOfCells();
		do {
 
			for (int d = 1; d <= totalloopCountRow; d++) {
				try {
					 
					if (sheetTD.getRow(r).getCell(d) == null || sheetTD.getRow(r).getCell(d).toString().equals("")) {

						isRowEmpty = true;

					} else {
						isRowEmpty = false;
					}

					if (d != 0 && isRowEmpty == false) {
						 
						break;
					}

				} catch (NullPointerException e) {

				}

			}
			 
			if (isRowEmpty == true) {
				r--;
			}
		} while (isRowEmpty == true);

		return r;
	}

}
