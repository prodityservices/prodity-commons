package io.prodity.commons.spigot.legacy.utils;

import java.io.File;
import java.io.IOException;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.file.YamlConfiguration;

@UtilityClass
public class FileUtils {

    public static void save(YamlConfiguration config, File file) {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
