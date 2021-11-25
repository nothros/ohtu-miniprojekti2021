package kurssikirjahylly;
import tietokantaDemo.libraryObjectDAO;
import tietokantaDemo.tietokantaDemo;

import kurssikirjahylly.ui.AppUi;


public class App {
    public String getGreeting() {
        return "Tervehdys. Ilman minua eka testi kaatuu.";
    }

/*
Luokka luo tietokannan /app/test.db ja lisää siihen kaksi oliota.
Poista k. tiedosto jos haluat nollata.
*/
    public static void main(String[] args) {
        AppUi.main(args);

        System.out.println(new App().getGreeting());
        libraryObjectDAO dao = new libraryObjectDAO();
        dao.getAll();


    }
}
