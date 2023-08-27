package net.purplegoose.didnb.languages;

import net.purplegoose.didnb.enums.Language;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class LanguageSwedish {

    private static final Map<String, Object> MESSAGES;

    static {
        Yaml yaml = new Yaml();
        InputStream inputStream = LanguageSwedish.class.getClassLoader()
                .getResourceAsStream(Language.SWEDISH.path);
        MESSAGES = yaml.load(inputStream);
    }

    private LanguageSwedish() {/*static use only*/}

    public static String getMessage(String message) {
        return MESSAGES.get(message).toString();
    }

}
