package com.chatbot.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ChatBotExcelOperation {

	public static String excelpathValue;
	public static FileInputStream inputStream;
	public static XSSFWorkbook workbook;
	public static XSSFSheet firstSheet;
	public static int totalRowCount;
	public static int  totalColCount;
	public static ArrayList<String> isExecute = new ArrayList<String>();
	public static ArrayList<String> executionStatus = new ArrayList<String>();
	public static ArrayList<String> tcNum = new ArrayList<String>();
	public static ArrayList<String> questions = new ArrayList<String>();
	public static HashMap<String,ArrayList<String>> mapAnswers = new HashMap<String,ArrayList<String>>();

	/**
	 * Constructor to get excel path
	 * @param excelpath
	 * @throws Exception 
	 */
	public ChatBotExcelOperation(String excelpath, String sheetName) throws Exception {
		this.excelpathValue=excelpath;
		try {
			System.out.println("Excel sheet path is "+excelpath);
			  inputStream = new FileInputStream(new File(excelpathValue));
			    workbook = new XSSFWorkbook(inputStream);
			      firstSheet = workbook.getSheet(sheetName);
			      System.out.println("Sheet name selected is  "+sheetName);
		} catch (FileNotFoundException e) {
			System.out.println("Excel path "+excelpath+"is not found");
		}
	}
	/**
	 * Method to get Question
	 * @param testcaseNumber
	 * @return
	 */
	public String getQuestion(String testcaseNumber) {
		String question = null;
		System.out.println("Checking for Required question");
		 for(int l=0;l<tcNum.size();l++) {	
			  
			 if(tcNum.get(l).equals(testcaseNumber)) {
				 System.out.println("Question for the selected test case "+testcaseNumber+" is "+questions.get(l).trim());
				 question= questions.get(l).trim();
				 break;
			 }
			  
			   
		 }
		 return question;
		
	}
	/**
	 * Method to get particular answer for the question
	 * @param testcaseNumber
	 * @param answerCol
	 * @return
	 */
	
	public String getAnswer(String testcaseNumber,String answerCol) {
		String answer = null;
		System.out.println("Checking for Required "+answerCol+"for test case "+testcaseNumber);
	 
 
			Iterator it = mapAnswers.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				
				if (pair.getKey().equals(testcaseNumber)) {
					System.out.println("Matched test case is "+pair.getKey());
					ArrayList<String> value = (ArrayList<String>) pair.getValue();
					System.out.println("Total Answer(s) is(are) " + value.size());
					String AnsCol = answerCol.toLowerCase();
					AnsCol = AnsCol.replace("answer", "").trim();
					int ansColNum = Integer.valueOf(AnsCol);
					System.out.println("ansColNum "+ansColNum);
					answer=value.get(ansColNum-1);
					
					 System.out.println("Answer for the selected test case "+testcaseNumber+" is "+answer);
					 break;
			        }
			    }
		 
		 return answer;
	}
	
	/**
	 * Method to get particular answer for the question
	 * @param testcaseNumber
	 * @param answerCol
	 * @return
	 */
	
	public ArrayList<String> getAllAnswers(String testcaseNumber) {
		ArrayList<String> answer = null;
		System.out.println("Checking Answers for test case "+testcaseNumber);
	 
 
			Iterator it = mapAnswers.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				
				if (pair.getKey().equals(testcaseNumber)) {
					System.out.println("Matched test case is "+pair.getKey());
					ArrayList<String> value = (ArrayList<String>) pair.getValue();
					System.out.println("Total Answer(s) is(are) " + value.size());
				
					answer=value;
					System.out.println(" Answer(s) is(are) " + answer);
					 break;
			        }
			    }
		 
		 return answer;
	}
	
	/**
	 * to get total row count which contains data
	 * 
	 * @param sheetTD
	 * @return
	 */
	private static int getTotalRowCount(XSSFSheet sheetTD) {
		int totalRowCount = sheetTD.getLastRowNum();
    System.out.println("last row count "+totalRowCount);
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
	

	
	
	//public static void main(String[] args) throws Exception {
		public   void fetchChatBotDetails() throws Exception {
		/*ChatBotExcelOperation cbExcel = null;
		try {
			  cbExcel= new ChatBotExcelOperation("D:\\workspace\\ChatBotExcelReader\\TestData\\chatbotQA.xlsx", "chatbot");
		} catch (Exception e) {			 
			e.printStackTrace();
		}*/
		
		  totalRowCount = getTotalRowCount(firstSheet);
		 System.out.println("Total Row count "+totalRowCount);
		 
		 for (int i = 1; i <= totalRowCount; i++) {
			 ArrayList<String> answers = new ArrayList<String>();
			 isExecute.add(firstSheet.getRow(i).getCell(0).toString().trim());
			 executionStatus.add(firstSheet.getRow(i).getCell(1).toString().trim());
			 tcNum.add(firstSheet.getRow(i).getCell(2).toString().trim());
			 questions.add(firstSheet.getRow(i).getCell(3).toString().trim());
			 totalColCount = totalColumnCount(firstSheet,i);
			 System.out.println("Total Column count of Row  "+i+" is "+totalColCount);
			 for(int k=4;k<totalColCount;k++) {	
				
				 answers.add(firstSheet.getRow(i).getCell(k).toString().trim());				
			 }
			 mapAnswers.put(firstSheet.getRow(i).getCell(2).toString().trim(), answers);
		 
			}
		  workbook.close();
		  inputStream.close();
		  System.out.println("Chatbot excel is read  successfully!!!");
		  
	/*	  for(int l=0;l<isExecute.size();l++) {	
			  
			  System.out.println("isExecute "+l+" "+isExecute.get(l));
			  System.out.println("executionStatus "+l+" "+executionStatus.get(l));
			  System.out.println("tcNum "+l+" "+tcNum.get(l));
			  System.out.println("questions "+questions.get(l));
				  			
			
		  Iterator it = mapAnswers.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        System.out.println(pair.getKey());
		        if(pair.getKey().equals(tcNum.get(l))) {
		        ArrayList<String> value=   (ArrayList<String>) pair.getValue();
		        System.out.println("value size is "+value.size());
		        for(int m=0;m<value.size();m++) {	
					  
					  System.out.println("Answers   "+m+" "+value.get(m));
					  
						  			
					 }
		        }
		    }
		  }*/
		  
		/*  cbExcel.getQuestion("CB_01");
		  cbExcel.getQuestion("CB_02");
		  cbExcel.getQuestion("CB_03");
		  cbExcel.getAnswer("CB_01","Answer1");
		  cbExcel.getAnswer("CB_03","Answer2");
		  cbExcel.getAnswers("CB_03");*/
		 
		  
	}
	

}
