package bookplanner.controllers;

import bookplanner.application.App;
import bookplanner.data.BookProject;
import bookplanner.data.Language;
import bookplanner.data.ProjectFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;

public class NewProjectController implements Initializable {

	private static HashMap<String, String> map = new HashMap<>();
	private static final String username=System.getProperty("user.name");
	private static final String projectPath="C:/Users/"+username+"/Documents/BookPlanner/projects";

	private String[] projects;

	private @FXML TextField newProjectTitle;
	private @FXML TextArea newProjectDescription;
	private @FXML Button cancelButton;
	private @FXML Button createButton;
	private @FXML Label ErrorLabel;
	private @FXML ImageView newProject;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {handleLanguage();}catch (IOException e) {e.printStackTrace();}
		onOpen();
	}

	@FXML
    private void switchToMenu() throws IOException {
        App.setRoot("MainMenu");
    }
	
	@FXML
    private void switchToDisplayProject() throws IOException {
    	if(createBookProject()){
    		App.setRoot("DisplayProject2");
    	}     
    }

	private void onOpen(){
		//https://cooltext.com/Logo-Design-Legal
		Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(map.get("img"))));
		newProject.setImage(img);
	}

	private void handleLanguage() throws IOException {
		map = Language.getInstance().newProject();
		newProjectTitle.setPromptText(map.get("1stPrompt"));
		newProjectDescription.setPromptText(map.get("2ndPrompt"));
		cancelButton.setText(map.get("cancel"));
		createButton.setText(map.get("validate"));
	}

	private boolean createBookProject() throws IOException {
		loadProjectNames();
		String newProjectTitle = this.newProjectTitle.getText();
		String newProjectDescription= this.newProjectDescription.getText();
		
		if(!newProjectTitle.equals("") && isUniqueTitle(newProjectTitle)) {
			BookProject p = new BookProject(newProjectTitle, newProjectDescription);
			ProjectFactory.saveProject(p);
			passProjectName(newProjectTitle);
			return true;
		}
		ErrorLabel.setText(map.get("error"));
		return false;
	}
	
	private void loadProjectNames() {
		File f = new File(projectPath);
		projects=f.list();
	}

	 private boolean isUniqueTitle(String newProjectTitle) {
		 for(String s : projects) {
			 if (s.equals(newProjectTitle+".ser")) {
				return false;
			}	 
		 }	 
		 return true;
	 }

	private void passProjectName(String projectName) throws IOException {
		String projectNamePath="C:/Users/"+username+"/Documents/BookPlanner/"+"currentProjectName.txt";
		File f=new File(projectNamePath);
		f.createNewFile();
		FileWriter writer=new FileWriter(f);
		writer.write(projectName);
		writer.close();
	}
}
