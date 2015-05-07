package com.noahhuppert.reflect.messaging;

import android.database.Cursor;
import android.provider.ContactsContract;

import com.venmo.cursor.IterableCursorWrapper;

import java.net.URI;
import java.net.URISyntaxException;

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

    /* Actions */
    @Override
    public String toString() {
        return "[id: " + getId() +
                ", protocol: " + getProtocol() +
                ", displayName: " + getDisplayName() +
                ", avatarUri: " + getAvatarUrl() +
                ", Uri: " + getUri() + "]";
    }

    /* Cursor Wrappers */
    /**
     * A cursor wrapper for taking a Contacts table query and converting it into a cursor
     */
    public static class SmsCursor extends IterableCursorWrapper<ReflectContact>{
        public static final String KEY_ID = ContactsContract.Contacts.LOOKUP_KEY;
        public static final String KEY_DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY;
        public static final String KEY_AVATAR_URI = ContactsContract.Contacts.PHOTO_URI;

        public SmsCursor(Cursor cursor) {
            super(cursor);
        }

        @Override
        public ReflectContact peek() {
            try {
                //Get information
                String id = getString(KEY_ID, "");
                CommunicationType protocol = CommunicationType.SMS;
                String displayName = getString(KEY_DISPLAY_NAME, "");

                String avatarUriString = getString(KEY_AVATAR_URI, "");
                URI avatarUri = null;
                if(avatarUriString != null){
                    avatarUri = new URI(avatarUriString);
                }


                //Set data
                ReflectContact reflectContact = new ReflectContact();

                reflectContact.setId(id);
                reflectContact.setProtocol(protocol);
                reflectContact.setDisplayName(displayName);
                reflectContact.setAvatarUrl(avatarUri);

                return reflectContact;
            } catch (URISyntaxException e){
                return null;
            }
        }
    }

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
