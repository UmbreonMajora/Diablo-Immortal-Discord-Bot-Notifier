package net.purplegoose.didnb.cache;

import java.util.ArrayList;
import java.util.List;

public class CustomMessagesCache {

    private final List<String> usedIdentifiers = new ArrayList<>();

    public boolean isIdentifierInUse(String ID) {
        return usedIdentifiers.contains(ID);
    }

    public void addIdentifier(String ID) {
        usedIdentifiers.add(ID);
    }

    public void removeIdentifier(String ID) {
        usedIdentifiers.remove(ID);
    }

}
