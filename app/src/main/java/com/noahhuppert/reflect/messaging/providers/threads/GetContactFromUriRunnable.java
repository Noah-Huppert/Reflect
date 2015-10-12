package com.noahhuppert.reflect.messaging.providers.threads;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.providers.MessagingProviderManager;
import com.noahhuppert.reflect.messaging.providers.threads.base.HandlerMessagePayload;
import com.noahhuppert.reflect.messaging.providers.threads.base.MessagingProviderRunnable;

public class GetContactFromUriRunnable extends MessagingProviderRunnable {
    private Uri contactUri;

    public GetContactFromUriRunnable(@NonNull Handler handler, Context context, @CommunicationType String communicationType, @NonNull Uri contactUri) {
        super(handler, HandlerMessagePayload.CONTACT, context, communicationType);
        this.contactUri = contactUri;
    }

    @Override
    public Object onRun() {
        return MessagingProviderManager.get(communicationType).getContactFromUri(context, contactUri);
    }
}
