/*
 * Name: Jinxiao Li
 * Andrew ID: jinxiaol
 */


package hw3;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Case implements Comparable<Case>{
	
	// Set up property of Case class
	private StringProperty caseDate = new SimpleStringProperty();
	private StringProperty caseTitle = new SimpleStringProperty();
	private StringProperty caseType = new SimpleStringProperty();
	private StringProperty caseNumber = new SimpleStringProperty();
	private StringProperty caseLink = new SimpleStringProperty();
	private StringProperty caseCategory = new SimpleStringProperty();
	private StringProperty caseNotes = new SimpleStringProperty();
	
	
	// Constructor to set a new Case with variables
	Case(String caseDate, String caseTitle, String caseType,
			String caseNumber, String caseLink, String caseCategory, String caseNotes){
		this.caseDate.set(caseDate);
		this.caseTitle.set(caseTitle);
		this.caseType.set(caseType);
		this.caseNumber.set(caseNumber);
		this.caseLink.set(caseLink);
		this.caseCategory.set(caseCategory);
		this.caseNotes.set(caseNotes);
	}
	
	// All the get methods are here
	public String getCaseDate() { return caseDate.get(); }
	public String getCaseTitle() { return caseTitle.get(); }
	public String getCaseType() { return caseType.get(); }
	public String getCaseNumber() { return caseNumber.get(); }
	public String getCaseLink() { return caseLink.get(); }
	public String getCaseCategory() { return caseCategory.get(); }
	public String getCaseNotes() { return caseNotes.get(); }
	
	// All the set methods are here
	// To set new values for the variables
	public void setCaseDate(String date) { this.caseDate.set(date); }
	public void setCaseTitle(String title) { this.caseTitle.set(title); }
	public void setCaseType(String type) { this.caseType.set(type); }
	public void setCaseNumber(String number) { this.caseNumber.set(number); }
	public void setCaseLink(String link) { this.caseLink.set(link); }
	public void setCaseCategory(String category) { this.caseCategory.set(category); }
	public void setCaseNotes(String notes) { this.caseNotes.set(notes); }

	// All the return property methods are here
	public StringProperty caseDateProperty() { return caseDate; }
	public StringProperty caseTitleProperty() { return caseTitle; }
	public StringProperty caseTypeProperty() { return caseType; }
	public StringProperty caseNumberProperty() { return caseNumber; }
	public StringProperty caseLinkProperty() { return caseLink; }
	public StringProperty caseCategoryProperty() { return caseCategory; }
	public StringProperty caseNotesProperty() { return caseNotes; }

	
	@Override
	public String toString() {
		return caseNumber.toString();	
	}

	// Compare with each other by Date of Case
	@Override
	public int compareTo(Case c) {
		return this.getCaseDate().compareTo(c.getCaseDate());
	}
}
