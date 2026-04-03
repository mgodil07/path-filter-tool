package pathfilter;

import java.io.InputStream;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class Loader {

  public static Config load(InputStream configStream) {

    if (configStream == null) {
      throw new IllegalArgumentException("Config input stream cannot be null");
    }

    LoaderOptions options = new LoaderOptions();
    Constructor constructor = new Constructor(Config.class, options);

    Yaml yaml = new Yaml(constructor);

    Config config = yaml.load(configStream);

    if (config == null || config.filters == null) {
      throw new IllegalStateException("Invalid configuration: 'filters' node missing or empty.");
    }

    return config;
  }
}
