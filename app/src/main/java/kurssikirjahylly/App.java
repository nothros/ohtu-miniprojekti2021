package kurssikirjahylly;

import tietokantaDemo.libraryObjectDAO;

import kurssikirjahylly.ui.AppUi;
import tietokantaDemo.libraryObject;

public class App {

    /*
LibraryObjectDAO:n kautta saat yhteyden lokaaliin tietokantaan.

     */
    public static void main(String[] args) {
        AppUi.main(args);
        
        /*
        //Seuraavat rivit alustavat uuden tietokannan ja lisäävät siihen olioita.
        libraryObjectDAO dao = new libraryObjectDAO("jdbc:sqlite:test.db");
        libraryObjectDAO.createNewDatabase("test.db");
        dao.createNewTable();
        dao.insert(1, "Weapons of Math Destruction","Cathy O'Neil", "123456", null);
        dao.insert(1, "Clean code","Joku Muu", "111222", null);
         */
        
        
    /*
        
        // luodaan testiobjekti joka lis�t��n tietokantaan insertill�, j�lkeenp�in poistetaan deletell�.
        
        libraryObject testObject = new libraryObject(1, "poistettava", "joku", "54321", null);
        dao.insert(testObject.getLaji(), testObject.getOtsikko(), testObject.getKirjoittaja(), testObject.getISBN(), testObject.getURL());
        dao.delete(testObject);
        
    */
                
        
        //dao.getAll();

    }

}
