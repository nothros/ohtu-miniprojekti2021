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
    private String tooShortISBN = "123456789";
    private String tooLongISBN = "11122233344455";
    private String validISBN = "12345678910";
    private String testDB;

    @Before
    public void init() throws SQLException {
        testDB = "servicetest.db";
        service = new LibraryService(testDB);

    }

    @Test
    public void testEmptyDatabaseSize() {
        assertEquals(0, service.getAllObjects().size());
    }


    @Test
    public void validISBNisAdded() {
        service.createLibraryObject("book", "Title", "Author", validISBN, "");
        List<LibraryObject> objs = service.getAllObjects();
        assertEquals("12345678910", objs.get(0).getISBN());
    }

    @Test
    public void tooShortISBNisNotAdded() {
        service.createLibraryObject("book", "Title", "Author", tooShortISBN, "");
        assertEquals(0, service.getAllObjects().size());
    }

    @Test
    public void tooLongISBNisNotAdded() {
        service.createLibraryObject("book", "Title", "Author", tooLongISBN, "");
        assertEquals(0, service.getAllObjects().size());
    }

    @Test
    public void duplicateISBNisRejected() {
        service.createLibraryObject("book", "A valid ISBN", "Author", "12345678910", "");
        assertTrue(service.createLibraryObject("book", "This is a duplicate", "Author", "12345678910", "").equals("A unique ISBN value must be given."));
    }

    @Test
    public void testBookCantBeAddedWithoutTitle() {
        String emptyTitle = service.createLibraryObject("book", "", "Author", "12345678910", "");
        assertEquals("Title field can not be empty.", emptyTitle);
    }

    @Test
    public void testBookCantBeAddedWithoutISBN() {
        String emptyIsbn = service.createLibraryObject("book", "Title", "Author", "", "");
        assertEquals("ISBN field can not be empty.", emptyIsbn);
    }

    @Test
    public void testUpdateBookWorks(){
        service.createLibraryObject("book", "Wrong", "Author", "0101010101", "");
        List<LibraryObject> objs = service.getAllObjects();
        int id = service.getLibId("0101010101");
        String s = service.updateLibraryObject(id, "book", "TITTLE", "Author", "0101010101", "");
        assertEquals("", s);
    }

    @Test
    public void testUpdateBlogpost(){
        service.createLibraryObject("blogpost", "Wrong", "Author", "0101010101", "");
        List<LibraryObject> objs = service.getAllObjects();
        int id = service.getLibId("0101010101");
        String s = service.updateLibraryObject(id, "blogpost", "TITTLE", "Author", "0101010101", "");
        assertEquals("", s);
    }

    @Test
    public void testUpdatePodcast(){
        service.createLibraryObject("podcast", "Wrong", "Author", "0101010101", "");
        List<LibraryObject> objs = service.getAllObjects();
        int id = service.getLibId("0101010101");
        String s = service.updateLibraryObject(id, "podcast", "TITTLE", "Author", "0101010101", "");
        assertEquals("", s);
    }

    @Test
    public void testBookCantBeAddedWithoutAuthor() {
        String emptyAuthor = service.createLibraryObject("book", "title", "", "12345678910", "");
        assertEquals("Author field can not be empty.", emptyAuthor);
    }

    @Test
    public void testPodcastAdd() {
        service.createLibraryObject("podcast", "title", "author", "url", "");
        assertEquals(1, service.getAllObjects().size());
    }
    
    @Test
    public void testBlogpostAdd() {
        service.createLibraryObject("blogpost", "title", "author", "url", "");
        assertEquals(1, service.getAllObjects().size());
    }

    @Test
    public void testBlogpostCantBeMadeWithEmptyWebsite() {
        String emptyWebsite = service.createLibraryObject("blogpost", "title", "author", "", "");
        assertEquals("Website field can not be empty.", emptyWebsite);
    }

    @Test
    public void testPodcastCantBeMadeWithEmptyWebsite() {
        String emptyWebsite = service.createLibraryObject("podcast", "title", "author", "", "");
        assertEquals("Website field can not be empty.", emptyWebsite);
    }

    @Test 
    public void typeIsWrongAndItIsNotACase() {
        assertTrue(service.createLibraryObject("wrong", "A valid ISBN2", "Author2", "123456789102", "").equals(""));
    }
    
    @Test
    public void addingTagWorks() {
        service.createLibraryObject("blogpost", "title", "author", "url", "");
        service.addTagsToLatestLibraryObject("tag");
        assertEquals(1, service.getAllObjects().size());
    }

    @After
    public void tearDown() throws SQLException {
        service.tearDown(testDB);
    }
}
