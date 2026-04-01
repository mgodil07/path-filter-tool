package pathfilter;

import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            System.out.println("Usage: path-filter <files>");
            return;
        }

        List<String> files = Arrays.asList(args);

        Config config = Loader.load("filters.yaml");

        Map<String, Boolean> results =
                Engine.evaluate(config, files);

        for (var entry : results.entrySet()) {
            System.out.println(
                entry.getKey() + "=" + entry.getValue()
            );
        }
    }
}