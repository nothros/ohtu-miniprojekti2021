package bookcase.ui;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;

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
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.control.TextArea;

public class AppUi extends Application {

    private Scene mainScene;
    private Scene addReadble;
    private Scene listReadbles;
    private Scene infoScene;
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
            }
        }
        library = new LibraryObjectDAO(database);
        service = new LibraryService(library);
        service.createNewTablesIfNotExists();
        addReadble = buildAddReableScene("");
    }

    public void start(Stage primaryStage) throws Exception {

        mainStage = primaryStage;
        mainStage.setTitle("Bookcase");
        mainStage.setWidth(937);
        mainStage.setHeight(800);
        mainScene = buildMainScene();
        mainStage.setScene(mainScene);
        mainStage.show();
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
        scenetitle.setStyle("-fx-font: 15px Arial; -fx-font-weight: bold;");
        grid.add(scenetitle, 0, 0, 2, 1);
        for (int i = 0; i < listOfTitles.length; i++) {
            Label s = new Label(listOfTitles[i]);
            grid.add(s, 0, i + 1);
        }
        Label commentTitle = new Label("Comments:");
        grid.add(commentTitle, 0, 6);
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
        grid.add(commentTF,1, 6);

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
            if (service.createLibraryObject(index, title, author, isbn_website, course, comment)) {
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
     *  Main scene for bookcase app.
     */
    public Scene buildMainScene() {
    	final ObservableList<LibraryObject> data = FXCollections.observableArrayList(service.getAllObjects());
        FilteredList<LibraryObject> flLibObj = new FilteredList<LibraryObject>(data, p -> true);
        SortedList<LibraryObject> sortedData = new SortedList<LibraryObject>(flLibObj);
        TableView<LibraryObject> table = new TableView<LibraryObject>();
		sortedData.comparatorProperty().bind(table.comparatorProperty());
		
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(5, 5, 5, 5));
        VBox addPane = new VBox();
        addPane.setPadding(new Insets(5, 5, 5, 5));
        addPane.setAlignment(Pos.CENTER);
        Label welcome = new Label("Welcome to bookcase");
        welcome.setStyle("-fx-font: 30px Arial; -fx-stroke: grey; -fx-font-weight: bold;");
        welcome.setId("welcome");
    	table.setPlaceholder(new Label(""));
        BorderPane pane2 = new BorderPane();
        pane2.setPadding(new Insets(20, 0, 0, 0));
        //  Add library items button.
        HBox comboBoxAndButton = new HBox();
        comboBoxAndButton.setPadding(new Insets(40, 0, 0, 0));
        comboBoxAndButton.setAlignment(Pos.CENTER_LEFT);
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.setId("combobox");
        typeComboBox.setMinWidth(100);
        typeComboBox.getItems().addAll("Book", "Blogpost", "Podcast");
        typeComboBox.getSelectionModel().selectFirst();
        Button addBook = new Button("Add");
        addBook.setMinWidth(50);
        addBook.setId("addBook");
        addBook.setOnAction(e -> {
            String typeValue = (String) typeComboBox.getValue();
            mainStage.setScene(buildAddReableScene(typeValue));
        });
        comboBoxAndButton.getChildren().addAll(typeComboBox, addBook);
        // Search library items.
        HBox searchButton = new HBox();
        searchButton.setAlignment(Pos.CENTER_LEFT);
        ComboBox<String> searchComboBox = new ComboBox<String>();
        searchComboBox.setMinWidth(100);
        searchComboBox.getItems().addAll("Title", "Author", "Type");
        searchComboBox.getSelectionModel().selectFirst();
        Button searchBook = new Button("Search");
        searchBook.setMinWidth(100);
        searchBook.setId("searchBook");
        searchBook.setOnAction(e -> {
            String typeValue = (String) searchComboBox.getValue();
            mainStage.setScene(buildAddReableScene(typeValue));
        });
        TextField textField = new TextField();
        textField.setMinWidth(250);
        textField.setPromptText("Enter search here!");
        searchButton.getChildren().addAll(searchBook);
        Label header = new Label("Reading tips:");
        header.setStyle("-fx-font: 20px Arial; -fx-font-weight: bold;");
        pane2.setLeft(header);
        TableColumn<LibraryObject, String> colTitle = new TableColumn<>("Title");
        colTitle.setMinWidth(250);
        colTitle.setMaxWidth(250);
        colTitle.setSortable(true);
        colTitle.setStyle("-fx-alignment: CENTER_LEFT;");
        colTitle.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTitle()));
        TableColumn<LibraryObject, String> colAuthor = new TableColumn<>("Author");
        colAuthor.setMinWidth(150);
        colAuthor.setMaxWidth(150);
        colAuthor.setStyle("-fx-alignment: CENTER_LEFT;");
        colAuthor.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getAuthor()));
        TableColumn<LibraryObject, String> colType = new TableColumn<>("Type");
        colType.setMinWidth(60);
        colType.setMaxWidth(60);
        colType.setSortable(false);
        colType.setStyle("-fx-alignment: CENTER_LEFT;");
        colType.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getType())));
        TableColumn<LibraryObject, String> colTags = new TableColumn<>("Tags");
        colTags.setStyle("-fx-alignment: CENTER_LEFT;");
        colTags.setSortable(false);
        TableColumn<LibraryObject, String> colCourse = new TableColumn<>("Courses");
        colCourse.setStyle("-fx-alignment: CENTER_LEFT;");
        colCourse.setSortable(false);
        colCourse.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCourse()));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Add extra column for a show detailed info button
        TableColumn<LibraryObject, Void> colButton = new TableColumn<LibraryObject, Void>("Detais");
        Callback<TableColumn<LibraryObject, Void>, TableCell<LibraryObject, Void>> cellFactory = new Callback<TableColumn<LibraryObject, Void>, TableCell<LibraryObject, Void>>() {
            @Override
            public TableCell<LibraryObject, Void> call(final TableColumn<LibraryObject, Void> param) {
                final TableCell<LibraryObject, Void> cell = new TableCell<LibraryObject, Void>() {
                    Button infoButton = new Button("show");
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                        	infoButton.setMinWidth(50);
                            setGraphic(infoButton);
                            infoButton.setOnAction(e -> {
                                LibraryObject data = getTableView().getItems().get(getIndex());
                        		mainScene = buildInfoScene(data.getId());
                                mainStage.setScene(mainScene);
                            });
                        }
                    }
                };
                return cell;
            }
        };
        colButton.setCellFactory(cellFactory);
        colButton.setStyle( "-fx-alignment: CENTER;");
        colButton.setMinWidth(80);
        colButton.setMaxWidth(80);
        colButton.setSortable(false);
        // Search box listener that automatically updates on textfield entry.
        textField.textProperty().addListener((obs, oldVal, newVal) -> {
            switch (searchComboBox.getValue())
            {
                case "Title":
                    flLibObj.setPredicate(p -> p.getTitle().toLowerCase().contains(newVal.toLowerCase().trim()));
                    break;
                case "Author":
                    flLibObj.setPredicate(p -> p.getAuthor().toLowerCase().contains(newVal.toLowerCase().trim()));
                    break;
                case "Type":
                    flLibObj.setPredicate(p -> p.getType().toLowerCase().contains(newVal.toLowerCase().trim()));
                    break;
            }
        });
        searchComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null)
                textField.setText("");
        });
        table.setItems(sortedData);
        table.getColumns().addAll(Arrays.asList(colTitle, colAuthor, colType, colTags, colCourse));
        table.getColumns().add(colButton);
        HBox searchBox = new HBox(searchComboBox, textField);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        ScrollPane sp1 = new ScrollPane(table);
        sp1.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp1.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        sp1.setFitToWidth(true);
        sp1.setHmax(addPane.getWidth());
        sp1.setPrefViewportWidth(addPane.getWidth());  
        addPane.getChildren().addAll(welcome, comboBoxAndButton, searchBox, pane2, sp1);
        return new Scene(addPane);
    }
    
    /*
     *  Detailed library item info scene.
     */
    public Scene buildInfoScene(int id) {
    	BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(5, 5, 5, 5));
        VBox addPane = new VBox();
        addPane.setPadding(new Insets(10, 5, 5, 5));
        addPane.setAlignment(Pos.CENTER);
        BorderPane pane2 = new BorderPane();
        GridPane gPane = new GridPane();
        Hyperlink hlink = new Hyperlink();
        if (id != 0) {
        List<LibraryObject> data = service.getAllObjects(id);
        Text header = new Text("Detailed information:");
        header.setStyle("-fx-font: 20px Arial; -fx-font-weight: bold;");
        pane2.setLeft(header);
        Text txt1 = new Text("Title: ");
        Text txt2 = new Text("Author: ");
        Text txt3 = new Text("Type: ");
        Text txt4, txt44;
        if (data.get(0).getType().equals("book")) {
        	txt4 = new Text("ISBN: ");
        	txt44 = new Text(data.get(0).getISBN());
        	txt44.setStyle("-fx-font: 20px Arial;");
        } else {
        	txt4 = new Text("URL: ");
        	txt44 = new Text("");
        	hlink.setText(data.get(0).getURL());
        	hlink.setStyle("-fx-font: 20px Arial;");
        }
        Text txt5 = new Text("Tags: ");
        Text txt6 = new Text("Related courses: ");
        Text txt7 = new Text("Comments: ");
        Text txt11 = new Text(data.get(0).getTitle());
        Text txt22 = new Text(data.get(0).getAuthor());
        Text txt33 = new Text(String.valueOf(data.get(0).getType()));
        Text txt55 = new Text("");
        Text txt66 = new Text(data.get(0).getCourse());
        TextArea txt77 = new TextArea(data.get(0).getComment());
        txt77.setEditable(false);
        txt77.prefWidthProperty().bind(gPane.widthProperty());
        txt77.setWrapText(true);     
        txt77.setPrefRowCount(4);
        txt1.setStyle("-fx-font: 20px Arial;");
        txt2.setStyle("-fx-font: 20px Arial;");
        txt3.setStyle("-fx-font: 20px Arial;");
        txt4.setStyle("-fx-font: 20px Arial;");
        txt5.setStyle("-fx-font: 20px Arial;");
        txt6.setStyle("-fx-font: 20px Arial;");
        txt7.setStyle("-fx-font: 20px Arial;");
        txt11.setStyle("-fx-font: 20px Arial;");
        txt22.setStyle("-fx-font: 20px Arial;");
        txt33.setStyle("-fx-font: 20px Arial;");
        txt55.setStyle("-fx-font: 20px Arial;");
        txt66.setStyle("-fx-font: 20px Arial;");
        txt77.setFocusTraversable(false);
        txt77.setStyle("-fx-font: 20px Arial; -fx-control-inner-background: #F5F5F5; -fx-focus-color: #F5F5F5;"
        		+ "-fx-faint-focus-color: #F5F5F5; -fx-faint-color: #F5F5F5; -fx-background-color: #F5F5F5");
        gPane.add(txt1, 0, 0, 1, 1);
        gPane.add(txt2, 0, 1, 1, 1);
        gPane.add(txt3, 0, 2, 1, 1);
        gPane.add(txt4, 0, 3, 1, 1);
        gPane.add(txt5, 0, 4, 1, 1);
        gPane.add(txt6, 0, 5, 1, 1);
        gPane.add(txt7, 0, 6, 1, 1);
        gPane.add(txt11, 1, 0, 1, 1);
        gPane.add(txt22, 1, 1, 1, 1);
        gPane.add(txt33, 1, 2, 1, 1);
        if (data.get(0).getType().equals("book")) {
        	gPane.add(txt44, 1, 3, 1, 1);
        } else {
        	gPane.add(hlink, 1, 3, 1, 1);
        }
        gPane.add(txt55, 1, 4, 1, 1);
        gPane.add(txt66, 1, 5, 1, 1);
        gPane.add(txt77, 0, 7, 2, 2);
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(20, 0, 0, 0));
        Button edit = new Button("Edit");
        edit.setPrefWidth(100);
        Button remove = new Button("Remove");
        remove.setPrefWidth(100);
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
        	mainScene = buildMainScene();
            mainStage.setScene(mainScene);
        });
        pane.setRight(returnB);
        pane.setCenter(addPane);
        Scene scene = new Scene(pane);
        return scene;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
