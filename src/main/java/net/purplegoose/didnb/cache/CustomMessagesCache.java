package net.purplegoose.didnb.cache;

import java.util.ArrayList;
import java.util.List;

public class CustomMessagesCache {

    private final List<String> usedIdentifiers = new ArrayList<>();

    public boolean isIdentifierInUse(String identifier) {
        return usedIdentifiers.contains(identifier);
    }

    public void addIdentifier(String identifier) {
        usedIdentifiers.add(identifier);
    }

    public void removeIdentifier(String identifier) {
        usedIdentifiers.remove(identifier);
    }

}
