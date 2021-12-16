package bookcase.dao;

import bookcase.domain.LibraryObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class LibraryObjectDaoTest {

    private LibraryObjectDAO dao;
    private Connection conn;
    private String testDB;

    /**
     * This method is performed before every test. It creates an empty db into
     * memory.
     */
    @Before
    public void init() throws SQLException {
        testDB = "daotest.db";
        conn = DriverManager.getConnection("jdbc:sqlite:" + testDB);
        dao = new LibraryObjectDAO(conn);
        dao.createNewTable();
    }

    @Test
    public void testEmptyDatabaseSize() {
        assertEquals(0, dao.getAll().size());
    }
    
    @Test
    public void testFailCreateNewTable() {
    	try {
    		dao.deleteDatabase(testDB);
    		dao.createNewTable();
    	} catch(Exception e) {
    		assertTrue(e.getMessage().contains("Error: createNewTable()."));
    	}
    }
    
    @Test
    public void testFailGetCurrLibraryId() {
        try {
        	dao.deleteDatabase(testDB);
            dao.getCurrLibraryId();
        } catch(Exception e) {
            assertTrue(e.getMessage().contains("Error: getCurrLibraryId()."));
        }
    }
    
    @Test
    public void testFailGetLibraryId() {
        try {
        	dao.deleteDatabase(testDB);
        	dao.getLibraryId("1111111111");
        } catch(Exception e) {
            assertTrue(e.getMessage().contains("Error: getLibraryId()."));
        }
    }
    
    @Test
    public void testFailInsertLibrary() {
        try {
        	dao.deleteDatabase(testDB);
        	LibraryObject book = new LibraryObject("book", "Weapons of Math Destruction", "Cathy O'Neil", "12345678910", null, null);
            dao.insertLibrary(book);
        } catch(Exception e) {
            assertTrue(e.getMessage().contains("Error: insertLibrary()."));
        }
    }
    
    @Test
    public void testFailUpdateLibrary() {
        try {
        	dao.deleteDatabase(testDB);
        	LibraryObject book = new LibraryObject("book", "Weapons of Math Destruction", "Cathy O'Neil", "12345678910", null, null);
            dao.updateLibrary(book);
        } catch(Exception e) {
            assertTrue(e.getMessage().contains("Error: updateLibrary()."));
        }
    }
    
    @Test
    public void testFailIsUnique() {
        try {
        	dao.deleteDatabase(testDB);
            dao.isUnique("1111111111");
        } catch(Exception e) {
            assertTrue(e.getMessage().contains("Error: isUnique()."));
        }
    }
    
    @Test
    public void testFailDeleteEntry() {
        try {
        	dao.deleteDatabase(testDB);
            dao.deleteEntry(1);
        } catch(Exception e) {
            assertTrue(e.getMessage().contains("Error: deleteEntry()."));
        }
    }
    
    @Test
    public void testFailGetAll() {
        try {
        	dao.deleteDatabase(testDB);
            dao.getAll();
        } catch(Exception e) {
            assertTrue(e.getMessage().contains("Error: getAll()."));
        }
    }

    @Test
    public void testFailGetllAllWithId() {
        try {
        	dao.deleteDatabase(testDB);
            dao.getAll(1);
        } catch(Exception e) {
            assertTrue(e.getMessage().contains("Error: getAll()."));
        }
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
    public void testSave() {
        LibraryObject book = new LibraryObject("book", "Weapons of Math Destruction", "Cathy O'Neil", "12345678910", null, null);
        dao.save(book);
        assertEquals(1, dao.getAll().size());
    }

    @Test
    public void testDelete() {
        LibraryObject book = new LibraryObject(1, "book", "title", "author", "44433322211", null, null);
        dao.insertLibrary(book);
        assertEquals(1, dao.getAll().size());
        dao.delete(book);
        assertEquals(0, dao.getAll().size());
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
    
    @Test
    public void testShortIsbnReturnsFalse() {
        assertEquals(false, dao.isValidISBN("123456789"));
    }
    
    @Test
    public void testLongIsbnReturnsFalse() {
        assertEquals(false, dao.isValidISBN("123456789123456"));
    }
    
    @Test
    public void testValidIsbnReturnsTrue() {
        assertEquals(true, dao.isValidISBN("1234567890"));
    }
    
    @Test
    public void testValidNonUniqueISBNisNotUniqueReturnsTrue() {
    	LibraryObject book = new LibraryObject("book", "Test Book", "Test Writer", "44433322211", null, null);
        dao.insertLibrary(book);
        assertTrue(dao.isUnique("1111111111"));
    }
    
    @Test
    public void testValidUniqueISBNisUniqueReturnsFalse() {
    	LibraryObject book = new LibraryObject("book", "Test Book", "Test Writer", "44433322211", null, null);
        dao.insertLibrary(book);
        assertFalse(dao.isUnique("44433322211"));
    }
    
    @Test
    public void testCurrLibraryIdIsValid() {
    	LibraryObject book = new LibraryObject("book", "Test Book", "Test Writer", "44433322211", null, null);
        dao.insertLibrary(book);
    	assertEquals(1, dao.getCurrLibraryId());
    	book = new LibraryObject("book", "Test Book", "Test Writer", "44433322212", null, null);
        dao.insertLibrary(book);
    	assertEquals(2, dao.getCurrLibraryId());
    }
    
    @Test
    public void testUpdateLibrary() {
    	LibraryObject book = new LibraryObject("book", "title", "author", "44433322211", null, null);
        dao.insertLibrary(book);
        assertEquals("title", dao.getAll(1).get(0).getTitle());
    	dao.updateLibrary(new LibraryObject(1, "book", "new title", "author", "44433322211", null, null));
    	assertEquals("new title", dao.getAll(1).get(0).getTitle());
    }
    
    @Test
    public void testDeleteEntry() {
    	LibraryObject book = new LibraryObject("book", "title", "author", "44433322211", null, null);
        dao.insertLibrary(book);
        assertEquals(1, dao.getAll().size());
        dao.deleteEntry(1);
        assertEquals(0, dao.getAll().size());
    }

    @After
    public void tearDown() throws SQLException {
        dao.deleteDatabase(testDB);
    }

}
