package bookplanner.data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class Language {

    private static HashMap <String, String> map;
    private static final String username=System.getProperty("user.name");
    private static final String propertiesPath = "C:/Users/"+username+"/Documents/BookPlanner/app.properties";
    private static int language = 0;

    private static Language singleton = null;

    private Language(){

    }

    public static Language getInstance(){
        if(singleton == null){
            singleton = new Language();
        }
        return singleton;
    };

    public static HashMap<String, String> mainMenu() throws IOException {
        getLanguage();

        LinkedList <String> text = new LinkedList<>();
        text.add("new");
        text.add("load");
        text.add("set");
        text.add("exit");

        LinkedList <String> lang = new LinkedList<>();
        if(language == 0){
            //english
            lang.add("NEW PROJECT");
            lang.add("LOAD PROJECT");
            lang.add("SETTINGS");
            lang.add("EXIT");
        }else if(language == 1){
            //french
            lang.add("NOUVEAU PROJET");
            lang.add("CHARGER UN PROJET");
            lang.add("PARAMÈTRES");
            lang.add("QUITTER");
        }

        for( int i = 0 ; i < text.size() ; i++ ){
            map.put(text.get(i), lang.get(i));
        }

        return map;
    }

    public static HashMap<String, String> newProject() throws IOException {
        getLanguage();

        LinkedList <String> text = new LinkedList<>();
        text.add("1stPrompt");
        text.add("2ndPrompt");
        text.add("validate");
        text.add("cancel");
        text.add("img");
        text.add("error");
        text.add("confirmation");

        LinkedList <String> lang = new LinkedList<>();
        if(language == 0){
            //english
            lang.add("Project Title");
            lang.add("Project Description");
            lang.add("CREATE");
            lang.add("CANCEL");
            lang.add("newprojecteng.png");
            lang.add("Incorrect title");
            lang.add("Are you sure you want to delete that ?");
        }else if(language == 1){
            //french
            lang.add("Titre du Projet");
            lang.add("Description du Projet");
            lang.add("CONFIRMER");
            lang.add("ANNULER");
            lang.add("newprojectfr.png");
            lang.add("Titre Incorrect");
            lang.add("Voulez-vous vraiment faire ça ?");
        }

        for( int i = 0 ; i < text.size() ; i++ ){
            map.put(text.get(i), lang.get(i));
        }

        return map;
    }

    public static HashMap<String, String> loadProject() throws IOException {
        getLanguage();

        LinkedList <String> text = new LinkedList<>();
        text.add("new");
        text.add("load");
        text.add("cancel");
        text.add("delete");
        text.add("img");
        text.add("confirmation");
        text.add("alertHeader");
        text.add("alertTitle");
        LinkedList <String> lang = new LinkedList<>();
        if(language == 0){
            //english
            lang.add("NEW");
            lang.add("LOAD");
            lang.add("CANCEL");
            lang.add("DELETE");
            lang.add("loadprojecteng.png");
            lang.add("Are you sure you want to delete that ?");
            lang.add("You're about to delete");
            lang.add("Delete confirmation pannel");
        }else if(language == 1){
            //french
            lang.add("NOUVEAU");
            lang.add("CHARGER");
            lang.add("ANNULER");
            lang.add("SUPPRIMER");
            lang.add("loadprojectfr.png");
            lang.add("Voulez-vous vraiment faire ça ?");
            lang.add("Vous êtes sur le point de supprimer");
            lang.add("Confirmation de suppression");
        }

        for( int i = 0 ; i < text.size() ; i++ ){
            map.put(text.get(i), lang.get(i));
        }

        return map;
    }

    public static HashMap<String, String> settings() throws IOException {
        getLanguage();

        LinkedList <String> text = new LinkedList<>();
        text.add("confirm");
        text.add("cancel");
        text.add("checkbox");
        text.add("img");
        text.add("tooltip");


        LinkedList <String> lang = new LinkedList<>();
        if(language == 0){
            //english
            lang.add("CONFIRM");
            lang.add("CANCEL");
            lang.add("Safe Mode");
            lang.add("settingseng.png");
            lang.add("If selected, a confirmation message will pop before any deletion");
        }else if(language == 1){
            //french
            lang.add("VALIDER");
            lang.add("ANNULER");
            lang.add("Mode sûr");
            lang.add("settingsfr.png");
            lang.add("Ajoute un écran de confirmation en cas de tentative de suppression");
        }

        for( int i = 0 ; i < text.size() ; i++ ){
            map.put(text.get(i), lang.get(i));
        }
        return map;
    }

    public static HashMap<String, String> displayProject() throws IOException {
        getLanguage();

        LinkedList <String> text = new LinkedList<>();
        text.add("charDB");
        text.add("newChar");
        text.add("menu");
        text.add("export");
        text.add("description");
        text.add("newScene");
        text.add("newChapter");
        text.add("delete");
        text.add("alert");
        text.add("dupe");
        text.add("alertHeader");
        text.add("alertTitle");
        LinkedList <String> lang = new LinkedList<>();
        if(language == 0){
            //english
            lang.add("CHARACTER DATABASE");
            lang.add("NEW CHARACTER");
            lang.add("MAIN MENU");
            lang.add("EXPORT");
            lang.add("Description");
            lang.add("New scene");
            lang.add("New chapter");
            lang.add("Delete");
            lang.add("Are you sure ?");
            lang.add("(Duplicate)");
            lang.add("You're about to delete");
            lang.add("Delete confirmation pannel");
        }else if(language == 1){
            //french
            lang.add("PERSONNAGES");
            lang.add("NOUVEAU PERSONNAGE");
            lang.add("MENU PRINCIPAL");
            lang.add("EXPORTER");
            lang.add("Description");
            lang.add("Nouvelle scène");
            lang.add("Nouveau chapitre");
            lang.add("Supprimer");
            lang.add("Voulez vous vraiment faire ça ?");
            lang.add("(Doublon)");
            lang.add("Vous êtes sur le point de supprimer");
            lang.add("Confirmation de suppression");
        }
        for( int i = 0 ; i < text.size() ; i++ ){
            map.put(text.get(i), lang.get(i));
        }
        return map;
    }

    public static HashMap<String, String> newCharacter() throws IOException {
        getLanguage();

        LinkedList <String> text = new LinkedList<>();
        text.add("create");
        text.add("cancel");
        text.add("selectPicture");

        text.add("name");
        text.add("nickname");
        text.add("phys");
        text.add("moral");
        text.add("weakness");
        text.add("notes");

        text.add("nameBox");
        text.add("nicknameBox");
        text.add("physBox");
        text.add("moralBox");
        text.add("weaknessBox");
        text.add("notesBox");
        LinkedList <String> lang = new LinkedList<>();
        if(language == 0){
            //english
            lang.add("CREATE");
            lang.add("CANCEL");
            lang.add("SELECT PICTURE");

            lang.add("Name");
            lang.add("Nickname");
            lang.add("Physical description");
            lang.add("Moral description");
            lang.add("Weakness");
            lang.add("Notes");

            lang.add("Name");
            lang.add("Nickname");
            lang.add("Physical description");
            lang.add("Moral description");
            lang.add("Weakness");
            lang.add("Notes");
        }else if(language == 1){
            //french
            lang.add("CRÉER");
            lang.add("ANNULER");
            lang.add("CHOISIR UNE IMAGE");

            lang.add("Nom");
            lang.add("Surnom");
            lang.add("Description physique");
            lang.add("Description morale");
            lang.add("Faille");
            lang.add("Notes");

            lang.add("Nom");
            lang.add("Surnom");
            lang.add("Description physique");
            lang.add("Description morale");
            lang.add("Faille");
            lang.add("Notes");
        }
        for( int i = 0 ; i < text.size() ; i++ ){
            map.put(text.get(i), lang.get(i));
        }
        return map;
    }

    public static HashMap<String, String> charDB() throws IOException {
        getLanguage();

        LinkedList <String> text = new LinkedList<>();
        text.add("new");
        text.add("back");
        text.add("edit");
        text.add("delete");
        text.add("img");
        text.add("alert");
        text.add("editButton");
        text.add("deleteButton");
        text.add("alertHeader");
        text.add("alertTitle");
        LinkedList <String> lang = new LinkedList<>();
        if(language == 0){
            //english
            lang.add("NEW");
            lang.add("BACK");
            lang.add("Edit");
            lang.add("Delete");
            lang.add("characterseng.png");
            lang.add("Are you sure ?");
            lang.add("EDIT");
            lang.add("DELETE");
            lang.add("You're about to delete");
            lang.add("Delete confirmation pannel");
        }else if(language == 1){
            //french
            lang.add("CRÉER");
            lang.add("RETOUR");
            lang.add("Éditer");
            lang.add("Supprimer");
            lang.add("charactersfr.png");
            lang.add("Voulez-vous vraiment faire ça ?");
            lang.add("EDITER");
            lang.add("SUPPRIMER");
            lang.add("Vous êtes sur le point de supprimer");
            lang.add("Confirmation de suppression");
        }
        for( int i = 0 ; i < text.size() ; i++ ){
            map.put(text.get(i), lang.get(i));
        }
        return map;
    }

    public static HashMap<String, String> charProfile() throws IOException {
        getLanguage();

        LinkedList <String> text = new LinkedList<>();
        text.add("physDescription");
        text.add("moralDescription");
        text.add("weakness");
        text.add("notes");
        text.add("charactersButton");
        text.add("editButton");
        LinkedList <String> lang = new LinkedList<>();
        if(language == 0){
            //english
            lang.add("Physical description");
            lang.add("Moral description");
            lang.add("Weakness");
            lang.add("Notes");
            lang.add("BACK");
            lang.add("EDIT");
        }else if(language == 1){
            //french
            lang.add("Description physique");
            lang.add("Description morale");
            lang.add("Faille");
            lang.add("Notes");
            lang.add("RETOUR");
            lang.add("ÉDITER");
        }
        for( int i = 0 ; i < text.size() ; i++ ){
            map.put(text.get(i), lang.get(i));
        }
        return map;
    }

    public static HashMap<String, String> charEdit() throws IOException {
        getLanguage();

        LinkedList <String> text = new LinkedList<>();
        text.add("confirm");
        text.add("cancel");
        text.add("picture");

        text.add("nameBox");
        text.add("nicknameBox");
        text.add("physBox");
        text.add("moralBox");
        text.add("weaknessBox");
        text.add("notesBox");

        text.add("name");
        text.add("nickname");
        text.add("phys");
        text.add("moral");
        text.add("weak");
        text.add("notes");
        LinkedList <String> lang = new LinkedList<>();
        if(language == 0){
            //english
            lang.add("CONFIRM");
            lang.add("CANCEL");
            lang.add("SELECT PICTURE");

            lang.add("Name");
            lang.add("Nickname");
            lang.add("Physical description");
            lang.add("Moral description");
            lang.add("Weakness");
            lang.add("Notes");

            lang.add("Name");
            lang.add("Nickname");
            lang.add("Physical description");
            lang.add("Moral description");
            lang.add("Weakness");
            lang.add("Notes");
        }else if(language == 1){
            //french
            lang.add("CONFIRMER");
            lang.add("ANNULER");
            lang.add("CHOISIR UNE IMAGE");

            lang.add("Nom");
            lang.add("Surnom");
            lang.add("Description physique");
            lang.add("Description morale");
            lang.add("Faille");
            lang.add("Notes");

            lang.add("Nom");
            lang.add("Surnom");
            lang.add("Description physique");
            lang.add("Description morale");
            lang.add("Faille");
            lang.add("Notes");
        }
        for( int i = 0 ; i < text.size() ; i++ ){
            map.put(text.get(i), lang.get(i));
        }
        return map;
    }

    public static boolean getSafeMode() throws IOException {
        File f = new File(propertiesPath);
        Scanner reader = new Scanner(f);
        String [] str = new String[0];
        while(reader.hasNextLine()){
            str = reader.nextLine().split("=");
        }
        if (str[1].equals("false")){
            reader.close();
            return false;
        }
        reader.close();
        return true;
    }

    public static int getLanguageId() throws IOException {
        getLanguage();
        return language;
    }

    public static void getLanguage() throws IOException {
        map = new HashMap<>();
        File f = new File(propertiesPath);
        Scanner reader = new Scanner(f);
        String [] str;
        str=reader.nextLine().split("=");
        language = Integer.parseInt(str[1]);
        reader.close();
    }
}
