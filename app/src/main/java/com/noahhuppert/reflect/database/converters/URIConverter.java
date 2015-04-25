package com.noahhuppert.reflect.database.converters;

import android.util.Log;

import com.raizlabs.android.dbflow.converter.TypeConverter;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Converts a URI to a String
 */
@com.raizlabs.android.dbflow.annotation.TypeConverter
public class URIConverter extends TypeConverter<String, URI> {
    private static final String TAG = URIConverter.class.getSimpleName();

    @Override
    public String getDBValue(URI model) {
        return model.toString();
    }

    @Override
    public URI getModelValue(String data) {
        try{
            return new URI(data);
        } catch(URISyntaxException e){
            Log.e(TAG, "Failed to convert \"" + data + "\" to a URI, exception: " + e.toString());
            return null;
        }
    }
}
