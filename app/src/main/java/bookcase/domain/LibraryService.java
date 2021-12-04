package bookcase.domain;

import java.util.List;
import database.CourseObject;
import database.LibraryObject;
import database.LibraryObjectDAO;

public class LibraryService {
    private LibraryObjectDAO libraryDao;

    public LibraryService(LibraryObjectDAO libraryDao){
        this.libraryDao = libraryDao;
    }

    public boolean createLibraryObject(int type, String title, String author, String ISBN, String URL){
        if (title.isEmpty() || author.isEmpty() || ISBN.isEmpty()) {
            return false;
        }
        if (!libraryDao.isUnique(ISBN)) {
        	System.out.println("Not a unique ISBN.");
    		return false;
    	}
        if (!libraryDao.isValidISBN(ISBN)) {
    		System.out.println("Not a valid ISBN.");
    		return false;
    	}
        LibraryObject libraryObject = new LibraryObject(type, title, author, ISBN, URL);
        return libraryDao.insertLibrary(libraryObject);
    }
    
    public boolean createCourseObject(String name, String isbn) {
    	if (libraryDao.isUniqueCourse(name)) {
    		CourseObject courseObj = new CourseObject(name);
    		libraryDao.insertCourse(courseObj);
    	}
    	libraryDao.insertCL(libraryDao.getLibraryId(isbn),libraryDao.getCourseId(name));
    	return true;
    }

    public List<LibraryObject> getAllObjects(){
        return libraryDao.getAll();
    }
    
    public List<LibraryObject> getAllObjects(String s){
        return libraryDao.getAll(s);
    }

    public void createNewTablesIfNotExists(){
        libraryDao.createNewTable();
    }

}