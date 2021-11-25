package kurssikirjahylly.ui;


import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.control.TextArea;
public class AppUi extends Application {
     // Olen täällä javafx:ää varten. ja keulin nyt kaiken muun edelle
        private Scene mainScene;
        private Scene addReadble;
        private Scene listReadbles;
        private Stage mainStage;
    
        @Override
        public void init() {
            //Initoi tähän tietokanta ja luokka ja service, muista importtaa
    
            mainScene=buildMainScene();
            addReadble=buildAddReableScene();
    
        }
    
        public void start(Stage primaryStage) throws Exception {

    
         mainStage = primaryStage;
            mainStage.setTitle("Kurssikirjahylly");
            mainStage.setWidth(800);
            mainStage.setHeight(600);
            mainStage.setScene(mainScene);
            mainStage.show();  
         }
    
        public Scene buildMainScene(){
            VBox mainPane = new VBox();
            mainPane.setAlignment(Pos.CENTER);
            Button addBook = new Button("Add new book");
            addBook.setOnAction(e -> {
                mainStage.setScene(addReadble);
            }); 
            mainPane.getChildren().addAll(addBook);
            mainScene = new Scene(mainPane);
            return mainScene;
        }
    
        public Scene buildAddReableScene(){
            BorderPane pane = new BorderPane();
            pane.setPadding(new Insets(5, 5, 5, 5));
            VBox addPane = new VBox();
            addPane.setPadding(new Insets(10, 5, 5, 5));
            addPane.setAlignment(Pos.CENTER);
            String [] listOfTitles = {"Title:", "Author:", "Type:", "ISBN/website:","Tags:","Course:"};
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(25, 25, 25, 25));
            Label error = new Label();
            error.setText("ERROR");
            error.setTextFill(Color.RED);
            error.setVisible(false);

            Text scenetitle = new Text("Add new Book");
            grid.add(scenetitle, 0, 0, 2, 1);
            for(int i = 0; i < listOfTitles.length; i++){
                Label s = new Label(listOfTitles[i]);
                grid.add(s, 0, i+1);
            }
            Label commentTitle  = new Label("Comments:");
            grid.add(commentTitle, 0, 0, 2, 12);

            TextField titleTF = new TextField();
            titleTF.setId("title");
            TextField authorTF = new TextField();
            titleTF.setId("author");
            TextField typeTF = new TextField();
            titleTF.setId("type");
            TextField ISBNTF = new TextField();
            titleTF.setId("isbn");
            TextField tagsTF = new TextField();
            titleTF.setId("tags");
            TextField courseTF = new TextField();
            titleTF.setId("course");
            TextArea commentTF = new TextArea();
            titleTF.setId("comment");
            commentTF.setPrefSize(50, 200);
            commentTF.setWrapText(true);

            grid.add(titleTF, 1, 1);
            grid.add(authorTF, 1, 2);
            grid.add(typeTF, 1, 3);
            grid.add(ISBNTF, 1, 4);
            grid.add(tagsTF, 1, 5);
            grid.add(courseTF, 1, 6);
            grid.add(commentTF, 1, 7);
            Button createBookB = new Button("Add new book!");
            createBookB.setOnAction(e -> {
                String title=titleTF.getText();
                String author = authorTF.getText();
                String isbn = ISBNTF.getText();
                String tags = tagsTF.getText();
                String course = courseTF.getText();
                String comment = commentTF.getText();
                if(title.isEmpty()){
                    error.setVisible(true);
                }else{
                    error.setText("Kirja lisätty");
                     error.setTextFill(Color.GREEN);
                    error.setVisible(true);
                }

            }); 
            grid.add(createBookB, 1, 8);
            addPane.getChildren().addAll(error, grid);

            Button returnB = new Button("Palaa takaisin");
            returnB.setOnAction(e -> {
                mainStage.setScene(mainScene);
                error.setVisible(false);
            }); 

            pane.setCenter(addPane);
            pane.setRight(returnB);
            Scene scene = new Scene(pane);
            return scene;
        }
    
        public Scene buildListReableScene(){
            VBox addPane = new VBox();
            addPane.setPadding(new Insets(10, 5, 5, 5));
            addPane.setAlignment(Pos.CENTER);
            String [] listOfTitles = {"Title:", "Author:", "Type:", "ISBN/website:","Tags:","Course:"};
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(25, 25, 25, 25));
            Label error = new Label();
            error.setText("ERROR");
            error.setTextFill(Color.RED);
            error.setVisible(false);

            Text scenetitle = new Text("Add new Book");
            grid.add(scenetitle, 0, 0, 2, 1);
            for(int i = 0; i < listOfTitles.length; i++){
                Label s = new Label(listOfTitles[i]);
                grid.add(s, 0, i+1);
            }
            Label commentTitle  = new Label("Comments:");
            grid.add(commentTitle, 0, 0, 2, 12);

            TextField titleTF = new TextField();
            titleTF.setId("title");
            TextField authorTF = new TextField();
            titleTF.setId("author");
            TextField typeTF = new TextField();
            titleTF.setId("type");
            TextField ISBNTF = new TextField();
            titleTF.setId("isbn");
            TextField tagsTF = new TextField();
            titleTF.setId("tags");
            TextField courseTF = new TextField();
            titleTF.setId("course");
            TextArea commentTF = new TextArea();
            titleTF.setId("comment");
            commentTF.setPrefSize(50, 200);
            commentTF.setWrapText(true);

            grid.add(titleTF, 1, 1);
            grid.add(authorTF, 1, 2);
            grid.add(typeTF, 1, 3);
            grid.add(ISBNTF, 1, 4);
            grid.add(tagsTF, 1, 5);
            grid.add(courseTF, 1, 6);
            grid.add(commentTF, 1, 7);
            Button createBookB = new Button("Add new book!");
            createBookB.setOnAction(e -> {
                String title=titleTF.getText();
                String author = authorTF.getText();
                String isbn = ISBNTF.getText();
                String tags = tagsTF.getText();
                String course = courseTF.getText();
                String comment = commentTF.getText();
                if(title.isEmpty()){
                    error.setVisible(true);
                }else{
                    error.setText("Kirja lisätty");
                     error.setTextFill(Color.GREEN);
                    error.setVisible(true);
                }

            }); 
            grid.add(createBookB, 1, 8);
            addPane.getChildren().addAll(error, grid);
            Scene scene = new Scene(addPane);
            return scene;
        }
    public static void main(String[] args) {
        launch(args);
    }
}

