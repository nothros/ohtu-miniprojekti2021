package tietokantaDemo;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/*
    TODO: Refaktorointi siis tässä kesken. 
    Tulen poistamaan tämän luokan; Yhteys tietokantaan libraryObjectDAO:n kautta.
    - Leo
*/
public class tietokantaDemo {
    
    private String url = "jdbc:sqlite:test.db";
    private Connection conn;

    // Luo tietokanta
    public void createNewDatabase(String fileName) {
        url = "jdbc:sqlite:" + fileName;
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Luo taulu
    public void createNewTable() {
        // SQL statement for creating a new table
        // laji saa arvokseen 1 (kirja), 2 (blogi) tai 3 (podcast)
        String sql = "CREATE TABLE libraryObjects " +
        "(id integer PRIMARY KEY, " +
        "laji integer NOT NULL," +
        "otsikko text NOT NULL," +
        "kirjoittaja text," + 
        "ISBN text UNIQUE," + 
        "URL text);";
        
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            // Taulu luodaan tässä.
            stmt.execute(sql);
            System.out.println("Table created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    // Lisää olio
    public void insert(int laji, String otsikko, String kirjoittaja, String ISBN, String URL) {
        String sql = "INSERT INTO libraryObjects(laji, otsikko, kirjoittaja, ISBN, URL) VALUES(?,?,?,?,?)";

        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, laji);
            pstmt.setString(2, otsikko);
            pstmt.setString(3, kirjoittaja);
            pstmt.setString(4, ISBN);
            pstmt.setString(5, URL);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    // Tietokantayhteys
    public Connection connect() {
        conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    
 
    // Haku
    public void selectAll(){
        String sql = "SELECT id, laji, otsikko, kirjoittaja, ISBN, URL FROM libraryObjects";
        
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" + 
                                   rs.getString("otsikko") + "\t" +
                                   rs.getString("ISBN"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
}
