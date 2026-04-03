package pathfilter;

import java.io.InputStream;
import java.nio.file.*;
import java.util.*;

public class Main {

  public static void main(String[] args) {

    if (args.length == 0) {
      System.out.println("Usage: path-filter <files>");
      System.exit(1);
    }

    Path configPath = Paths.get("filters.yaml");

    if (!Files.exists(configPath)) {
      System.err.println("Error: filters.yaml not found in current directory.");
      System.exit(1);
    }

    try (InputStream configStream = Files.newInputStream(configPath)) {

      Config config = Loader.load(configStream);

      List<String> files = Arrays.asList(args);

      Map<String, Boolean> results = Engine.evaluate(config, files);

      results.entrySet().stream()
          .sorted(Map.Entry.comparingByKey())
          .forEach(e -> System.out.println(e.getKey() + "=" + e.getValue()));

    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      System.exit(1);
    }
  }
}
