package me.umbreon.didn.languages;

import me.umbreon.didn.enums.Language;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class LanguageFrench {

    private static final Map<String, Object> messages;

    static {
        Yaml yaml = new Yaml();
        InputStream inputStream = LanguageFrench.class.getClassLoader().getResourceAsStream(Language.FRENCH.path);
        messages = yaml.load(inputStream);
    }

    private LanguageFrench() {
        //All static methods.
    }

    public static String getMessage(String message) {
        return messages.get(message).toString();
    }

}
