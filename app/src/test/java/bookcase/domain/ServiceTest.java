package bookcase.domain;

import org.junit.Test;

import bookcase.domain.LibraryService;
import database.LibraryObject;
import database.LibraryObjectDAO;

import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

public class ServiceTest {

    private LibraryService service;
    private LibraryObjectDAO dao;
    private String tooShortISBN = "123456789";
    private String tooLongISBN = "11122233344455";
    private String validISBN = "12345678910";
    private String testDB;

    @Before
    public void init() throws SQLException {
        testDB = "servicetest.db";
        dao = new LibraryObjectDAO(testDB);
        service = new LibraryService(dao);
        dao.createNewTable();

    }

    @Test
    public void testEmptyDatabaseSize() {
        assertEquals(0, dao.getAll().size());
    }

    @Test
    public void tooShortISBNisNotAdded() {
        service.createLibraryObject("book", "Title", "Author", tooShortISBN, "");
        assertEquals(0, dao.getAll().size());
    }

    @Test
    public void tooLongISBNisNotAdded() {
        service.createLibraryObject("book", "Title", "Author", tooLongISBN, "");
        assertEquals(0, dao.getAll().size());
    }

    @Test
    public void validISBNisAdded() {
        service.createLibraryObject("book", "Title", "Author", validISBN, "");
        List<LibraryObject> objs = dao.getAll();
        assertEquals("12345678910", objs.get(0).getISBN());
    }

    @Test
    public void duplicateISBNisRejected() {
        service.createLibraryObject("book", "A valid ISBN", "Author", "12345678910", "");
        assertTrue(service.createLibraryObject("book", "This is a duplicate", "Author", "12345678910", "").equals("A unique ISBN value must be given."));
    }

    @Test
    public void testPodcastAdd(){
        service.createLibraryObject("podcast", "title", "author", "url", "");
        assertEquals(1, dao.getAll().size());
    }
    
    @Test
    public void testBlogpostAdd(){
        service.createLibraryObject("blogpost", "title", "author", "url", "");
        assertEquals(1, dao.getAll().size());
    }

    @Test 
    public void typeIsWrongAndItIsNotACase(){
        assertTrue(service.createLibraryObject("wrong", "A valid ISBN2", "Author2", "123456789102", "").equals(""));
    }
    
    @Test
    public void addingTagWorks(){
        service.createLibraryObject("blogpost", "title", "author", "url", "");
        service.addTagsToLatestLibraryObject("tag");
        assertEquals(1, dao.getTagDao().getAllTags().size());
    }

    @Test
    public void addingMultipleTagsWorks(){
        service.createLibraryObject("blogpost", "title", "author", "url", "");
        service.addTagsToLatestLibraryObject("tag1, tag2, tag3");
        assertEquals(3, dao.getTagDao().getAllTags().size());
    }

    @After
    public void tearDown() throws SQLException {
        dao.deleteDatabase(testDB);
    }
}
