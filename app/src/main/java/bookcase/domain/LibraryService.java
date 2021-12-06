package bookcase.domain;

import java.util.List;
import database.CourseObject;
import database.LibraryObject;
import database.LibraryObjectDAO;

public class LibraryService {

    private LibraryObjectDAO libraryDao;

    public LibraryService(LibraryObjectDAO libraryDao) {
        this.libraryDao = libraryDao;
    }

    public boolean createLibraryObject(String type, String title, String author, String isbnWebsite, String course){
    	LibraryObject libObj;
    	switch(type) {
    		case "book":
    			if ((title.isEmpty() || author.isEmpty() || isbnWebsite.isEmpty())) {
    	            return false;
    	        }
    	        if (!libraryDao.isUnique(isbnWebsite)) {
    	        	System.out.println("Not a unique ISBN.");
    	    		return false;
    	    	}
    	        if (!libraryDao.isValidISBN(isbnWebsite)) {
    	    		System.out.println("Not a valid ISBN.");
    	    		return false;
    	    	}
    	        course = course.trim().replaceAll("\\s{2,}"," ");
    	        libObj = new LibraryObject(type, title, author, isbnWebsite, null, course);
    	        return libraryDao.insertLibrary(libObj);
    		case "blogpost":
    			if ((title.isEmpty() || isbnWebsite.isEmpty())) {
    	            return false;
    	        }
    			course = course.trim().replaceAll("\\s{2,}"," ");
    	        libObj = new LibraryObject(type, title, author, null, isbnWebsite, course);
    	        System.out.println(libObj);
    	        return libraryDao.insertLibrary(libObj);
    		case "podcast":
    			if ((title.isEmpty() || isbnWebsite.isEmpty())) {
    	            return false;
    	        }
    			course = course.trim().replaceAll("\\s{2,}"," ");
    	        libObj = new LibraryObject(type, title, author, null, isbnWebsite, course);
    	        return libraryDao.insertLibrary(libObj);
    	}
        return false;
    }

    private boolean createBook(String title, String author, String isbn, String course) {
        LibraryObject libObj;
        if (!libraryDao.isUnique(isbn)) {
            System.out.println("Not a unique ISBN.");
            return false;
        }
        if (!libraryDao.isValidISBN(isbn)) {
            System.out.println("Not a valid ISBN.");
            return false;
        }
        libObj = new LibraryObject("book", title, author, isbn, null, course);
        return libraryDao.insertLibrary(libObj);
    }

    public boolean createCourseObject(String name, String isbn) {
        String[] course = name.split(",");
        for (String s : course) {
            name = s.trim().replaceAll("\\s{2,}", " ");
            if (libraryDao.isUniqueCourse(name)) {
                CourseObject courseObj = new CourseObject(name);
                libraryDao.insertCourse(courseObj);
            }
            libraryDao.insertCL(libraryDao.getCurrLibraryId(), libraryDao.getCourseId(name));
        }
        return true;
    }
    
    public List<LibraryObject> getsAllObjects(){
        return libraryDao.getsAll();
    }

    public List<LibraryObject> getAllObjects() {
        return libraryDao.getAll();
    }
    
    public List<LibraryObject> getAllObjects(int id){
        return libraryDao.getAll(id);
    }
    
    public List<LibraryObject> getAllObjects(String s){
        return libraryDao.getAll(s);
    }

    public LibraryObject getByIsbn(String isbn) {
        return libraryDao.getByIsbn(isbn);
    }

    public void removeById(LibraryObject t) {
        libraryDao.removeEntry(t);
    }

    public void createNewTablesIfNotExists() {
        libraryDao.createNewTable();
    }
}
