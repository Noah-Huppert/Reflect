package com.noahhuppert.reflect.database.converters;

import com.noahhuppert.reflect.core.messaging.CommunicationType;
import com.raizlabs.android.dbflow.converter.TypeConverter;

/**
 * Converts the CommunicationType enum into a String and back
 */
@com.raizlabs.android.dbflow.annotation.TypeConverter
public class CommunicationTypeConverter extends TypeConverter<String, CommunicationType>{

    @Override
    public String getDBValue(CommunicationType model) {
        return model.toString();
    }

    @Override
    public CommunicationType getModelValue(String data) {
        try {
            return CommunicationType.valueOf(CommunicationType.class, data);
        } catch(IllegalArgumentException e){
            return null;
        }
    }
}
