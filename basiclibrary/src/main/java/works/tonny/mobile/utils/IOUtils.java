package works.tonny.mobile.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class IOUtils {
    private static final int EOF = -1;

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        return copyLarge(input, output, new byte[DEFAULT_BUFFER_SIZE]);
    }

    public static long copyLarge(InputStream input, OutputStream output, byte[] buffer) throws IOException {
        long count = 0;
        int n = 0;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static void close(OutputStream outputStream) {
        if (outputStream == null) {
            return;
        }
        try {
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void close(InputStream inputStream) {
        if (inputStream == null) {
            return;
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void cacheObject(Object o, File file) throws IOException {
        FileOutputStream output = new FileOutputStream(file);
        ObjectOutputStream outputStream = new ObjectOutputStream(output);
        try {
            outputStream.writeObject(o);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(output);
            close(outputStream);
        }
    }

    public static Object getCachedObject(File file) throws IOException {
        if (!file.exists()) {
            return null;
        }
        FileInputStream input = new FileInputStream(file);
        ObjectInputStream inputStream = new ObjectInputStream(input);
        try {
            Object o = inputStream.readObject();
            return o;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            close(input);
            close(inputStream);
        }
        return null;
    }
}
