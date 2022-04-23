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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Scanner;

public class NewCharacterController implements Initializable {

	private static final String username=System.getProperty("user.name");

	private @FXML TextField nameBox;
	private @FXML TextField nicknameBox;
	private @FXML TextArea physBox;
	private @FXML TextArea moralBox;
	private @FXML TextArea weaknessBox;
	private @FXML TextArea notesBox;
	private @FXML Label name;
	private @FXML Label nickname;
	private @FXML Label phys;
	private @FXML Label moral;
	private @FXML Label weakness;
	private @FXML Label notes;
	private @FXML Button cancel;
	private @FXML Button create;
	private @FXML Button selectPicture;
	private @FXML ImageView img;

	private String currentProjectName;
	private BookProject p = null;
	private String path="";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {getProjectName();} catch (FileNotFoundException e){e.printStackTrace();}
		try {handleLanguage();} catch (IOException e){e.printStackTrace();}
	}

	@FXML 
	private void backToDisplay() throws IOException {
		App.setRoot("DisplayProject2");
	}
	
	@FXML 
	private void goToDB() throws IOException, ClassNotFoundException {
		saveNewCharacter();
		App.setRoot("CharacterDB");
	}

	private void saveNewCharacter() throws ClassNotFoundException, IOException {
		p = ProjectFactory.loadProject(currentProjectName);

		String charName = checkForUniqueName(nameBox.getText());
		String charNickname = nicknameBox.getText();
		String charDescP = physBox.getText();
		String charDescM = moralBox.getText();
		String charRift = weaknessBox.getText();
		String charNotes = notesBox.getText();

		p.getCharDB().addCharacterToDB(new Character(charName, charNickname, charDescP, charDescM, charRift, charNotes, path));
		ProjectFactory.saveProject(p);
	}

	private String checkForUniqueName(String charName) {
		String name = charName;
		for(Character c : p.getCharDB().getCharacters()){
			if(c.getCharName().equals(name)){
				name = name + " (dupe)";
			}
		}
		return name;
	}

	private void getProjectName() throws FileNotFoundException {
		String projectNamePath="C:/Users/"+username+"/Documents/BookPlanner/currentProjectName.txt";
		File f=new File(projectNamePath);
		Scanner reader = new Scanner(f);
		if(reader.hasNextLine()){
			currentProjectName=reader.nextLine();
		}
		reader.close();
	}

	@FXML
	private void choosePic() throws IOException {
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose profile picture");
		File home = FileSystemView.getFileSystemView().getHomeDirectory();
		fc.setInitialDirectory(home);

		String [] str = {"*.png", "*.jpg"};
		FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Image", str);
		fc.getExtensionFilters().add(filter);

		File f = fc.showOpenDialog(null);

		if(f != null) {
			Path original = Paths.get(f.getAbsolutePath());
			String picName = f.getName();
			Path copy = Paths.get("C:/Users/"+username+"/Documents/BookPlanner/pictures/"+picName);
			File file = new File (copy.toString());
			if(!file.exists()){
				Files.copy(original, copy);
			}
			path = "C:/Users/"+username+"/Documents/BookPlanner/pictures/"+picName;
			Image image = new Image (path);
			img.setImage(image);
		}
	}

	private void handleLanguage() throws IOException {
		HashMap<String, String> map = Language.getInstance().newCharacter();

		cancel.setText(map.get("cancel"));
		create.setText(map.get("create"));
		selectPicture.setText(map.get("selectPicture"));

		name.setText(map.get("name"));
		nickname.setText(map.get("nickname"));
		phys.setText(map.get("phys"));
		moral.setText(map.get("moral"));
		weakness.setText(map.get("weakness"));
		notes.setText(map.get("notes"));

		nameBox.setPromptText(map.get("nameBox"));
		nicknameBox.setPromptText(map.get("nicknameBox"));
		physBox.setPromptText(map.get("physBox"));
		moralBox.setPromptText(map.get("moralBox"));
		weaknessBox.setPromptText(map.get("weaknessBox"));
		notesBox.setPromptText(map.get("notesBox"));
	}
}
