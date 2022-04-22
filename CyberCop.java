/*
 * Name: Jinxiao Li
 * Andrew ID: jinxiaol
 */

package hw3;

import javafx.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class CyberCop extends Application{

	public static final String DEFAULT_PATH = "data"; //folder name where data files are stored
	public static final String DEFAULT_HTML = "/CyberCop.html"; //local HTML
	public static final String APP_TITLE = "Cyber Cop"; //displayed on top of app

	CCView ccView = new CCView();
	CCModel ccModel = new CCModel();

	CaseView caseView; //UI for Add/Modify/Delete menu option

	GridPane cyberCopRoot;
	Stage stage;

	static Case currentCase; //points to the case selected in TableView.

	public static void main(String[] args) {
		launch(args);
	}

	/** start the application and show the opening scene */
	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		primaryStage.setTitle("Cyber Cop");
		cyberCopRoot = ccView.setupScreen();  
		setupBindings();
		Scene scene = new Scene(cyberCopRoot, ccView.ccWidth, ccView.ccHeight);
		setupActions();
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		ccView.webEngine.load(getClass().getResource(DEFAULT_HTML).toExternalForm());
		primaryStage.show();
	}

	/** setupBindings() binds all GUI components to their handlers.
	 * It also binds disableProperty of menu items and text-fields 
	 * with ccView.isFileOpen so that they are enabled as needed
	 */
	void setupBindings() {
		//Set the common action handlers on the menu items
		ccView.addCaseMenuItem.setOnAction(new CaseMenuItenHandler());
		ccView.modifyCaseMenuItem.setOnAction(new CaseMenuItenHandler());
		ccView.deleteCaseMenuItem.setOnAction(new CaseMenuItenHandler());
		
		// bind the disable properties to the close, open and other menu items according to the isFileOpen status
		ccView.closeFileMenuItem.disableProperty().bind(ccView.isFileOpen.not());
		ccView.openFileMenuItem.disableProperty().bind(ccView.isFileOpen);
		ccView.addCaseMenuItem.disableProperty().bind(ccView.isFileOpen.not());
		ccView.modifyCaseMenuItem.disableProperty().bind(ccView.isFileOpen.not());
		ccView.deleteCaseMenuItem.disableProperty().bind(ccView.isFileOpen.not());
		ccView.caseCountChartMenuItem.disableProperty().bind(ccView.isFileOpen.not());
		ccView.saveFileMenuItem.disableProperty().bind(ccView.isFileOpen.not());
		
		ccView.caseNumberTextField.disableProperty().bind(ccView.isFileOpen.not());
		ccView.titleTextField.disableProperty().bind(ccView.isFileOpen.not());
		ccView.caseTypeTextField.disableProperty().bind(ccView.isFileOpen.not());
		ccView.yearComboBox.disableProperty().bind(ccView.isFileOpen.not());
		ccView.searchButton.disableProperty().bind(ccView.isFileOpen.not());
		ccView.clearButton.disableProperty().bind(ccView.isFileOpen.not());
		
		
	}
	
	void setupActions() {
		
		//OpenFileMenuItemHandler
		ccView.openFileMenuItem.setOnAction(event ->{
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			fileChooser.setInitialDirectory(new File(DEFAULT_PATH));
			File file = fileChooser.showOpenDialog(stage);
			String filename = file.getAbsolutePath();
			ccModel.readCases(filename);
			ccModel.buildYearMapAndList();
			ccView.isFileOpen.setValue(true);
			
			ccView.yearComboBox.setItems(ccModel.yearList);
				for(Case c:ccModel.caseList) {
				ccView.caseTableView.getItems().add(c);
			}
			currentCase = ccModel.caseList.get(0);
			ccView.titleTextField.setText(currentCase.getCaseTitle());
			ccView.caseTypeTextField.setText(currentCase.getCaseType());
			ccView.caseNumberTextField.setText(currentCase.getCaseNumber());
			ccView.yearComboBox.setValue(currentCase.getCaseDate().substring(0, 4));
			ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());
			ccView.messageLabel.setText(String.valueOf(ccModel.caseList.size())+" cases.");
			
		});
		
		/*
		 * when clicking on the caseTableView, update the currentcase 
		 * and update the values shown in the text fields
		 */
		ccView.caseTableView.setOnMouseClicked(event ->{
			if(ccView.caseTableView.getSelectionModel().getSelectedItems().size()>0) {
				
				currentCase = ccView.caseTableView.getSelectionModel().getSelectedItem();
				
				ccView.titleTextField.setText(currentCase.getCaseTitle());
				ccView.caseTypeTextField.setText(currentCase.getCaseType());
				ccView.caseNumberTextField.setText(currentCase.getCaseNumber());
				ccView.yearComboBox.setValue(currentCase.getCaseDate().substring(0, 4));
				ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());
				
				// visit the web pages here 
				
				if(ccModel.caseList.size()>0 && currentCase != null) {
					
					if (currentCase.getCaseLink() == null || currentCase.getCaseLink().isBlank()) {  //if no link in data
						URL url = getClass().getClassLoader().getResource(DEFAULT_HTML);  //default html
						if (url != null) ccView.webEngine.load(url.toExternalForm());
					} else if (currentCase.getCaseLink().toLowerCase().startsWith("http")){  //if external link
						ccView.webEngine.load(currentCase.getCaseLink());
					} else {
						URL url = getClass().getClassLoader().getResource(currentCase.getCaseLink().trim());  //local link
						if (url != null) ccView.webEngine.load(url.toExternalForm());
					}
				}

			}
		});
		
		//SaveFileMenuItemHandler
		ccView.saveFileMenuItem.setOnAction(event ->{
			/*
			 * 
			 */
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save file");
			fileChooser.setInitialDirectory(new File(DEFAULT_PATH));
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));

			File file = fileChooser.showSaveDialog(stage);
			fileChooser.setInitialFileName("myfile.txt");
			
			String filename = file.getPath();
			if(ccModel.writeCases(filename) == true) {
				ccView.messageLabel.setText(filename+ " saved");
			}else ccView.messageLabel.setText("falied to save");
		});
		
		
		//CloseFileMenuItemHandler
		ccView.closeFileMenuItem.setOnAction(event ->{
			
			ccView.isFileOpen.setValue(false);
			
			// Clear the GUI components
			ccView.caseTableView.getItems().clear();
			ccView.titleTextField.clear();
			ccView.caseNumberTextField.clear();
			ccView.caseTypeTextField.clear();
			ccView.yearComboBox.getSelectionModel().clearSelection();
			ccView.messageLabel.setText("");
			
			//Clear caselists and casemaps in the ccModel when close the file
			ccModel.caseList.clear();
			ccModel.caseMap.clear();
			ccModel.yearList.clear();
			ccModel.yearMap.clear();
			
			ccView.webEngine.load(getClass().getResource(DEFAULT_HTML).toExternalForm());
			
		});
		
		//ExitMenuItemHandler
		ccView.exitMenuItem.setOnAction(event ->{
				stage.close();
		});
		
		
		//SearchButtonHandler
		ccView.searchButton.setOnAction(event ->{
			ccView.caseTableView.getItems().clear();
			
			// Convert the value of userinput into "" if input nothing
			String title,caseType,year,caseNumber;
			if(ccView.titleTextField.getText().trim().length()==0) {
				title = "";
			}else title = ccView.titleTextField.getText().trim();
			
			if(ccView.caseTypeTextField.getText().trim().length()==0) {
				caseType = "";
			}else caseType = ccView.caseTypeTextField.getText().trim();
			
			if(ccView.yearComboBox.getSelectionModel().getSelectedItem() == null) {
				year = "";
			}else year = ccView.yearComboBox.getSelectionModel().getSelectedItem();
			
			if(ccView.caseNumberTextField.getText().trim().length()==0) {
				caseNumber = "";
			}else caseNumber = ccView.caseNumberTextField.getText().trim();
			
			// Search cases and return the result to returnlist
			List<Case> returnlist = ccModel.searchCases(title, caseType, year, caseNumber);
			Collections.sort(returnlist);
			
			if(returnlist.size()>0) {
				for(Case c:returnlist) {
					ccView.caseTableView.getItems().add(c);
				}
			}
			ccView.messageLabel.setText(String.valueOf(returnlist.size())+" cases.");
			
		});
		
		
		//ClearButtonHandler
		ccView.clearButton.setOnAction(event ->{
				ccView.titleTextField.setText("");
				ccView.caseNumberTextField.setText("");
				ccView.caseTypeTextField.setText("");
				ccView.yearComboBox.getSelectionModel().clearSelection();

			});
		
		ccView.caseCountChartMenuItem.setOnAction(event ->{
			ccView.showChartView(ccModel.yearMap);
			
		});
		
	}
	
	//CaseMenuItenHandler
	class CaseMenuItenHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
                MenuItem mItem = (MenuItem) event.getSource();
                String side = mItem.getText();
                if ("Add case".equalsIgnoreCase(side)) {
                	
                	caseView = new	AddCaseView("Add Case");
                	caseView.updateButton.setOnAction(new AddButtonHandler());
                	caseView.buildView().show();
                	
                } else if ("Modify case".equalsIgnoreCase(side)) {
                	caseView = new ModifyCaseView("Modify Case");
                	caseView.updateButton.setOnAction(new ModifyButtonHandler());
                	caseView.buildView().show();

                } else if ("Delete case".equalsIgnoreCase(side)) {
                	caseView = new DeleteCaseView("Delete Case");
                	caseView.updateButton.setOnAction(new DeleteButtonHandler());
                	caseView.buildView().show();
                }  
               
                /*
                 * Set on actions to the buttons in the caseview.
                 */
                caseView.clearButton.setOnAction(new EventHandler<ActionEvent> () {
					@Override
					public void handle(ActionEvent arg0) {
						caseView.caseLinkTextField.clear();
						caseView.caseNumberTextField.clear();
						caseView.caseTypeTextField.clear();
						caseView.categoryTextField.clear();
						caseView.caseNotesTextArea.clear();
						caseView.titleTextField.clear();
						caseView.caseDatePicker.setValue(null);
					}
                });
                
                caseView.closeButton.setOnAction(new EventHandler<ActionEvent> () {
					@Override
					public void handle(ActionEvent arg0) {
						caseView.stage.close();
					}
                });
              
       }	
	}
	
	//AddButtonHandler
	class AddButtonHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {	
			try {
				String dt = caseView.caseDatePicker.getValue().format( 
						DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				String tt = caseView.titleTextField.getText().trim();
				String tp = caseView.caseTypeTextField.getText().trim();
				String nb = caseView.caseNumberTextField.getText().trim();
				String lk = caseView.caseLinkTextField.getText().trim();
				String cy = caseView.categoryTextField.getText().trim();
				String nt = caseView.caseNotesTextArea.getText().trim();
				if(dt.isBlank() || tt.isBlank() ||tp.isBlank()||nb.isBlank()) {
					 throw new DataException("Case must have date, title, type, and case number!");
				}
				else if(ccModel.caseMap.containsKey(nb)) {
					 throw new DataException("Duplicate case number!");
				}
				else {
					if(lk.isBlank()) lk = " ";
					if(cy.isBlank()) cy = " ";
					if(nt.isBlank()) nt = " ";
					ccModel.caseList.add(new Case(dt, tt, tp, nb, lk, cy, nt));
					ccModel.caseMap.put(nb, new Case(dt, tt, tp, nb, lk, cy, nt));
					ccModel.buildYearMapAndList();
					// Refresh the items shown in the screen
					ccView.caseTableView.getItems().clear();
					
					for(Case c:ccModel.caseList) {
						ccView.caseTableView.getItems().add(c);
					}
				}
			}catch (DataException e) {
			}
			ccView.messageLabel.setText(String.valueOf(ccModel.caseList.size())+" cases.");

		}
	}
	
	//ModifyButtonHandler
	class ModifyButtonHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {	
			
			try {
			String dt = caseView.caseDatePicker.getValue().format( 
					DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			String tt = caseView.titleTextField.getText().trim();
			String tp = caseView.caseTypeTextField.getText().trim();
			String nb = caseView.caseNumberTextField.getText().trim();
			String lk = caseView.caseLinkTextField.getText().trim();
			String cy = caseView.categoryTextField.getText().trim();
			String nt = caseView.caseNotesTextArea.getText().trim();
			
			
			if(dt.isBlank() || tt.isBlank() ||tp.isBlank()||nb.isBlank()) {
				 throw new DataException("Case must have date, title, type, and case number!");
			}
			else if(ccModel.caseMap.containsKey(nb)) {
				 throw new DataException("Duplicate case number!");
			}
			else {
			// Modify the current case
				if(lk.isBlank()) lk = " ";
				if(cy.isBlank()) cy = " ";
				if(nt.isBlank()) nt = " ";
				currentCase.setCaseDate(dt);
				currentCase.setCaseTitle(tt);
				currentCase.setCaseType(tp);
				currentCase.setCaseNumber(nb);
				currentCase.setCaseLink(lk);
				currentCase.setCaseCategory(cy);
				currentCase.setCaseNotes(nt);
				
				ccModel.caseMap.put(currentCase.getCaseNumber(), currentCase);
				
				ccModel.buildYearMapAndList();

			
			// Refresh the items shown in the screen
			ccView.caseTableView.getItems().clear();
			for(Case c:ccModel.caseList) {
				ccView.caseTableView.getItems().add(c);
				}
			}
			ccView.messageLabel.setText(String.valueOf(ccModel.caseList.size())+" cases.");
			
			
			}catch (DataException e) {
			}
		}
	}
	
	//DeleteButtonHandler
	class DeleteButtonHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			ccModel.caseList.remove(currentCase);
			ccModel.caseMap.remove(currentCase.getCaseTitle());
			ccView.messageLabel.setText(String.valueOf(ccModel.caseList.size())+" cases.");
			ccView.caseTableView.getItems().clear();
			
			for(Case c:ccModel.caseList) {
				ccView.caseTableView.getItems().add(c);
			}
		}
	}
	
	
}
