package bookplanner.data;

import java.io.*;

public class ProjectFactory {

	static String username=System.getProperty("user.name");
	static String projectPath="C:/Users/"+username+"/Documents/BookPlanner/projects/";
	
	public static boolean saveProject(BookProject p) throws IOException {
		File f = new File(projectPath+p.getProjectName()+".ser");
		if(!f.exists()){
			f.createNewFile();
		}
		FileOutputStream fileOut = new FileOutputStream(projectPath+p.getProjectName()+".ser");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(p);
		out.close();
		fileOut.close();
        return false;
    }
	
	public static BookProject loadProject(String projectName) throws ClassNotFoundException, IOException {
		BookProject p;
		FileInputStream fileIn = new FileInputStream(projectPath+projectName+".ser");
		ObjectInputStream in = new ObjectInputStream(fileIn);
		p=(BookProject) in.readObject();
		in.close();
		fileIn.close();
		return p;
	}	
}
