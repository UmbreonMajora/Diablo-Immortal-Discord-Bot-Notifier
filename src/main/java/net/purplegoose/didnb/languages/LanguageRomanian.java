package net.purplegoose.didnb.languages;

import net.purplegoose.didnb.enums.Language;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class LanguageRomanian {

    private static final Map<String, Object> MESSAGES;

    static {
        Yaml yaml = new Yaml();
        InputStream inputStream = LanguageRomanian.class.getClassLoader()
                .getResourceAsStream(Language.DANISH.path);
        MESSAGES = yaml.load(inputStream);
    }

    private LanguageRomanian() {/*static use only*/}

    public static String getMessage(String message) {
        return MESSAGES.get(message).toString();
    }

}
