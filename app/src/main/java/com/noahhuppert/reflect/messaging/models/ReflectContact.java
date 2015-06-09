package com.noahhuppert.reflect.messaging.models;

import android.net.Uri;

import com.noahhuppert.reflect.messaging.CommunicationType;

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
    private @CommunicationType String protocol;

    /**
     * The display name of the contact
     */
    private String displayName;

    /**
     * The uri of the contact's avatar
     */
    private Uri avatarUri;

    /**
     * The URI of the contact.
     *
     * For an SMS contact this may be: sms://cell_phone_number
     *
     * For a XMPP contact this may be: jabber://username@host:port
     */
    private Uri uri;

    /* Actions */
    @Override
    public String toString() {
        return "[id: " + getId() +
                ", protocol: " + getProtocol() +
                ", displayName: " + getDisplayName() +
                ", avatarUri: " + getAvatarUrl() +
                ", Uri: " + getUri() + "]";
    }


    /* Getters */
    public String getId() {
        return id;
    }

    public
    @CommunicationType
    String getProtocol() {
        return protocol;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Uri getAvatarUrl() {
        return avatarUri;
    }

    public Uri getUri() {
        return uri;
    }

    /* Setters */
    public void setId(String id) {
        this.id = id;
    }

    public void setProtocol(@CommunicationType String protocol) {
        this.protocol = protocol;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setAvatarUrl(Uri avatarUrl) {
        this.avatarUri = avatarUrl;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
