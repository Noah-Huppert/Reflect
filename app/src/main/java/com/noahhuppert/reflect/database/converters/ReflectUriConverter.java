package com.noahhuppert.reflect.database.converters;

import com.noahhuppert.reflect.core.uri.ReflectUri;
import com.raizlabs.android.dbflow.converter.TypeConverter;

public class ReflectUriConverter extends TypeConverter<String, ReflectUri> {
    @Override
    public String getDBValue(ReflectUri model) {
        return model.toString();
    }

    @Override
    public ReflectUri getModelValue(String data) {
        return new ReflectUri(data);
    }
}
