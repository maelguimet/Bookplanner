package bookplanner.controllers;


import bookplanner.application.App;
import bookplanner.data.Language;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;

public class SettingsController implements Initializable{

	private static final String username=System.getProperty("user.name");
	private static final String propertiesPath = "C:/Users/"+username+"/Documents/BookPlanner/app.properties";

	private @FXML CheckBox safeMode;
	private @FXML ImageView settings;
	private @FXML ComboBox lang;
	private @FXML Button cancelButton;
	private @FXML Button confirmButton;

	private static HashMap<String, String> map;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {handleLanguage();} catch (IOException e) {e.printStackTrace();}
		onOpen();
	}

	@FXML
    private void switchToMenu() throws IOException {
        App.setRoot("MainMenu");
    }

	private void onOpen(){
		Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(map.get("img"))));
		settings.setImage(img);

		Tooltip t = new Tooltip(map.get("tooltip"));
		safeMode.setTooltip(t);
	}

    @SuppressWarnings("unchecked")
	private void stuffLanguageBox() {
    	lang.getItems().add("English");
    	lang.getItems().add("Français");
    }
    
    @FXML
	private void saveAndQuit() throws IOException {
    	File f = new File(propertiesPath);
    	FileWriter writer = new FileWriter(f);	
    	writer.write("lang="+lang.getSelectionModel().getSelectedIndex()+"\n");
    	writer.write("safeMode=" + safeMode.isSelected()+"\n");
    	writer.close();
    	switchToMenu();
	}

	private void handleLanguage() throws IOException {
		stuffLanguageBox();
		map = Language.getInstance().settings();
		safeMode.setText(map.get("checkbox"));
		confirmButton.setText(map.get("confirm"));
		cancelButton.setText(map.get("cancel"));

		safeMode.setSelected(Language.getSafeMode());
		lang.getSelectionModel().select(Language.getLanguageId());
	}
}
