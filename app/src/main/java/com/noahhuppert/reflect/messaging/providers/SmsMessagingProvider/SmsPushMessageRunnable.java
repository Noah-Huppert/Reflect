package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.content.Context;
import android.telephony.SmsManager;

import com.noahhuppert.reflect.messaging.ReflectMessage;
import com.noahhuppert.reflect.messaging.providers.MessagingProviderPushRunnable;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

public class SmsPushMessageRunnable extends MessagingProviderPushRunnable<ReflectMessage> {
    public SmsPushMessageRunnable(ReflectMessage data, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler) {
        super(data, context, threadResultHandler);
    }

    @Override
    protected ReflectMessage execute() throws Exception {
        String destinationAddress = data.getReceiverUri().getPath();
        String messageText = data.getBody();

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("7742191704", null, "Test text from Reflect", null, null);
        return data;
    }
}
