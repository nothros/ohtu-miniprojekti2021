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

public class TagDAO {

    private Connection conn;
    private String url;

    public TagDAO(String dbname) throws SQLException {
        this.conn = DriverManager.getConnection("jdbc:sqlite:" + dbname);
    }

    public TagDAO(Connection conn) {
        this.conn = conn;
    }

    private Connection getConnection() throws SQLException {
        return conn;    //DriverManager.getConnection(url);
    }

    /* Creates tag tables in database*/
    public void createNewTable() {
        String sq1 = "CREATE TABLE IF NOT EXISTS TAGS (ID INTEGER PRIMARY KEY, NAME TEXT NOT NULL)";
        String sq2 = "CREATE TABLE IF NOT EXISTS TAG_LIBRARY (ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "LIBRARY_ID INTEGER NOT NULL,"
                + "TAG_ID INTEGER NOT NULL, "
                + "FOREIGN KEY(LIBRARY_ID) REFERENCES LIBRARY(ID) ON DELETE CASCADE,"
                + "FOREIGN KEY(TAG_ID) REFERENCES TAGS(ID) ON DELETE CASCADE)";
        try (Statement stmt = getConnection().createStatement()) {
            tryCreateTable(stmt, sq1, "TAGS");
            tryCreateTable(stmt, sq2, "TAG_LIBRARY");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void tryCreateTable(Statement stmt, String sql, String name) {
        try {
            stmt.execute(sql);
            //System.out.println("Table " + name + " created.");
        } catch (SQLException e) {
            System.out.println("Failed to create " + name + " table.");
        }
    }

    /**
     * Returns a list of tags for a specific library object
     *
     * @param libraryObjectId id of object in library table, get this from
     * libraryObjectDao
     */
    public ArrayList<String> getTags(int libraryObjectId) {
        ArrayList<String> list = new ArrayList<String>();
        String sq1 = "SELECT b.NAME FROM TAG_LIBRARY a, TAGS b WHERE b.ID = a.TAG_ID AND a.LIBRARY_ID = ?";
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
     * Returns a list of tags for a specific library object
     *
     * @param libraryObjectId id of object in library table, get this from
     * libraryObjectDao
     */
    public ArrayList<String> getTagAndId(int libraryObjectId) {
        ArrayList<String> list = new ArrayList<String>();
        String sq1 = "SELECT b.id, b.NAME FROM TAG_LIBRARY a, TAGS b WHERE b.ID = a.TAG_ID AND a.LIBRARY_ID = ?";
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
     * Returns a list of all tags connected to any library object.
     */
    public ArrayList<String> getAllTags() {
        ArrayList<String> list = new ArrayList<String>();
        String sq1 = "SELECT DISTINCT b.NAME FROM TAG_LIBRARY a, TAGS b WHERE b.ID = a.TAG_ID";
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
     * Add a tag to a library object
     *
     * @param libraryObjectId id of object in library table, get this from
     * libraryObjectDao
     * @param tag new tag
     */
    public boolean addTag(int libraryObjectId, String tag) {
        int tagId = getTagId(tag);
        if (tagId == -1) { //tag not found yet, add to table
            if (!insertTag(tag)) { //try to insert
                return false;
            }
            tagId = getTagId(tag);
            if (tagId == -1) {    //failed to getTagId
                return false;
            }
        }

        return connectTagToLibrary(libraryObjectId, tagId);
    }

    private boolean connectTagToLibrary(int libraryId, int tagId) {
        String sql = "INSERT INTO TAG_LIBRARY(LIBRARY_ID, TAG_ID) VALUES(?,?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, libraryId);
            pstmt.setInt(2, tagId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /* Returns id of tag, -1 if not found*/
    private int getTagId(String name) {
        String sql = "SELECT ID FROM TAGS WHERE NAME = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
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

    /* Insert tag to table TAGS*/
    private boolean insertTag(String tag) {
        String sql = "INSERT INTO TAGS(NAME) VALUES(?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, tag);
            pstmt.executeUpdate();
            //System.out.println("insertTag: TAG ADDED");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteFromTagLibrary(int libId){
        String sql = "DELETE FROM TAG_LIBRARY WHERE LIBRARY_ID=?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, libId);
            pstmt.executeUpdate();
            //System.out.println("insertTag: TAG ADDED");
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
