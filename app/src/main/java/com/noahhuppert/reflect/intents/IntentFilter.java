package com.noahhuppert.reflect.intents;

import java.net.URI;

/**
 *  A class to store data about when to handle an intent
 */
public class IntentFilter {
    /**
     * The action of the intent to handle
     */
    private final String action;

    /**
     * The Uri of the intent to handle
     */
    private final URI uri;

    /**
     * Creates a IntentFilter
     * @param action {@link IntentFilter#action}
     * @param uri {@link IntentFilter#uri}
     */
    public IntentFilter(String action, URI uri) {
        this.action = action;
        this.uri = uri;
    }

    /* Actions */
    @Override
    public String toString() {
        return "[action: " + getAction() +
                ", uri: " + getUri() + "]";
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof IntentFilter) {
            IntentFilter intentFilter = (IntentFilter) o;
            return intentFilter.getAction().equals(getAction()) &&
                    intentFilter.getUri().equals(getUri());
        }

        return false;
    }

    /* Getters */
    public String getAction() {
        return action;
    }

    public URI getUri() {
        return uri;
    }
}
