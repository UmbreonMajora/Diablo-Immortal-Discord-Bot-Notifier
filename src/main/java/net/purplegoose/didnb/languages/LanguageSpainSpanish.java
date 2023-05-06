package net.purplegoose.didnb.languages;

import net.purplegoose.didnb.enums.Language;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class LanguageSpainSpanish {

    private static final Map<String, Object> messages;

    static {
        Yaml yaml = new Yaml();
        InputStream inputStream = LanguageSpainSpanish.class.getClassLoader()
                .getResourceAsStream(Language.SPAIN_SPANISH.path);
        messages = yaml.load(inputStream);
    }

    private LanguageSpainSpanish() {
        //All static methods.
    }

    public static String getMessage(String message) {
        return messages.get(message).toString();
    }

}
