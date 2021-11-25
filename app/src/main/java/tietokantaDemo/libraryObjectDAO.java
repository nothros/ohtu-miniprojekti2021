package tietokantaDemo;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/*
Luokka sisältää tällä hetkellä vain selectAll()-metodin.
Aja 'createNewDatabase("testi.db")' main-metodissa jos tarvitset uuden tietokannan.
*/
public class libraryObjectDAO implements DAO<libraryObject> {
    
    private List<libraryObject> list = new ArrayList<>();
    private String url = "jdbc:sqlite:test.db";
    private Connection conn;

    public libraryObjectDAO() {
        //tietokantaDemo td = new tietokantaDemo();
        //td.connect();
        conn = connect();
    }
    
    public libraryObjectDAO(String url) {
        this.url = url;
        conn = connect();
    }

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
        
        try (Statement stmt = conn.createStatement()) {
            // Tässä uusi tietokanta luodaan
            stmt.execute(sql);
            System.out.println("Table created");
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
            s.execute("DROP TABLE libraryObjects");
            System.out.println("Table deleted");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    // Lisää olio
    public void insert(int laji, String otsikko, String kirjoittaja, String ISBN, String URL) {
        String sql = "INSERT INTO libraryObjects(laji, otsikko, kirjoittaja, ISBN, URL) VALUES(?,?,?,?,?)";
        // prepared statement on aika kömpelö, mutta jokainen kokonaisluku viittaa yllä olevan SQL-komennon kysymysmerkkiin.
        try (PreparedStatement pstmt = conn.prepareStatement(sql))
        {    
            pstmt.setInt(1, laji);
            pstmt.setString(2, otsikko);
            pstmt.setString(3, kirjoittaja);
            pstmt.setString(4, ISBN);
            pstmt.setString(5, URL);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
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

    @Override
    public List<libraryObject> getAll() {
    
        String sql = "SELECT id, laji, otsikko, kirjoittaja, ISBN, URL FROM libraryObjects";
            
        try (Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                libraryObject libraryObjectFromDB = new libraryObject(rs.getInt("laji"), rs.getString("otsikko"), rs.getString("kirjoittaja"), rs.getString("ISBN"), rs.getString("URL"));
                list.add(libraryObjectFromDB);
                System.out.println("GET: \n" + libraryObjectFromDB.toString());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    
    
        return list;
    }

    @Override
    public void save(libraryObject t) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(libraryObject t, String[] params) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void delete(libraryObject t) {
        // TODO Auto-generated method stub
        
    }

}