package excellent.cancer.gray.light.document;

import lombok.NonNull;

import java.nio.file.Path;

public final class PathFilters {

    private static final String IGONRED = ".";

    public static boolean igonred(@NonNull Path path) {
        return path.getFileName().toString().startsWith(IGONRED);
    }
}
