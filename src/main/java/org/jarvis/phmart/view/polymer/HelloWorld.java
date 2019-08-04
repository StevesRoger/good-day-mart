package org.jarvis.phmart.view.polymer;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Optional;

/**
 * Created: kim chheng
 * Date: 07-Apr-2019 Sun
 * Time: 10:55 PM
 */
@Tag("hello-world")
@HtmlImport("frontend://view/hello-world.html")
public class HelloWorld extends PolymerTemplate<HelloWorldModel> {
    private static final String EMPTY_NAME_GREETING = "Please enter your name";

    /**
     * Creates the hello world template.
     */
    public HelloWorld() {
        setId("template");
        getModel().setGreeting(EMPTY_NAME_GREETING);
    }

    @EventHandler
    private void sayHello() {
        // Called from the template click handler
        getModel().setGreeting(Optional.ofNullable(getModel().getUserInput())
                .filter(userInput -> !userInput.isEmpty())
                .map(greeting -> String.format("Hello %s!", greeting))
                .orElse(EMPTY_NAME_GREETING));
    }

    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://www.nbc.org.kh/english/economic_research/exchange_rate.php").get();
        System.out.println(doc.title());
        Elements newsHeadlines = doc.select("#fm-ex font");
        for (Element headline : newsHeadlines) {
            System.out.println(headline);
        }
    }
}