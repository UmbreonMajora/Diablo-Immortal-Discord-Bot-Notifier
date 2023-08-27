package net.purplegoose.didnb.languages;

import net.purplegoose.didnb.enums.Language;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class LanguageLatvian {

    private static final Map<String, Object> MESSAGES;

    static {
        Yaml yaml = new Yaml();
        InputStream inputStream = LanguageLatvian.class.getClassLoader()
                .getResourceAsStream(Language.LATVIAN.path);
        MESSAGES = yaml.load(inputStream);
    }

    private LanguageLatvian() {/*static use only*/}

    public static String getMessage(String message) {
        return MESSAGES.get(message).toString();
    }

}
