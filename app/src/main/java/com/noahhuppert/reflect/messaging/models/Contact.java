package com.noahhuppert.reflect.messaging.models;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;

import com.noahhuppert.reflect.caches.CircleTileDrawableLruCache;
import com.noahhuppert.reflect.caches.ContactAvatarLruCache;
import com.noahhuppert.reflect.messaging.CommunicationType;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * A class for storing information about a {@link com.noahhuppert.reflect.messaging.MessagingResourceType#CONTACT}
 * retrieved by a {@link com.noahhuppert.reflect.messaging.providers.MessagingProvider}
 */
public class Contact {
    public static final String SMS_SCHEME = "sms";
    public static final String XMPP_SCHEME = "xmpp";

    /**
     * The id of the contact
     *
     * If the {@link #communicationType} is {@link com.noahhuppert.reflect.messaging.CommunicationType#SMS}
     * then the id will be the {@link android.provider.ContactsContract.Contacts#LOOKUP_KEY}
     */
    public String id;

    /**
     * The name of the contact
     */
    public String name;

    /**
     * The Uri used to communicate with the contact
     *
     * The Uri must have a specific scheme which is determined by the {@link #communicationType}
     *      {@link CommunicationType#SMS} => {@link #SMS_SCHEME}
     *      {@link CommunicationType#XMPP} => {@link #XMPP_SCHEME}
     */
    public Uri uri;

    /**
     * A Uri pointing to the contacts avatar
     */
    public Uri avatarUri;

    /**
     * The {@link CommunicationType} that the contact was retrieved from
     */
    public @CommunicationType String communicationType;

    /* Actions */
    @Override
    public String toString() {
        return "[" +
                    "id => " + id +
                    ", name => \"" + name + "\"" +
                    ", uri => " + uri +
                    ", avatarUri => " + avatarUri +
                    ", communicationType => " + communicationType +
                "]";
    }

    /* Getters */
    /**
     * Guaranted to return some sort of name that can be shown to the user to identify the contact
     *
     * If {@link #name} is {@code null} then the authority of the {@link #uri} will be used
     * @return A name to identify the contact by
     */
    public @NonNull String getNonNullName(){
        if(name != null){
            return name;
        } else if(uri != null && uri.getAuthority() != null){
            return uri.getAuthority();
        } else {
            return "Unknown";
        }
    }

    public boolean willReturnCircleTileWithLetter(){
        return Character.isLetter(getNonNullName().charAt(0));
    }

    @WorkerThread
    public @NonNull Drawable getCircleTileDrawable(){
        String circleTileText = "";
        if(willReturnCircleTileWithLetter()) {
            circleTileText = getNonNullName().charAt(0) + "";
        }

        return CircleTileDrawableLruCache.getInstance().get(getNonNullName(), circleTileText);
    }

    @WorkerThread
    public @NonNull Drawable getNonNullAvatarDrawable(final Context context){
        if(avatarUri == null || avatarUri.getAuthority() == null){
            return getCircleTileDrawable();
        } else {
            Drawable avatarDrawable = ContactAvatarLruCache.getInstance().get(avatarUri, context);

            return avatarDrawable != null ? avatarDrawable : getCircleTileDrawable();
        }
    }
}
