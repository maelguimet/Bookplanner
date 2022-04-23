package bookplanner.controllers;

import bookplanner.application.App;
import bookplanner.data.Language;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

	private final String username = System.getProperty("user.name");
	private @FXML ImageView logo;
	private @FXML Button newProjectButton;
	private @FXML Button LoadProjectButton;
	private @FXML Button settingsButton;
	private @FXML Button exitButton;
	private @FXML ImageView title;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {generateDirectories();} catch (IOException e1){e1.printStackTrace();}
		try {handleLanguage();} catch (IOException e){e.printStackTrace();}
		onOpen();
	}

	@FXML
    private void switchToNewProject() throws IOException {
    	App.setRoot("NewProject");
    }

    @FXML
    private void switchToLoadProject() throws IOException {
        App.setRoot("LoadProject");
    }

    @FXML
    private void switchToSettings() throws IOException {
        App.setRoot("Settings");
    }

    @FXML
    private void exit() {
    	Platform.exit();
    }

	private void onOpen(){
		javafx.scene.Scene scene = App.getScene();

		if(scene != null && scene.getAccelerators() != null){
			KeyCombination[] obj = scene.getAccelerators().keySet().toArray(new KeyCombination[0]);

			for(KeyCombination kc : obj){
				scene.getAccelerators().remove(kc);
			}
		}

		Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo_small.png")));
		logo.setImage(img);

		//https://cooltext.com/Logo-Design-Legal
		Image titleImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("bookplanner.png")));
		title.setImage(titleImg);
	}

	private void handleLanguage() throws IOException {
		HashMap<String, String> map = Language.getInstance().mainMenu();
		newProjectButton.setText(map.get("new"));
		LoadProjectButton.setText(map.get("load"));
		settingsButton.setText(map.get("set"));
		exitButton.setText(map.get("exit"));
	}

    private void generateDirectories() throws IOException {
    	String picPath = "C:/Users/"+username+"/Documents/BookPlanner/pictures";
    	String projPath = "C:/Users/"+username+"/Documents/BookPlanner/projects";
    	Files.createDirectories(Paths.get(picPath));
    	Files.createDirectories(Paths.get(projPath));
		generateProperties();
    }

    private void generateProperties() throws IOException {
    	String path = "C:/Users/"+username+"/Documents/BookPlanner/app.properties";
    	File f = new File(path);
    	if(!f.exists() && f.createNewFile()) {
			FileWriter writer = new FileWriter(f);
			writer.write("lang="+0+"\n");
			writer.write("safeMode=" + "true"+"\n");
			writer.close();
    	}
    }
}
