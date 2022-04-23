package bookplanner.controllers;

import bookplanner.application.App;
import bookplanner.data.Language;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class LoadProjectController  implements Initializable{

	private static final String username=System.getProperty("user.name");
	private static final String projectPath="C:/Users/"+username+"/Documents/BookPlanner/projects/";

	private @FXML ListView <String> listView;
	private @FXML Button deleteButton;
	private @FXML Button newButton;
	private @FXML Button cancelButton;
	private @FXML Button loadButton;
	private @FXML ImageView loadProject;

	private boolean safe;
	private static HashMap <String, String> map = new HashMap<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		getProjectNames();
		try {handleLanguage();} catch (IOException e){e.printStackTrace();}
		onOpen();
	}

	@FXML
	private void switchToNewProject() throws IOException {
		App.setRoot("NewProject");
	}

	@FXML
    private void switchToDisplayProject() throws IOException {
		prepareLoading();
		App.setRoot("DisplayProject2");
    }
	
	@FXML
    private void switchToMenu() throws IOException {
        App.setRoot("MainMenu");
    }

	private void handleLanguage() throws IOException {
		map = Language.getInstance().loadProject();
		safe = Language.getSafeMode();
		newButton.setText(map.get("new"));
		loadButton.setText(map.get("load"));
		cancelButton.setText(map.get("cancel"));
		deleteButton.setText(map.get("delete"));
	}

	private void onOpen(){
		setDelShortcut();
		doubleClickOnItem();

		Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(map.get("img"))));
		loadProject.setImage(img);
	}

    @FXML
    private void deleteSelectedItem() {
    	
    	if(listView.getSelectionModel().getSelectedItem() == null) {
    		return;
    	}
    	
    	if(safe) {
    		Alert alert = new Alert(AlertType.CONFIRMATION, map.get("confirmation"));
			alert.setHeaderText(map.get("alertHeader")+ " " + listView.getSelectionModel().getSelectedItem()+ ".");
			alert.setTitle(map.get("alertTitle"));

			DialogPane dp = alert.getDialogPane();
			dp.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
			dp.getStyleClass().add("myDialog");

    		Optional <ButtonType> result = alert.showAndWait();
    		if(result.isPresent() && result.get() == ButtonType.OK) {
				deleteConfirmed();
    		}
    		return;
    	}
		deleteConfirmed();
	}

	private void deleteConfirmed() {
		String selectedItem = listView.getSelectionModel().getSelectedItem();
		int selectedItemIndex = listView.getSelectionModel().getSelectedIndex();
		File f = new File(projectPath+selectedItem+".ser");
		if(	f.delete()){
			listView.getItems().remove(selectedItemIndex);
		}
	}

	@FXML
    public void textAction(KeyEvent e) throws IOException{
        if(e.getCode().equals(KeyCode.ENTER))
        	switchToDisplayProject();
    }
    
    private void doubleClickOnItem() {
    	listView.setOnMouseClicked(click -> {
			if(listView.getSelectionModel().getSelectedItem() == null){
				return;
			}
			if (click.getClickCount() == 2) {
				try {switchToDisplayProject();} catch (IOException e){e.printStackTrace();}
			}
		});
    }
    
	public void getProjectNames(){
		File f = new File(projectPath);
		String[] projects = f.list();

		if(projects == null ||projects.length == 0){
			return;
		}

		LinkedList <String> temp = new LinkedList<>(Arrays.asList(projects));
		LinkedList <String> listProjects = new LinkedList<>();

		for(String s : temp){
			listProjects.add(s.substring(0, s.length()-4));
		}
		listView.getItems().addAll(listProjects);
	}
	
	private void prepareLoading() throws IOException {
		String currentProjectNamePath="C:/Users/"+username+"/Documents/BookPlanner/currentProjectName.txt";

		if(listView.getSelectionModel().getSelectedItem() == null){
			return;
		}

		String projectName = listView.getSelectionModel().getSelectedItem();
		File f=new File(currentProjectNamePath);
		f.createNewFile();
		FileWriter writer=new FileWriter(f);
		writer.write(projectName);
		writer.close();
	}

	private void setDelShortcut() {
		javafx.scene.Scene scene = App.getScene();

		KeyCombination[] obj = scene.getAccelerators().keySet().toArray(new KeyCombination[0]);

		for(KeyCombination kc : obj){
			scene.getAccelerators().remove(kc);
		}

		scene.getAccelerators().put(
				new KeyCodeCombination(KeyCode.DELETE), new Runnable() {
					@FXML public void run() {
						deleteSelectedItem();
					}
				});

		listView.addEventFilter( KeyEvent.KEY_PRESSED, keyEvent -> {
			if( keyEvent.getCode() == KeyCode.ESCAPE) {
				cancelButton.fire();
				keyEvent.consume();
			}
		});
	}
}
