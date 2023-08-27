package net.purplegoose.didnb.languages;

import net.purplegoose.didnb.enums.Language;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class LanguageSlovak {

    private static final Map<String, Object> MESSAGES;

    static {
        Yaml yaml = new Yaml();
        InputStream inputStream = LanguageSlovak.class.getClassLoader()
                .getResourceAsStream(Language.SLOVAK.path);
        MESSAGES = yaml.load(inputStream);
    }

    private LanguageSlovak() {/*static use only*/}

    public static String getMessage(String message) {
        return MESSAGES.get(message).toString();
    }

}
