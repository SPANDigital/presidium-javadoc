package net.spandigital.presidium;

import java.io.*;

public final class IO {

    public static final int DEFAULT_BUFFER_SIZE = 8192;

    private IO() {
    }

    public static int skipLines(RandomAccessFile f, int n) throws IOException {

        if (n < 1) {
            return 0;
        }

        while (n > 0) {
            if (f.readLine() == null) {
                break;
            }
            n -= 1;
        }

        return n;
    }

    public static void copy(File source, RandomAccessFile dest) throws IOException {
        byte[] buff = new byte[DEFAULT_BUFFER_SIZE];
        try (BufferedInputStream ins = new BufferedInputStream(new FileInputStream(source), DEFAULT_BUFFER_SIZE)) {
            while (true) {
                int n = ins.read(buff);
                if (n == -1) break;
                dest.write(buff, 0, n);
            }
        }
    }

    /**
     * Wraps any exception due to an IO error as a runtime exception
     */
    public static class Exception extends RuntimeException {
        public Exception(String message, IOException cause) {
            super(message);
            initCause(cause);
        }
    }

    public static String readText(File file) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try(InputStream fs = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE)) {
            byte[] buff = new byte[DEFAULT_BUFFER_SIZE];
            while (true) {
                int n = fs.read(buff);
                if (n == -1) break;
                bytes.write(buff, 0, n);
            }
        }
        return bytes.toString("utf-8");
    }

}
