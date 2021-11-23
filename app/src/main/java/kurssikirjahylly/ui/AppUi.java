package kurssikirjahylly.ui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class AppUi extends Application {
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
    public static void main(String[] args) {
        launch(args);
    }
}

