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
		/*
		 *  The absolute file path to your database file in windows is for example:
		 *  Path p = Paths.get("C:\\Users\\tomit\\git\\ohtu-miniprojekti2021\\app\\test.db");
		 *  You can also use Path p = Paths.get("test.db");
		 *  
		 *  Remove the database by deleting the file. Initialize a new empty database
		 *  with tables LIBRARY, COURSE, COURSE_LIBRARY under test.db.
		 *  
		 *  Add 5 book entries to LIBRARY. Add 4 course entries to COURSE. 
		 *  
		 *  Modify me to your needs.
		 *  
		 *  Note: If you want to retain the state of a demo database you can simply copy it to another
		 *  directory and then copy it back to the projects codes root directory. Or comment out this
		 *  function.
		 */
		
		Path p = Paths.get("test.db");
		if (Files.isRegularFile(p)) {
			try {
				Files.delete(p);
			}
			catch (Exception e) {
				System.out.println(e);
			}
        }
	}
	
    public static void main(String[] args) {
    	try {
    		dao = new LibraryObjectDAO(url);
    		//initDummyDatabaseItems();
    	} catch(SQLException ex) {
    		System.out.println(ex.getMessage());
    	}
        AppUi.main(args);
    }

}