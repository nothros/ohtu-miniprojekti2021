package bookcase.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CourseDAO {

    private Connection conn;

    public CourseDAO(String dbname) throws SQLException {
        this.conn = DriverManager.getConnection("jdbc:sqlite:" + dbname);
    }

    public CourseDAO(Connection conn) {
        this.conn = conn;
    }

    private Connection getConnection() throws SQLException {
        return conn;
    }

    /* Creates course tables in database*/
    public void createNewTable() {
        String sq1 = "CREATE TABLE IF NOT EXISTS COURSES (ID INTEGER PRIMARY KEY, NAME TEXT NOT NULL)";
        String sq2 = "CREATE TABLE IF NOT EXISTS COURSE_LIBRARY (ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "LIBRARY_ID INTEGER NOT NULL,"
                + "COURSE_ID INTEGER NOT NULL, "
                + "FOREIGN KEY(LIBRARY_ID) REFERENCES LIBRARY(ID) ON DELETE CASCADE,"
                + "FOREIGN KEY(COURSE_ID) REFERENCES COURSES(ID) ON DELETE CASCADE)";
        try (Statement stmt = getConnection().createStatement()) {
            tryCreateTable(stmt, sq1, "COURSES");
            tryCreateTable(stmt, sq2, "COURSE_LIBRARY");
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            System.out.println("Tables already exists.");
        }
    }

    private void tryCreateTable(Statement stmt, String sq1, String name) {
        try {
            stmt.execute(sq1);
            //System.out.println("Table " + name + " created.");
        } catch (SQLException e) {
            System.out.println("Failed to create " + name + " table.");
        }
    }

    /**
     * Returns a list of courses for a specific library object
     *
     * @param libraryObjectId id of object in library table, get this from
     * libraryObjectDao
     */
    public ArrayList<String> getCourses(int libraryObjectId) {
        ArrayList<String> list = new ArrayList<String>();
        String sq1 = "SELECT b.NAME FROM COURSE_LIBRARY a, COURSES b WHERE b.ID = a.COURSE_ID AND a.LIBRARY_ID = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sq1)) {
            pstmt.setInt(1, libraryObjectId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("NAME"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    /**
     * Returns a list of all courses connected to any library object.
     */
    public ArrayList<String> getAllCourses() {
        ArrayList<String> list = new ArrayList<String>();
        String sq1 = "SELECT DISTINCT b.NAME FROM COURSE_LIBRARY a, COURSES b WHERE b.ID = a.COURSE_ID";
        try (Statement stmt = getConnection().createStatement()) {
            stmt.execute(sq1);
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {
                list.add(rs.getString("NAME"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    /**
     * Add a course to a library object
     *
     * @param libraryObjectId id of object in library table, get this from
     * libraryObjectDao
     * @param course new course
     */
    public boolean addCourse(int libraryObjectId, String course) {
        int courseId = getCourseId(course);
        if (courseId == -1) { 
            if (!insertCourse(course)) {
                return false;
            }
            courseId = getCourseId(course);
            if (courseId == -1) {
                return false;
            }
        }
        return connectCourseToLibrary(libraryObjectId, courseId);
    }

    private boolean connectCourseToLibrary(int libraryId, int courseId) {
        String sq1 = "INSERT INTO COURSE_LIBRARY(LIBRARY_ID, COURSE_ID) VALUES(?,?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sq1)) {
            pstmt.setInt(1, libraryId);
            pstmt.setInt(2, courseId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /* Returns id of course, -1 if not found*/
    private int getCourseId(String name) {
        String sq1 = "SELECT ID FROM COURSES WHERE NAME = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sq1)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.isClosed()) {
                return -1;
            }
            return rs.getInt("ID");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    /* Insert course to table COURSES*/
    private boolean insertCourse(String course) {
        String sql = "INSERT INTO COURSES(NAME) VALUES(?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, course);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /*
     *  Remove databasefile
     */
    public void deleteDatabase(String database) throws SQLException {
        conn.close();
        File deletedDB = new File(database);
        deletedDB.delete();
    }

}