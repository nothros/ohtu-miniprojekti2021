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
import java.util.regex.Pattern;

/*
Luokka sisältää tällä hetkellä vain selectAll()-metodin.
Aja 'createNewDatabase("testi.db")' main-metodissa jos tarvitset uuden tietokannan.
*/
public class LibraryObjectDAO implements DAO<LibraryObject> {
    private Connection conn;

    public LibraryObjectDAO(String url) {
        conn = connect(url);
    }

    // Luo tietokanta
    public static void createNewDatabase(String fileName) {
        String url = "jdbc:sqlite:" + fileName;
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
    public boolean insert(LibraryObject libraryObject) {
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
            return false;
        }
        return true;
    }

    // Tietokantayhteys
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
     * Tarkista onko taulu %name jo olemassa tietokannassa.
    */
    public boolean hasTable(String name) {
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
     * Tarkista %s on validi ISBN arvo. 
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
     * Tarkista %s arvo on uniikki taulussa %table.
    */
    public boolean isUnique(String s, String table) {
    	boolean t = false;
        String sql = "SELECT COUNT(*) FROM " + table + " where ISBN=?";
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
     * Poista kannasta tietue %t sen ISBN arvon mukaan. 
     */
    public void removeEntry(LibraryObject t) {         
        String sq1 = "DELETE FROM libraryObjects WHERE ISBN = ?"; 
        try (PreparedStatement ptmt = conn.prepareStatement(sq1)){
            ptmt.setString(1, t.getISBN());
            ptmt.execute();
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    /*
     * Hae kannasta kaikki lajia %s olevat objektit. 
    */
    public List<LibraryObject> getAll(String s) {
        ArrayList<LibraryObject> list = new ArrayList<LibraryObject>();
        String sql = "SELECT * FROM libraryObjects WHERE laji = ?";
        try {
        	Pattern p = Pattern.compile("[123]");
            if (!p.matcher(s).matches())
            	return List.of();
        	PreparedStatement ptmt  = conn.prepareStatement(sql);
        	ptmt.setString(1, s);
            ResultSet rs = ptmt.executeQuery();
            while (rs.next()) {
                LibraryObject libObj = new LibraryObject(rs.getInt("laji"), rs.getString("otsikko"), rs.getString("kirjoittaja"), rs.getString("ISBN"), rs.getString("URL"));
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