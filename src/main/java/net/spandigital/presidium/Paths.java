package net.spandigital.presidium;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Paths {

    private Paths() {}

    public static File requiredFile(Path path)  {
        File file = path.toFile();
        if (!file.exists()) {
            File parent = file.getParentFile();
            if (!parent.exists() && !parent.mkdirs()) {
                throw new IO.Exception("failed to create path:" + path, new FileNotFoundException(file.toString()));
            }
            try {
                Files.createFile(path);
            } catch (IOException e) {
                throw new IO.Exception("unable to create file: "+path, e);
            }
        }
        return file;
    }

}
