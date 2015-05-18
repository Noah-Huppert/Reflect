package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;

import com.noahhuppert.reflect.exceptions.InvalidMessagingProviderPushData;
import com.noahhuppert.reflect.intents.IntentFilter;
import com.noahhuppert.reflect.intents.IntentHandler;
import com.noahhuppert.reflect.intents.IntentReceiver;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.MessagingResourceType;
import com.noahhuppert.reflect.messaging.models.ReflectMessage;
import com.noahhuppert.reflect.messaging.providers.MessagingProviderPushRunnable;
import com.noahhuppert.reflect.threading.ThreadResultHandler;
import com.noahhuppert.reflect.uri.MessagingUriBuilder;
import com.noahhuppert.reflect.utils.UriUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SmsPushMessageRunnable extends MessagingProviderPushRunnable<ReflectMessage> {
    private final String tempMessageId;
    private final IntentHandler sentIntentHandler;

    public SmsPushMessageRunnable(ReflectMessage data, Context context, ThreadResultHandler<ReflectMessage> threadResultHandler, IntentHandler sentIntentHandler) {
        super(data, context, threadResultHandler);
        tempMessageId = UUID.randomUUID().toString();
        this.sentIntentHandler = sentIntentHandler;
    }

    @Override
    protected ReflectMessage execute() throws Exception {
        if(data == null){
            throw new InvalidMessagingProviderPushData("The ReflectMessage provided cannot be null", null);
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

        String receiver = data.getReceiverUri().getHost();
        List<String> messages = SmsManager.getDefault().divideMessage(data.getBody());
        List<Intent> sentIntents = generateSentIntents(messages.size());

        SmsManager smsManager = SmsManager.getDefault();

        int i = 0;
        for(String messageText : messages){
            smsManager.sendTextMessage(receiver, null, messageText, sentIntents.get(i), null);
            i++;
        }

        return data;

        /**
         * Requirement:
         *  - Identify total message parts
         *  - Identify message part
         *  - Provide Intent
         */

        /**
         * TODO:
         *      - Set delivered intent
         *      - Handle sent and delivered intents
         *      - Send messages in parts
         *      - Get new message id from system once sent
         */
        /*
        //Get message data
        String destinationAddress = data.getReceiverUri().getPath();
        String messageText = data.getBody();

        //Send text
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(destinationAddress, null, messageText, null, null);

        return data;*/
    }

    private List<Intent> generateSentIntents(int messagesCount) throws URISyntaxException{
        List<Intent> sentIntents = new ArrayList<>();

        for(int i = 0; i < messagesCount; i++){
            URI javaUri = MessagingUriBuilder.Build(MessagingResourceType.MESSAGE, CommunicationType.SMS, tempMessageId);
            Uri uri = UriUtils.ToAndroidUri(javaUri);

            Intent intent;

            synchronized (context){
                intent = new Intent(SmsMessagingProvider.INTENT_ACTION_MESSAGE_SENT,
                        uri,
                        context,
                        IntentReceiver.class);
            }

            intent.putExtra(SmsMessagingProvider.INTENT_EXTRA_TOTAL_MESSAGE_PARTS, messagesCount);
            intent.putExtra(SmsMessagingProvider.INTENT_EXTRA_MESSAGE_PART, i);

            IntentReceiver.getInstance().RegisterIntentHandler(new IntentFilter(SmsMessagingProvider.INTENT_ACTION_MESSAGE_SENT, javaUri), sentIntentHandler);

            sentIntents.add(intent);
        }

        return sentIntents;
    }
}
