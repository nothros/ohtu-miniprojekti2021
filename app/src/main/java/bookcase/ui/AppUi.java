package bookcase.ui;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.sql.rowset.serial.SerialArray;
import com.google.common.util.concurrent.Service;

import bookcase.domain.LibraryService;
import database.DAO;
import database.LibraryObject;
import database.LibraryObjectDAO;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.ResizeFeatures;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.control.TextArea;

public class AppUi extends Application {

    private Scene mainScene;
    private Scene addReadble;
    private Scene listReadbles;
    private Scene showBooks;
    private Scene addCourse;
    /*
     *  I am a TEST SCENE feel free to edit me as you wish.
     */
    private Scene testScene;
    private Stage mainStage;
    private LibraryObjectDAO library;
    private LibraryService service;
    private String database;

    public AppUi(){
        //database = "bookcase.db";
        database = "test.db";
    }

    @Override
    public void init() throws SQLException{
        library = new LibraryObjectDAO(database);
        service = new LibraryService(library);
        service.createNewTablesIfNotExists();
        addReadble = buildAddReableScene("");
        showBooks = buildShowBooksScene();
        testScene = buildTestScene();
        addCourse = buildAddCourseScene();
        LibraryObject l = service.getByIsbn("6767676766");
            if(l == null){
                return;
            }else{
                service.removeById(l);
            }
    }

    public void start(Stage primaryStage) throws Exception {
        
        mainStage = primaryStage;
        mainStage.setTitle("Bookcase");
        mainStage.setWidth(800);
        mainStage.setHeight(600);
        mainScene = buildMainScene();
        mainStage.setScene(mainScene);
        mainStage.show();
    }
    
    public Scene buildAddCourseScene() {
    	return null;
    }

    public Scene buildMainScene() {
        VBox mainPane = new VBox();
        mainPane.setSpacing(5);
        mainPane.setPadding(new Insets(5, 5, 5, 5));
        mainPane.setAlignment(Pos.CENTER);
        Label welcome = new Label("Welcome to bookcase");
        welcome.setId("welcome");
        Text addReadable = new Text("Choose what you want to add");
        HBox comboBoxAndButton = new HBox();
        comboBoxAndButton.setAlignment(Pos.CENTER);
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll(
            "Book",
            "Blogpost",
            "Podcast"
        );

        typeComboBox.getSelectionModel().selectFirst();
        Button addBook = new Button("Add new LibraryItem");
        addBook.setId("addBook");
        addBook.setOnAction(e -> {
            String typeValue = (String) typeComboBox.getValue();
            //System.out.println(typeValue);
            mainStage.setScene(buildAddReableScene(typeValue));
        });
        comboBoxAndButton.getChildren().addAll(typeComboBox, addBook);

        Button viewBooks = new Button("Show books");
        viewBooks.setOnAction(e -> {
        	showBooks = buildShowBooksScene();
            mainStage.setScene(showBooks);
        });
        Button testButton = new Button("Test");
        testButton.setStyle("-fx-text-fill: #ff0000; ");
        testButton.setOnAction(e -> {
        	testScene = buildTestScene();
            mainStage.setScene(testScene);
        });
        
        ListView<String> bookview = new ListView<String>();
        List<LibraryObject> books = service.getAllObjects();
        for (LibraryObject book : books){
            String bookString = book.toString();
            bookview.getItems().add(bookString);
        }
        
        ScrollPane scrollpane = new ScrollPane(bookview);
        scrollpane.setFitToWidth(true);
        
      /*  FlowPane mainPane = new FlowPane();
        mainPane.setPadding(new Insets(10));
        mainPane.setHgap(10);
        mainPane.setVgap(10);
        mainPane.setAlignment(Pos.CENTER);
        mainPane.getChildren().addAll(addBook, viewBooks);
        mainScene = new Scene(mainPane);*/
        
        mainPane.getChildren().addAll(welcome,addReadable,comboBoxAndButton,viewBooks, scrollpane,testButton);
        return new Scene(mainPane);
    }
    
    public Scene buildAddReableScene(String typeValue) {
        String[] listOfTitles = {"Title:", "Author:", "ISBN:", "Tags:", "Course:"};
        if (typeValue != "Book"){
            listOfTitles[2] = "Website";
        }
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(5, 5, 5, 5));
        VBox addPane = new VBox();
        addPane.setPadding(new Insets(10, 5, 5, 5));
        addPane.setAlignment(Pos.CENTER);
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Label error = new Label();
        error.setId("error");
        /*
         * Huom: Alla olevia kolmea rivi� ei kait tarvita?
        */
        error.setText("Impossible to add "+typeValue);
        error.setTextFill(Color.RED);
        error.setVisible(false);

        Text scenetitle = new Text("Add new " + typeValue);
        grid.add(scenetitle, 0, 0, 2, 1);
        for (int i = 0; i < listOfTitles.length; i++) {
            Label s = new Label(listOfTitles[i]);
            grid.add(s, 0, i + 1);
        }
        Label commentTitle = new Label("Comments:");
        grid.add(commentTitle, 0, 0, 2, 13);

        TextField titleTF = new TextField();
        titleTF.setId("title");
        TextField authorTF = new TextField();
        authorTF.setId("author");
        TextField ISBNTF = new TextField();
        ISBNTF.setId("isbn_website");
        TextField tagsTF = new TextField();
        tagsTF.setId("tags");
        TextField courseTF = new TextField();
        courseTF.setId("course");
        TextArea commentTF = new TextArea();
        commentTF.setId("comment");
        commentTF.setPrefSize(50, 200);
        commentTF.setWrapText(true);

        grid.add(titleTF, 1, 1);
        grid.add(authorTF, 1, 2);
        grid.add(ISBNTF, 1, 3);
        grid.add(tagsTF, 1, 4);
        grid.add(courseTF, 1, 5);
        grid.add(commentTF, 1, 6);

        Button createBook = new Button("Add new " + typeValue);
        createBook.setId("createBook");
        createBook.setOnAction(e -> {
            String title = titleTF.getText();
            String author = authorTF.getText();
            String isbn_website = ISBNTF.getText();
            String tags = tagsTF.getText();
            String course = courseTF.getText();
            String comment = commentTF.getText();
            String index = "";
            switch(typeValue) {
            	case("Book"):
            		index="1";
            		break;
            	case("Blogpost"):
            		index="2";
            		break;
            	case("Podcast"):
            		index="3";
            		break;
            }
            if (service.createLibraryObject(Integer.parseInt(index), title, author, isbn_website, course)) {
            	//if (course.length() != 0)
            	//	service.createCourseObject(course, isbn);
                error.setText("New " + typeValue + " added");
                error.setTextFill(Color.GREEN);
                error.setVisible(true);
         		titleTF.clear();
             	authorTF.clear();
                ISBNTF.clear();
                tagsTF.clear();
                courseTF.clear();
                commentTF.clear();
            } else {
                error.setText("Something went wrong while adding new "+typeValue);
                error.setTextFill(Color.RED);
                error.setVisible(true);
            }

        });
        grid.add(createBook, 1, 7);
        addPane.getChildren().addAll(error, grid);

        Button returnB = new Button("Back");
        returnB.setId("backButton");
        returnB.setOnAction(e -> {
            mainScene = buildMainScene();
            mainStage.setScene(mainScene);
            error.setVisible(false);
        });

        pane.setCenter(addPane);
        pane.setRight(returnB);
        
        Scene scene = new Scene(pane);
        return scene;
    }
    
    /*
     *  Auto fit the column width in table view.
     */
    public void autoResizeColumns(TableView<LibraryObject> table) 
    {
        table.setColumnResizePolicy( TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getColumns().stream().forEach( (column) ->
        {
            Text t = new Text(column.getText());
            double max = t.getLayoutBounds().getWidth();
            for (int i = 0; i < table.getItems().size(); i++)
            {
                if (column.getCellData(i) != null)
                {
                    t = new Text(column.getCellData(i).toString());
                    double calcwidth = t.getLayoutBounds().getWidth();
                    if (calcwidth > max)
                        max = calcwidth;
                }
            }
            column.setPrefWidth(max + 10.0d);
        } );
    }
    
    /*
     *  I am a test scene for the bookcase.
     */
    public Scene buildTestScene() {
    	BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(5, 5, 5, 5));
        pane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
        //pane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        VBox addPane = new VBox();
        addPane.setPadding(new Insets(10, 5, 5, 5));
        addPane.setAlignment(Pos.CENTER);
        //addPane.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
        addPane.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, null)));
    	TabPane tabPane = new TabPane();
    	tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
    	tabPane.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, null, null)));
        Tab tab1 = new Tab("Book", new Label("Show all books"));
        /////////////
        TableView<LibraryObject> table1 = new TableView<LibraryObject>();
    	table1.setPlaceholder(new Label(""));
        final ObservableList<LibraryObject> data = 
        		FXCollections.observableArrayList(service.getAllObjects("1"));
        //pane.setPadding(new Insets(5, 5, 5, 5));
        VBox addPane12 = new VBox();
        addPane12.setPadding(new Insets(10, 5, 75, 5));
        addPane12.setAlignment(Pos.CENTER);
        //addPane2.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        addPane12.setBorder(new Border(new BorderStroke(Color.PURPLE, BorderStrokeStyle.SOLID, null, null)));
        GridPane grid1 = new GridPane();
        grid1.setAlignment(Pos.CENTER);
        grid1.setPadding(new Insets(25, 25, 25, 25));
        grid1.setBorder(new Border(new BorderStroke(Color.ORANGE, BorderStrokeStyle.SOLID, null, null)));
        Label error1 = new Label();
        //Text scenetitle = new Text("Bookcase items:");
        TableColumn<LibraryObject, String> colTitle1 = new TableColumn<>("Title");
        colTitle1.setPrefWidth(100);
        colTitle1.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTitle()));
        TableColumn<LibraryObject, String> colAuthor1 = new TableColumn<>("Author");
        colAuthor1.setPrefWidth(100);
        colAuthor1.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getAuthor()));
        TableColumn<LibraryObject, String> colISBN1 = new TableColumn<>("ISBN");
        colISBN1.setPrefWidth(100);
        colISBN1.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getISBN()));
        TableColumn<LibraryObject, String> colTags1 = new TableColumn<>("Tags");
        colTags1.setPrefWidth(100);
        TableColumn<LibraryObject, String> colCourse1 = new TableColumn<>("Related courses");
        colCourse1.setPrefWidth(200);
        table1.setItems(data);
        table1.getColumns().addAll(Arrays.asList(colTitle1, colAuthor1, colISBN1, colTags1, colCourse1));
        autoResizeColumns(table1);
        ScrollPane sp1 = new ScrollPane(table1);
        sp1.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        sp1.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        sp1.setHmax(3);
        VBox vbox1 = new VBox();
        vbox1.setSpacing(5);
        vbox1.setPadding(new Insets(10, 0, 0, 10));
        vbox1.getChildren().addAll(sp1);
        Button removeBook = new Button("Remove");
        removeBook.setOnAction(e -> {
        	if (table1.getSelectionModel().getSelectedItem() != null) {
        		library.hideEntry(table1.getSelectionModel().getSelectedItem());
        		//library.removeEntry(table.getSelectionModel().getSelectedItem()); 
        		table1.getItems().removeAll(table1.getSelectionModel().getSelectedItems());
        	}
        });
        grid1.add(vbox1, 2, 1, 1, 1);
        Hyperlink myHyperlink = new Hyperlink();
        myHyperlink.setText("My Link Text");

        myHyperlink.setOnAction(e -> {
        	HostServices host = getHostServices();
            host.showDocument("http://google.com");
        });
        addPane12.getChildren().addAll(error1, grid1, removeBook,myHyperlink);
        tab1.setContent(addPane12);
        Tab tab2 = new Tab("Blog"  , new Label("Show all blogs"));
        Tab tab3 = new Tab("Podcast" , new Label("Show all podcasts"));
        Tab tab4 = new Tab("Course" , new Label("Show all courses"));
        tabPane.getTabs().add(tab1);
        tabPane.getTabs().add(tab2);
        tabPane.getTabs().add(tab3);
        tabPane.getTabs().add(tab4);
        Button returnB = new Button("Back");
        returnB.setId("backButton");
        returnB.setOnAction(e -> {
            mainScene = buildMainScene();
            mainStage.setScene(mainScene);
        });
        addPane.getChildren().addAll(tabPane);
        pane.setRight(returnB);
        pane.setCenter(addPane);
        Scene scene = new Scene(pane);
        return scene;
    }
    
    /*
     *  Generates a table view of entries in LIBRARY. Currently books (type=1).
     */
    public Scene buildShowBooksScene() {
    	TableView<LibraryObject> table = new TableView<LibraryObject>();
    	table.setPlaceholder(new Label(""));
        final ObservableList<LibraryObject> data = 
        		FXCollections.observableArrayList(service.getAllObjects());
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(5, 5, 5, 5));
        VBox addPane = new VBox();
        addPane.setPadding(new Insets(10, 5, 75, 5));
        addPane.setAlignment(Pos.CENTER);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Label error = new Label();
        error.setId("error");
        Text scenetitle = new Text("Bookcase items:");
        
        TableColumn<LibraryObject, String> colTitle = new TableColumn<>("Title");
        colTitle.setPrefWidth(100);
        colTitle.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTitle()));
        TableColumn<LibraryObject, String> colAuthor = new TableColumn<>("Author");
        colAuthor.setPrefWidth(100);
        colAuthor.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getAuthor()));
        TableColumn<LibraryObject, String> colISBN = new TableColumn<>("ISBN");
        colISBN.setPrefWidth(100);
        colISBN.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getISBN()));
        table.setItems(data);

        //ADDED COURSE
        TableColumn<LibraryObject, String> colCourse = new TableColumn<>("Course");
        colCourse.setPrefWidth(100);
        colCourse.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCourse()));
        table.setItems(data);
        table.getColumns().addAll(Arrays.asList(colTitle, colAuthor, colISBN, colCourse));
        ScrollPane sp = new ScrollPane(table);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        sp.setHmax(3);
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(sp);
        Button createBookB = new Button("Remove");

        // WILL REMOVE LIBRARY ITEM!
        createBookB.setOnAction(e -> {
        	if (table.getSelectionModel().getSelectedItem() != null) {
        		library.removeEntry(table.getSelectionModel().getSelectedItem());
        		//library.removeEntry(table.getSelectionModel().getSelectedItem()); 
        		table.getItems().removeAll(table.getSelectionModel().getSelectedItems());
        	}
        });
        grid.add(scenetitle, 2, 0, 1, 1);
        grid.add(vbox, 2, 1, 1, 1);
        addPane.getChildren().addAll(error, grid, createBookB);
        Button returnB = new Button("Back");
        returnB.setId("backButton");
        returnB.setOnAction(e -> {
        	mainScene = buildMainScene();
            mainStage.setScene(mainScene);
            error.setVisible(false);
        });
        pane.setCenter(addPane);
        pane.setRight(returnB);
        Scene scene = new Scene(pane);
        return scene;
    }


    public static void main(String[] args) {
        launch(args);
    }
    
}