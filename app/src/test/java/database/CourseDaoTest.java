package database;

import org.junit.Test;

import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.SQLException;

public class CourseDaoTest {

    private CourseDAO dao;
    private String testDB;
    private Connection conn;
    /**
     * This method is performed before every test. It creates an empty db into
     * memory.
     */
    @Before
    public void init() throws SQLException {
        testDB = "daotest.db";
        conn = DriverManager.getConnection("jdbc:sqlite:" + testDB);
        dao = new CourseDAO(conn);
        dao.createNewTable();
    }

    @Test
    public void testEmptyDatabaseSize() {
        assertEquals(0, dao.getCourses(0).size());
    }

    @Test
    public void testAddCourseToLibrary() {
        dao.addCourse(1, "Course");
        assertEquals(1, dao.getCourses(1).size());
    }

    @Test
    public void testGetCourse() {
        dao.addCourse(1, "Course");
        assertEquals("Course", dao.getCourses(1).get(0));
    }

    @Test
    public void testCourseIsOnlyAddedToSelectedLibrary() {
        dao.addCourse(4, "Course");
        assertEquals(0, dao.getCourses(1).size());
        assertEquals(1, dao.getCourses(4).size());
    }

    @Test
    public void testAddCoursesToDifferentObjects() {
        //gives 2 courses to obj 1 and 1 to obj 2
        dao.addCourse(1, "Course");
        dao.addCourse(1, "Course2");
        dao.addCourse(2, "Course");
        assertEquals(2, dao.getCourses(1).size());
        assertEquals(1, dao.getCourses(2).size());

        assertEquals("Course", dao.getCourses(1).get(0));
        assertEquals("Course2", dao.getCourses(1).get(1));
        assertEquals("Course", dao.getCourses(2).get(0));
    }

    @Test
    public void testGetAllCourses(){
        dao.addCourse(1, "Course1");
        dao.addCourse(3, "Course2");
        dao.addCourse(2, "Course3");
        dao.addCourse(5, "Course4");
        assertEquals(4, dao.getAllCourses().size());
    }
    
    @Test
    public void testGetAllCoursesDuplicates(){
        dao.addCourse(1, "Course1");
        dao.addCourse(3, "Course1");
        dao.addCourse(2, "Course2");
        dao.addCourse(5, "Course2");
        assertEquals(2, dao.getAllCourses().size());
    }
    
    @Test
    public void testWithLibraryDao() throws SQLException {
        LibraryObjectDAO ld = new LibraryObjectDAO(conn);
        ld.createNewTable();
        ld.insertLibrary(new LibraryObject(1, "book", "name", "author", "1111222233", "url", ""));
        dao.addCourse(ld.getLibraryId("1111222233"), "kurssi");
        assertEquals(1, dao.getCourses(ld.getLibraryId("1111222233")).size());
    }

    @After
    public void tearDown() throws SQLException {
        dao.deleteDatabase(testDB);
    }

}