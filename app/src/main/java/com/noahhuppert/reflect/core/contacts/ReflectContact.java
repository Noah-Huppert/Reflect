package com.noahhuppert.reflect.core.contacts;

import com.noahhuppert.reflect.core.messaging.CommunicationType;
import com.noahhuppert.reflect.core.uri.ReflectUri;
import com.noahhuppert.reflect.database.ReflectDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Reflect contact model. Used for storing contact information
 */
@Table(databaseName = ReflectDatabase.NAME)
public class ReflectContact extends BaseModel {
    /**
     * The id of the reflect contact
     */
    @Column(columnType = Column.PRIMARY_KEY)
    String id;

    /**
     * The resource indicator for the contact.
     * If the contact type is CommunicationType.XMPP then it will follow the format of:
     *      "username@host:port"
     * The port is not needed if it is the default XMPP port(5222)
     *
     * If the contact type is CommunicationType.SMS then it will follow the format of:
     *      "rawGoogleContactId"
     */
    ReflectUri uri;

    /**
     * The communication type of the contact. This describes if the contact is an XMPP or SMS contact
     */
    CommunicationType communicationType;

    /**
     * The first name of the contact
     */
    String firstName;

    /**
     * The last name of the contact
     */
    String lastName;

    /**
     * The url of the contacts avatar
     */
    String avatarUrl;

    /* Getters */
    public String getId() {
        return id;
    }

    public ReflectUri getUri() {
        return uri;
    }

    public CommunicationType getCommunicationType() {
        return communicationType;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    /* Setters */
    public void setId(String id) {
        this.id = id;
    }

    public void ReflectUri(ReflectUri uri) {
        this.uri = uri;
    }

    public void setCommunicationType(CommunicationType communicationType) {
        this.communicationType = communicationType;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
