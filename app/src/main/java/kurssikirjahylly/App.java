package kurssikirjahylly;

import tietokantaDemo.tietokantaDemo;

public class App {
    public String getGreeting() {
        return "Tervehdys. Ilman minua eka testi kaatuu.";
    }

/*
Luokka luo tietokannan /app/test.db ja lisää siihen kaksi oliota.
Poista k. tiedosto jos haluat nollata.
*/
    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
        tietokantaDemo td = new tietokantaDemo();
        td.createNewDatabase("test");
        td.createNewTable();
        td.insert("Weapons of Math Destruction");
        td.insert("Clean Code");
        td.connect();
        td.selectAll();
    }
}
