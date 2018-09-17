package com.chatbot.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.chatbot.base.TestBase;

public class ExcelReader {

	public static String excelFilePath = null;
	public static String sheetName = null;
	static int totalColCount = 0;
	static int totalNCount = 0;

	public String[][] getDataProviderData(String excelpath, String sheetName1) throws IOException {
		excelFilePath = excelpath;
		sheetName = sheetName1;

		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		XSSFSheet firstSheet = workbook.getSheet(sheetName);
		System.out.println("Sheet name selected is  " + sheetName1);
		int totalRowCount = getTotalRowCount(firstSheet);
		System.out.println("Total row count " + totalRowCount);

		String data[][] = null;

		for (int x = 1; x <= totalRowCount; x++) {

			totalColCount = totalColumnCount(firstSheet, x);
		}
		// System.out.println(totalColCount);
		for (int n = 1; n <= totalRowCount; n++) {
			if (firstSheet.getRow(n).getCell(0).getStringCellValue().equalsIgnoreCase("n")) {
				totalNCount = totalNCount + 1;
			}

		}
		totalRowCount = totalRowCount - totalNCount;

		data = new String[totalRowCount][totalColCount];

		for (int x = 1; x <= totalRowCount; x++) {

			Row row = firstSheet.getRow(x);

			for (int m = 0; m < totalColCount; m++) {
				Cell cell = row.getCell(m);
				if (cell.getStringCellValue().equalsIgnoreCase("n")) {
					System.out.println("Row number " + x + " is set to N for Execution");
					break;
				} else {
					try {
						System.out.println("Row number " + x + " is set to Y for Execution");
						CellType cellType = cell.getCellTypeEnum();

						switch (cellType) {
						case STRING:
							data[x - 1][m] = cell.getStringCellValue();
							System.out.println(
									"Data String value at " + (x - 1) + " " + m + " " + cell.getStringCellValue());
							break;

						case FORMULA:
							data[x - 1][m] = cell.getStringCellValue();
							System.out.println("Data Formula String value at " + (x - 1) + " " + m + " "
									+ cell.getStringCellValue());
							break;
						default:
							data[x - 1][m] = "EMPTY";
							break;

						}
					} catch (Exception e) {
						System.out.println("can not read cell data");
					}
				}
			}

		}

		System.out.println("data size is " + data.length);

		for (int a = 0; a < totalRowCount; a++) {

			for (int b = 0; b < totalColCount; b++) {
				System.out.println("Column Value at: " + data[a][b]);
			}

		}

		workbook.close();
		return data;

	}

	/**
	 * to get total row count which contains data
	 * 
	 * @param sheetTD
	 * @return
	 */
	public static int getTotalRowCount(XSSFSheet sheetTD) {
		int totalRowCount = sheetTD.getLastRowNum();
		// System.out.println("last row count "+totalRowCount);
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

	/**
	 * To get actual column count which contains data
	 * 
	 * @param rowNum
	 * @param sheetTD
	 */

	private static int totalColumnCount(XSSFSheet sheetTD, int tcRowNum) {

		boolean isColEmpty = false;
		int col = 0;
		do {

			try {

				if (sheetTD.getRow(tcRowNum).getCell(col) == null
						|| sheetTD.getRow(tcRowNum).getCell(col).toString().equals("")) {

					isColEmpty = true;

				} else {
					isColEmpty = false;
				}

			} catch (NullPointerException e) {

			}

			if (isColEmpty == false) {
				col++;
			}

			if (isColEmpty == true) {

				break;
			}

		} while (isColEmpty == false);
		return col;
	}

	public HashMap<String, String> getExcelFirstSetOfData(String excelpath, String sheetName1) throws IOException {
		HashMap<String, String> hmap = new HashMap<String, String>();
		excelFilePath = excelpath;
		sheetName = sheetName1;

		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		XSSFSheet firstSheet = workbook.getSheet(sheetName);
		System.out.println("Sheet name selected is  " + sheetName1);

		int totalColCount = totalColumnCount(firstSheet, 0);

		Row row = firstSheet.getRow(0);
		Row dataRow = firstSheet.getRow(1);

		for (int m = 0; m < totalColCount; m++) {
			Cell cell = row.getCell(m);
			Cell datacell = dataRow.getCell(m);
			try {

				CellType cellType = datacell.getCellTypeEnum();

				switch (cellType) {
				case STRING:
					hmap.put(cell.getStringCellValue(), datacell.getStringCellValue());

					break;

				case FORMULA:
					hmap.put(cell.getStringCellValue(), datacell.getStringCellValue());
					break;
				default:
					break;

				}
			} catch (Exception e) {
				System.out.println("can not read cell data");
			}
		}

		workbook.close();
		return hmap;

	}

	public HashMap<String, String> getExcelData(String excelpath, String sheetname) throws IOException {

		HashMap<String, String> hmap = new HashMap<String, String>();
		excelFilePath = excelpath;
		sheetName = sheetname;

		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		XSSFSheet firstSheet = workbook.getSheet(sheetName);
		System.out.println("Sheet name selected is  " + sheetName);

		int totalRowCount = getTotalRowCount(firstSheet);

		for (int k = 1; k <= totalRowCount; k++) {

			hmap.put(firstSheet.getRow(k).getCell(0).getStringCellValue(),
					firstSheet.getRow(k).getCell(1).getStringCellValue());

		}
		workbook.close();
		for (Map.Entry<String, String> entry : hmap.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		return hmap;

	}

	public void setCellValue(int totalColCount, XSSFSheet firstSheet, String value, int rownumber, String mapValue) {
		for (int i = 2; i <= totalColCount; i++) {
			String hdrname = firstSheet.getRow(0).getCell(i).getStringCellValue();

			if (hdrname.equalsIgnoreCase(value)) {
				firstSheet.getRow(rownumber).getCell(i).setCellValue(mapValue);

				break;
			}
		}
	}

	public String getBaseURL(String excelpath, String sheetName1) throws IOException {

		excelFilePath = excelpath;
		sheetName = sheetName1;
		String baseurlValue = null;
		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		XSSFSheet firstSheet = workbook.getSheet(sheetName);
		System.out.println("Sheet name selected is  " + sheetName1);

		Row row = firstSheet.getRow(0);
		Cell cell = row.getCell(1);
		baseurlValue = cell.getStringCellValue();

		workbook.close();
		return baseurlValue;

	}

	public void navigateToChatBotFromExcel(String excelpath, String sheetname) throws IOException {

		FileInputStream inputStream = new FileInputStream(new File(excelpath));

		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		XSSFSheet firstSheet = workbook.getSheet(sheetname);
		System.out.println("Sheet name selected is  " + sheetname);

		int totalRowCount = getTotalRowCount(firstSheet);
		System.out.println("Total Row Count  " + totalRowCount);

		for (int k = 2; k <= totalRowCount; k++) {
			String commandType = firstSheet.getRow(k).getCell(0).getStringCellValue();
			commandType = commandType.toLowerCase();
			String locatorType;
			String locatorTypeValue;
			String testDataValue;
			WebElement element;
			switch (commandType) {
			case "click":
				locatorType = firstSheet.getRow(k).getCell(1).getStringCellValue();
				locatorTypeValue = firstSheet.getRow(k).getCell(2).getStringCellValue();
				element = getWebElement(locatorType, locatorTypeValue);
				element.click();
				break;
			case "wait":
				String waitValue = firstSheet.getRow(k).getCell(3).getStringCellValue();
				try {
					Thread.sleep(Integer.valueOf(waitValue));
				} catch (NumberFormatException e) {

					e.printStackTrace();
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				break;
			case "type":
				locatorType = firstSheet.getRow(k).getCell(1).getStringCellValue();
				locatorTypeValue = firstSheet.getRow(k).getCell(2).getStringCellValue();
				testDataValue = firstSheet.getRow(k).getCell(3).getStringCellValue();
				if (!testDataValue.equalsIgnoreCase("question")) {
					element = getWebElement(locatorType, locatorTypeValue);
					element.sendKeys(testDataValue);
				}
				break;
			case "switchtoframe":
				locatorType = firstSheet.getRow(k).getCell(2).getStringCellValue();
				TestBase.getDriver().switchTo().frame(locatorType);
				break;
			default:
				System.out.println("Action " + commandType + " is related to chatbot popup");
				break;
			}

		}
		workbook.close();

	}

	public WebElement getWebElement(String locatorType, String locatorTypeValue) {
		WebElement element = null;
		locatorType = locatorType.toLowerCase();
		switch (locatorType) {
		case "xpath":
			element = TestBase.getDriver().findElement(By.xpath(locatorTypeValue));
			break;
		case "id":
			element = TestBase.getDriver().findElement(By.id(locatorTypeValue));
			break;
		case "name":
			element = TestBase.getDriver().findElement(By.name(locatorTypeValue));
			break;
		case "cssselector":
			element = TestBase.getDriver().findElement(By.cssSelector(locatorTypeValue));
			break;
		case "classname":
			element = TestBase.getDriver().findElement(By.className(locatorTypeValue));
			break;
		default:
			break;
		}
		return element;
	}

	public void enterQuestions(String question) throws Exception {
		FileInputStream inputStream = new FileInputStream(new File(TestBase.excelpath));

		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		XSSFSheet firstSheet = workbook.getSheet("InitialNavigation");
		 
		int totalRowCount = getTotalRowCount(firstSheet);
		System.out.println("Total Row Count  " + totalRowCount);
	 
		for (int k = 2; k <= totalRowCount; k++) {
			String commandType = firstSheet.getRow(k).getCell(0).getStringCellValue();
			commandType = commandType.toLowerCase();
			String locatorType;
			String locatorTypeValue;
			 
			WebElement element;
			if(commandType.contains("chatbot")) {
			switch (commandType) {
			case "chatbot_click":
				locatorType = firstSheet.getRow(k).getCell(1).getStringCellValue();
				locatorTypeValue = firstSheet.getRow(k).getCell(2).getStringCellValue();
				element = getWebElement(locatorType, locatorTypeValue);
				element.click();
				break;
			case "chatbot_wait":
				String waitValue = firstSheet.getRow(k).getCell(3).getStringCellValue();
				try {
					Thread.sleep(Integer.valueOf(waitValue));
				} catch (NumberFormatException e) {

					e.printStackTrace();
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				break;
			case "chatbot_type":
				locatorType = firstSheet.getRow(k).getCell(1).getStringCellValue();
				locatorTypeValue = firstSheet.getRow(k).getCell(2).getStringCellValue();		 
					element = getWebElement(locatorType, locatorTypeValue);
					element.sendKeys(question); 
				break;
			case "chatbot_switchtoframe":
				locatorType = firstSheet.getRow(k).getCell(2).getStringCellValue();
				TestBase.getDriver().switchTo().frame(locatorType);
				break;
			default:
			 
				break;
			}

		}
		}	
		workbook.close();
	}		 
}