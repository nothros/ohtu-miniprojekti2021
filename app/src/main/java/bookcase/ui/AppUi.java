package bookcase.ui;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.control.TextArea;

public class AppUi extends Application {

    private Scene mainScene;
    private Scene addReadble;
    private Scene listReadbles;
    private Scene showBooks;
    private Scene addCourse;
    private Scene infoScene;
    /*
     *  I am a TEST SCENE feel free to edit me as you wish.
     */
    private Scene testScene;
    private Stage mainStage;
    private LibraryObjectDAO library;
    private LibraryService service;
    private String database;

    public AppUi() {
        //database = "bookcase.db";
        database = "test.db";
    }

    @Override
    public void init() throws SQLException {
        if(getParameters() != null){
            List<String> args = getParameters().getRaw();
            if(args.size() == 1){
                database = args.get(0);
                System.out.println("APPUI: Database:"+database);
            }
        }
        library = new LibraryObjectDAO(database);
        service = new LibraryService(library);
        service.createNewTablesIfNotExists();
        addReadble = buildAddReableScene("");
        showBooks = buildShowBooksScene();
        testScene = buildTestScene();
        addCourse = buildAddCourseScene();
        LibraryObject l = service.getByIsbn("6767676766");
        if (l == null) {
            return;
        } else {
            service.removeById(l);
        }
        infoScene = buildInfoScene(0);
    }

    public void start(Stage primaryStage) throws Exception {

        mainStage = primaryStage;
        mainStage.setTitle("Bookcase");
        mainStage.setWidth(1200);
        mainStage.setHeight(800);
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
        for (LibraryObject book : books) {
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
        mainPane.getChildren().addAll(welcome, addReadable, comboBoxAndButton, viewBooks, scrollpane, testButton);
        return new Scene(mainPane);
    }

    public Scene buildAddReableScene(String typeValue) {
        String[] listOfTitles = {"Title:", "Author:", "ISBN:", "Tags:", "Course:"};
        if (typeValue != "Book") {
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
        error.setText("Impossible to add " + typeValue);
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
            switch (typeValue) {
                case ("Book"):
                    index = "book";
                    break;
                case ("Blogpost"):
                    index = "blogpost";
                    break;
                case ("Podcast"):
                    index = "podcast";
                    break;
            }
            if (service.createLibraryObject(index, title, author, isbn_website, course)) {
                if (course.length() != 0)
                	service.createCourseObject(course, isbn_website);
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
                error.setText("Something went wrong while adding new " + typeValue);
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
    public void autoResizeColumns(TableView<LibraryObject> table) {
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getColumns().stream().forEach((column)
                -> {
            Text t = new Text(column.getText());
            double max = t.getLayoutBounds().getWidth();
            for (int i = 0; i < table.getItems().size(); i++) {
                if (column.getCellData(i) != null) {
                    t = new Text(column.getCellData(i).toString());
                    double calcwidth = t.getLayoutBounds().getWidth();
                    if (calcwidth > max) {
                        max = calcwidth;
                    }
                }
            }
            column.setPrefWidth(max + 10.0d);
        });
    }

    /*
     *  I am a test scene for the bookcase.
     */
    public Scene buildTestScene() {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(5, 5, 5, 5));
        VBox addPane = new VBox();
        addPane.setPadding(new Insets(10, 5, 5, 5));
        addPane.setAlignment(Pos.CENTER);
        //addPane.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, null)));
        TableView<LibraryObject> table = new TableView<LibraryObject>();
    	table.setPlaceholder(new Label(""));
        final ObservableList<LibraryObject> data = 
        		FXCollections.observableArrayList(service.getsAllObjects());
        BorderPane pane2 = new BorderPane();
        //pane2.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, null)));
        Text header = new Text("Reading tips:");
        header.setStyle("-fx-font: 20px Arial; -fx-stroke: grey;");
        pane2.setLeft(header);
        TableColumn<LibraryObject, String> colTitle = new TableColumn<>("Title");
        colTitle.setPrefWidth(300);
        colTitle.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTitle()));
        TableColumn<LibraryObject, String> colType = new TableColumn<>("Type");
        colType.setPrefWidth(75);
        colType.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getType())));
        TableColumn<LibraryObject, String> colTags = new TableColumn<>("Tags");
        colTags.setPrefWidth(200);
        TableColumn<LibraryObject, String> colCourse = new TableColumn<>("Related courses");
        colCourse.setPrefWidth(250);
        colCourse.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCourse()));
        table.setItems(data);
        table.getColumns().addAll(Arrays.asList(colTitle, colType, colTags, colCourse));
        //autoResizeColumns(table1);
        ScrollPane sp1 = new ScrollPane(table);
        //sp1.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, null)));
        sp1.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        sp1.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        sp1.setHmax(3);
        sp1.setFitToWidth(true);
        sp1.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        sp1.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp1.setHmax(addPane.getWidth());
        sp1.setPrefViewportWidth(addPane.getWidth());
        HBox hbox = new HBox();
        Button info = new Button("Info");
        Button remove = new Button("Remove");
        hbox.getChildren().addAll(info, remove);
        info.setOnAction(e -> {
        	if (table.getSelectionModel().getSelectedItem() != null) {
        		//System.out.println(table.getItems().get(table.getSelectionModel().getSelectedIndex()).getId());
        		int id = table.getItems().get(table.getSelectionModel().getSelectedIndex()).getId();
        		mainScene = buildInfoScene(id);
                mainStage.setScene(mainScene);
        	} else {
        		mainScene = buildTestScene();
                mainStage.setScene(mainScene);
        	}
        });
        remove.setOnAction(e -> {
        	if (table.getSelectionModel().getSelectedItem() != null) {
        		library.deleteEntry(table.getSelectionModel().getSelectedItem());
        		table.getItems().removeAll(table.getSelectionModel().getSelectedItems());
        	}
        });
        addPane.getChildren().addAll(pane2, sp1, hbox);
        Button returnB = new Button("Back");
        returnB.setId("backButton");
        returnB.setOnAction(e -> {
            mainScene = buildMainScene();
            mainStage.setScene(mainScene);
        });
        pane.setRight(returnB);
        pane.setCenter(addPane);
        Scene scene = new Scene(pane);
        return scene;
    }
    
    /*
     *  I am a library item info scene. Fix me into nice JAVAFX. I am bubble gum code.
     */
    public Scene buildInfoScene(int id) {
    	BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(5, 5, 5, 5));
        VBox addPane = new VBox();
        addPane.setPadding(new Insets(10, 5, 5, 5));
        addPane.setAlignment(Pos.CENTER);
        //addPane.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, null)));
        BorderPane pane2 = new BorderPane();
        //pane2.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, null)));
        GridPane gPane = new GridPane();
        Hyperlink hlink = new Hyperlink();
        //gPane.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, null, null)));
        if (id != 0) {
        List<LibraryObject> data = service.getAllObjects(id);
        String out = 
        			"Title: " + data.get(0).getTitle() + 
            		"\nAuthor: " + data.get(0).getAuthor() +
            		"\nType: " + data.get(0).getType() +
            		"\nISBN: " + data.get(0).getISBN() +
            		"\nTags: " +
            		"\nRelated courses: " + data.get(0).getCourse();
        //System.out.println(out);
        Text header = new Text("");
        header.setStyle("-fx-font: 30px Arial; -fx-stroke: grey;");
        pane2.setLeft(header);
        Text txt1 = new Text("Title: ");
        Text txt2 = new Text("Author: ");
        Text txt3 = new Text("Type: ");
        Text txt4, txt44;
        if (data.get(0).getType() == "Book") {
        	txt4 = new Text("ISBN: ");
        	txt44 = new Text(data.get(0).getISBN());
        	txt44.setStyle("-fx-font: 30px Arial;");
        } else {
        	txt4 = new Text("URL: ");
        	txt44 = new Text("");
        	hlink.setText(data.get(0).getURL());
        	hlink.setStyle("-fx-font: 30px Arial;");
        }
        Text txt5 = new Text("Tags: ");
        Text txt6 = new Text("Related courses: ");
        Text txt11 = new Text(data.get(0).getTitle());
        Text txt22 = new Text(data.get(0).getAuthor());
        Text txt33 = new Text(String.valueOf(data.get(0).getType()));
        Text txt55 = new Text("");
        Text txt66 = new Text(data.get(0).getCourse());
        txt1.setStyle("-fx-font: 30px Arial;");
        txt2.setStyle("-fx-font: 30px Arial;");
        txt3.setStyle("-fx-font: 30px Arial;");
        txt4.setStyle("-fx-font: 30px Arial;");
        txt5.setStyle("-fx-font: 30px Arial;");
        txt6.setStyle("-fx-font: 30px Arial;");
        txt11.setStyle("-fx-font: 30px Arial;");
        txt22.setStyle("-fx-font: 30px Arial;");
        txt33.setStyle("-fx-font: 30px Arial;");
        txt55.setStyle("-fx-font: 30px Arial;");
        txt66.setStyle("-fx-font: 30px Arial;");
        gPane.add(txt1, 0, 0, 1, 1);
        gPane.add(txt2, 0, 1, 1, 1);
        gPane.add(txt3, 0, 2, 1, 1);
        gPane.add(txt4, 0, 3, 1, 1);
        gPane.add(txt5, 0, 4, 1, 1);
        gPane.add(txt6, 0, 5, 1, 1);
        gPane.add(txt11, 1, 0, 1, 1);
        gPane.add(txt22, 1, 1, 1, 1);
        gPane.add(txt33, 1, 2, 1, 1);
        if (data.get(0).getType() == "Book") {
        	gPane.add(txt44, 1, 3, 1, 1);
        } else {
        	gPane.add(hlink, 1, 3, 1, 1);
        }
        gPane.add(txt55, 1, 4, 1, 1);
        gPane.add(txt66, 1, 5, 1, 1);
        Line line = new Line(0, 150, 200, 150);   
        line.setStrokeWidth(20); 
        line.setStroke(Color.web("000000"));
        Line line2 = new Line();
        line2.setStartX(100.0); 
        line2.setStartY(150.0); 
        line2.setEndX(500.0); 
        line2.setEndY(150.0);
        //autoResizeColumns(table1);
        HBox hbox = new HBox();
        Button edit = new Button("Edit");
        Button remove = new Button("Remove");
        hbox.getChildren().addAll(edit, remove);
        hbox.setAlignment(Pos.CENTER);
        edit.setOnAction(e -> {
        	mainScene = buildMainScene();
            mainStage.setScene(mainScene);
        });
        remove.setOnAction(e -> {
        	if (data.get(0) != null) {
        		library.deleteEntry(data.get(0));
        	}
        	mainScene = buildMainScene();
            mainStage.setScene(mainScene);
        });
        hlink.setOnAction(e -> {
        	HostServices host = getHostServices();
            host.showDocument("http://" + data.get(0).getURL());
        });
        addPane.getChildren().addAll(pane2, gPane, hbox);
        }
        Button returnB = new Button("Back");
        returnB.setOnAction(e -> {
        	mainScene = buildTestScene();
            mainStage.setScene(mainScene);
        });
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
        final ObservableList<LibraryObject> data
                = FXCollections.observableArrayList(service.getAllObjects());
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
        Button remove = new Button("Remove");
        remove.setOnAction(e -> {
        	if (table.getSelectionModel().getSelectedItem() != null) {
        		library.deleteEntry(table.getSelectionModel().getSelectedItem());
        		table.getItems().removeAll(table.getSelectionModel().getSelectedItems());
        	}
        });
        grid.add(scenetitle, 2, 0, 1, 1);
        grid.add(vbox, 2, 1, 1, 1);
        addPane.getChildren().addAll(error, grid, remove);
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
