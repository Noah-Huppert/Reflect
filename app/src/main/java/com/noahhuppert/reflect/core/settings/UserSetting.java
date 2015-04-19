package com.noahhuppert.reflect.core.settings;

import com.noahhuppert.reflect.database.ReflectDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Class for holding a single user setting
 */
@Table(databaseName = ReflectDatabase.NAME)
public class UserSetting extends BaseModel {
    /**
     * The name of the setting
     */
    @Column(columnType = Column.PRIMARY_KEY)
    String key;

    /**
     * The value of the setting
     */
    String value;

    /**
     * Default value of setting
     */
    String defaultValue;

    public UserSetting() {
    }

    public UserSetting(String key, String defaultValue, String value) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.value = value;
    }

    public UserSetting(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    /* Getters */
    public String getKey() {
        return key;
    }

    public String getValue(){
        return value != null ? value : getDefaultValue();
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    /* Setters */
    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
