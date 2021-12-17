package bookcase.logic;

import java.util.List;
import bookcase.domain.LibraryObject;
import bookcase.dao.LibraryObjectDAO;
import bookcase.dao.TagDAO;
import bookcase.dao.CourseDAO;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class LibraryService {

    private Connection conn;
    private LibraryObjectDAO libraryDao;
    private TagDAO tagDao;
    private CourseDAO courseDao;

    public LibraryService(String url) throws SQLException {
        conn = DriverManager.getConnection("jdbc:sqlite:" + url);
        this.libraryDao = new LibraryObjectDAO(conn);
        this.tagDao = new TagDAO(conn);
        this.courseDao = new CourseDAO(conn);
        createNewTablesIfNotExists();

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

    public void deleteEntry(int id) {
        libraryDao.deleteEntry(id);
    }

    private String createBlogpost(String title, String author, String url, String comment) {
    	if (title.trim().isEmpty()) {
            return "Title field can not be empty.";
        }
    	if (url.trim().isEmpty()) {
            return "Website field can not be empty.";
        }
    	String urls[] = url.split("www.");
    	url = urls[urls.length - 1];
        LibraryObject libObj = new LibraryObject("blogpost", title, author, null, url, comment);
        libraryDao.insertLibrary(libObj);
        return "";
    }

    private String createPodcast(String title, String author, String url, String comment) {
    	if (title.trim().isEmpty()) {
    		return "Title field can not be empty.";
        }
    	if (url.trim().isEmpty()) {
            return "Website field can not be empty.";
        }
    	String urls[] = url.split("www.");
    	url = urls[urls.length - 1];
        LibraryObject libObj = new LibraryObject("podcast", title, author, null, url, comment);
        libraryDao.insertLibrary(libObj);
        return "";
    }

    private String createBook(String title, String author, String isbn, String comment) {
    	if (title.trim().isEmpty()) {
    		return "Title field can not be empty.";
        }
    	if (author.trim().isEmpty()) {
    		return "Author field can not be empty.";
        }
    	if (isbn.trim().isEmpty()) {
        	return "ISBN field can not be empty.";
        }
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
    	if (title.trim().isEmpty()) {
    		return "Title field can not be empty.";
        }
    	if (url.trim().isEmpty()) {
        	return "Website field can not be empty.";
        }
    	String urls[] = url.split("www.");
    	url = urls[urls.length - 1];
        LibraryObject libObj = new LibraryObject(id, "blogpost", title, author, null, url, comment);
        libraryDao.updateLibrary(libObj);
        return "";
    }

    private String updatePodcast(int id, String title, String author, String url, String comment) {
    	if (title.trim().isEmpty()) {
    		return "Title field can not be empty.";
        }
    	if (url.trim().isEmpty()) {
        	return "Website field can not be empty.";
        }
    	String urls[] = url.split("www.");
    	url = urls[urls.length - 1];
        LibraryObject libObj = new LibraryObject(id, "podcast", title, author, null, url, comment);
        libraryDao.updateLibrary(libObj);
        return "";
    }

    private String updateBook(int id, String title, String author, String isbn, String comment) {
    	if (title.trim().isEmpty()) {
    		return "Title field can not be empty.";
        }
    	if (author.trim().isEmpty()) {
    		return "Author field can not be empty.";
        }
    	if (isbn.trim().isEmpty()) {
        	return "ISBN field can not be empty.";
        }
        if (!libraryDao.isValidISBN(isbn)) {
            return "ISBN must be a 10-13 digit numeric value.";
        }
        if (libraryDao.getLibraryId(isbn) != id && libraryDao.getLibraryId(isbn) != 0) {
        	return "Another book already has that ISBN.";
        }
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
        tagDao.createNewTable();
        courseDao.createNewTable();
    }

    public void addTagsToLatestLibraryObject(String tags) {
    	if (!tags.trim().equals("")) {
	        String[] taglist = tags.split(",");
	        int latestId = libraryDao.getCurrLibraryId();
	
	        for (String s : taglist) {
	            tagDao.addTag(latestId, s.trim());
	        }
    	}
    }

    public void updateTags(int id, String tags) {
        ArrayList<String> tagsByItem = getTagsById(id);
        for (int i = 0; i < tagsByItem.size(); i++) {
            tagDao.deleteFromTagLibrary(id);
        }
        if (!tags.trim().equals("")) {
        	addTagsByLibraryObjectId(id, tags);
        }
    }

    public void updateCourses(int id, String courses) {
        ArrayList<String> coursesById = getTagsById(id);
        for (int i = 0; i < coursesById.size(); i++) {
            courseDao.deleteFromCourseLibrary(id);
        }
        if (!courses.trim().equals("")) {
        	addCoursesByLibraryObjectId(id, courses);
        }
    }

    public void addTagsByLibraryObjectId(int id, String tags) {
        String[] taglist = tags.split(",");
        for (String s : taglist) {
            tagDao.addTag(id, s.trim());
        }
    }

    public void addCoursesByLibraryObjectId(int id, String courses) {
        String[] courseList = courses.split(",");
        for (String s : courseList) {
            courseDao.addCourse(id, s.trim());
        }
    }

    public ArrayList<String> getTagsById(int id) {
        return tagDao.getTags(id);
    }

    public ArrayList<String> getCoursesById(int id) {
        return courseDao.getCourses(id);
    }

    public String getTagString(int id) {
        ArrayList<String> tags = tagDao.getTags(id);
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
    	if (!courses.trim().equals("")) {
	        String[] courselist = courses.split(",");
	        int latestId = libraryDao.getCurrLibraryId();
	        for (String s : courselist) {
	            courseDao.addCourse(latestId, s.trim());
	        }
    	}
    }

    public String getCourseString(int id) {
        ArrayList<String> courses = courseDao.getCourses(id);
        String result = "";
        for (int i = 0; i < courses.size(); i++) {
            result += courses.get(i);
            if (i != courses.size() - 1) {
                result += ", ";
            }
        }
        return result;
    }

    public int getLibId(String isbn) {
        return libraryDao.getLibraryId(isbn);
    }

    public void tearDown(String db) throws SQLException {
        libraryDao.deleteDatabase(db);
    }
}