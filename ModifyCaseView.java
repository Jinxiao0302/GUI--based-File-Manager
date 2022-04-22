/*
 * Name: Jinxiao Li
 * Andrew ID: jinxiaol
 */

package hw3;

import java.time.LocalDate;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class ModifyCaseView extends CaseView{
	
	Case currentCase = CyberCop.currentCase;
	
	// set up the contents in the text fields
	ModifyCaseView(String header) {
		super(header);
		this.updateButton.setText(header);
		this.titleTextField.setText(currentCase.getCaseTitle());
		this.caseTypeTextField.setText(currentCase.getCaseType());
		this.caseNumberTextField.setText(currentCase.getCaseNumber());
		this.caseDatePicker.setValue(LocalDate.of(Integer.valueOf(currentCase.getCaseDate().substring(0,4)), 
								Integer.valueOf(currentCase.getCaseDate().substring(5,7)), 
								Integer.valueOf(currentCase.getCaseDate().substring(8,10))));
	}

	// Set up the scene 
	@Override
	Stage buildView() {
		Scene scene = new Scene(this.updateCaseGridPane, 700, 500);
		this.updateCaseGridPane.requestFocus(); 
		this.stage.setScene(scene);
		return (this.stage);
	}

}
