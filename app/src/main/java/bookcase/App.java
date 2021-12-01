package bookcase;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import bookcase.ui.AppUi;
import database.LibraryObject;
import database.LibraryObjectDAO;

public class App {

	// private static String url = "jdbc:sqlite:test.db";
	// private static String filePath = "test.db";
	
	private static LibraryObjectDAO dao;
    /*
LibraryObjectDAO:n kautta saat yhteyden lokaaliin tietokantaan.

     */
    public static void main(String[] args) {
    	
    	/*
    	* Testaa onko tietokanta jo olemassa.
    	*/
    	/*
    	Path p = Paths.get(filePath);
    	if (Files.isRegularFile(p)) {
    		try {
    			dao = new LibraryObjectDAO(url);
    			if (!dao.hasTable("libraryObjects")) {
    				dao.createNewTable();
    			}
    		} catch (Exception e) {
    			System.out.println(e);
    		}
    	}
    	else {
    		dao = new LibraryObjectDAO(url);
    		dao.createNewDatabase(filePath);
    		dao.createNewTable();
    	}
    	*/

        AppUi.main(args);

        //LibraryObjectDAO dao = new LibraryObjectDAO();

        /* 
        
//        Seuraavat rivit alustavat uuden tietokannan ja lisäävät siihen olioita.
        LibraryObjectDAO.createNewDatabase("test.db");
        dao.createNewTable();
        dao.insert(1, "Weapons of Math Destruction","Cathy O'Neil", "123456", null);
        dao.insert(1, "Clean code","Joku Muu", "111222", null);
        
         */
        
        
    /*
        
        // luodaan testiobjekti joka lisätään tietokantaan insertillä jälkeenpäin poistetaan deletellä
        
        libraryObject testObject = new libraryObject(1, "poistettava", "joku", "54321", null);
        dao.insert(testObject.getLaji(), testObject.getOtsikko(), testObject.getKirjoittaja(), testObject.getISBN(), testObject.getURL());
        dao.delete(testObject);
        
    */
                
        
        //dao.getAll();

    }

}