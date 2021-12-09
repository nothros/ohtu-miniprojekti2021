package bookcase.ui;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import bookcase.ui.AppUi;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import bookcase.ui.TestFXBase;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import static org.testfx.matcher.control.TableViewMatchers.hasTableCell;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;

public class StepDefinitions extends TestFXBase {

    @Given("application has opened")
    public void applicationHasOpened() throws Exception {
        ApplicationTest.launch(AppUi.class);
        WaitForAsyncUtils.waitForFxEvents();
    }

    @When("add item button is clicked")
    public void add_item_button_is_clicked() {
        clickOn("#addBook");
    }

    @When("form is filled with {string} as name and {string} as author and {string}")
    public void fillNewBookForm(String title, String author, String isbnWeb) {
        TextField nameField = find("#title");
        nameField.setText(title);
        TextField authorField = find("#author");
        authorField.setText(author);
        TextField isbnWebsite = find("#isbn_website");
        isbnWebsite.setText(isbnWeb);
    }

    @When("create item is clicked")
    public void createItemIsClicked() {
        clickOn("#createBook");
    }

    @When("Choose {string} from list")
    public void choose_from_list(String string) {
        if(string.equals("Blogpost")){
            ComboBox<String> comboBox = find("#combobox");
            interact(() -> {
                comboBox.getSelectionModel().select(1);
              });
        }
        else if(string.equals("Podcast")){
            ComboBox<String> comboBox = find("#combobox");
            interact(() -> {
                comboBox.getSelectionModel().select(2);
              });
        }else{
            ComboBox<String> comboBox = find("#combobox");
            interact(() -> {
                comboBox.getSelectionModel().select(0);
              });
        }
}


    @Then("error label has text {string}")
    public void errorLabelHasText(String string) {
        verifyThat("#error", hasText(string));

    }
}