
package database;

import org.junit.Test;

import database.LibraryObject;
import database.LibraryObjectDAO;

import org.junit.Before;
import static org.junit.Assert.*;
import java.util.List;


public class LibraryObjectDaoTest {
    
    private LibraryObjectDAO dao;
    
    /**
    Tämä metodi suoritetaan ennen jokaista testiä. Se luo tyhjän tietokannan muistiin.
    */
    @Before
    public void init(){
        dao = new LibraryObjectDAO("jdbc:sqlite::memory:");
        dao.createNewTable();
    }
    
    @Test
    public void testEmptyDatabaseSize(){
        assertEquals(0, dao.getAll().size());
    }
    
    @Test
    public void testDatabaseInsertion(){
        LibraryObject book1 = new LibraryObject(1, "Weapons of Math Destruction","Cathy O'Neil", "12345678910", null);
        LibraryObject book2 = new LibraryObject(1, "Clean code","Joku Muu", "11122233344", null);
        dao.insertLibrary(book1);
        dao.insertLibrary(book2);
        assertEquals(2, dao.getAll().size());
    }
    
    @Test
    public void testReadAfterInsertion(){
        LibraryObject book2 = new LibraryObject(1, "Clean code","Joku Muu", "111222", null);

        dao.insertLibrary(book2);
        List<LibraryObject> objs = dao.getAll();
        assertEquals(1, objs.size());
        
        LibraryObject book = objs.get(0);
        assertEquals("Clean code", book.getOtsikko());
    }

   
    
}