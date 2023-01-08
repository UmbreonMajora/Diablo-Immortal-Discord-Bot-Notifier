package net.purplegoose.didnb.languages;

import net.purplegoose.didnb.enums.Language;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class LanguageBrazilianPortuguese {

    private static final Map<String, Object> messages;

    static {
        Yaml yaml = new Yaml();
        InputStream inputStream = LanguageBrazilianPortuguese.class.getClassLoader().getResourceAsStream(Language.BRAZILIAN_PORTUGUESE.path);
        messages = yaml.load(inputStream);
    }

    private LanguageBrazilianPortuguese() {
        //All static methods.
    }

    public static String getMessage(String message) {
        return messages.get(message).toString();
    }

}
