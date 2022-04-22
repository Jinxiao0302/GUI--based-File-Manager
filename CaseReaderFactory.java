/*
 * Name: Jinxiao Li
 * Andrew ID: jinxiaol
 */

package hw3;

public class CaseReaderFactory {
	public CaseReader createReader(String filename) {
		
		CaseReader casereader;
		
		/* Get the extension from the filename
		 * If the extension is csv, set caseReader to CSVCaseReader
		 */
		 String[] sp = filename.split("\\.");
		if(sp[sp.length-1].equalsIgnoreCase("csv")) {
			casereader =  new CSVCaseReader(filename);
			return casereader;
		}
		
		/* 
		 * If the extension is tsv, set caseReader to TSVCaseReader
		 */
		else if(sp[sp.length-1].equalsIgnoreCase("tsv")) {
			casereader =  new TSVCaseReader(filename);
			return casereader;
		}	
		return null;
	}
}
