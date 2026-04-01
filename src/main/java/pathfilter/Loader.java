package pathfilter;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;

public class Loader {

    public static Config load(String file) throws Exception {

        Yaml yaml = new Yaml();

        try (InputStream input =
                Loader.class.getClassLoader().getResourceAsStream(file)) {

            return yaml.loadAs(input, Config.class);
        }
    }
}