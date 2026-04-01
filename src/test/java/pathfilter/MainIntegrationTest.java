package pathfilter;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

class MainIntegrationTest {

    @Test
    void mainPrintsFilterResultsForInputFiles() throws Exception {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output));

            Main.main(new String[] {
                    "src/api/handlers/users.py",
                    "src/api/tests/test_users.py",
                    "README.md"
            });
        } finally {
            System.setOut(originalOut);
        }

        String out = output.toString();

        assertTrue(out.contains("api_sources=true"));
        assertTrue(out.contains("tests=true"));
        assertTrue(out.contains("documentation=true"));
        assertTrue(out.contains("infra=false"));
    }
}
