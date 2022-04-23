package bookplanner.data;

import java.io.Serial;
import java.io.Serializable;

public class Scene implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	private String sceneName;
	private String sceneDescription;

	public Scene(String sceneName, String sceneDescription) {
		this.sceneDescription=sceneDescription;
		this.sceneName=sceneName;
	}

	public String getSceneDescription() {
		return sceneDescription;
	}
	public void setSceneDescription(String sceneDescription) {
		this.sceneDescription = sceneDescription;
	}

	public String getSceneName() {
		return sceneName;
	}
	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}

	@Override
	public String toString() {
		return this.sceneName;
	}
}
