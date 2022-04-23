package bookplanner.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;

public class CharacterDB implements Serializable{

	@Serial
	private static final long serialVersionUID = 1L;
	private LinkedList <Character> characters;
	
	public CharacterDB() {}

	public LinkedList<Character> getCharacters() {
		return orderedList();
	}

	private LinkedList<Character> orderedList() {

		LinkedList <Character> temp = new LinkedList<>();
		if(characters == null || characters.isEmpty()){return temp;}

		LinkedList <String> names = new LinkedList<>();

		for(Character c : characters){
			names.add(c.getCharName());
		}

		java.util.Collections.sort(names);

		for(String s : names){
			temp.add(searchForCharacter(s));
		}

		return temp;
	}

	public void addCharacterToDB(Character c) {
		if(characters == null){
			characters = new LinkedList<>();
		}
		characters.add(c);
	}

	public Character searchForCharacter(String s) {
		for(Character c : characters) {
			if(c.getCharName().equals(s)){
				return c;
			}
		}
		return null;
	}

	public void editCharacter(String inputName, String charName, String charNickname, String charDescP, String charDescM, String charRift, String charNotes, String path) {

		if(characters == null || characters.isEmpty()){
			return;
		}

		for (Character character : characters) {
			if (character.getCharName().equals(inputName)) {
				character.setCharName(charName);
				character.setCharNickname(charNickname);
				character.setCharDescP(charDescP);
				character.setCharDescM(charDescM);
				character.setCharWeakness(charRift);
				character.setCharNotes(charNotes);
				character.setPath(path);
				return;
			}
		}
	}
	
	public void removeCharacterFromDB(String s) {
		characters.removeIf(c -> c.getCharName().equals(s));
	}	
}
