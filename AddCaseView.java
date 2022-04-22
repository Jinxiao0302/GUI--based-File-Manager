/*
 * Name: Jinxiao Li
 * Andrew ID: jinxiaol
 */

package hw3;

import java.time.LocalDate;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class AddCaseView extends CaseView{

	// Set up caseDatePicker and text of updateButton
	AddCaseView(String header) {
		super(header);
		this.updateButton.setText(header);
		this.caseDatePicker.setValue(LocalDate.now());

	}

	// set scence in the stage 
	@Override
	Stage buildView() {
		Scene scene = new Scene(this.updateCaseGridPane, 700, 500);
		this.updateCaseGridPane.requestFocus(); 
		this.stage.setScene(scene);
		return (this.stage);
		
	}

}
