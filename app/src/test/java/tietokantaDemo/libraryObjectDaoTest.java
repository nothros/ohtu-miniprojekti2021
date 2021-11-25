
package tietokantaDemo;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.List;


public class libraryObjectDaoTest {
    
    private libraryObjectDAO dao;
    
    /**
    Tämä metodi suoritetaan ennen jokaista testiä. Se luo tyhjän tietokannan muistiin.
    */
    @Before
    public void init(){
        dao = new libraryObjectDAO("jdbc:sqlite::memory:");
        dao.createNewTable();
    }
    
    @Test
    public void testEmptyDatabaseSize(){
        assertEquals(0, dao.getAll().size());
    }
    
    @Test
    public void testDatabaseInsertion(){
        dao.insert(1, "Weapons of Math Destruction","Cathy O'Neil", "123456", null);
        dao.insert(1, "Clean code","Joku Muu", "111222", null);
        assertEquals(2, dao.getAll().size());
    }
    
    @Test
    public void testReadAfterInsertion(){
        dao.insert(1, "Clean code","Joku Muu", "111222", null);
        List<libraryObject> objs = dao.getAll();
        assertEquals(1, objs.size());
        
        libraryObject book = objs.get(0);
        assertEquals("Clean code", book.getOtsikko());
    }
    
}