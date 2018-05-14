package io.prodity.commons.file;

import com.google.common.base.Preconditions;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

public enum FileUtil {

    ;

    public static String readFileContents(File file) throws IOException, SecurityException {
        Preconditions.checkNotNull(file, "file");
        final Path path = file.toPath();
        return FileUtil.readFileContents(path);
    }

    public static String readFileContents(Path path) throws IOException, SecurityException {
        Preconditions.checkNotNull(path, "deserialize");

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