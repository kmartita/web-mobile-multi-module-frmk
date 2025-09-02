package project.tools;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PathToFile {

    private static final List<String> modules = Arrays.asList("module-app", "module-tools", "module-tests");

    public static String getRootOfProject() {
        Path path = Paths.get(Objects.requireNonNull(PathToFile.class.getClassLoader().getResource(".")).getPath());
        while (modules.stream().noneMatch(path::endsWith)) {
            path = path.getParent();
        }
        return path.getParent().toString();
    }
}
