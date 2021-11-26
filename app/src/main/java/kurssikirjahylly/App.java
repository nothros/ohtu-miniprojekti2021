package kurssikirjahylly;

import tietokantaDemo.LibraryObjectDAO;
import tietokantaDemo.tietokantaDemo;

import kurssikirjahylly.ui.AppUi;
import tietokantaDemo.LibraryObject;

public class App {

    private LibraryObjectDAO dao;

    public String getGreeting() {
        return "Tervehdys. Ilman minua eka testi kaatuu.";
    }


    /*
LibraryObjectDAO:n kautta saat yhteyden lokaaliin tietokantaan.

     */
    public static void main(String[] args) {
        AppUi.main(args);

        System.out.println(new App().getGreeting());
        LibraryObjectDAO dao = new LibraryObjectDAO();


        /* 
        
//        Seuraavat rivit alustavat uuden tietokannan ja lisäävät siihen olioita.
        dao.createNewDatabase("test.db");
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
                
        
        dao.getAll();

    }

}
