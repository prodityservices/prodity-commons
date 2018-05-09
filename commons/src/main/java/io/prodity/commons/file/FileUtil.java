package io.prodity.commons.file;

import com.google.common.base.Preconditions;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;

public enum FileUtil {

    ;

    @Nonnull
    public static String readFileContents(@Nonnull File file) throws IOException, SecurityException {
        Preconditions.checkNotNull(file, "file");
        final Path path = file.toPath();
        return FileUtil.readFileContents(path);
    }

    @Nonnull
    public static String readFileContents(@Nonnull Path path) throws IOException, SecurityException {
        Preconditions.checkNotNull(path, "value");

        final List<String> lines = Files.readAllLines(path);
        final StringBuilder fileContentsBuilder = new StringBuilder();
        for (Iterator<String> iterator = lines.iterator(); iterator.hasNext(); ) {
            final String line = iterator.next();
            fileContentsBuilder.append(line);
            if (iterator.hasNext()) {
                fileContentsBuilder.append(System.lineSeparator());
            }
        }

        return fileContentsBuilder.toString();
    }

}