package pathfilter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class EngineTest {

  @Test
  void evaluateAppliesIncludeAndExcludePatterns() {
    Config config = new Config();
    config.filters =
        Map.of(
            "api_sources", List.of("src/api/**/*.py", "!src/api/tests/**"),
            "tests", List.of("src/api/tests/**"),
            "documentation", List.of("README.md", "docs/**/*.md"),
            "infra", List.of("infra/**"));

    List<String> files =
        List.of("src/api/handlers/users.py", "src/api/tests/test_users.py", "README.md");

    Map<String, Boolean> results = Engine.evaluate(config, files);

    assertTrue(results.get("api_sources"));
    assertTrue(results.get("tests"));
    assertTrue(results.get("documentation"));
    assertFalse(results.get("infra"));
  }

  @Test
  void excludeOnlyFilterShouldWork() {
    Config config = new Config();
    config.filters = Map.of("backend", List.of("!src/backend/legacy/**"));

    List<String> files = List.of("src/backend/service/api.py");

    Map<String, Boolean> result = Engine.evaluate(config, files);

    assertTrue(result.get("backend"));
  }

  @Test
  void emptyFileListReturnsEmptyMap() {
    Config config = new Config();
    config.filters = Map.of("api", List.of("src/api/**"));

    Map<String, Boolean> result = Engine.evaluate(config, List.of());

    assertTrue(result.isEmpty());
  }

  @Test
  void invalidGlobPatternThrowsException() {
    Config config = new Config();

    Map<String, List<String>> filters = new HashMap<>();
    filters.put("bad_filter", List.of("[invalid_glob"));

    config.filters = filters;

    List<String> files = List.of("src/test/file.java");

    IllegalArgumentException thrown =
        assertThrows(IllegalArgumentException.class, () -> Engine.evaluate(config, files));

    assertTrue(thrown.getMessage().contains("bad_filter"));
  }
}
