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
    public void clickAddItemButton() {
        clickOn("#addBook");
    }

    @When("form is filled with {string} as name and {string} as author and {string}")
    public void fillNewItemForm(String title, String author, String isbn) {
        TextField nameField = find("#title");
        nameField.setText(title);
        TextField authorField = find("#author");
        authorField.setText(author);
        TextField pageCountField = find("#isbn_website");
        pageCountField.setText(isbn);
    }

    @When("create item is clicked")
    public void createItemButtonClicked() {
        clickOn("#createBook");
    }

    @When("Choose {string} from list")
    public void chooseItemFromList(String item){
        if(item.equals("Blogpost")){
            ComboBox<String> comboBox = find("#combobox");
            interact(() -> {
                comboBox.getSelectionModel().select(1);
              });
        }
        else if(item.equals("Podcast")){
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
