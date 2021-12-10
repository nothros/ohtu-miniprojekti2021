package database;

import org.junit.Test;

import database.LibraryObject;
import database.TagDAO;

import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

public class TagDaoTest {

    private TagDAO dao;
    private String testDB;

    /**
     * This method is performed before every test. It creates an empty db into
     * memory.
     */
    @Before
    public void init() throws SQLException {
        testDB = "daotest.db";
        dao = new TagDAO(testDB);
        dao.createNewTable();
    }

    @Test
    public void testEmptyDatabaseSize() {
        assertEquals(0, dao.getTags(0).size());
    }

    @Test
    public void testAddTagToLibrary() {
        dao.addTag(1, "Tag");
        assertEquals(1, dao.getTags(1).size());
    }

    @Test
    public void testGetTag() {
        dao.addTag(1, "Tag");
        assertEquals("Tag", dao.getTags(1).get(0));
    }

    @Test
    public void testTagIsOnlyAddedToSelectedLibrary() {
        dao.addTag(4, "Tag");
        assertEquals(0, dao.getTags(1).size());
        assertEquals(1, dao.getTags(4).size());
    }

    @Test
    public void testAddTagsToDifferentObjects() {
        //gives 2 tags to obj 1 and 1 to obj 2
        dao.addTag(1, "Tag");
        dao.addTag(1, "Tag2");
        dao.addTag(2, "Tag");
        assertEquals(2, dao.getTags(1).size());
        assertEquals(1, dao.getTags(2).size());

        assertEquals("Tag", dao.getTags(1).get(0));
        assertEquals("Tag2", dao.getTags(1).get(1));
        assertEquals("Tag", dao.getTags(2).get(0));
    }

    @Test
    public void testGetAllTags(){
        dao.addTag(1, "Tag1");
        dao.addTag(3, "Tag2");
        dao.addTag(2, "Tag3");
        dao.addTag(5, "Tag4");
        assertEquals(4, dao.getAllTags().size());
    }
    
    @Test
    public void testGetAllTagsDuplicates(){
        dao.addTag(1, "Tag1");
        dao.addTag(3, "Tag1");
        dao.addTag(2, "Tag2");
        dao.addTag(5, "Tag2");
        assertEquals(2, dao.getAllTags().size());
    }
    
    @Test
    public void testWithLibraryDao() {
        try {
            LibraryObjectDAO ld = new LibraryObjectDAO(testDB);
            ld.createNewTable();
            ld.insertLibrary(new LibraryObject("book", "namE", "author", "1111222233", "url", "course"));
            dao.addTag(ld.getLibraryId("1111222233"), "tagi");
            assertEquals(1, dao.getTags(ld.getLibraryId("1111222233")).size());
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @After
    public void tearDown() throws SQLException {
        dao.deleteDatabase(testDB);
    }

}
