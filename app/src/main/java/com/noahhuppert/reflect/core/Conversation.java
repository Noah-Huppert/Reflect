package com.noahhuppert.reflect.core;

import com.noahhuppert.reflect.database.ReflectDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Reflect Conversation model. Holds information about conversation
 */
@Table(databaseName = ReflectDatabase.NAME)
public class Conversation extends BaseModel {
    /**
     * The id of the conversation
     */
    @Column(columnType = Column.PRIMARY_KEY)
    String id;

    /**
     * A list of user Ids that belong in the conversation
     */
    List<String> contactIds;

    /* Getters */
    public String getId() {
        return id;
    }

    public List<String> getContactIds() {
        return contactIds;
    }

    /* Setters */
    public void setId(String id) {
        this.id = id;
    }

    public void setContactIds(List<String> contactIds) {
        this.contactIds = contactIds;
    }
}
