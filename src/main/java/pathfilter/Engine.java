package pathfilter;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Engine {

    static class Filter {

        String name;
        List<PathMatcher> includes;
        List<PathMatcher> excludes;

        Filter(String name, List<String> patterns) {

            this.name = name;
            this.includes = new ArrayList<>();
            this.excludes = new ArrayList<>();

            if (patterns == null || patterns.isEmpty()) {
                throw new IllegalArgumentException(
                        "Filter '" + name + "' must contain at least one pattern");
            }

            String separator = FileSystems.getDefault().getSeparator();

            for (String p : patterns) {

                try {

                    boolean isExclude = p.startsWith("!");
                    String pattern = isExclude ? p.substring(1) : p;

                    // normalize pattern for platform
                    String normalized = pattern.replace("/", separator);

                    PathMatcher matcher =
                            FileSystems.getDefault().getPathMatcher("glob:" + normalized);

                    if (isExclude) {
                        excludes.add(matcher);
                    } else {
                        includes.add(matcher);
                    }

                } catch (IllegalArgumentException e) {

                    throw new IllegalArgumentException(
                            "Invalid pattern in filter '" + name + "': " + p,
                            e
                    );
                }
            }
        }

        boolean matches(Path path) {

            boolean isIncluded = includes.isEmpty() || matchesAny(includes, path);
            boolean isExcluded = matchesAny(excludes, path);

            return isIncluded && !isExcluded;
        }

        private boolean matchesAny(List<PathMatcher> matchers, Path path) {

            for (PathMatcher matcher : matchers) {
                if (matcher.matches(path)) {
                    return true;
                }
            }

            return false;
        }
    }

    public static Map<String, Boolean> evaluate(Config config, List<String> files) {

        if (files == null || files.isEmpty() || config.filters == null) {
            return Collections.emptyMap();
        }

        Set<Path> uniquePaths = files.stream()
                .map(f -> Paths.get(f).normalize())
                .collect(Collectors.toSet());

        return config.filters.entrySet()
                .parallelStream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            Filter filter =
                                    new Filter(entry.getKey(), entry.getValue());

                            return uniquePaths.stream()
                                    .anyMatch(filter::matches);
                        }
                ));
    }
}