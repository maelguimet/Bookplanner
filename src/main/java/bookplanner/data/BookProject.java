package bookplanner.data;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class BookProject implements Serializable{

	@Serial
	private static final long serialVersionUID = 1L;
	private String projectName;
	private String projectDescription;
	private LinkedList <Chapter> chapters = new LinkedList<>();
	private CharacterDB charDB= new CharacterDB();

	public BookProject(String projectName, String projectDescription){
		this.projectName = projectName;
		this.projectDescription = projectDescription;
	}

	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectDescription() {
		return projectDescription;
	}
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public LinkedList<Chapter> getChapters() {
		return chapters;
	}

	public CharacterDB getCharDB() {
		return charDB;
	}

	public void deleteChapter(String s) {
		
		for(Chapter c : chapters) {
			if(c.getChapterName().equals(s)) {
				chapters.remove(c);
				return;
			}
		}
	}

	public Chapter searchForChapter(String s) {

		for(Chapter c : chapters) {
			if(c.getChapterName().equals(s)) {
				return c;
			}
		}
		return null;
	}

	public void addChapter(Chapter c) {
		chapters.add(c);
	}

	public void addChapterAt(int i, Chapter c) {
		chapters.add(i, c);
	}

    public void export(String str) throws IOException {

		for(Chapter c : chapters){
			String path = str+"/"+this.getProjectName() + "/" + c.getChapterName();
			System.out.println(path);
			Files.createDirectories(Paths.get(path));
			File f = new File(path + "/" + c.getChapterName() + ".txt");
			FileWriter writer = new FileWriter(f);
			writer.write(c.getChapterDescription());
			writer.close();
			for(Scene s : c.getScenes()){
				File ff = new File(path+ "/" + s.getSceneName() + ".odt");
				ff.createNewFile();
			}
		}
    }

	@Override
	public String toString() {
		return this.projectName;
	}
}
