import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LanguagesConsistencyTest {

    private final String directoryPath = "languages/";

    @Test
    void testConsistentKeysInYamlFiles() {
        List<String> yamlFiles = listYamlFiles();

        Map<String, Set<String>> keySets = new HashMap<>();

        for (String yamlFile : yamlFiles) {
            Map<String, Object> yamlData = loadYamlData(yamlFile);
            assertNotNull(yamlData, "Failed to load YAML data from " + yamlFile);

            Set<String> keys = yamlData.keySet();
            keySets.put(yamlFile, keys);
        }

        Set<String> allKeys = new HashSet<>();
        for (Set<String> keys : keySets.values()) {
            allKeys.addAll(keys);
        }

        for (Map.Entry<String, Set<String>> entry : keySets.entrySet()) {
            String yamlFile = entry.getKey();
            Set<String> missingKeys = new HashSet<>(allKeys);
            missingKeys.removeAll(entry.getValue());

            if (missingKeys.size() == 0) {
                System.out.println("No missing keys in " + yamlFile + ". Containing " + entry.getValue().size() + ".");
            }

            assertTrue(missingKeys.isEmpty(), "Missing keys found in " + yamlFile + ": " + missingKeys);
        }
    }

    private List<String> listYamlFiles() {
        List<String> yamlFiles = new ArrayList<>();
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(directoryPath);
            if (inputStream != null) {
                Scanner scanner = new Scanner(inputStream);
                while (scanner.hasNext()) {
                    String fileName = scanner.next();
                    System.out.println(fileName);
                    if (fileName.endsWith(".yaml")) {
                        yamlFiles.add(fileName);
                    }
                }
                scanner.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return yamlFiles;
    }

    private Map<String, Object> loadYamlData(String yamlFile) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(directoryPath + yamlFile)) {
            Yaml yaml = new Yaml();
            return yaml.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
