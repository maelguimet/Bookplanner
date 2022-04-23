package bookplanner.controllers;

import bookplanner.application.App;
import bookplanner.data.BookProject;
import bookplanner.data.Character;
import bookplanner.data.Language;
import bookplanner.data.ProjectFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Scanner;

public class CharacterProfileController implements Initializable {

	private static final String username=System.getProperty("user.name");
	private javafx.scene.Scene scene = App.getScene();

	private @FXML Label name;
	private @FXML Label physDescription;
	private @FXML Label moralDescription;
	private @FXML Label	weakness;
	private @FXML Label	notes;
	private @FXML ImageView image;
	private @FXML Button charactersButton;
	private @FXML Button editButton;
	private @FXML Label pDesc;
	private @FXML Label mDesc;
	private @FXML Label weak;
	private @FXML Label not;

	private BookProject p;
	private Character c;
	private String projectName;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {onOpen();} catch (IOException e) {e.printStackTrace();} catch (ClassNotFoundException e) {e.printStackTrace();
		}
	}

	private void handleLanguage() throws IOException {
		HashMap<String, String> map = Language.getInstance().charProfile();
		charactersButton.setText(map.get("charactersButton"));
		editButton.setText(map.get("editButton"));
		pDesc.setText(map.get("physDescription"));
		mDesc.setText(map.get("moralDescription"));
		weak.setText(map.get("weakness"));
		not.setText(map.get("notes"));
	}

	@FXML 
	private void goToDB() throws IOException {
		App.setRoot("CharacterDB");
	}

	@FXML
	private void goToEdit() throws IOException {
		App.setRoot("CharacterEdit");
	}

	private void onOpen() throws IOException, ClassNotFoundException {
		handleLanguage();
		loadProject();
		loadCharacter();
		setupAccelerators();
		setupUI();
	}
	
	private void loadCharacter() throws FileNotFoundException {
		String path = "C:/Users/"+username+"/Documents/BookPlanner/currChar.txt";
		File f = new File(path);
		Scanner reader = new Scanner(f);
		String charName = "";
		if(reader.hasNextLine()){
			charName = reader.nextLine();
		}
		reader.close();
		c = p.getCharDB().searchForCharacter(charName);
	}
	
	private void loadProject() throws ClassNotFoundException, IOException {
		getProjectName();
		p = ProjectFactory.loadProject(projectName);
	}
	
	private void getProjectName() throws FileNotFoundException {
		String path = "C:/Users/"+username+"/Documents/BookPlanner/currentProjectName.txt";
		File f = new File(path);
		Scanner reader = new Scanner(f);
		if(reader.hasNextLine()){
			projectName = reader.nextLine();
		}
		reader.close();
	}
	
	private void setupUI() {
		String labelName = c.getCharName();
		
		if(!c.getCharNickname().equals("")) {
			labelName = labelName + " - "+c.getCharNickname();
		}

		name.setText(labelName);
		physDescription.setText(c.getCharDescP());
		moralDescription.setText(c.getCharDescM());
		weakness.setText(c.getCharWeakness());
		notes.setText(c.getCharNotes());

		String path = c.getPath();
		if(!path.equals("")){
			Image img = new Image(path);
			image.setImage(img);
		}
	}

	private void setupAccelerators(){

		KeyCombination[] obj = scene.getAccelerators().keySet().toArray(new KeyCombination[0]);

		for(KeyCombination kc : obj){
			scene.getAccelerators().remove(kc);
		}

		scene.getAccelerators().put(
				new KeyCodeCombination(KeyCode.E), new Runnable() {
					@FXML public void run() {
							editButton.fire();
					}
				});

		scene.getAccelerators().put(
				new KeyCodeCombination(KeyCode.ESCAPE), new Runnable() {
					@FXML public void run() {charactersButton.fire();}
				});
	}
}
