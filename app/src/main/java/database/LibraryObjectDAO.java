package database;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LibraryObjectDAO implements DAO<LibraryObject> {

    private Connection conn;

    public LibraryObjectDAO(String url) throws SQLException {
        conn = DriverManager.getConnection("jdbc:sqlite:" + url);
    }

    /*
     *  If database is empty then create all database tables.
     *  DELETE ME ?
     */
 /*  public void createNewDatabase(String fileName) {
        String url = "jdbc:sqlite:" + fileName;
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName() + ".");
                File f = new File(fileName);
                if (f.length() == 0) {
                    System.out.println("A new database has been created.");
                }
                createNewTable();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    } */

 /*
     *  Queries for all database tables, if they do not exist.
     */
    public void createNewTable() {
        String sq1 = "CREATE TABLE IF NOT EXISTS LIBRARY "
                + "(ID INTEGER PRIMARY KEY, "
                + "TYPE TEXT NOT NULL,"
                + "TITLE TEXT NOT NULL,"
                + "AUTHOR TEXT,"
                + "ISBN TEXT,"
                + "URL TEXT,"
                + "COURSE TEXT,"
                + "DELETED INTEGER);";
        try (Statement stmt = conn.createStatement()) {
            if (!existsTable("LIBRARY")) {
                stmt.execute(sq1);
                System.out.println("Table LIBRARY created.");
            }
            createCourseTables(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createCourseTables(Statement stmt) throws SQLException {
        String sq1 = "CREATE TABLE IF NOT EXISTS COURSE "
                + "(ID INTEGER PRIMARY KEY,"
                + "NAME TEXT NOT NULL)";
        String sq2 = "CREATE TABLE IF NOT EXISTS COURSE_LIBRARY "
                + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "LIBRARY_ID INTEGER NOT NULL,"
                + "COURSE_ID INTEGER NOT NULL, "
                + "FOREIGN KEY(LIBRARY_ID) REFERENCES LIBRARY(ID) ON DELETE CASCADE,"
                + "FOREIGN KEY(COURSE_ID) REFERENCES COURSE(ID) ON DELETE CASCADE)";
        if (!existsTable("COURSE")) {
            stmt.execute(sq1);
            System.out.println("Table COURSE created.");
        }
        if (!existsTable("COURSE_LIBRARY")) {
            stmt.execute(sq2);
            System.out.println("Table COURSE_LIBRARY created.");
        }
    }

    /*
     *  Remove all database tables from conn objects location.
     */
    public void deleteTable() {
        try {
            Statement s = conn.createStatement();
            if (existsTable("LIBRARY")) {
                s.execute("DROP TABLE LIBRARY");
                System.out.println("Table LIBRARY deleted.");
            }
            if (existsTable("COURSE")) {
                s.execute("DROP TABLE COURSE");
                System.out.println("Table COURSE deleted.");
            }
            if (existsTable("COURSE_LIBRARY")) {
                s.execute("DROP TABLE COURSE_LIBRARY");
                System.out.println("Table COURSE_LIBRARY deleted.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     *  Get Id from COURSE where NAME is name. 
     */
    public int getCourseId(String name) {
        int id;
        String sql = "SELECT ID FROM COURSE WHERE NAME = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            id = rs.getInt("ID");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return 1;
        }
        return id;
    }

    /*
     *  Get current index for table LIBRARY. 
     */
    public int getCurrLibraryId() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM LIBRARY";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            count = rs.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return count;
    }

    /*
     *  Get Id from LIBRARY where ISBN is isbn. 
     */
    public int getLibraryId(String isbn) {
        int id;
        String sql = "SELECT ID FROM LIBRARY WHERE ISBN = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();
            id = rs.getInt("ID");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return -1;
        }
        return id;
    }

    /*
     *  I am a placeholder table that links LIBRARY and COURSE content.
     */
    public boolean insertCL(int a, int b) {
        String sql = "INSERT INTO COURSE_LIBRARY(LIBRARY_ID, COURSE_ID) VALUES(?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, a);
            pstmt.setInt(2, b);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /*
     *  Insert entries into table LIBRARY.
     */
    public boolean insertLibrary(LibraryObject libObj) {
        String sql = "INSERT INTO LIBRARY(TYPE, TITLE, AUTHOR, ISBN, URL, COURSE, DELETED) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, libObj.getType());
            pstmt.setString(2, libObj.getTitle());
            pstmt.setString(3, libObj.getAuthor());
            pstmt.setString(4, libObj.getISBN());
            pstmt.setString(5, libObj.getURL());
            pstmt.setString(6, libObj.getCourse());
            pstmt.setInt(7, 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /*
     *  Check string %s is a unique course name in table COURSE.
     */
    public boolean isUniqueCourse(String s) {
        boolean t = false;
        String sq1 = "SELECT COUNT(*) FROM COURSE WHERE NAME = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sq1);
            s = s.split(" ")[0];
            pstmt.setString(1, s);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            if (rs.getInt(1) == 0) {
                t = true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return t;
    }

    /*
     *  Insert entries into table COURSE.
     */
    public boolean insertCourse(CourseObject courseObject) {
        String sql = "INSERT INTO COURSE(NAME) VALUES(?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseObject.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /* 
     * Check if table %name exists in database.
     */
    public boolean existsTable(String name) {
        boolean t = false;
        try (ResultSet rs = conn.getMetaData().getTables(null, null, name, null)) {
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                if (tableName != null && tableName.equals(name)) {
                    t = true;
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return t;
    }

    /*
     *  Check string %s is a valid ISBN in table LIBRARY. 
     */
    public boolean isValidISBN(String s) {
        if (s == null) {
            return false;
        }
        if (s.length() < 10 || s.length() > 13) {
            return false;
        }
        Pattern p = Pattern.compile("^\\d+$");
        return p.matcher(s).matches();
    }

    /*
     *  Check string %s is a unique ISBN in table LIBRARY.
     */
    public boolean isUnique(String s) {
        boolean t = false;
        String sq1 = "SELECT COUNT(*) FROM LIBRARY WHERE ISBN = ? AND DELETED = 0";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sq1);
            pstmt.setString(1, s);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            if (rs.getInt(1) == 0) {
                t = true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return t;
    }

    /*
     *  Remove record from LIBRARY based on its ID value. Also
     *  cascade and remove any foreign keys that reference the
     *  entry.
     */
    public void removeEntry(LibraryObject t) {
        try {
            // This is needed for cascading deletes for foreign keys.
            String sq1 = "PRAGMA FOREIGN_KEYS = ON";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sq1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        String sql = "DELETE FROM LIBRARY WHERE ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, t.getId());
            pstmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /*
     *  On startup to display all entries in LIBRARY table in database.
     *  This is redundant now that the database is created in AppUi, but
     *  you can define "test.db" there.
     */
    public void helperFunction() {
        String sq1 = "UPDATE LIBRARY SET DELETED = 0";
        try (PreparedStatement ptmt = conn.prepareStatement(sq1)) {
            ptmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     *  Hides entry instead of completely deleting it from database
     */
    public void hideEntry(LibraryObject t) {
        String sq1 = "UPDATE LIBRARY SET DELETED = 1 WHERE ID = ?";

        try (PreparedStatement ptmt = conn.prepareStatement(sq1)) {
            ptmt.setInt(1, t.getId());
            ptmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Fetch all entries of type %s from LIBRARY.
     */
    public List<LibraryObject> getAll(String s) {
        ArrayList<LibraryObject> list = new ArrayList<LibraryObject>();
        String sq1 = "SELECT * FROM LIBRARY WHERE TYPE = ? AND DELETED = 0";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sq1);
            pstmt.setString(1, s);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                LibraryObject libObj = new LibraryObject(rs.getInt("ID"), rs.getString("TYPE"), rs.getString("TITLE"), rs.getString("AUTHOR"), rs.getString("ISBN"), rs.getString("URL"), rs.getString("COURSE"));
                list.add(libObj);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    /*
     *  Fetch entries from LIBRARY, not including ID field.
     */
    @Override
    public List<LibraryObject> getAll() {
        ArrayList<LibraryObject> list = new ArrayList<LibraryObject>();
        String sq1 = "SELECT ID, TYPE, TITLE, AUTHOR, ISBN, URL, COURSE FROM LIBRARY WHERE DELETED = 0";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sq1)) {
            while (rs.next()) {
                LibraryObject libObj = new LibraryObject(rs.getInt("ID"), rs.getString("TYPE"), rs.getString("TITLE"), rs.getString("AUTHOR"), rs.getString("ISBN"), rs.getString("URL"), rs.getString("COURSE"));
                list.add(libObj);
                System.out.println("GET: \n" + libObj.toString());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    @Override
    public void save(LibraryObject t) {
        insertLibrary(t);
    }

    @Override
    public void update(LibraryObject t, String[] params) {

    }

    /*
     *  Delete record from LIBRARY based on its ID.
     */
    @Override
    public void delete(LibraryObject t) {
        String sq1 = "DELETE FROM LIBRARY WHERE ID = ?";
        try (PreparedStatement ptmt = conn.prepareStatement(sq1)) {
            ptmt.setInt(1, t.getId());
            ptmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public LibraryObject getByIsbn(String isbn) {
        ArrayList<LibraryObject> list = new ArrayList<LibraryObject>();
        String sq1 = "SELECT * FROM LIBRARY WHERE ISBN = ? AND DELETED = 0";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sq1);
            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                LibraryObject lib = new LibraryObject(rs.getInt("ID"), rs.getString("TYPE"), rs.getString("TITLE"), rs.getString("AUTHOR"), rs.getString("ISBN"), rs.getString("URL"), rs.getString("COURSE"));
                return lib;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
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
