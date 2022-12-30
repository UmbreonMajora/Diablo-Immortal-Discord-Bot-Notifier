package me.umbreon.didn.utils;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FilesUtil {

    public static String loadResourceToString(String path) {
        InputStream stream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(path);
        try {
            return IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    public static List<String> getResourceFiles(String path, Class<?> c) throws IOException {
        List<String> filenames = new ArrayList<>();

        try (
                InputStream in = getResourceAsStream(path, c);
                BufferedReader br = new BufferedReader(new InputStreamReader(in))
        ) {
            String resource;

            while ((resource = br.readLine()) != null) {
                filenames.add(resource);
            }
        }

        return filenames;
    }

    private static InputStream getResourceAsStream(String resource, Class<?> c) {
        InputStream in = getContextClassLoader().getResourceAsStream(resource);
        return in == null ? c.getResourceAsStream(resource) : in;
    }

    private static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

}
