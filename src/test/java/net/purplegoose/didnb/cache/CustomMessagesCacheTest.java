package net.purplegoose.didnb.cache;

import net.purplegoose.didnb.cache.CustomMessagesCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomMessagesCacheTest {

    private CustomMessagesCache cache;

    @BeforeEach
    public void setUp() {
        cache = new CustomMessagesCache();
    }

    @Test
    void testNewCacheIsEmpty() {
        assertFalse(cache.isIdentifierInUse("identifier"));
    }

    @Test
    void testAddIdentifier() {
        cache.addIdentifier("identifier");
        assertTrue(cache.isIdentifierInUse("identifier"));
    }

    @Test
    void testAddAndRemoveIdentifier() {
        cache.addIdentifier("identifier");
        cache.removeIdentifier("identifier");
        assertFalse(cache.isIdentifierInUse("identifier"));
    }

    @Test
    void testRemoveNonExistentIdentifier() {
        cache.removeIdentifier("nonExistentIdentifier");
        assertFalse(cache.isIdentifierInUse("nonExistentIdentifier"));
    }

    @Test
        void testAddDuplicateIdentifier() {
        cache.addIdentifier("identifier");
        cache.addIdentifier("identifier"); // Adding the same identifier again
        assertTrue(cache.isIdentifierInUse("identifier"));
    }
}
