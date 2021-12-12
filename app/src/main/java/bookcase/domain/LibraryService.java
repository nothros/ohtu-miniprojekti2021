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

    public boolean createLibraryObject(String type, String title, String author, String isbnWebsite, String course, String comment) {
        if (course != null) {
            course = course.trim().replaceAll("\\s{2,}", " ");
        }
        switch (type) {
            case "book":
                return createBook(title, author, isbnWebsite, course, comment);
            case "blogpost":
                return createBlogpost(title, author, isbnWebsite, course, comment);
            case "podcast":
                return createPodcast(title, author, isbnWebsite, course, comment);
        }
        return false;
    }

    private boolean createBlogpost(String title, String author, String url, String course, String comment) {
        if ((title.isEmpty() || url.isEmpty())) {
            return false;
        }
        LibraryObject libObj = new LibraryObject("blogpost", title, author, null, url, course, comment);
        return libraryDao.insertLibrary(libObj);
    }

    private boolean createPodcast(String title, String author, String url, String course, String comment) {
        if ((title.isEmpty() || url.isEmpty())) {
            return false;
        }
        LibraryObject libObj = new LibraryObject("podcast", title, author, null, url, course, comment);
        return libraryDao.insertLibrary(libObj);
    }

    private boolean createBook(String title, String author, String isbn, String course, String comment) {
        if ((title.isEmpty() || author.isEmpty() || isbn.isEmpty())) {
            return false;
        }
        if (!libraryDao.isUnique(isbn)) {
            System.out.println("Not a unique ISBN.");
            return false;
        }
        if (!libraryDao.isValidISBN(isbn)) {
            System.out.println("Not a valid ISBN.");
            return false;
        }
        LibraryObject libObj = new LibraryObject("book", title, author, isbn, null, course, comment);
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

    public List<LibraryObject> getAllObjects() {
        return libraryDao.getAll();
    }

    public List<LibraryObject> getAllObjects(int id) {
        return libraryDao.getAll(id);
    }

    public List<LibraryObject> getAllObjects(String s) {
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
