package com.noahhuppert.reflect.core.contacts;

import com.noahhuppert.reflect.core.messaging.CommunicationType;
import com.noahhuppert.reflect.database.ReflectDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * A contact that represents one person that may have multiple individual ReflectContacts associated
 * with them.
 */

@Table(databaseName = ReflectDatabase.NAME)
public class ReflectCombinedContact extends BaseModel {
    /**
     * The id of the ReflectCombinedContact
     */
    @Column(columnType = Column.PRIMARY_KEY)
    String id;

    /**
     * A list of the individual ReflectContact ids
     */
    List<String> contactIds;

    /**
     * The contacts preferred communication type. This is based off of the contacts mobile signal
     * status or wifi status
     */
    CommunicationType preferredCommunicationType;

    /* Getters */
    public String getId() {
        return id;
    }

    public List<String> getContactIds() {
        return contactIds;
    }

    public CommunicationType getPreferredCommunicationType() {
        return preferredCommunicationType;
    }

    /* Setters */
    public void setId(String id) {
        this.id = id;
    }

    public void setContactIds(List<String> contactIds) {
        this.contactIds = contactIds;
    }

    public void setPreferredCommunicationType(CommunicationType preferredCommunicationType) {
        this.preferredCommunicationType = preferredCommunicationType;
    }
}
