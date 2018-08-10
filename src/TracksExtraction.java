import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TracksExtraction {
	
	private static ArrayList<String> IDList = new ArrayList<>();

	
	
	// This function populates the IDList from a external txt file
	public static void getPlaylistIDs() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("C:\\Projects\\Workspaces\\SpotifyPlaylistMigration\\results"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String i = "";
		try {
			while ((i=br.readLine())!=null) {
				IDList.add(i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		getPlaylistIDs();
	}

}
