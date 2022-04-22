/*
 * Name: Jinxiao Li
 * Andrew ID: jinxiaol
 */

package hw3;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class CCModel {
	
	ObservableList<Case> caseList = FXCollections.observableArrayList(); 			//a list of case objects
	ObservableMap<String, Case> caseMap = FXCollections.observableHashMap();		//map with caseNumber as key and Case as value
	ObservableMap<String, List<Case>> yearMap = FXCollections.observableHashMap();	//map with each year as a key and a list of all cases dated in that year as value. 
	ObservableList<String> yearList = FXCollections.observableArrayList();			//list of years to populate the yearComboBox in ccView

	/** readCases() performs the following functions:
	 * It creates an instance of CaseReaderFactory, 
	 * invokes its createReader() method by passing the filename to it, 
	 * and invokes the caseReader's readCases() method. 
	 * The caseList returned by readCases() is sorted 
	 * in the order of caseDate for initial display in caseTableView. 
	 * Finally, it loads caseMap with cases in caseList. 
	 * This caseMap will be used to make sure that no duplicate cases are added to data
	 * @param filename
	 */
	void readCases(String filename) {
		CaseReaderFactory crf = new CaseReaderFactory();
		 List<Case> caselist = (crf.createReader(filename)).readCases();
		 
		 // sort the cases in natural order defined as Date
		 Collections.sort(caselist);
		 
		 // Put the Cases in caselist into the CaseMap and caseList
		 for(Case c:caselist) {
			 caseList.add(c);
			 caseMap.put(c.getCaseNumber(), c);
		 }
	}

	/** buildYearMapAndList() performs the following functions:
	 * 1. It builds yearMap that will be used for analysis purposes in Cyber Cop 3.0
	 * 2. It creates yearList which will be used to populate yearComboBox in ccView
	 * Note that yearList can be created simply by using the keySet of yearMap.
	 */
	void buildYearMapAndList() {
		yearMap.clear();
		for(Case c:caseList) {
			String dat = c.getCaseDate().substring(0, 4);
			List<Case> lis = new ArrayList<Case>();
			
			if(yearMap.get(dat) == null) {
				lis.add(c);
			}else {
				lis = yearMap.get(dat);
				lis.add(c);
			}
			yearMap.put(dat, lis);
		}
		
		for(String year :yearMap.keySet()) {
			yearList.add(year);
		};
	}

	/**searchCases() takes search criteria and 
	 * iterates through the caseList to find the matching cases. 
	 * It returns a list of matching cases.
	 */
	List<Case> searchCases(String title, String caseType, String Year, String caseNumber) {
		List<Case> result = new ArrayList<>();
		
		// Set the value to "" if it is null
		if(title == null)title = "";
		if(caseType == null)caseType = "";
		if(Year == null)Year = "";
		if(caseNumber == null)caseNumber = "";
		
		// Build regular expressions to search the cases
		String patitle = ".*"+title.toLowerCase()+".*";
		String patype = ".*"+caseType.toLowerCase()+".*";
		String patyear = ".*"+Year.toLowerCase()+".*";
		String patnb = ".*"+caseNumber.toLowerCase()+".*";


		for(Case c:caseList) {
			if(c.getCaseTitle().toLowerCase().matches(patitle) &&c.getCaseType().toLowerCase().matches(patype)
					&& c.getCaseDate().toLowerCase().matches(patyear) && c.getCaseNumber().toLowerCase().matches(patnb)) {
				result.add(c);
			}
		}
		return result;
	}
	
	boolean writeCases(String filename){		
		try(
			FileWriter output = new FileWriter(filename);
				){
			for(Case c: caseList) {
				output.write(c.getCaseDate()+"\t");
				output.write(c.getCaseTitle()+"\t");
				output.write(c.getCaseType()+"\t");
				output.write(c.getCaseNumber()+"\t");
				if(c.getCaseLink().isBlank()) {
					output.write("  "+"\t");
				}else output.write(c.getCaseLink()+"\t");
				
				if(c.getCaseCategory().isBlank()) {
					output.write("  "+"\t");
				}else output.write(c.getCaseCategory()+"\t");
				
				if(c.getCaseNotes().isBlank()) {
					output.write("  "+"\t");
				}else output.write(c.getCaseNotes()+"\t");
				
				output.write("\n");
			}
			output.close();
			return true;
		}catch (IOException e) {
			return false;
		}	
	}
}
