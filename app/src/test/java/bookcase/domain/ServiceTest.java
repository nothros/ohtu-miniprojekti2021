package bookcase.domain;
import org.junit.Test;

import bookcase.domain.LibraryService;
import database.LibraryObject;
import database.LibraryObjectDAO;

import org.junit.Before;
import static org.junit.Assert.*;
import java.util.List;

public class ServiceTest {
    private LibraryService service;
    private LibraryObjectDAO dao;
    private String tooShortISBN =  "123456789";
    private String tooLongISBN =  "11122233344455";
    private String validISBN = "12345678910";
    
    @Before
    public void init(){
        dao = new LibraryObjectDAO("jdbc:sqlite::memory:");
        service = new LibraryService(dao);
        dao.createNewTable();

    }

    @Test
    public void testEmptyDatabaseSize(){
        assertEquals(0, dao.getAll().size());
    }
    @Test
    public void tooShortISBNisNotAdded() {
        service.createLibraryObject(1, "","Author", tooShortISBN, null);
        assertEquals(0, dao.getAll().size());
    }
    @Test
    public void tooLongISBNisNotAdded() {
        service.createLibraryObject(1, "","Author", tooLongISBN, null);
        assertEquals(0, dao.getAll().size());
    }

    @Test
    public void validISBNisAdded() {
        service.createLibraryObject(1, " ","Author", validISBN, null);
        List<LibraryObject> objs = dao.getAll();
        assertEquals("12345678910", objs.get(0).getISBN() );
    }

    @Test
    public void duplicateISBNisRejected() {
        service.createLibraryObject(1, "A valid ISBN","Author", "12345678910", null);
        assertFalse(service.createLibraryObject(1, "This is a duplicate","Author", "12345678910", null));
    }
}
