package bookplanner.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;


public class Chapter implements Serializable{

	@Serial
	private static final long serialVersionUID = 1L;
	private String chapterName;
	private String chapterDescription;
	private LinkedList <Scene> scenes = new LinkedList<>();

	public Chapter(String chapterName, String chapterDescription) {
		this.chapterName=chapterName;
		this.chapterDescription=chapterDescription;
	}

	public String getChapterName() {
		return chapterName;
	}
	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}

	public String getChapterDescription() {
		return chapterDescription;
	}
	public void setChapterDescription(String chapterDescription) {
		this.chapterDescription = chapterDescription;
	}

	public LinkedList<Scene> getScenes() {
		return scenes;
	}

	public Scene searchForScene(String s) {
		if(scenes.isEmpty()){return null;}
		for (Scene ss : scenes) {
			if(ss.getSceneName().equals(s)) {
				return ss;
			}
		}
		return null;
	}
	
	public void addScene(Scene s) {
		scenes.add(s);
	}

	public void addSceneAt(int index, Scene temp) {
		scenes.add(index, temp);
	}

	public void deleteScene(String s) {
		if(scenes.isEmpty()){return;}
		for(Scene ss : scenes) {
			if(ss.getSceneName().equals(s)) {
				scenes.remove(ss);
				return;
			}
		}
	}

	@Override
	public String toString() {
		return this.chapterName;
	}


}
