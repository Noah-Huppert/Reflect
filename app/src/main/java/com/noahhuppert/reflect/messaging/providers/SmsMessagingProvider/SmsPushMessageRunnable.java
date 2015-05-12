package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Telephony;
import android.telephony.SmsManager;

import com.noahhuppert.reflect.exceptions.InvalidMessagingProviderPushData;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.MessagingResourceType;
import com.noahhuppert.reflect.messaging.ReflectMessage;
import com.noahhuppert.reflect.messaging.providers.MessagingProviderPushRunnable;
import com.noahhuppert.reflect.threading.ThreadResultHandler;
import com.noahhuppert.reflect.uri.MessagingUriBuilder;
import com.noahhuppert.reflect.uri.MessagingUriUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SmsPushMessageRunnable extends MessagingProviderPushRunnable<ReflectMessage> {
    public SmsPushMessageRunnable(ReflectMessage data, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler) {
        super(data, context, threadResultHandler);
    }

    @Override
    protected ReflectMessage execute() throws Exception {
        if(data == null){
            throw new InvalidMessagingProviderPushData("The ReflectMessage provided cannot be null", data.toString());
        }

        if(data.getBody() == null){
            throw new InvalidMessagingProviderPushData("The ReflectMessage provided cannot have a null body", data.toString());
        }

        if(data.getReceiverUri() == null){
            throw new InvalidMessagingProviderPushData("The ReflectMessage provided cannot have a null receiverUri", data.toString());
        }

        if(!data.getReceiverUri().getScheme().equals(SmsMessagingProvider.SMS_URI_SCHEME)){
            throw new InvalidMessagingProviderPushData("The ReflectMessage provided must have a recieverUri with the scheme \"" +
                    SmsMessagingProvider.SMS_URI_SCHEME + "\"", data.toString());
        }

        String tempMessageId = UUID.randomUUID().toString();

        String receiver = data.getReceiverUri().getPath();
        List<String> messages = SmsManager.getDefault().divideMessage(data.getBody());
        List<Intent> sentIntents = generateSentIntents(messages.size(), tempMessageId);

        /**
         * TODO:
         *      - Set delivered intent
         *      - Handle sent and delivered intents
         *      - Send messages in parts
         *      - Get new message id from system once sent
         */

        return null;
        /*
        //Get message data
        String destinationAddress = data.getReceiverUri().getPath();
        String messageText = data.getBody();

        //Send text
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(destinationAddress, null, messageText, null, null);

        return data;*/
    }

    private List<Intent> generateSentIntents(int messagesCount, String tempMessageId) throws URISyntaxException{
        List<Intent> sentIntents = new ArrayList<>();

        for(int i = 0; i <= messagesCount; i++){
            String action = SmsMessagingProvider.INTENT_ACTION_MESSAGE_SENT;
            URI javaUri = MessagingUriBuilder.Build(MessagingResourceType.MESSAGE, CommunicationType.SMS, tempMessageId + "/" + i);
            Uri uri = MessagingUriUtils.ToAndroidUri(javaUri);

            Intent sentIntent;

            synchronized (context) {
                sentIntent = new Intent(action,
                        uri,
                        context,
                        SmsPushMessageRunnable.SmsMessageDeliveryListener.class);
            }

            sentIntents.add(sentIntent);
        }

        return sentIntents;
    }

    private Intent generateDeliveredIntent(String tempMessageId) throws URISyntaxException{
        String action = SmsMessagingProvider.INTENT_ACTION_MESSAGE_DELIVERED;
        URI javaUri = MessagingUriBuilder.Build(MessagingResourceType.MESSAGE, CommunicationType.SMS, tempMessageId);
        Uri uri = MessagingUriUtils.ToAndroidUri(javaUri);

        Intent deliveredIntent;

        synchronized (context){
            deliveredIntent = new Intent(action,
                    uri,
                    context,
                    SmsPushMessageRunnable.SmsMessageDeliveryListener.class);
        }

        return deliveredIntent;
    }

    public static class SmsMessageDeliveryListener extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
