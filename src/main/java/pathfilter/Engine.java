package pathfilter;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
                System.err.println("Warning: Filter '" + name + "' has no patterns and will never match.");
                return;
            }
            for (String p : patterns) {
                try {
                    if (p.startsWith("!")) {
                        excludes.add(FileSystems.getDefault().getPathMatcher("glob:" + p.substring(1)));
                    } else {
                        includes.add(FileSystems.getDefault().getPathMatcher("glob:" + p));
                    }
                } catch (Exception e) {
                    System.err.println("Invalid pattern in filter '" + name + "': " + p);
                }
            }
        }

        boolean matches(Path path) {
            return matchesAny(includes, path) && !matchesAny(excludes, path);
        }

        private boolean matchesAny(List<PathMatcher> matchers, Path path) {
            for (PathMatcher m : matchers) {
                if (m.matches(path)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static Map<String, Boolean> evaluate(Config config, List<String> files) {

        Map<String, Boolean> results = new java.util.concurrent.ConcurrentHashMap<>();
    
        List<Filter> filters = new ArrayList<>();
    
        for (var entry : config.filters.entrySet()) {
            filters.add(new Filter(entry.getKey(), entry.getValue()));
            results.put(entry.getKey(), false);
        }
    
        if (files == null || files.isEmpty()) {
            return results;
        }
    
        Set<String> uniqueFiles = new HashSet<>(files);
    
        uniqueFiles.parallelStream().forEach(f -> {
    
            Path path = Paths.get(f);
    
            for (Filter filter : filters) {
    
                if (!results.get(filter.name) && filter.matches(path)) {
                    results.put(filter.name, true);
                }
    
            }
    
        });
    
        return results;
    }
}
