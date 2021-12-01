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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class LibraryObjectDAO implements DAO<LibraryObject> {

    private Connection conn;

    public LibraryObjectDAO(String url) {
        conn = connect(url);
    }

    /*
     *  If database is empty then create all database tables.
     */
    public void createNewDatabase(String fileName) {
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
    }

    /*
     *  Queries for all database tables, if they do not exist.
     */
    public void createNewTable() {
        String sq1 = "CREATE TABLE IF NOT EXISTS LIBRARY "
                + "(ID integer PRIMARY KEY, "
                + "TYPE integer NOT NULL,"
                + "TITLE text NOT NULL,"
                + "AUTHOR text,"
                + "ISBN text UNIQUE,"
                + "URL text,"
                + "deleted integer);";
        String sq2 = "CREATE TABLE IF NOT EXISTS COURSE "
                + "(ID INTEGER PRIMARY KEY,"
                + "NAME TEXT NOT NULL,"
                + "DEPARTMENT text)";
        String sq3 = "CREATE TABLE IF NOT EXISTS COURSE_LIBRARY "
                + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "LIBRARY_ID INTEGER NOT NULL,"
                + "COURSE_ID INTEGER NOT NULL, "
                + "FOREIGN KEY(LIBRARY_ID) REFERENCES LIBRARY(ID) ON DELETE CASCADE,"
                + "FOREIGN KEY(COURSE_ID) REFERENCES COURSE(ID) ON DELETE CASCADE)";
        try (Statement stmt = conn.createStatement()) {
            if (!existsTable("LIBRARY")) {
                stmt.execute(sq1);
                System.out.println("Table LIBRARY created.");
            }
            if (!existsTable("COURSE")) {
                stmt.execute(sq2);
                System.out.println("Table COURSE created.");
            }
            if (!existsTable("COURSE_LIBRARY")) {
                stmt.execute(sq3);
                System.out.println("Table COURSE_LIBRARY created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
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
        String sql = "INSERT INTO LIBRARY(TYPE, TITLE, AUTHOR, ISBN, URL, deleted) VALUES(?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, libObj.getType());
            pstmt.setString(2, libObj.getTitle());
            pstmt.setString(3, libObj.getAuthor());
            pstmt.setString(4, libObj.getISBN());
            pstmt.setString(5, libObj.getURL());
            pstmt.setInt(6, 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /*
     *  Insert entries into table COURSE.
     */
    public boolean insertCourse(CourseObject courseObject) {
        String sql = "INSERT INTO COURSE(NAME, DEPARTMENT) VALUES(?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseObject.getName());
            pstmt.setString(2, courseObject.getDepartment());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /*
     *  Database connection driver. 
     */
    public Connection connect(String url) {
        conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
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
        String sq1 = "SELECT COUNT(*) FROM LIBRARY where ISBN=?";
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
            System.out.println(t.getId());
            pstmt.setInt(1, t.getId());
            pstmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /*
    Hides an entry from the database by changing deleted from 0 to 1.
     */
    public void hideEntry(LibraryObject t) {
        String sq1 = "UPDATE LIBRARY SET deleted = 1 WHERE ID = ?";

        try {
            PreparedStatement ptmt = conn.prepareStatement(sq1);
            ptmt.setInt(1, t.getId());
            ptmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Get all items of type %s from LIBRARY.
     */
    public List<LibraryObject> getAll(String s) {
        ArrayList<LibraryObject> list = new ArrayList<LibraryObject>();
        String sql = "SELECT * FROM LIBRARY WHERE laji = ? AND deleted = 0";
        try {
            Pattern p = Pattern.compile("[123]");
            if (!p.matcher(s).matches()) {
                return List.of();
            }
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, s);
            ResultSet rs = ptmt.executeQuery();
            while (rs.next()) {
                LibraryObject libObj = new LibraryObject(rs.getInt("id"), rs.getInt("type"), rs.getString("title"), rs.getString("author"), rs.getString("ISBN"), rs.getString("URL"));
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
        String sq1 = "SELECT ID, TYPE, TITLE, AUTHOR, ISBN, URL FROM LIBRARY AND deleted = 0";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sq1)) {
            while (rs.next()) {
                LibraryObject libObj = new LibraryObject(rs.getInt("type"), rs.getString("title"), rs.getString("author"), rs.getString("ISBN"), rs.getString("URL"));
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
        try (PreparedStatement pstmt = conn.prepareStatement(sq1)) {
            pstmt.setInt(1, t.getId());
            pstmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
