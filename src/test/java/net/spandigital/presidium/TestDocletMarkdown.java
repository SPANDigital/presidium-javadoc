package net.spandigital.presidium;

import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Options;
import com.sun.tools.javadoc.JavadocTool;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static net.spandigital.presidium.TestUtils.mustHaveDir;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("unused")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestDocletMarkdown {

    @TempDir
    private File workDir;
    private File docsDir;
    private String sourcePath;
    private String subpackages;
    private Context context;


    @BeforeAll
    public void setupTests() {
        docsDir = mustHaveDir(new File(workDir, "docs"));
        sourcePath = new File(System.getProperty("user.dir"), "src/test/java").getAbsolutePath();
        subpackages = "net.spandigital.presidium.fixtures";
        assertTrue(Files.exists(Paths.get(sourcePath)));
        context = new Context();
        Options compOpts = Options.instance(context);
        compOpts.put("-sourcepath", sourcePath);

        JavaCompiler javadocTool = JavadocTool.instance(context);
    }

    @Test
    public void testMarkdown() {
        assertDoesNotThrow(this::runDoclet);
    }

    private void runDoclet() throws Exception {
//
//        out.printf("Generating markdown docs to: %s%n", docsDir);
//        Main.execute("Markdown Generator", new String[]{
//                "-sourcepath", sourcePath,
//                "-subpackages", subpackages,
//                "-d", docsDir.getPath()
//        });
    }
}

