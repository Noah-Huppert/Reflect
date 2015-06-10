package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.telephony.SmsManager;

import com.noahhuppert.reflect.exceptions.InvalidMessagingCreationData;
import com.noahhuppert.reflect.intents.SmsOutgoingSent;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.MessagingResourceType;
import com.noahhuppert.reflect.messaging.models.ReflectMessage;
import com.noahhuppert.reflect.threading.ResultHandlerThread;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SmsCreateMessageRunnable extends ResultHandlerThread<ReflectMessage> {
    private ReflectMessage reflectMessage;
    private Context context;

    public SmsCreateMessageRunnable(ReflectMessage reflectMessage, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler){
        super(threadResultHandler);
        this.reflectMessage = reflectMessage;
        this.context = context;
    }

    @Override
    protected ReflectMessage execute() throws Exception {
        String tempMessageId = "temp-" + UUID.randomUUID().toString();

        String receiverPhoneNumber;
        List<String> messageParts;
        List<PendingIntent> sentIntents;

        synchronized (reflectMessage) {
            if (reflectMessage == null) {
                throw new InvalidMessagingCreationData("The provided message can not be null", null);
            }

            if (reflectMessage.getBody() == null) {
                throw new InvalidMessagingCreationData("The provided message body can not be null", null);
            }

            if (reflectMessage.getReceiverUri() == null) {
                throw new InvalidMessagingCreationData("The provided message receiver uri can not be null", null);
            }

            if(!reflectMessage.getReceiverUri().getScheme().equals(SmsMessagingProvider.SMS_URI_SCHEME)){
                throw new InvalidMessagingCreationData("The provided message receiver uri must use the \"" + SmsMessagingProvider.SMS_URI_SCHEME + "\" scheme",
                        reflectMessage.getReceiverUri().getScheme());
            }

            receiverPhoneNumber = reflectMessage.getReceiverUri().getAuthority();
            messageParts = SmsManager.getDefault().divideMessage(reflectMessage.getBody());
            sentIntents = generateSentIntents(messageParts.size(), tempMessageId);
            //TODO Make delivery intents
            /*
            TODO Handle sent and delivery intents
            To handle sent intents maybe have a singleton that holds all messages pending delivery,
            find a way to store these in memory in case the app is closed. Once a sent intent is
            received look at the singleton and mark the message status appropriately and then call
            for conversation update or something
             */
        }

        return null;
    }

    private List<PendingIntent> generateSentIntents(int messageParts, String tempMessageId){
        List<PendingIntent> sentIntents = new ArrayList<>();

        Uri.Builder uriBuilder = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(CommunicationType.SMS)
                .appendPath(MessagingResourceType.MESSAGE)
                .appendQueryParameter(SmsMessagingProvider.SMS_CREATE_MESSAGE_URI_QUERY_TOTAL_MESSAGE_PARTS, messageParts + "");

        for(int i = 0; i < messageParts; i++){
            Uri uri = uriBuilder
                    .appendPath(tempMessageId)
                    .appendPath(i + "")
                    .build();

            Intent intent;

            synchronized (context){
                intent = new Intent(SmsOutgoingSent.ACTION_SENT,
                        uri,
                        context,
                        SmsOutgoingSent.class);

                sentIntents.add(PendingIntent.getBroadcast(context, 0, intent, 0));
            }
        }

        return sentIntents;
    }
}
