package bookplanner.controllers;

import bookplanner.application.App;
import bookplanner.impl.TextFieldTreeCellImpl;
import bookplanner.data.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class DisplayProjectController implements Initializable{

	private static HashMap<String, String> map = new HashMap<>();
	private static final String username=System.getProperty("user.name");
    private static BookProject liveProject;

    private @FXML HBox mainTabRoot;
    private @FXML Label title;
    private @FXML TextArea descriptionBox;
    private @FXML TreeView <String> treeView;
    private @FXML ContextMenu contextMenu;
    private @FXML Button collapse;
    private @FXML Button charDBButton;
    private @FXML Button newCharButton;
    private @FXML Button menuButton;
    private @FXML Button exportButton;

    public static TreeItem <String> selectedItem;
    private TreeItem <String> oldSelectedItem;
    private TreeItem <String> dragged;
    private TreeItem <String> draggedOn;

    private String currentProjectName;
    private boolean safeModeIsOn =false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
        try {onOpen();} catch (IOException | ClassNotFoundException e){e.printStackTrace();}
    }

	@FXML
    private void switchToMenu() throws IOException {
		save();
        App.setRoot("MainMenu");
    }

	@FXML 
	private void goToCharDB() throws IOException {
        save();
		App.setRoot("CharacterDB");
	}

	@FXML
	private void goToNewChar() throws IOException {
        save();
		App.setRoot("CharacterNew");
	}
	
	private void getProjectName() throws FileNotFoundException {
		String projectNamePath="C:/Users/"+username+"/Documents/BookPlanner/currentProjectName.txt";
		File f=new File(projectNamePath);
        if(f.exists()) {
            Scanner reader = new Scanner(f);
            if (reader.hasNextLine()) {
                currentProjectName = reader.nextLine();
            }
            reader.close();
        }
	}
	
	@FXML
	private void updateUI() {
		generateTree();
		updateSelectedItem();
		title.setText(liveProject.getProjectName());
		descriptionBox.setText(liveProject.getProjectDescription());
	}

    private void loseFocus() {
        mainTabRoot.setOnMouseClicked(event -> treeView.requestFocus());
    }

    private void onOpen() throws IOException, ClassNotFoundException {
        getProjectName();
        liveProject=ProjectFactory.loadProject(currentProjectName);
        loseFocus();
        updateUI();
        setupAccelerators();
        treeView.getSelectionModel().select(treeView.getRoot());
        selectedItem = treeView.getRoot();
        handleLanguage();
    }

	private void generateTree() {
		TreeItem <String> bookProject = new TreeItem<>(liveProject.getProjectName());

		for(Chapter c : liveProject.getChapters()) {

			TreeItem <String> chapter = new TreeItem<>(c.getChapterName());

			for(Scene s : c.getScenes()) {
				TreeItem <String> scene = new TreeItem<>(s.getSceneName());
				chapter.getChildren().add(scene);
				chapter.setExpanded(true);
			}
			bookProject.getChildren().add(chapter);
		}	
		bookProject.setExpanded(true);
		treeView.setRoot(bookProject);
        treeView.setEditable(true);
        
		treeView.setOnKeyPressed(event -> {
            if(selectedItem != null && event.getCode() == KeyCode.ADD){

                if(isRoot(selectedItem)){
                    try {newChapter();}catch(IOException e){e.printStackTrace();}
                }else if(isChapter(selectedItem)){
                    try {newScene();}catch(IOException e){e.printStackTrace();}
                }else if(isScene(selectedItem)){
                    try {newScene();}catch(IOException e){e.printStackTrace();}
                }
                try {save();} catch (IOException e) {e.printStackTrace();}
                updateSelectedItem();
                return;
            }
            updateSelectedItem();});

			treeView.setCellFactory(p -> {
				  TextFieldTreeCellImpl tf = new TextFieldTreeCellImpl();
                  
				  tf.setOnMouseEntered(event -> {

                      if(tf.isEmpty()){return;}

                      Tooltip t = new Tooltip();
                      t.setMaxWidth(500);
                      t.setWrapText(true);

                      if(isRoot(tf.getTreeItem())){
                          if (liveProject.getProjectDescription().equals("")) {return;}
                          t.setText(liveProject.getProjectDescription());
                      }else if(isScene(tf.getTreeItem())){
                          if (liveProject.searchForChapter(tf.getTreeItem().getParent().getValue()).searchForScene(tf.getTreeItem().getValue()).getSceneDescription().equals("")) {return;}
                          t.setText(liveProject.searchForChapter(tf.getTreeItem().getParent().getValue()).searchForScene(tf.getTreeItem().getValue()).getSceneDescription());
                      }else if(isChapter(tf.getTreeItem())){
                          if (liveProject.searchForChapter(tf.getTreeItem().getValue()).getChapterDescription().equals("")) {return;}
                          t.setText(liveProject.searchForChapter(tf.getTreeItem().getValue()).getChapterDescription());
                      }
                      tf.setTooltip(t);
                  });

				  tf.setOnDragDetected(event -> {
                      if (tf.isEmpty()){
                          return;
                      }
                      
					  dragged = tf.getTreeItem();
					  if(dragged.getParent() == null) {return;}
					  // root can't be dragged

					  Dragboard db = tf.startDragAndDrop(TransferMode.MOVE);						 
					  ClipboardContent content = new ClipboardContent();
					  content.putString(dragged.getValue());
					  db.setContent(content);
					  db.setDragView(tf.snapshot(null, null));
					  event.consume();
				  });
				  
				  tf.setOnDragOver(event -> {
					  if (!event.getDragboard().hasString()) {
						  return;
					  }
					  
					 draggedOn = tf.getTreeItem();			   
				   
					  // can't drop on itself
					  if (dragged == null || draggedOn == null || draggedOn == dragged) {
						  return;
					  }
					  event.acceptTransferModes(TransferMode.MOVE);		  
				  });

				  tf.setOnDragDropped(event -> {
                    //This is awful
					  if(isScene(draggedOn)) {
						  
						  int draggedOnChapterIndex = draggedOn.getParent().getParent().getChildren().indexOf(draggedOn.getParent());
						  int draggedOnSceneIndex = draggedOn.getParent().getChildren().indexOf(draggedOn);
						  int draggedChapterIndex = dragged.getParent().getParent().getChildren().indexOf(dragged.getParent());
						  int draggedSceneIndex = dragged.getParent().getChildren().indexOf(dragged);
						  String draggedChapter = dragged.getParent().getValue();
						  String draggedOnChapter = draggedOn.getParent().getValue();
						  
						  if(isScene(dragged)){
							  //drop scene on scene						    	
							  Scene temp = liveProject.searchForChapter(draggedChapter).searchForScene(dragged.getValue());//copies scene to move
							  liveProject.searchForChapter(draggedChapter).deleteScene(dragged.getValue());//deletes scene from source
							  liveProject.searchForChapter(draggedOnChapter).addSceneAt(draggedOnSceneIndex, temp);//adds temp scene where it's supposed to

							  treeView.getRoot().getChildren().get(draggedChapterIndex).getChildren().remove(draggedSceneIndex);
							  treeView.getRoot().getChildren().get(draggedOnChapterIndex).getChildren().add(draggedOnSceneIndex, dragged);
							  treeView.getSelectionModel().select(treeView.getRoot().getChildren().get(draggedOnChapterIndex).getChildren().get(draggedOnSceneIndex));

						  }
					  }else if(isChapter(draggedOn)) {

						  if(isChapter(dragged)) {
							  int draggedChapterIndex = dragged.getParent().getChildren().indexOf(dragged);
							  int draggedOnChapterIndex = draggedOn.getParent().getChildren().indexOf(draggedOn);
							  //drop chap on chap
							  //dropped chap takes droppedon chap place
							  Chapter temp = liveProject.searchForChapter(dragged.getValue());//stores dragged chapter
							  liveProject.deleteChapter(dragged.getValue());//removes dragged chapter
							  liveProject.addChapterAt(draggedOnChapterIndex, temp);
							  
							  treeView.getRoot().getChildren().remove(draggedChapterIndex);
							  treeView.getRoot().getChildren().add(draggedOnChapterIndex, dragged);
							  treeView.getSelectionModel().select(treeView.getRoot().getChildren().get(draggedOnChapterIndex));
							  
						  }else if(isScene(dragged)){
							  int draggedChapterIndex = dragged.getParent().getParent().getChildren().indexOf(dragged.getParent());
							  int draggedOnChapterIndex = draggedOn.getParent().getChildren().indexOf(draggedOn);
							  
							  Scene temp = liveProject.searchForChapter(dragged.getParent().getValue()).searchForScene(dragged.getValue());
							  liveProject.searchForChapter(dragged.getParent().getValue()).deleteScene(dragged.getValue());
							  liveProject.searchForChapter(draggedOn.getValue()).addScene(temp);
							  
							  treeView.getRoot().getChildren().get(draggedChapterIndex).getChildren().remove(dragged.getParent().getChildren().indexOf(dragged));	
							  treeView.getRoot().getChildren().get(draggedOnChapterIndex).getChildren().add(dragged);
							  int size = treeView.getRoot().getChildren().get(draggedOnChapterIndex).getChildren().size();
							  treeView.getSelectionModel().select(treeView.getRoot().getChildren().get(draggedOnChapterIndex).getChildren().get(size-1));
						  }
					  }
					  
				  });
				  
				  tf.setOnDragDone(event -> {try{save();} catch (IOException e){e.printStackTrace();}});

				  return tf ;
			 });
	}

	@FXML 
	private void setUpContextMenu() {
		MenuItem addScene = new MenuItem(map.get("newScene"));
		MenuItem addChapter = new MenuItem(map.get("newChapter"));
		MenuItem delete = new MenuItem(map.get("delete"));

		contextMenu.getItems().remove(0, contextMenu.getItems().size());
		
		addScene.setOnAction(e -> {try {newScene();} catch (IOException ex) {ex.printStackTrace();}});
		
		addChapter.setOnAction(e -> {try {newChapter();} catch (IOException ex) {ex.printStackTrace();}});
		
		delete.setOnAction(e -> {try {delete();} catch (IOException ex) {ex.printStackTrace();}});
		
		if(isScene(selectedItem)) {
			contextMenu.getItems().addAll(addScene, addChapter, delete);
		}else if (isChapter(selectedItem)) {
			contextMenu.getItems().addAll(addScene, addChapter, delete);
		}else if (isRoot(selectedItem)) {
			contextMenu.getItems().addAll(addChapter);
		}	
	}

	private void handleLanguage() throws IOException {
		map = Language.getInstance().displayProject();
		safeModeIsOn = Language.getSafeMode();
		charDBButton.setText(map.get("charDB"));
		newCharButton.setText(map.get("newChar"));
		menuButton.setText(map.get("menu"));
		exportButton.setText(map.get("export"));
		descriptionBox.setPromptText(map.get("description"));
	}

	private void newScene() throws IOException {
		
		TreeItem <String> temp = treeView.getSelectionModel().getSelectedItem();
		
		if(isScene(temp)) {
			int chapterIndex = temp.getParent().getParent().getChildren().indexOf(temp.getParent());
			int sceneIndex = temp.getParent().getChildren().indexOf(temp)+1;

			String name = map.get("newScene")+ " " +sceneIndex;
			name = checkSceneName(temp, name);

			liveProject.getChapters().get(chapterIndex).addSceneAt(sceneIndex, new Scene(name, ""));
			treeView.getRoot().getChildren().get(chapterIndex).getChildren().add(sceneIndex, new TreeItem<>(name));
			treeView.getSelectionModel().select(treeView.getRoot().getChildren().get(chapterIndex).getChildren().get(sceneIndex));
			
			return;
		}
		if(isChapter(temp)){
			int chapterIndex = temp.getParent().getChildren().indexOf(temp);
			int size = temp.getChildren().size();

			String name = map.get("newScene")+ " " +size;
			try {
				name = checkSceneName(temp.getChildren().get(0), name);
			}catch (Exception e){}

			liveProject.getChapters().get(chapterIndex).addScene(new Scene(name, ""));
			treeView.getRoot().getChildren().get(chapterIndex).getChildren().add(new TreeItem<>(name));
			treeView.getSelectionModel().select(treeView.getRoot().getChildren().get(chapterIndex).getChildren().get(treeView.getRoot().getChildren().get(chapterIndex).getChildren().size()-1));
		}
		save();
		updateSelectedItem();
	}
	
	private void newChapter() throws IOException {

		TreeItem <String> temp = treeView.getSelectionModel().getSelectedItem();

		if(isRoot(temp)) {

			String name = map.get("newChapter")+ " " +temp.getChildren().size();
            name = checkChapterName(temp, name);

			liveProject.addChapter(new Chapter(name, ""));
			treeView.getRoot().getChildren().add(new TreeItem<>(name));
			treeView.getSelectionModel().select(treeView.getRoot().getChildren().get(treeView.getRoot().getChildren().size()-1));

            return;

		}else if (isChapter(temp)) {

			String name = map.get("newChapter")+ " " +temp.getChildren().size();
            name = checkChapterName(temp.getParent(), name);

			int chapterIndex = temp.getParent().getChildren().indexOf(temp);
			liveProject.addChapterAt(chapterIndex, new Chapter(name, ""));
			treeView.getRoot().getChildren().add(chapterIndex, new TreeItem<>(name));
			treeView.getSelectionModel().select(treeView.getRoot().getChildren().get(chapterIndex+1));

            return;

		}else if (isScene(temp)) {

			int chapterIndex = temp.getParent().getParent().getChildren().indexOf(temp.getParent());
			String name = map.get("newChapter")+ " " +chapterIndex;
            name = checkChapterName(temp.getParent().getParent(), name);

			liveProject.getChapters().add(chapterIndex, new Chapter (name, ""));
			treeView.getRoot().getChildren().add(chapterIndex, new TreeItem<>(name));
			treeView.getSelectionModel().select(treeView.getRoot().getChildren().get(chapterIndex));

            return;

		}
		save();
		updateSelectedItem();
	}
	
	private void delete() throws IOException {

		TreeItem <String> target;

        if(treeView.getSelectionModel().getSelectedItem() == null){
          return;
        }

        target = treeView.getSelectionModel().getSelectedItem();

		if(isRoot(target)){
			return;
		}
		
		if(safeModeIsOn) {
			Alert alert = new Alert(AlertType.CONFIRMATION, map.get("alert"));
			alert.setHeaderText(map.get("alertHeader")+ " " + treeView.getSelectionModel().getSelectedItem().getValue()+ ".");
			alert.setTitle(map.get("alertTitle"));

			DialogPane dp = alert.getDialogPane();
			dp.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
			dp.getStyleClass().add("myDialog");

    		Optional <ButtonType> result = alert.showAndWait();
    		if(result.isPresent() && result.get() == ButtonType.OK) {
				handleObject(isRoot(target), isChapter(target), target, liveProject, treeView, isScene(target));
				save();
			}
    		return;
		}
		handleObject(isRoot(target), isChapter(target), target, liveProject, treeView, isScene(target));
        save();
	}

	private void handleObject(boolean isRoot, boolean isChapter, TreeItem<String> targetedItem, BookProject liveProject, TreeView<String> treeView, boolean isScene) {

		if(isRoot) {return;}

		if(isChapter) {
			int chapterIndex = targetedItem.getParent().getChildren().indexOf(targetedItem);
			liveProject.getChapters().remove(chapterIndex);
			treeView.getRoot().getChildren().remove(chapterIndex);
		}else if (isScene) {
			int chapterIndex = targetedItem.getParent().getParent().getChildren().indexOf(targetedItem.getParent());
			int sceneIndex = targetedItem.getParent().getChildren().indexOf(targetedItem);
			liveProject.getChapters().get(chapterIndex).getScenes().remove(sceneIndex);
			treeView.getRoot().getChildren().get(chapterIndex).getChildren().remove(sceneIndex);
		}
	}

	@FXML
	private void updateSelectedItem() {

        if(treeView.getSelectionModel().getSelectedItem() != null ){
            selectedItem=treeView.getSelectionModel().getSelectedItem();
        }else{
            treeView.getSelectionModel().select(treeView.getRoot());
        }

		if(isScene(selectedItem)) {
			String chapter = selectedItem.getParent().getValue();
			title.setText(liveProject.searchForChapter(chapter).searchForScene(selectedItem.getValue()).getSceneName());
			descriptionBox.setText(liveProject.searchForChapter(chapter).searchForScene(selectedItem.getValue()).getSceneDescription());

		}else if(isChapter(selectedItem)) {
			title.setText(liveProject.searchForChapter(selectedItem.getValue()).getChapterName());
			descriptionBox.setText(liveProject.searchForChapter(selectedItem.getValue()).getChapterDescription());

		}else if(isRoot(selectedItem)) {
			title.setText(liveProject.getProjectName());
			descriptionBox.setText(liveProject.getProjectDescription());
		}
		setUpContextMenu();
	}

    @FXML
    private void getDescriptionBoxText() throws IOException {

        if(treeView.getSelectionModel().getSelectedItem() == null ){
            return;
        }

        if(isScene(selectedItem)) {
            String chapter = selectedItem.getParent().getValue();
            liveProject.searchForChapter(chapter).searchForScene(selectedItem.getValue()).setSceneDescription(descriptionBox.getText());

        }else if(isChapter(selectedItem)) {
            liveProject.searchForChapter(selectedItem.getValue()).setChapterDescription(descriptionBox.getText());

        }else if(isRoot(selectedItem)) {
            liveProject.setProjectDescription(descriptionBox.getText());
        }
        save();
    }

	public static boolean isScene(TreeItem <String> p) {
		try {
			if(p.getParent().getParent().getParent()==null) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public static boolean isChapter(TreeItem <String> p) {
		try {
			if(p.getParent().getParent()==null) {
			//Checks if parent is root
			return true;
			}
		} catch (Exception e) {
			return false;
		}

		return false;
	}

	public static boolean isRoot(TreeItem<String> p) {
		try {
			if(p.getParent()==null) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}

		return false;
	}

	@FXML
	private void updateOldItemName() {
		oldSelectedItem=selectedItem;
	}
	
	@FXML
	private void editCancel() {
		oldSelectedItem=null;
	}
	
	@FXML
	private void updateListOnEdit() throws IOException {
		if(isScene(oldSelectedItem)) {
			String chapter = oldSelectedItem.getParent().getValue();
			liveProject.searchForChapter(chapter).searchForScene(oldSelectedItem.getValue()).setSceneName(selectedItem.getValue());
			title.setText(selectedItem.getValue());
		}else if(isChapter(oldSelectedItem)) {
			liveProject.searchForChapter(oldSelectedItem.getValue()).setChapterName(selectedItem.getValue());
			title.setText(selectedItem.getValue());
		}else if(isRoot(oldSelectedItem)) {
			liveProject.setProjectName(selectedItem.getValue());
			title.setText(selectedItem.getValue());
			renameProject(oldSelectedItem.getValue());
		}		
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
    private void renameProject(String oldName) throws IOException {
		String oldPath="C:/Users/"+username+"/Documents/BookPlanner/projects/"+oldName+".ser";
		File f = new File(oldPath);
		if(ProjectFactory.saveProject(liveProject)){
			f.delete();
		}
	}

	@FXML
	private void collapse() {
		if(treeView.getRoot().isExpanded()){
			treeView.getRoot().setExpanded(false);
			return;
		}
		treeView.getRoot().setExpanded(true);
	}

	private String checkChapterName(TreeItem<String> root, String c){
		String name = c;
		for(TreeItem<String> ti : root.getChildren()){
			if(ti.getValue().equals(name)){
				name = name + " " + map.get("dupe");
				name = checkChapterName(root, name);
			}
		}
		return name;
	}

	private String checkSceneName(TreeItem<String> scene, String c){
		String name = c;
		for(TreeItem<String> ti : scene.getParent().getChildren()){
			if(ti.getValue().equals(name)){
				name = name + " " + map.get("dupe");
                name = checkSceneName(scene, name);
			}
		}
		return name;
	}

	@FXML
	private void export() throws IOException {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File home = FileSystemView.getFileSystemView().getHomeDirectory();
		directoryChooser.setInitialDirectory(home);
		File selectedDirectory = directoryChooser.showDialog(null);

		if(selectedDirectory == null){
			return;
		}
		liveProject.export(selectedDirectory.getAbsolutePath().replace("\\", "/"));
	}

	private void setupAccelerators(){

		javafx.scene.Scene scene = App.getScene();

        KeyCombination[] obj = scene.getAccelerators().keySet().toArray(new KeyCombination[0]);

        for(KeyCombination kc : obj){
            scene.getAccelerators().remove(kc);
        }

        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.DELETE), new Runnable() {
                    @FXML public void run() {
                        try {
                            delete();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

		scene.getAccelerators().put(
				new KeyCodeCombination(KeyCode.N), new Runnable() {
					@FXML public void run() {
						try {
							goToNewChar();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});

		scene.getAccelerators().put(
				new KeyCodeCombination(KeyCode.C), new Runnable() {
					@FXML public void run() {
						try {
							goToCharDB();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});

		scene.getAccelerators().put(
				new KeyCodeCombination(KeyCode.DELETE), new Runnable() {
					@FXML public void run() {
						try {
							delete();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});

		scene.getAccelerators().put(
				new KeyCodeCombination(KeyCode.X), new Runnable() {
					@FXML public void run() {
							collapse.fire();
					}
				});

		treeView.addEventFilter( KeyEvent.KEY_PRESSED, keyEvent -> {
			if( keyEvent.getCode() == KeyCode.ESCAPE && !keyEvent.isAltDown() && !keyEvent.isControlDown()
					&& !keyEvent.isMetaDown() && !keyEvent.isShiftDown()
			) {
				final Parent parent = treeView.getParent();
				parent.fireEvent( keyEvent.copyFor( parent, parent ) );
				keyEvent.consume();
			}
		} );
	}

	public static void save() throws IOException {
		ProjectFactory.saveProject(liveProject);
	}
}
