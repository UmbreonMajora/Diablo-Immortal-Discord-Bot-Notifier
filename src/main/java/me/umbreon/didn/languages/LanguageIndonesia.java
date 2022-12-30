package me.umbreon.didn.languages;

import me.umbreon.didn.enums.Language;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class LanguageIndonesia {

    private static final Map<String, Object> messages;

    static {
        Yaml yaml = new Yaml();
        InputStream inputStream = LanguageIndonesia.class.getClassLoader().getResourceAsStream(Language.INDONESIA.path);
        messages = yaml.load(inputStream);
    }

    private LanguageIndonesia() {
        //All static methods.
    }

    public static String getMessage(String message) {
        return messages.get(message).toString();
    }

}
