package me.umbreon.didn.languages;

import me.umbreon.didn.enums.Language;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class LanguageEnglish {

    private static final Map<String, Object> messages;

    static {
        Yaml yaml = new Yaml();
        InputStream inputStream = LanguageEnglish.class.getClassLoader().getResourceAsStream(Language.ENGLISH.path);
        messages = yaml.load(inputStream);
    }

    private LanguageEnglish() {
        //All static methods.
    }

    public static String getMessage(String message) {
        return messages.get(message).toString();
    }

}
