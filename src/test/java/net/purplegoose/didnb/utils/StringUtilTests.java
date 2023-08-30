package net.purplegoose.didnb.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilTests {

    @Test
    void isStringNotInTimePattern_Test() {
        List<String> validCombinations = new ArrayList<>();

        String timeHour;
        String timeMinute;
        String time;

        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 60; j++) {

                if (i < 10) {
                    timeHour = "0" + i;
                } else {
                    timeHour = String.valueOf(i);
                }

                if (j < 10) {
                    timeMinute = "0" + j;
                } else {
                    timeMinute = String.valueOf(j);
                }

                time = timeHour + ":" + timeMinute;
                validCombinations.add(time);
                assertFalse(StringUtil.isStringNotInTimePattern(time));
            }
        }

        for (int i = 0; i < 99; i++) {
            for (int j = 0; j < 99; j++) {

                if (i < 10) {
                    timeHour = "0" + i;
                } else {
                    timeHour = String.valueOf(i);
                }

                if (j < 10) {
                    timeMinute = "0" + j;
                } else {
                    timeMinute = String.valueOf(j);
                }

                time = timeHour + ":" + timeMinute;

                if (validCombinations.contains(time)) {
                    assertFalse(StringUtil.isStringNotInTimePattern(time));
                } else {
                    Assertions.assertTrue(StringUtil.isStringNotInTimePattern(time));
                }
            }
        }
    }

    @Test
    void testIsStringSingleDashWithDigits() {
        assertTrue(StringUtil.isStringSingleDashWithDigits("-123"));
        assertFalse(StringUtil.isStringSingleDashWithDigits("123"));
        assertFalse(StringUtil.isStringSingleDashWithDigits("-12a"));
    }

    @Test
    void testRemoveAllNonNumericCharacters() {
        assertEquals("12345", StringUtil.removeAllNonNumericCharacters("12abc3d45"));
        assertEquals("6789", StringUtil.removeAllNonNumericCharacters("6ef7g89"));
    }

    @Test
    void testIsStringOnlyContainingNumbers() {
        assertTrue(StringUtil.isStringOnlyContainingNumbers("12345"));
        assertFalse(StringUtil.isStringOnlyContainingNumbers("12a345"));
        assertFalse(StringUtil.isStringOnlyContainingNumbers("abc"));
    }

    @Test
    void testGenerateRandomID() {
        String randomID = StringUtil.generateRandomID();
        assertNotNull(randomID);
        assertEquals(4, randomID.length());
    }

    /* save this for command recode
    @Test
    public void tests() {
        List<CommandData> COMMANDS = new ArrayList<>();
        try {
            for (Class<?> aClass : getClasses("net.purplegoose.didnb.commands")) {
                if (aClass.isAnnotationPresent(CommandAnnotation.class)) {
                    CommandAnnotation annotation = aClass.getAnnotation(CommandAnnotation.class);
                    String commandDescription = annotation.description();
                    String commandName = annotation.name();
                    String commandUsage = annotation.usage();
                    CommandData commandData = Commands.slash(commandName, commandDescription)
                    COMMANDS.
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Class<?>> getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);

        List<File> dirs = new ArrayList<>();
        List<Class<?>> classes = new ArrayList<>();

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if (resource.getProtocol().equalsIgnoreCase("jar")) {
                JarURLConnection jarURLConnection = (JarURLConnection) resource.openConnection();
                JarFile jarFile = jarURLConnection.getJarFile();
                classes.addAll(findClassesFromJar(jarFile, packageName));
            } else {
                dirs.add(new File(resource.getFile()));
                for (File directory : dirs) {
                    classes.addAll(findClasses(directory, packageName));
                }
            }
        }

        return classes;
    }

    private List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        if (!directory.exists()) {
            return Collections.emptyList();
        }
        List<Class<?>> classes = new ArrayList<>();
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                Class<?> clazz = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
                classes.add(clazz);
            }
        }
        return classes;
    }

    private List<Class<?>> findClassesFromJar(JarFile jarFile, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().endsWith(".class") && entry.getName().startsWith(packageName.replace('.', '/'))) {
                String className = entry.getName().replace('/', '.').replace('\\', '.').replace(".class", "");
                classes.add(Class.forName(className));
            }
        }
        return classes;
    }
    */
}
