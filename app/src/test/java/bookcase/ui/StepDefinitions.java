package bookcase.ui;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import bookcase.ui.AppUi;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
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

    @When("add book button is clicked")
    public void clickAddBookButton() {
        clickOn("#addBook");
    }

    @When("form is filled with {string} as name and {string} as author and {string} as isbn")
    public void fillNewBookForm(String title, String author, String isbn){
        TextField nameField = find("#title");
        nameField.setText(title);
        TextField authorField = find("#author");
        authorField.setText(author);
        TextField pageCountField = find("#isbn_website");
        pageCountField.setText(isbn);
    }


@When("create book is clicked")
public void create_book_is_clicked() {
    clickOn("#createBook");
}

@Then("error label has text {string}")
public void error_label_has_text(String string) {
    verifyThat("#error", hasText(string));

}



}