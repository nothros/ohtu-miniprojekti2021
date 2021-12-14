package database;

import java.io.File;
import java.sql.Connection;
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
    private TagDAO tagdao;
    private CourseDAO coursedao;
    
    public LibraryObjectDAO(Connection conn) throws SQLException {
        this.conn = conn;
        this.tagdao = new TagDAO(conn);
        this.coursedao = new CourseDAO(conn);

    }

    /*
     *  Queries for all database tables, if they do not exist.
     */
    public void createNewTable() {
        String sq1 = "CREATE TABLE IF NOT EXISTS LIBRARY "
                + "(ID INTEGER PRIMARY KEY, " + "TYPE TEXT NOT NULL,"
                + "TITLE TEXT NOT NULL," + "AUTHOR TEXT,"
                + "ISBN TEXT," + "URL TEXT,"
                + "COMMENT TEXT," + "DELETED INTEGER);";
        try (Statement stmt = conn.createStatement()) {
        	stmt.execute(sq1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        tagdao.createNewTable();
        coursedao.createNewTable();
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
        int id = 0;
        String sql = "SELECT ID FROM LIBRARY WHERE ISBN = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next())
            	id = rs.getInt("ID");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return id;
    }

    /*
     *  Insert entries into table LIBRARY.
     */
    public boolean insertLibrary(LibraryObject libObj) {
        String sq1 = "INSERT INTO LIBRARY(TYPE, TITLE, AUTHOR, ISBN, URL, COMMENT, DELETED) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sq1)) {
            pstmt.setString(1, libObj.getType());
            pstmt.setString(2, libObj.getTitle());
            pstmt.setString(3, libObj.getAuthor());
            pstmt.setString(4, libObj.getISBN());
            pstmt.setString(5, libObj.getURL());
            pstmt.setString(6, libObj.getComment());            
            pstmt.setInt(7, 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    /*
     *  Update table LIBRARY entries on edit.
     */
    public boolean updateLibrary(LibraryObject libObj) {
    	String sq1 = "UPDATE LIBRARY SET TITLE=?, AUTHOR=?, ISBN=?, URL=?, COMMENT=? WHERE ID=?";
    	try (PreparedStatement pstmt = conn.prepareStatement(sq1)) {
            pstmt.setString(1, libObj.getTitle());
            pstmt.setString(2, libObj.getAuthor());
            pstmt.setString(3, libObj.getISBN());
            pstmt.setString(4, libObj.getURL());
            pstmt.setString(5, libObj.getComment());   
            pstmt.setInt(6, libObj.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    	
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
     *  Hides the entry instead of completely deleting it from database
     */
    public void deleteEntry(LibraryObject t) {
        String sq1 = "UPDATE LIBRARY SET DELETED = 1 WHERE ID = ?";

        try (PreparedStatement ptmt = conn.prepareStatement(sq1)) {
            ptmt.setInt(1, t.getId());
            ptmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Fetch entry with ID %id from LIBRARY.
     */
    public List<LibraryObject> getAll(int id) {
        ArrayList<LibraryObject> list = new ArrayList<LibraryObject>();
        String sq1 = "SELECT * FROM LIBRARY WHERE ID = ? AND DELETED = 0";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sq1);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                LibraryObject libObj = new LibraryObject(rs.getInt("ID"), rs.getString("TYPE"), rs.getString("TITLE"), rs.getString("AUTHOR"), rs.getString("ISBN"), rs.getString("URL"), rs.getString("COMMENT"));
                list.add(libObj);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    /*
     *  Fetch all entries from LIBRARY.
     */
    @Override
    public List<LibraryObject> getAll() {
        ArrayList<LibraryObject> list = new ArrayList<LibraryObject>();
        String sq1 = "SELECT * FROM LIBRARY WHERE DELETED = 0";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sq1)) {
            while (rs.next()) {
                LibraryObject libObj = new LibraryObject(rs.getInt("ID"), rs.getString("TYPE"), rs.getString("TITLE"), rs.getString("AUTHOR"), rs.getString("ISBN"), rs.getString("URL"), rs.getString("COMMENT"));
                list.add(libObj);
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

    public TagDAO getTagDao() {
        return tagdao;
    }
    
    public CourseDAO getCourseDao() {
        return coursedao;
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
