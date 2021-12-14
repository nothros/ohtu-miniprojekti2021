package bookcase.domain;

import java.util.List;
import database.LibraryObject;
import database.LibraryObjectDAO;
import database.TagDAO;
import database.CourseDAO;
import java.util.ArrayList;

public class LibraryService {

    private LibraryObjectDAO libraryDao;
    private TagDAO tagDao;
    private CourseDAO courseDao;

    public LibraryService(LibraryObjectDAO libraryDao) {
        this.libraryDao = libraryDao;
        this.tagDao = libraryDao.getTagDao();
        this.courseDao = libraryDao.getCourseDao();
    }

    public String createLibraryObject(String type, String title, String author, String isbnWebsite, String comment) {
        switch (type) {
            case "book":
                return createBook(title, author, isbnWebsite, comment);
            case "blogpost":
                return createBlogpost(title, author, isbnWebsite, comment);
            case "podcast":
                return createPodcast(title, author, isbnWebsite, comment);
        }
        return "";
    }

    private String createBlogpost(String title, String author, String url, String comment) {
    	if (title.trim().isEmpty())
    		return "Title field can not be empty.";
    	if (url.trim().isEmpty())
        		return "Website field can not be empty.";
    	String urls[] = url.split("www.");
    	url = urls[urls.length - 1];
        LibraryObject libObj = new LibraryObject("blogpost", title, author, null, url, comment);
        libraryDao.insertLibrary(libObj);
        return "";
    }

    private String createPodcast(String title, String author, String url, String comment) {
    	if (title.trim().isEmpty())
    		return "Title field can not be empty.";
    	if (url.trim().isEmpty())
        		return "Website field can not be empty.";
    	String urls[] = url.split("www.");
    	url = urls[urls.length - 1];
        LibraryObject libObj = new LibraryObject("podcast", title, author, null, url, comment);
        libraryDao.insertLibrary(libObj);
        return "";
    }

    private String createBook(String title, String author, String isbn, String comment) {
    	if (title.trim().isEmpty())
    		return "Title field can not be empty.";
    	if (author.trim().isEmpty())
    		return "Author field can not be empty.";
    	if (isbn.trim().isEmpty())
        	return "ISBN field can not be empty.";
        if (!libraryDao.isUnique(isbn)) {
            return "A unique ISBN value must be given.";
        }
        if (!libraryDao.isValidISBN(isbn)) {
            return "ISBN must be a 10-13 digit numeric value.";
        }
        LibraryObject libObj = new LibraryObject("book", title, author, isbn, null, comment);
        libraryDao.insertLibrary(libObj);
        return "";
    }
    
    public String updateLibraryObject(int id, String type, String title, String author, String isbnWebsite, String comment) {
        switch (type) {
            case "book":
                return updateBook(id, title, author, isbnWebsite, comment);
            case "blogpost":
                return updateBlogpost(id, title, author, isbnWebsite, comment);
            case "podcast":
                return updatePodcast(id, title, author, isbnWebsite, comment);
        }
        return "";
    }
    
    private String updateBlogpost(int id, String title, String author, String url, String comment) {
    	if (title.trim().isEmpty())
    		return "Title field can not be empty.";
    	if (url.trim().isEmpty())
        		return "Website field can not be empty.";
    	String urls[] = url.split("www.");
    	url = urls[urls.length - 1];
        LibraryObject libObj = new LibraryObject(id, "blogpost", title, author, null, url, comment);
        libraryDao.updateLibrary(libObj);
        return "";
    }

    private String updatePodcast(int id, String title, String author, String url, String comment) {
    	if (title.trim().isEmpty())
    		return "Title field can not be empty.";
    	if (url.trim().isEmpty())
        		return "Website field can not be empty.";
    	String urls[] = url.split("www.");
    	url = urls[urls.length - 1];
        LibraryObject libObj = new LibraryObject(id, "podcast", title, author, null, url, comment);
        libraryDao.updateLibrary(libObj);
        return "";
    }

    private String updateBook(int id, String title, String author, String isbn, String comment) {
    	if (title.trim().isEmpty())
    		return "Title field can not be empty.";
    	if (author.trim().isEmpty())
    		return "Author field can not be empty.";
    	if (isbn.trim().isEmpty())
        	return "ISBN field can not be empty.";
        if (!libraryDao.isValidISBN(isbn)) {
            return "ISBN must be a 10-13 digit numeric value.";
        }
        if (libraryDao.getLibraryId(isbn) != id && libraryDao.getLibraryId(isbn) != 0)
        	return "Another book already has that ISBN.";
        LibraryObject libObj = new LibraryObject(id, "book", title, author, isbn, null, comment);
        libraryDao.updateLibrary(libObj);
        return "";
    }

    public List<LibraryObject> getAllObjects() {
        return libraryDao.getAll();
    }

    public List<LibraryObject> getAllObjects(int id) {
        return libraryDao.getAll(id);
    }

    public void createNewTablesIfNotExists() {
        libraryDao.createNewTable();
    }

    public void addTagsToLatestLibraryObject(String tags) {
        String[] taglist = tags.split(",");
        int latestId = libraryDao.getCurrLibraryId();

        for (String s : taglist) {
            tagDao.addTag(latestId, s.trim());
        }
    }

    public String getTagString(LibraryObject obj) {
        ArrayList<String> tags = tagDao.getTags(obj.getId());
        String result = "";
        for (int i = 0; i < tags.size(); i++) {
            result += tags.get(i);
            if (i != tags.size() - 1) {
                result += ", ";
            }
        }
        return result;
    }
    
    public void addCoursesToLatestLibraryObject(String courses) {
        String[] courselist = courses.split(",");
        int latestId = libraryDao.getCurrLibraryId();
        for (String s : courselist)
            courseDao.addCourse(latestId, s.trim());
    }

    public String getCourseString(LibraryObject obj) {
        ArrayList<String> courses = courseDao.getCourses(obj.getId());
        String result = "";
        for (int i = 0; i < courses.size(); i++) {
            result += courses.get(i);
            if (i != courses.size() - 1) {
                result += ", ";
            }
        }
        return result;
    }
    
}
