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
public class LibraryObjectDAO implements DAO<LibraryObject> {
    private List<LibraryObject> list = new ArrayList<>();
    private String url = "jdbc:sqlite:test.db";
    private Connection conn;

    public LibraryObjectDAO() {
        //tietokantaDemo td = new tietokantaDemo();
        //td.connect();
        conn = connect();
    }
    
    public LibraryObjectDAO(String url) {
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
    //Välihuom. Tarvittas se service luokka sitä varten, et se tarkistas onko syotteet vaaria. (tagitkin pitas erotella)
    //Sillon voitas kayttaa booleania, eli jos lisaaminen onnistuu, niin virheilmo toimii oikein.
    public void insert(LibraryObject libraryObject) {
        String sql = "INSERT INTO libraryObjects(laji, otsikko, kirjoittaja, ISBN, URL) VALUES(?,?,?,?,?)";
        // prepared statement on aika kömpelö, mutta jokainen kokonaisluku viittaa yllä olevan SQL-komennon kysymysmerkkiin.
        try (PreparedStatement pstmt = conn.prepareStatement(sql))
        {    
            pstmt.setInt(1, libraryObject.getLaji());
            pstmt.setString(2, libraryObject.getOtsikko());
            pstmt.setString(3, libraryObject.getKirjoittaja());
            pstmt.setString(4, libraryObject.getISBN());
            pstmt.setString(5, libraryObject.getURL());
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
    public List<LibraryObject> getAll() {
    
        String sql = "SELECT id, laji, otsikko, kirjoittaja, ISBN, URL FROM libraryObjects";
            
        try (Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
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
        insert(t);
    }

    @Override
    public void update(LibraryObject t, String[] params) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void delete(LibraryObject t) { //deletointi toistaiseksi vain objektin otsikon mukaisesti
        
        String sq1 = "DELETE FROM libraryObjects WHERE otsikko = ?"; 
        
        try (PreparedStatement prepStat = conn.prepareStatement(sq1)){
            prepStat.setString(1, t.getOtsikko());
            prepStat.execute();
            
            
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        
    }

}