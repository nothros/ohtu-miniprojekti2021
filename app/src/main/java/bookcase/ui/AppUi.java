package bookcase.ui;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.rowset.serial.SerialArray;
import com.google.common.util.concurrent.Service;

import bookcase.logic.LibraryService;
import bookcase.dao.DAO;
import bookcase.dao.CourseDAO;
import bookcase.dao.TagDAO;
import bookcase.dao.LibraryObjectDAO;
import bookcase.domain.LibraryObject;
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
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
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
        service = new LibraryService(database);
        service.createNewTablesIfNotExists();
        addReadble = buildAddReableScene("", 0);
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
    /*
     *  Add item to bookcase scene.
     */
    public Scene buildAddReableScene(String typeValue, int id) {
        String[] listOfTitles = {"Title:", "Author:", "ISBN:", "Tags:", "Course:"};
        if (!typeValue.equals("book")) {
            listOfTitles[2] = "Website";
        }
        final List<LibraryObject> data = service.getAllObjects(id);
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
        if (id != 0)
        	scenetitle.setText("Update "+ typeValue + " info");
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
        if (id != 0) {
        	titleTF.setText(data.get(0).getTitle());
        	authorTF.setText(data.get(0).getAuthor());
        	if (data.get(0).getType().equals("book"))
        		ISBNTF.setText(data.get(0).getISBN());
        	else
        		ISBNTF.setText(data.get(0).getURL());
        	tagsTF.setText(service.getTagString(data.get(0).getId()));
        	courseTF.setText(service.getCourseString(data.get(0).getId()));
        	commentTF.setText(data.get(0).getComment());
        }
        grid.add(titleTF, 1, 1);
        grid.add(authorTF, 1, 2);
        grid.add(ISBNTF, 1, 3);
        grid.add(tagsTF, 1, 4);
        grid.add(courseTF, 1, 5);
        grid.add(commentTF,1, 6);
        Button createBook = new Button("Add new " + typeValue);
        if (id != 0)
        	createBook.setText("Update " + typeValue);
        createBook.setId("createBook");
        createBook.setOnAction(e -> {
            String title = titleTF.getText();
            String author = authorTF.getText();
            String isbn_website = ISBNTF.getText();
            String tags = tagsTF.getText();
            String course = courseTF.getText();
            String comment = commentTF.getText();
            String errMsg = "Something went wrong adding " + typeValue;
            if (id == 0 && (errMsg = service.createLibraryObject(typeValue, title, author, isbn_website, comment)).equals("")) {
                service.addTagsToLatestLibraryObject(tags);
                service.addCoursesToLatestLibraryObject(course);
                error.setText("New " + typeValue + " added");
                error.setTextFill(Color.GREEN);
                error.setVisible(true);
                titleTF.clear();
                authorTF.clear();
                ISBNTF.clear();
                tagsTF.clear();
                courseTF.clear();
                commentTF.clear();
            } else if (id !=0 && (errMsg = service.updateLibraryObject(id, typeValue, title, author, isbn_website, comment)).equals("")) {
                service.updateTags(id, tags);
                error.setText(typeValue + " updated");
                error.setTextFill(Color.GREEN);
                error.setVisible(true);
            } else {
                error.setText(errMsg);
                error.setTextFill(Color.RED);
                error.setVisible(true);
            }
        });
        grid.add(createBook, 1, 7);
        addPane.getChildren().addAll(error, grid);

        Button returnB = new Button("Back");
        returnB.setId("backButton");
        returnB.setOnAction(e -> {
        	if (id != 0)
        		mainStage.setScene(buildInfoScene(id));
        	else
        		mainStage.setScene(buildMainScene());
            error.setVisible(false);
        });
        pane.setCenter(addPane);
        pane.setRight(returnB);
        Scene scene = new Scene(pane);
        return scene;
    }

    /*
     *  Main scene for bookcase app.
     */
    public Scene buildMainScene() {
    	final ObservableList<LibraryObject> data = FXCollections.observableArrayList(service.getAllObjects());
        FilteredList<LibraryObject> flLibObj = new FilteredList<LibraryObject>(data, p -> true);
        SortedList<LibraryObject> sortedData = new SortedList<LibraryObject>(flLibObj);
        TableView<LibraryObject> table = new TableView<LibraryObject>();
        table.setId("table");
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
        typeComboBox.getItems().addAll("book", "blogpost", "podcast");
        typeComboBox.getSelectionModel().selectFirst();
        Button addBook = new Button("Add");
        addBook.setMinWidth(50);
        addBook.setId("addBook");
        addBook.setOnAction(e -> {
            String typeValue = (String) typeComboBox.getValue();
            mainStage.setScene(buildAddReableScene(typeValue, 0));
        });
        comboBoxAndButton.getChildren().addAll(typeComboBox, addBook);
        // Search library items.
        ComboBox<String> searchComboBox = new ComboBox<String>();
        searchComboBox.setMinWidth(100);
        searchComboBox.getItems().addAll("Title", "Author", "Type");
        searchComboBox.getSelectionModel().selectFirst();
        TextField searchField = new TextField();
        searchField.setMinWidth(250);
        searchField.setPromptText("Enter search here!");
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
        colTags.setCellValueFactory(d -> new SimpleStringProperty(service.getTagString(d.getValue().getId())));
        TableColumn<LibraryObject, String> colCourse = new TableColumn<>("Courses");
        colCourse.setStyle("-fx-alignment: CENTER_LEFT;");
        colCourse.setSortable(false);
        colCourse.setCellValueFactory(d -> new SimpleStringProperty(service.getCourseString(d.getValue().getId())));
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
        // Search box listener that automatically updates on searchField entry.
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
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
                searchField.setText("");
        });
        table.setItems(sortedData);
        table.getColumns().addAll(Arrays.asList(colTitle, colAuthor, colType, colTags, colCourse));
        table.getColumns().add(colButton);
        HBox searchBox = new HBox(searchComboBox, searchField);
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
        hlink.setBorder(Border.EMPTY);
        hlink.setStyle("-fx-border-color: transparent; -fx-padding: 4 0 4 0;");
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
        } else {
        	txt4 = new Text("URL: ");
        	txt44 = new Text("");
        	hlink.setText(data.get(0).getURL());
        }
        Text txt5 = new Text("Tags: ");
        Text txt6 = new Text("Related courses: ");
        Text txt7 = new Text("Comments: ");
        Text txt11 = new Text(data.get(0).getTitle());
        Text txt22 = new Text(data.get(0).getAuthor());
        Text txt33 = new Text(String.valueOf(data.get(0).getType()));
        Text txt55 = new Text(service.getTagString(data.get(0).getId()));
        Text txt66 = new Text(service.getCourseString(data.get(0).getId()));
        TextArea txt77 = new TextArea(data.get(0).getComment());
        txt77.setEditable(false);
        txt77.prefWidthProperty().bind(gPane.widthProperty());
        txt77.setWrapText(true);     
        txt77.setPrefRowCount(4);
        txt77.setFocusTraversable(false);
        txt77.setStyle("-fx-control-inner-background: #F5F5F5; -fx-focus-color: #F5F5F5;"
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
            mainStage.setScene(buildAddReableScene(data.get(0).getType(), data.get(0).getId()));
        });
        remove.setOnAction(e -> {
        	if (data.get(0) != null) {
        		service.deleteEntry(data.get(0).getId());
        	}
            mainStage.setScene(buildMainScene());
        });
        hlink.setOnAction(e -> {
        	HostServices host = getHostServices();
            host.showDocument("www." + data.get(0).getURL());
        });
        gPane.setAlignment(Pos.CENTER);
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
