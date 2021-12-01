package bookcase;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import bookcase.ui.AppUi;
import database.CourseObject;
import database.LibraryObject;
import database.LibraryObjectDAO;

public class App {

	private static String url = "jdbc:sqlite:test.db";
	//private static String filePath = "test.db";
	
	private static LibraryObjectDAO dao;
    
	public static void initDummyDatabaseItems() {		
		/*
		 *  The absolute file path to your database file in windows is for example:
		 *  Path p = Paths.get("C:\\Users\\tomit\\git\\ohtu-miniprojekti2021\\app\\test.db");
		 *  You can also use Path p = Paths.get("test.db");
		 *  
		 *  Remove the database by deleting the file. Initialize a new empty database named
		 *  BOOKCASE with tables LIBRARY, COURSE, COURSE_LIBRARY under the file test.db.
		 *  
		 *  Add 5 book entries to LIBRARY. Add 4 course entries to COURSE. 
		 *  
		 *  Modify me to your needs.
		 *  
		 *  Note: If you want to retain the state of a demo database you can simply copy it to another
		 *  directory and then copy it back to the projects codes root directory.
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
		dao = new LibraryObjectDAO(url);
		dao.createNewDatabase("test.db");
		dao.insertLibrary(new LibraryObject(1, "Weapons of Math Destruction 1", "Cathy O'Neil", "1234567890", null));
		dao.insertLibrary(new LibraryObject(1, "Weapons of Math Destruction 2", "Cathy O'Neil", "1234567891", null));
		dao.insertLibrary(new LibraryObject(1, "Weapons of Math Destruction 3", "Cathy O'Neil", "1234567892", null));
		dao.insertLibrary(new LibraryObject(1, "Weapons of Math Destruction 4", "Cathy O'Neil", "1234567893", null));
		dao.insertLibrary(new LibraryObject(1, "Weapons of Mass Destruction", "Mathew O'Neil", "1234567894", null));

		dao.insertCourse(new CourseObject("Logic 1", "Department of Mathematics"));
		dao.insertCourse(new CourseObject("Logic 2", "Department of Mathematics"));
		dao.insertCourse(new CourseObject("Programming in Python", "Department of Computer Science"));
		dao.insertCourse(new CourseObject("Software Engineering", "Department of Computer Science"));
		
		/*
		 *  The above entries are indexed according to successive ids. In this case we can simply bind them.
		 *  Books 1 and 2 relate to course 1. Book 3 relates to course 2. Books 4 and 5 do not relate to any course.
		 *  Function insertCL is a placeholder. Need to decide how we implement the database etc.
		 */
		dao.insertCL(1, 1);
		dao.insertCL(2, 1);
		dao.insertCL(3, 2);
	}
	
    public static void main(String[] args) {
    	initDummyDatabaseItems();
        AppUi.main(args);
    }

}