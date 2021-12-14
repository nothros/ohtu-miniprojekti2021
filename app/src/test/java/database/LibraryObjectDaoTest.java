package database;

import org.junit.Test;

import database.LibraryObject;
import database.LibraryObjectDAO;

import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

public class LibraryObjectDaoTest {

    private LibraryObjectDAO dao;

    private String testDB;

    /**
     * This method is performed before every test. It creates an empty db into
     * memory.
     */
    @Before
    public void init() throws SQLException {
        testDB = "daotest.db";
        dao = new LibraryObjectDAO(testDB);
        dao.createNewTable();
    }

    @Test
    public void testEmptyDatabaseSize() {
        assertEquals(0, dao.getAll().size());
    }

    @Test
    public void testDatabaseInsertion() {
        LibraryObject book1 = new LibraryObject("book", "Weapons of Math Destruction", "Cathy O'Neil", "12345678910", null, null);
        LibraryObject book2 = new LibraryObject("book", "Clean code", "Joku Muu", "11122233344", null, null);
        dao.insertLibrary(book1);
        dao.insertLibrary(book2);
        assertEquals(2, dao.getAll().size());
    }

    @Test
    public void testReadAfterInsertion() {
        LibraryObject book2 = new LibraryObject("book", "Clean code", "Joku Muu", "111222", null, null);

        dao.insertLibrary(book2);
        List<LibraryObject> objs = dao.getAll();
        assertEquals(1, objs.size());

        LibraryObject book = objs.get(0);
        assertEquals("Clean code", book.getTitle());
    }

    @Test
    public void testIdReturnWithIsbn() {
        LibraryObject book = new LibraryObject("book", "Test Book", "Test Writer", "44433322211", null, null);
        dao.insertLibrary(book);

        assertEquals(1, dao.getLibraryId("44433322211"));
    }

    @Test
    public void testNullIsbnReturnsFalse() {
        assertEquals(false, dao.isValidISBN(null));
    }

    @After
    public void tearDown() throws SQLException {
        dao.deleteDatabase(testDB);
    }

}
