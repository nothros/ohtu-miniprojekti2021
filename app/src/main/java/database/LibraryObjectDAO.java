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

    public LibraryObjectDAO(String url) {
        conn = connect(url);
    }

    /*
     *  Create a database named BOOKCASE, if such does not exist.
     *  This will be saved to a file with path fileName.
     *  Further create all tables defined in createNewTable, if
     *  these do not already exist.
     */
    public void createNewDatabase(String fileName) {
        String url = "jdbc:sqlite:" + fileName;
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName() +".");
                File f = new File(fileName);
                if (f.length() == 0)
                	System.out.println("A new database has been created.");
                createNewTable();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     *  Create ALL database tables, if they do not exist.
     */
    public void createNewTable() {
        String sql1 = "CREATE TABLE IF NOT EXISTS LIBRARY " +
        "(ID integer PRIMARY KEY, " +
        "laji integer NOT NULL," +
        "otsikko text NOT NULL," +
        "kirjoittaja text," + 
        "ISBN text UNIQUE," + 
        "URL text);";
        String sql2 = "CREATE TABLE IF NOT EXISTS COURSE " +
        "(ID INTEGER PRIMARY KEY," +
        "NAME TEXT NOT NULL," +
        "DEPARTMENT text)";
        String sql3 = "CREATE TABLE IF NOT EXISTS COURSE_LIBRARY " +
        "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
        "LIBRARY_ID INTEGER NOT NULL," +
        "COURSE_ID INTEGER NOT NULL, " +
        "FOREIGN KEY(LIBRARY_ID) REFERENCES LIBRARY(ID) ON DELETE CASCADE," +
        "FOREIGN KEY(COURSE_ID) REFERENCES COURSE(ID) ON DELETE CASCADE)";
        try (Statement stmt = conn.createStatement()) {
        	if (!existsTable("LIBRARY")) {
        		stmt.execute(sql1);
        		System.out.println("Table LIBRARY created.");
        	}
        	if (!existsTable("COURSE")) {
        		stmt.execute(sql2);
        		System.out.println("Table COURSE created.");
        	}
        	if (!existsTable("COURSE_LIBRARY")) {
        		stmt.execute(sql3);
        		System.out.println("Table COURSE_LIBRARY created.");
        	}
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
    Poistaa libraryObjects taulukon conn olion osoittamasta tietokannasta
    */
    public void deleteTable(){
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
     *  Insert items into table LIBRARY
     */
    public boolean insertLibrary(LibraryObject libraryObject) {
        String sql = "INSERT INTO LIBRARY(laji, otsikko, kirjoittaja, ISBN, URL) VALUES(?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {    
            pstmt.setInt(1, libraryObject.getLaji());
            pstmt.setString(2, libraryObject.getOtsikko());
            pstmt.setString(3, libraryObject.getKirjoittaja());
            pstmt.setString(4, libraryObject.getISBN());
            pstmt.setString(5, libraryObject.getURL());
            pstmt.executeUpdate();
        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    /*
     *  Insert items into table COURSE.
     */
    public boolean insertCourse(CourseObject courseObject) {
        String sql = "INSERT INTO COURSE(name, department) VALUES(?,?)";
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
     *  Check string %s is a valid ISBN. 
    */
    public boolean isValidISBN(String s) {
        if (s == null)
            return false;
        if (s.length() < 10 || s.length() > 13)
        	return false;
        Pattern p = Pattern.compile("^\\d+$");
        return p.matcher(s).matches();
    }
    
    /*
     *  Check string %s is a unique ISBN in table LIBRARY.
    */
    public boolean isUnique(String s) {
    	boolean t = false;
        String sql = "SELECT COUNT(*) FROM LIBRARY where ISBN=?";
        try {
        	PreparedStatement ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, s);
            ResultSet rs = ptmt.executeQuery();
            rs.next();
            if (rs.getInt(1) == 0)
            	t = true;
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        } 
        return t;
    }
    
    /*
     *  Remove record from LIBRARY based on its ID value.
     */
    public void removeEntry(LibraryObject t) {     
    	try {
    		// This is needed for cascading deletes for foreign keys.
    		String sql = "PRAGMA FOREIGN_KEYS = ON";   
    		Statement stmt = conn.createStatement();
            int rs = stmt.executeUpdate(sql);
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        String sql = "DELETE FROM LIBRARY WHERE ID = ?"; 
        try (PreparedStatement ptmt = conn.prepareStatement(sql)){
        	System.out.println(t.getId());
            ptmt.setInt(1, t.getId());
            ptmt.execute();
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    /*
     * Get all items of type %s from LIBRARY.
    */
    public List<LibraryObject> getAll(String s) {
        ArrayList<LibraryObject> list = new ArrayList<LibraryObject>();
        String sql = "SELECT * FROM LIBRARY WHERE laji = ?";
        try {
        	Pattern p = Pattern.compile("[123]");
            if (!p.matcher(s).matches())
            	return List.of();
        	PreparedStatement ptmt  = conn.prepareStatement(sql);
        	ptmt.setString(1, s);
            ResultSet rs = ptmt.executeQuery();
            while (rs.next()) {
                LibraryObject libObj = new LibraryObject(rs.getInt("id"), rs.getInt("laji"), rs.getString("otsikko"), rs.getString("kirjoittaja"), rs.getString("ISBN"), rs.getString("URL"));
                list.add(libObj);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    @Override
    public List<LibraryObject> getAll() {
        ArrayList<LibraryObject> list = new ArrayList();
        String sql = "SELECT id, laji, otsikko, kirjoittaja, ISBN, URL FROM LIBRARY";
        try (Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql)){
            while (rs.next()) {
                LibraryObject libraryObjectFromDB = new LibraryObject(rs.getInt("laji"), rs.getString("otsikko"), rs.getString("kirjoittaja"), rs.getString("ISBN"), rs.getString("URL"));
                list.add(libraryObjectFromDB);
                System.out.println("GET: \n" + libraryObjectFromDB.toString());
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
     *  Delete record based on its id.
     */
    @Override
    public void delete(LibraryObject t) {
        String sq1 = "DELETE FROM LIBRARY WHERE ID = ?"; 
        try (PreparedStatement prepStat = conn.prepareStatement(sq1)){
            prepStat.setInt(1, t.getId());
            prepStat.execute();
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}