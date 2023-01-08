package net.purplegoose.didnb.languages;

import net.purplegoose.didnb.enums.Language;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class LanguageUkrainian {

    private static final Map<String, Object> messages;

    static {
        Yaml yaml = new Yaml();
        InputStream inputStream = LanguageUkrainian.class.getClassLoader().getResourceAsStream(Language.UKRAINIAN.path);
        messages = yaml.load(inputStream);
    }

    private LanguageUkrainian() {
        //All static methods.
    }

    public static String getMessage(String message) {
        return messages.get(message).toString();
    }

}
