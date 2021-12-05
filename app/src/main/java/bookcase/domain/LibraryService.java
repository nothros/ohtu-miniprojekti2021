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

    public boolean createLibraryObject(int type, String title, String author, String isbnWebsite, String URL, String course){
    	LibraryObject libObj;
    	switch(type) {
    		case 1:
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
    	        //course = course.trim().replaceAll("\\s{2,}"," ");
    	        libObj = new LibraryObject(type, title, author, isbnWebsite, URL, course);
    	        return libraryDao.insertLibrary(libObj);
    		case 2:
    			if ((title.isEmpty() || isbnWebsite.isEmpty())) {
    	            return false;
    	        }
    		//	course = course.trim().replaceAll("\\s{2,}"," ");
    	        libObj = new LibraryObject(type, title, author, null, isbnWebsite, course);
    	        return libraryDao.insertLibrary(libObj);
    		case 3:
    			if ((title.isEmpty() || isbnWebsite.isEmpty())) {
    	            return false;
    	        }
    		//	course = course.trim().replaceAll("\\s{2,}"," ");
    	        libObj = new LibraryObject(type, title, author, null, isbnWebsite, course);
    	        return libraryDao.insertLibrary(libObj);
    	}
        return false;
    }







    public boolean createCourseObject(String name, String isbn) {
    	//System.out.println(name);
    	String[] course = name.split(",");
    	for (String s : course) {
    		name = s.trim().replaceAll("\\s{2,}"," ");
    		if (libraryDao.isUniqueCourse(name)) {
    			CourseObject courseObj = new CourseObject(name);
    			libraryDao.insertCourse(courseObj);
    		}
    		libraryDao.insertCL(libraryDao.getCurrLibraryId(),libraryDao.getCourseId(name));
    	}
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