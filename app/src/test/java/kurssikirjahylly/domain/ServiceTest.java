package kurssikirjahylly.domain;
import org.junit.Test;

import tietokantaDemo.LibraryObject;
import tietokantaDemo.LibraryObjectDAO;

import org.junit.Before;
import static org.junit.Assert.*;
import java.util.List;

public class ServiceTest {
    private LibraryService service;
    private LibraryObjectDAO dao;
    
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
        service.createLibraryObject(1, "Invalid ISBN","Joku", "1", null);
        assertEquals(0, dao.getAll().size());
    }
    @Test
    public void tooLongISBNisNotAdded() {
        service.createLibraryObject(1, "len(ISBN)=14","Joku", "11122233344455", null);
        assertEquals(0, dao.getAll().size());
    }

    @Test
    public void validISBNisAdded() {
        LibraryObject niceISBN = new LibraryObject(1, "This is fine","Author", "12345678910", null);
        service.createLibraryObject(1, "This is fine","Author", "12345678910", null);
        List<LibraryObject> objs = dao.getAll();
        assertEquals("12345678910", objs.get(0).getISBN() );
    }
}
