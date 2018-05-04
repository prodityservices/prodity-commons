package io.prodity.commons.plugin;

import java.io.File;
import java.io.InputStream;

public interface ProdityPlugin {

    File getDataFolder();

    InputStream getResource(String fileNamea);

}