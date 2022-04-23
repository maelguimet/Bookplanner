package bookplanner.data;

import java.io.Serial;
import java.io.Serializable;

public class Character implements Serializable, Comparable {

	@Serial
	private static final long serialVersionUID = 1L;
	private String charName;
	private String charNickname;
	private String charDescP;
	private String charDescM;
	private String charWeakness ;
	private String charNotes;
	private String path;

	public Character(String charName, String charNickname, String charDescP, String charDescM, String charWeakness, String charNotes, String path) {
		this.charName = charName;
		this.charNickname = charNickname;
		this.charDescP = charDescP;
		this.charDescM = charDescM;
		this.charWeakness = charWeakness;
		this.charNotes = charNotes;
		this.path = path;
	}

	public String getCharName() {
		return charName;
	}
	public void setCharName(String charName) {
		this.charName = charName;
	}

	public String getCharNickname() {
		return charNickname;
	}
	public void setCharNickname(String charNickname) {
		this.charNickname = charNickname;
	}

	public String getCharDescP() {
		return charDescP;
	}
	public void setCharDescP(String charDescP) {
		this.charDescP = charDescP;
	}

	public String getCharDescM() {
		return charDescM;
	}
	public void setCharDescM(String charDescM) {
		this.charDescM = charDescM;
	}

	public String getCharWeakness() {
		return charWeakness;
	}
	public void setCharWeakness(String charRift) {
		this.charWeakness = charRift;
	}

	public String getCharNotes() {
		return charNotes;
	}
	public void setCharNotes(String charNotes) {
		this.charNotes = charNotes;
	}

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return this.charName;
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}
}
