package com.noahhuppert.reflect.database.converters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.raizlabs.android.dbflow.converter.TypeConverter;
import java.util.List;

/**
 * Converts a List<String> to a String
 */
@com.raizlabs.android.dbflow.annotation.TypeConverter
public class StringListConverter extends TypeConverter<String, List> {
    @Override
    public String getDBValue(List model) {
        return new Gson().toJson(model);
    }

    @Override
    public List getModelValue(String data) {
        return new Gson().fromJson(data, new TypeToken<List<String>>() {{
        }}.getType());
    }
}
