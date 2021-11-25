package kurssikirjahylly;
import tietokantaDemo.libraryObjectDAO;
import tietokantaDemo.tietokantaDemo;

import kurssikirjahylly.ui.AppUi;


public class App {

    private libraryObjectDAO dao;
    public String getGreeting() {
        return "Tervehdys. Ilman minua eka testi kaatuu.";
    }
    

/*
LibraryObjectDAO:n kautta saat yhteyden lokaaliin tietokantaan.

*/
    public static void main(String[] args) {
        AppUi.main(args);
        
        System.out.println(new App().getGreeting());
        libraryObjectDAO dao = new libraryObjectDAO();
        
     /* 
        // Seuraavat rivit alustavat uuden tietokannan ja lisäävät siihen olioita.
        dao.createNewDatabase("test.db");
        dao.createNewTable();
        dao.insert(1, "Weapons of Math Destruction","Cathy O'Neil", "123456", null);
        dao.insert(1, "Clean code","Joku Muu", "111222", null);
     */
        dao.getAll();
        

    }

    
    
}
