package pathfilter;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Usage: path-filter <files>");
            System.exit(1);
        }

        try {
            List<String> files = Arrays.asList(args);

            InputStream configStream =
                Main.class.getClassLoader().getResourceAsStream("filters.yaml");

            if (configStream == null) {
                throw new RuntimeException("filters.yaml not found in resources");
            }

            Config config = Loader.load(configStream);

            Map<String, Boolean> results = Engine.evaluate(config, files);

            results.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(e ->
                        System.out.println(e.getKey() + "=" + e.getValue())
                    );

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}