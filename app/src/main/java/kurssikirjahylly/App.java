package kurssikirjahylly;

import tietokantaDemo.tietokantaDemo;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class App extends Application{
    public String getGreeting() {
        return "Tervehdys. Ilman minua eka testi kaatuu.";
    }

    // Olen täällä javafx:ää varten. ja keulin nyt kaiken muun edelle
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hello world Application");
        primaryStage.setWidth(300);
        primaryStage.setHeight(200);
    
        Label helloWorldLabel = new Label("Hello world!");
        helloWorldLabel.setAlignment(Pos.CENTER);
        Scene primaryScene = new Scene(helloWorldLabel);
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }

/*
Luokka luo tietokannan /app/test.db ja lisää siihen kaksi oliota.
Poista k. tiedosto jos haluat nollata.
*/
    public static void main(String[] args) {
        Application.launch(args);
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
