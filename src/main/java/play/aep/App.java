package play.aep;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;

public class App {
    public static void main(String[] args) {
        String s = sayHello();
        App app = new App();
        String rs = app.getMyResource();

        System.out.printf("%s from %s\n", s, rs);
    }

    public static String sayHello() {
        return "Hello World!";
    }

    public String getMyResource() {
        InputStream is = getClass().getResourceAsStream("prop1.main");
        StringWriter writer = new StringWriter();
        if (is == null) {
            return "NULL";
        } else {
            try {
                IOUtils.copy(is, writer, StandardCharsets.UTF_8);
            } catch (IOException ioe) {
                System.err.println("ERROR " + ioe.getMessage());
            }
            return writer.toString();
        }
    }
}
