package bookplanner.controllers;

import bookplanner.application.App;
import bookplanner.data.BookProject;
import bookplanner.data.Character;
import bookplanner.data.Language;
import bookplanner.data.ProjectFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class CharacterDBController implements Initializable{

	private @FXML ImageView image;
	private @FXML FlowPane flowPane;
	private @FXML Button newButton;
	private @FXML Button backButton;
	private @FXML Button deleteButton;
	private @FXML Button editButton;
	private @FXML ScrollPane scrollPane;

	private static final String username = System.getProperty("user.name");

	private BookProject p;
	private VBox selectedBox = null;

	private boolean safe=true;
	private static HashMap<String, String> map = new HashMap<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {onOpen();} catch(IOException e){e.printStackTrace();} catch (ClassNotFoundException e) {e.printStackTrace();}
	}

	private void handleLanguage() throws IOException {
			map = Language.getInstance().charDB();
			safe = Language.getSafeMode();
			newButton.setText(map.get("new"));
			backButton.setText(map.get("back"));
			editButton.setText(map.get("editButton"));
			deleteButton.setText(map.get("deleteButton"));
	}

	@FXML 
	private void backToDisplay() throws IOException {
		App.setRoot("DisplayProject2");
	}
	
	@FXML 
	private void goToProfile() throws IOException {
		App.setRoot("CharacterProfile");
	}
	
	@FXML
	private void goToNewChar() throws IOException {
		App.setRoot("CharacterNew");
	}
	
	@FXML 
	private void goToEdit() throws IOException {
		App.setRoot("CharacterEdit");
	}

	private void onOpen() throws IOException, ClassNotFoundException {
		loadPlan();
		handleLanguage();
		genCharUi();
		setupAccelerators();

		Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(map.get("img"))));
		image.setImage(img);
		Platform.runLater(() -> {
			if(!flowPane.getChildren().isEmpty()){
				flowPane.getChildren().get(0).requestFocus();
				select((VBox)flowPane.getChildren().get(0));
			}
		});
	}

	private void genCharUi() {
		
		LinkedList <Character> l = p.getCharDB().getCharacters();
		String name;
		String path;
		
		String cssLayout = """
				-fx-font-family: Cambria;
				-fx-font-size: 14;
				-fx-text-alignment: center;
				""";

		for(Character c : l) {
			name = c.getCharName();
			path = c.getPath();

			VBox vbox = new VBox();
			vbox.setPadding(new Insets (5, 5, 5 ,5));
			vbox.maxHeight(320);
			vbox.maxWidth(200);
			vbox.setAlignment(Pos.TOP_CENTER);
			vbox.setCursor(Cursor.HAND);
			vbox.setId(name);
			vbox.setSpacing(5);
			vbox.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID,
					new CornerRadii( 0.0125), BorderWidths.DEFAULT,
					new Insets(1))));

			ImageView iv = new ImageView();
			iv.setFitHeight(300);
			iv.setFitWidth(200);

			Label lbl = new Label(name);
			lbl.setStyle(cssLayout);
			lbl.setContentDisplay(ContentDisplay.CENTER);
			lbl.setAlignment(Pos.CENTER);
			lbl.setPadding(new Insets(10,0,10,0));
			lbl.setPrefWidth(200);
			lbl.setMaxWidth(200);
			lbl.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID,
					new CornerRadii( 0.0125), BorderWidths.DEFAULT,
					new Insets(1))));

			Image image;

			if(!path.equals("")) {
				image = new Image(path);
			}else{
				image = new Image("C:\\Users\\"+username+"\\IdeaProjects\\application\\src\\main\\resources\\bookplanner\\application\\default.png");
			}

			iv.setImage(image);

			addContextMenu(vbox);
			addOnClickEvent(vbox, lbl);

			vbox.getChildren().add(lbl);
			vbox.getChildren().add(iv);
			flowPane.getChildren().add(vbox);
		}	
	}
	
	private void loadPlan() throws ClassNotFoundException, IOException {
		String path = "C:/Users/"+username+"/Documents/BookPlanner/currentProjectName.txt";
		File f = new File(path);
		Scanner reader = new Scanner(f);
		String projectName = null;
		if(reader.hasNextLine()){
			projectName = reader.nextLine();
		}
		reader.close();
		p = ProjectFactory.loadProject(projectName);
	}	
	
	private void addContextMenu(VBox vb) {
		
		ContextMenu cm = new ContextMenu();		
		
		MenuItem edit = new MenuItem(map.get("edit"));
		edit.setOnAction(event -> {try {edit();} catch (IOException e) {e.printStackTrace();}});
		
		MenuItem delete = new MenuItem(map.get("delete"));
		delete.setOnAction(event -> {
			try {if(selectedBox == null){return;}delete();} catch (IOException e) {e.printStackTrace();}});
		
		cm.getItems().addAll(edit, delete);
		vb.setOnContextMenuRequested(event -> cm.show(vb, null, event.getX(), event.getY()));
	}
	
	private void addOnClickEvent(VBox vb, Label lbl) {
		vb.setOnMouseClicked(event -> {
				select(vb);

			if(event.getClickCount() == 2){
				try {
					open();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		vb.setOnMouseEntered(event -> {
			ColorAdjust c = new ColorAdjust();
			c.setBrightness(0.05);
			vb.setEffect(c);
			lbl.setBorder(new Border(new BorderStroke(Color.DEEPSKYBLUE, BorderStrokeStyle.SOLID,
					new CornerRadii( 0.0125), BorderWidths.DEFAULT,
					new Insets(1))));
		});

		vb.setOnMouseExited(event -> {
			ColorAdjust c = new ColorAdjust();
			c.setBrightness(0);
			vb.setEffect(c);
			lbl.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID,
					new CornerRadii( 0.0125), BorderWidths.DEFAULT,
					new Insets(1))));
		});
	}

	@FXML
	private void open() throws IOException {
		if(selectedBox == null){
			return;
		}
		updateCurrChar();
		goToProfile();
	}

	private void updateCurrChar() throws IOException {
		String str = ((Label)selectedBox.getChildren().get(0)).getText();
		String path = "C:/Users/"+username+"/Documents/BookPlanner/currChar.txt";
		File f = new File(path);
		FileWriter writer = new FileWriter(f);
		writer.write(str);
		writer.close();
	}

	@FXML
	private void edit() throws IOException {
		if(selectedBox == null){
			return;
		}
		updateCurrChar();
		goToEdit();
	}

	private void select(VBox vb){
			if(selectedBox != null){
				unSelect(selectedBox);
			}
			selectedBox = vb;
			vb.setBorder(new Border(new BorderStroke(Color.ORANGE, BorderStrokeStyle.SOLID,
					new CornerRadii( 0.0125), BorderWidths.DEFAULT,
					new Insets(1))));
			vb.requestFocus();
	}

	private void unSelect(VBox vb){
		vb.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID,
				new CornerRadii( 0.0125), BorderWidths.DEFAULT,
				new Insets(1))));
	}

	@FXML
	private void delete() throws IOException {

		if(selectedBox == null){
			return;
		}

		String str = ((Label)selectedBox.getChildren().get(0)).getText();

		if(safe){
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION, map.get("alert"));
			alert.setHeaderText(map.get("alertHeader")+ " " + ((Label) selectedBox.getChildren().get(0)).getText()+ ".");
			alert.setTitle(map.get("alertTitle"));

			DialogPane dp = alert.getDialogPane();
			dp.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
			dp.getStyleClass().add("myDialog");

			Optional<ButtonType> result = alert.showAndWait();
			if(result.isPresent() && result.get() == ButtonType.OK) {
				deleteConfirmed(str);
			}
			return;
		}
		deleteConfirmed(str);
	}

	private void deleteConfirmed(String str) throws IOException {
		p.getCharDB().removeCharacterFromDB(str);
		ProjectFactory.saveProject(p);
		for ( int i = 0 ; i < flowPane.getChildren().size() ; i++ ) {
			if(flowPane.getChildren().get(i).getId().equals(str)) {
				flowPane.getChildren().remove(i);
				selectedBox = null;
				flowPane.requestFocus();
				return;
			}
		}
	}

	private void setupAccelerators(){
		javafx.scene.Scene scene = App.getScene();

		KeyCombination[] obj = scene.getAccelerators().keySet().toArray(new KeyCombination[0]);

		for(KeyCombination kc : obj){
			scene.getAccelerators().remove(kc);
		}

		scene.getAccelerators().put(
				new KeyCodeCombination(KeyCode.DELETE), new Runnable() {
					@FXML public void run() {try {delete();} catch(IOException e){e.printStackTrace();}}});

		scene.getAccelerators().put(
				new KeyCodeCombination(KeyCode.ENTER), new Runnable() {
					@FXML public void run() {try {open();} catch (IOException e) {e.printStackTrace();}}});

		scene.getAccelerators().put(
				new KeyCodeCombination(KeyCode.N), new Runnable() {
					@FXML public void run() {try {goToNewChar();} catch (IOException e) {e.printStackTrace();}}});

		scene.getAccelerators().put(
				new KeyCodeCombination(KeyCode.E), new Runnable() {
					@FXML public void run() {try {edit();} catch (IOException e) {e.printStackTrace();}}});

		scene.getAccelerators().put(
				new KeyCodeCombination(KeyCode.ESCAPE), new Runnable() {
					@FXML public void run() {try {backToDisplay();} catch (IOException e) {e.printStackTrace();}}});

		setupArrowControls();
	}

	private void setupArrowControls() {
		scrollPane.addEventFilter( KeyEvent.KEY_PRESSED, keyEvent -> {
			if( keyEvent.getCode() == KeyCode.UP && !keyEvent.isAltDown() && !keyEvent.isControlDown()
					&& !keyEvent.isMetaDown() && !keyEvent.isShiftDown()
			) {
				if(selectedBox == null && ! flowPane.getChildren().isEmpty()){
					select((VBox)flowPane.getChildren().get(0));
					return;
				}

				double iniX = selectedBox.getLayoutX();
				double iniY = selectedBox.getLayoutY();

				for(Object v : flowPane.getChildren()){
					if(iniX == ((VBox)v).getLayoutX() && iniY > ((VBox)v).getLayoutY() && iniY - ((VBox)v).getLayoutY()  < 400 ){
						select((VBox)v);
						centerNode(keyEvent);
						return;
					}
				}
				keyEvent.consume();
			}
		} );

		scrollPane.addEventFilter( KeyEvent.KEY_PRESSED, keyEvent -> {
			if( keyEvent.getCode() == KeyCode.DOWN && !keyEvent.isAltDown() && !keyEvent.isControlDown()
					&& !keyEvent.isMetaDown() && !keyEvent.isShiftDown()
			) {
				if(selectedBox == null && ! flowPane.getChildren().isEmpty()){
					select((VBox)flowPane.getChildren().get(0));
					return;
				}

				double iniX = selectedBox.getLayoutX();
				double iniY = selectedBox.getLayoutY();

				for(Object v : flowPane.getChildren()){
					if(iniX == ((VBox)v).getLayoutX() && iniY < ((VBox)v).getLayoutY() && ((VBox)v).getLayoutY() - iniY < 400){
						select((VBox)v);
						centerNode(keyEvent);
						return;
					}
				}
				keyEvent.consume();
			}
		} );
		scrollPane.addEventFilter( KeyEvent.KEY_PRESSED, keyEvent -> {
			if( keyEvent.getCode() == KeyCode.LEFT && !keyEvent.isAltDown() && !keyEvent.isControlDown()
					&& !keyEvent.isMetaDown() && !keyEvent.isShiftDown()
			) {
				if(selectedBox == null && ! flowPane.getChildren().isEmpty()){
					select((VBox)flowPane.getChildren().get(0));
					return;
				}

				double iniX = selectedBox.getLayoutX();
				double iniY = selectedBox.getLayoutY();

				for(Object v : flowPane.getChildren()){

					if(iniX > ((VBox)v).getLayoutX() && iniY == ((VBox)v).getLayoutY() && iniX - ((VBox)v).getLayoutX() < 250){
						select((VBox)v);
						centerNode(keyEvent);
						return;
					}
				}
				centerNode(keyEvent);
			}
		} );
		scrollPane.addEventFilter( KeyEvent.KEY_PRESSED, keyEvent -> {
			if( keyEvent.getCode() == KeyCode.RIGHT && !keyEvent.isAltDown() && !keyEvent.isControlDown()
					&& !keyEvent.isMetaDown() && !keyEvent.isShiftDown()
			) {
				if(selectedBox == null && ! flowPane.getChildren().isEmpty()){
					select((VBox)flowPane.getChildren().get(0));
					return;
				}

				double iniX = selectedBox.getLayoutX();
				double iniY = selectedBox.getLayoutY();

				for(Object v : flowPane.getChildren()){
					if(iniX < ((VBox)v).getLayoutX() && iniY == ((VBox)v).getLayoutY() && ((VBox)v).getLayoutX() - iniX < 250){
						select((VBox)v);
						centerNode(keyEvent);
						return;
					}
				}
				keyEvent.consume();
			}
		} );
	}

	private void centerNode(KeyEvent keyEvent) {
		double h = scrollPane.getContent().getBoundsInLocal().getHeight();
		double y = (selectedBox.getBoundsInParent().getMaxY() +
				selectedBox.getBoundsInParent().getMinY()) / 2.0;
		double v = scrollPane.getViewportBounds().getHeight();
		scrollPane.setVvalue(scrollPane.getVmax() * ((y - 0.5 * v) / (h - v)));
		keyEvent.consume();
	}
}
