package pathfilter;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;

public class Loader {

    public static Config load(InputStream configStream) throws IOException {

        Yaml yaml = new Yaml();

        try (InputStream input = configStream) {

            Config config = yaml.loadAs(input, Config.class);

            if (config == null || config.filters == null) {
                throw new IllegalStateException(
                        "Invalid configuration: 'filters' node missing or empty."
                );
            }

            return config;
        }
    }
}