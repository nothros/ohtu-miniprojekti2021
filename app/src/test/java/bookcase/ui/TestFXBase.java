package bookcase.ui;

import bookcase.ui.AppUi;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.junit.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Node;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.util.WaitForAsyncUtils;

public class TestFXBase extends ApplicationTest {

    Stage stage;

    @Before
    public void setUpClass() throws Exception {
        System.setProperty("isTestEnvironment", "true");
        ApplicationTest.launch(AppUi.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        AppUi sovellus = new AppUi();
        Application app = Application.class.cast(sovellus);
        app.init();
        app.start(stage);
        this.stage = stage;
    }

    @Test
    public void thereIsAButtonForAddingBooks() {
        WaitForAsyncUtils.waitForFxEvents();
        FxAssert.verifyThat(".button", LabeledMatchers.hasText("Add new LibraryItem"));
    }

    public <T extends Node> T find(final String query) {
        return lookup(query).query();
    }
}
