package bookcase.ui;

import org.junit.Test;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit.ApplicationTest;

import bookcase.ui.AppUi;

import static org.testfx.matcher.control.TextInputControlMatchers.hasText;
import static org.testfx.api.FxAssert.verifyThat;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Node;


/* This is the example code from the testfx github readme
 * TODO: create actual tests
 * */


public class AppUiTest extends ApplicationTest {
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        AppUi sovellus = new AppUi();
        Application app = Application.class.cast(sovellus);
        app.init();
	app.start(stage);
        this.stage = stage;
    }

    @Test
    public void testDemo() {    
        clickOn("Add new LibraryItem");
	TextField titleTF = lookup("#comment").query(); //FIX: titleTF id is set to comment in AppUI
	titleTF.setText("testi");
        verifyThat("#comment", hasText("testi"));
    }
    

}
