package com.noahhuppert.reflect.messaging;

import java.net.URI;

/**
 * A class for holding information about a general contact in Reflect. Every message provider must
 * provide this information.
 */
public class ReflectContact {
    /**
     * The id of the contact
     */
    private String id;

    /**
     * The protocol used to get the contact
     */
    private CommunicationType protocol;

    /**
     * The display name of the contact
     */
    private String displayName;

    /**
     * The uri of the contact's avatar
     */
    private URI avatarUri;

    /**
     * The URI of the contact.
     *
     * For an SMS contact this may be: sms://cell_phone_number
     *
     * For a XMPP contact this may be: jabber://username@host:port
     */
    private URI uri;

    /* Getters */
    public String getId() {
        return id;
    }

    public CommunicationType getProtocol() {
        return protocol;
    }

    public String getDisplayName() {
        return displayName;
    }

    public URI getAvatarUrl() {
        return avatarUri;
    }

    public URI getUri() {
        return uri;
    }

    /* Setters */
    public void setId(String id) {
        this.id = id;
    }

    public void setProtocol(CommunicationType protocol) {
        this.protocol = protocol;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setAvatarUrl(URI avatarUrl) {
        this.avatarUri = avatarUrl;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }
}
