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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Scanner;

public class CharacterEditController implements Initializable{

	private @FXML TextField nameBox;
	private @FXML TextField nicknameBox;
	private @FXML TextArea physBox;
	private @FXML TextArea moralBox;
	private @FXML TextArea weaknessBox;
	private @FXML TextArea notesBox;
	private @FXML HBox pane;
	private @FXML ImageView img;
	private @FXML Label labelName;
	private @FXML Label labelNickname;
	private @FXML Label labelPhys;
	private @FXML Label labelMoral;
	private @FXML Label labelWeak;
	private @FXML Label labelNotes;
	private @FXML Button selectPicture;
	private @FXML Button confirm;
	private @FXML Button cancel;

	private static final String username=System.getProperty("user.name");
	private Character c;
	private BookProject p = null;

	private String currentProjectName;
	private String path="";
	private String charName="";
	private String charNickname="";
	private String charDescP="";
	private String charDescM="";
	private String charRift="";
	private String charNotes="";
	private String currChar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {onOpen();} catch (IOException e){e.printStackTrace();} catch (ClassNotFoundException e){e.printStackTrace();}
	}

	private void handleLanguage() throws IOException {
		HashMap<String, String> map = Language.getInstance().charEdit();

		labelName.setText(map.get("name"));
		labelNickname.setText(map.get("nickname"));
		labelPhys.setText(map.get("phys"));
		labelMoral.setText(map.get("moral"));
		labelWeak.setText(map.get("weak"));
		labelMoral.setText(map.get("notes"));

		selectPicture.setText(map.get("picture"));
		confirm.setText(map.get("confirm"));
		cancel.setText(map.get("cancel"));

		nameBox.setPromptText(map.get("nameBox"));
		nicknameBox.setPromptText(map.get("nicknameBox"));
		physBox.setPromptText(map.get("physBox"));
		moralBox.setPromptText(map.get("moralBox"));
		weaknessBox.setPromptText(map.get("weaknessBox"));
		notesBox.setPromptText(map.get("notesBox"));
	}

	private void onOpen() throws IOException, ClassNotFoundException {
		getProjectName();
		loadCharacter();
		handleLanguage();
		setupAccelerators();
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

	@FXML
	private void goToProfile() throws IOException {
		saveCharacter();
		App.setRoot("CharacterProfile");
	}

	@FXML
	private void goToProfileNoSave() throws IOException {
		App.setRoot("CharacterProfile");
	}
	
	private void saveCharacter() throws IOException {
		String currentCharPath = "C:/Users/"+username+"/Documents/BookPlanner/currChar.txt";
		File f = new File(currentCharPath);
		if(f.createNewFile()){
			if(!currChar.equals(nameBox.getText())){
				charName = checkForUniqueName(nameBox.getText());
			}else{
				charName = currChar;
			}
			FileWriter writer = new FileWriter(f);
			writer.write(charName);
			writer.close();
		}

		charNickname = nicknameBox.getText();
		charDescP = physBox.getText();
		charDescM = moralBox.getText();
		charRift = weaknessBox.getText();
		charNotes = notesBox.getText();

		p.getCharDB().editCharacter(currChar, charName, charNickname, charDescP, charDescM, charRift, charNotes, path);
		ProjectFactory.saveProject(p);
	}
	
	private void getProjectName() throws FileNotFoundException {
		String projectNamePath="C:/Users/"+username+"/Documents/BookPlanner/currentProjectName.txt";
		File f=new File(projectNamePath);
		Scanner reader = new Scanner(f);
		currentProjectName=reader.nextLine();
		reader.close();
	}

	@FXML
	private void choosePic() {
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
			try {
				Files.copy(original, copy);
			} catch (Exception e) {
				e.printStackTrace();
			}
			path = "C:/Users/"+username+"/Documents/BookPlanner/pictures/"+picName;
			Image image = new Image (path);
			img.setImage(image);
		}
	}
	
	private void loadCharacter() throws ClassNotFoundException, IOException {
		p = ProjectFactory.loadProject(currentProjectName);
		String path = "C:/Users/"+username+"/Documents/BookPlanner/currChar.txt";
		File f = new File(path);
		Scanner reader = new Scanner(f);
		String charName = reader.nextLine();
		reader.close();
		currChar = charName;
		c = p.getCharDB().searchForCharacter(charName);
		loadUI();
	}
	
	private void loadUI() {
		nameBox.setText(c.getCharName());
		nicknameBox.setText(c.getCharNickname());
		physBox.setText(c.getCharDescP());
		moralBox.setText(c.getCharDescM());
		weaknessBox.setText(c.getCharWeakness());
		notesBox.setText(c.getCharNotes());
		path = c.getPath();
		
		if(!path.equals("")) {
			img.setImage(new Image(path));
		}
	}

	private void setupAccelerators(){
		javafx.scene.Scene scene = App.getScene();

		KeyCombination[] obj = scene.getAccelerators().keySet().toArray(new KeyCombination[0]);

		for(KeyCombination kc : obj){
			scene.getAccelerators().remove(kc);
		}

		scene.getAccelerators().put(
				new KeyCodeCombination(KeyCode.ESCAPE), new Runnable() {
					@FXML public void run() {
						try {
							goToProfile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
	}
}
