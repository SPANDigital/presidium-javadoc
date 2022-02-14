package net.spandigital.presidium;

import java.io.File;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class TestUtils {

    public static File mustHaveDir(File dir)  {
        if (dir.exists()) return dir;
        assertTrue(dir.mkdirs(), () -> format("unable to create directory: %s", dir));
        return dir;
    }
}

