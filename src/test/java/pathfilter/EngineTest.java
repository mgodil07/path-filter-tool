package pathfilter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class EngineTest {

    @Test
    void evaluateAppliesIncludeAndExcludePatterns() {
        Config config = new Config();
        config.filters = Map.of(
                "api_sources", List.of("src/api/**/*.py", "!src/api/tests/**"),
                "tests", List.of("src/api/tests/**"),
                "documentation", List.of("README.md", "docs/**/*.md"),
                "infra", List.of("infra/**")
        );

        List<String> files = List.of(
                "src/api/handlers/users.py",
                "src/api/tests/test_users.py",
                "README.md"
        );

        Map<String, Boolean> results = Engine.evaluate(config, files);

        assertTrue(results.get("api_sources"));
        assertTrue(results.get("tests"));
        assertTrue(results.get("documentation"));
        assertFalse(results.get("infra"));
    }
}
