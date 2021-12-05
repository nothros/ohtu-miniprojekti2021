package bookcase.ui;

    import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.testfx.framework.junit.ApplicationTest;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"},  features = "src/test/resources/bookcase")
public class RunCucumberTest {
    
    @BeforeClass
    public static void setupHeadlessMode() throws Exception {
        System.setProperty("isTestEnvironment", "true");
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("glass.platform", "Monocle");
        System.setProperty("monocle.platform", "headless");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        System.setProperty("java.awt.headless", "true");
    }
}

