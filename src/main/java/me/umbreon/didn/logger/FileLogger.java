package me.umbreon.didn.logger;

import me.umbreon.didn.utils.TimeUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;

public class FileLogger {

    private static final String path;

    static {
        Properties prop = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("logger.properties");

        try {
            prop.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            path = prop.getProperty("windows-path");
        } else {
            path = prop.getProperty("linux-path");
        }

        Path path = Paths.get(FileLogger.path);
        if (doPathNotExists(path)) {
            try {
                createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createGuildFileLog(String guildID, String message) {
        Path path = Paths.get(FileLogger.path + "\\logs\\" + guildID);
        if (doPathNotExists(path)) {
            createPath(String.valueOf(path));
        }

        Path path1 = Paths.get(FileLogger.path + "\\logs\\" + guildID + "\\server.log");
        if (doPathNotExists(path1)) {
            try {
                createFile(path1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File serverLogFile = new File(FileLogger.path + "\\logs\\" + guildID + "\\server.log");
        String timeStamp = "[" + TimeUtil.getCurrentDate() + " " + TimeUtil.getCurrentTime() + "]";

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(serverLogFile, true))) {
            bufferedWriter.append(timeStamp).append(" ").append(message).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createClientFileLog(String message) {
        File serverLogFile = new File(path + "\\logs\\server.log");
        if (!serverLogFile.exists()) {
            try {
                createFile(serverLogFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String timeStamp = "[" + TimeUtil.getCurrentDate() + " " + TimeUtil.getCurrentTime() + "]";

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(serverLogFile, true))) {
            bufferedWriter.append(timeStamp).append(" ").append(message).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createClientFileErrorLog(String message, Throwable throwable) {
        File serverLogFile = new File(path + "\\logs\\server.log");
        String timeStamp = "[" + TimeUtil.getCurrentDate() + " " + TimeUtil.getCurrentTime() + "]";

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(serverLogFile, true))) {
            bufferedWriter.append(timeStamp).append(" ").append(message).append("\n");
            bufferedWriter.append(Arrays.toString(throwable.getStackTrace()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean doPathNotExists(Path path) {
        return !Files.exists(path);
    }

    private static void createFile(Path path) throws IOException {
        Files.createFile(path);
    }

    private static void createPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }


}
