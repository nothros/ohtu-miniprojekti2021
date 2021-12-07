package bookcase;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import bookcase.ui.AppUi;
import database.CourseObject;
import database.LibraryObject;
import database.LibraryObjectDAO;

public class App {

	private static String url = "test.db";
	//private static String filePath = "test.db";
	
	private static LibraryObjectDAO dao;
    
	public static void initDummyDatabaseItems() throws SQLException {		
		
		Path p = Paths.get("test.db");
		if (Files.isRegularFile(p)) {
			try {
				Files.delete(p);
			} catch (Exception e) {
				System.out.println(e);
			}
        }
    }

    public static void main(String[] args) {
        AppUi.main(args);
    }

}
