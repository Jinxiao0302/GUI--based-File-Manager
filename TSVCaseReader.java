/*
 * Name: Jinxiao Li
 * Andrew ID: jinxiaol
 */

package hw3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Mortgage.InputOutOfRangeException;

public class TSVCaseReader extends CaseReader{
	
	static int invalidCount;
	TSVCaseReader(String filename) {
		super(filename);
	}

	@SuppressWarnings("resource")
	@Override
	List<Case> readCases() {
		List<Case> caseList = new ArrayList<>();
		String[] fileData;
		StringBuilder fileContent = new StringBuilder();
		Scanner fileScanner = null;
		
		try {
			File file = new File(filename); 
			fileScanner = new Scanner(file);
			while (fileScanner.hasNextLine()) { 		
				fileContent.append(fileScanner.nextLine()+"\n");		//append the token with \n; added back again
				}
			
			fileData = fileContent.toString().split("\n");
			invalidCount = 0;
			for(String s:fileData) {
				String[] list = s.split("\t");
				if(list[0].isBlank()|| list[1].isBlank()|| list[2].isBlank()|| list[3].isBlank()) {
					invalidCount +=1;
				}else {
					// construct the Cases with the inputs
					Case c = new Case(list[0], list[1], list[2], list[3], list[4], list[5], list[6]);
					caseList.add(c);
				}
			}
			
			if(invalidCount >0)throw new DataException(invalidCount+"cases rejected\n"
					+ "The file must have cases with \n"
					+ "tab separated date, title, type, and case number!");
			return caseList;

		}catch (DataException e1) {
			return caseList;
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		return caseList;
	}
	
}
