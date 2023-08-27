package net.purplegoose.didnb.languages;

import net.purplegoose.didnb.enums.Language;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class LanguageDanish {

    private static final Map<String, Object> MESSAGES;

    static {
        Yaml yaml = new Yaml();
        InputStream inputStream = LanguageDanish.class.getClassLoader()
                .getResourceAsStream(Language.DANISH.path);
        MESSAGES = yaml.load(inputStream);
    }

    private LanguageDanish() {/*static use only*/}

    public static String getMessage(String message) {
        return MESSAGES.get(message).toString();
    }

}
