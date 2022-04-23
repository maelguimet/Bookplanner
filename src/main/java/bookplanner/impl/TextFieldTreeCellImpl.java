package bookplanner.impl;

import bookplanner.controllers.DisplayProjectController;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public final class TextFieldTreeCellImpl extends TreeCell<String> {
	
    private TextField textField;
    private static final String username=System.getProperty("user.name");
    private static final String projectPath="C:/Users/"+username+"/Documents/BookPlanner/projects/";

	public TextFieldTreeCellImpl() {
        setStyle("-fx-indent: 20;");
        setWrapText(true);
        setPrefWidth(20);
    }

    @Override
    public void startEdit() {
        super.startEdit();

        if (textField == null) {
            createTextField();
        }
        setText(null);
        setGraphic(textField);
        textField.requestFocus();
        textField.selectAll();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem());
        setGraphic(getTreeItem().getGraphic());
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
   
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());
                setGraphic(getTreeItem().getGraphic());       
            }
        }
    }

    private void createTextField() {
        textField = new TextField(getString());
        textField.setOnKeyReleased(t -> {
            if (t.getCode() == KeyCode.ENTER) {
                DisplayProjectController.selectedItem = new TreeItem<>(textField.getText());
                if(textField.getText().equals("")){
                    cancelEdit();
                    return;
                }

                if(DisplayProjectController.isRoot(getTreeItem())){
                    for(String s : getProjectNames()){
                        if(s.equals(textField.getText())){
                            cancelEdit();
                            return;
                        }
                    }
                }else if(DisplayProjectController.isChapter(getTreeItem())){
                    for(TreeItem <String> s : getTreeItem().getParent().getChildren()){
                        if(s.getValue().equals(textField.getText())){
                            cancelEdit();
                            return;
                        }
                    }
                }else if(DisplayProjectController.isScene(getTreeItem())){
                    for(TreeItem <String> s : getTreeItem().getParent().getChildren()){
                        if(s.getValue().equals(textField.getText())){
                            cancelEdit();
                            return;
                        }
                    }
                }
                commitEdit(textField.getText());
                try {
                    DisplayProjectController.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (t.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });
    }

    private LinkedList<String> getProjectNames() {
        File f = new File(projectPath);
        String[] projects = f.list();
        assert projects != null;
        LinkedList<String> listProjects = new LinkedList<>(Arrays.asList(projects));

        for(int i = 0 ; i < listProjects.size() ; i++) {
            String temp = listProjects.get(i);
            int size = temp.length();
            temp = temp.substring(0, size-4);
            //noinspection SuspiciousListRemoveInLoop
            listProjects.remove(i);
            listProjects.add(i, temp);
        }
        return listProjects;
    }

    private String getString() {
        return getItem() == null ? "" : getItem();
    }
}