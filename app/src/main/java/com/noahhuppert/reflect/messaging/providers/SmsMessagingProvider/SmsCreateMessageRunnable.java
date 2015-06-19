package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Telephony;
import android.telephony.SmsManager;

import com.noahhuppert.reflect.exceptions.Errors;
import com.noahhuppert.reflect.exceptions.InvalidMessagingCreationData;
import com.noahhuppert.reflect.intents.SmsOutgoingDelivered;
import com.noahhuppert.reflect.intents.SmsOutgoingSent.SmsOutgoingSent;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.MessagingResourceType;
import com.noahhuppert.reflect.messaging.models.ReflectMessage;
import com.noahhuppert.reflect.threading.ResultHandlerThread;
import com.noahhuppert.reflect.threading.ThreadResultHandler;
import com.noahhuppert.reflect.utils.SmsThreadUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SmsCreateMessageRunnable extends ResultHandlerThread<ReflectMessage> {
    private static final String TAG = SmsCreateMessageRunnable.class.getSimpleName();

    private final ReflectMessage reflectMessage;
    private final Context context;

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
        List<PendingIntent> deliveryIntents;

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
            deliveryIntents = generateDeliveryIntents(messageParts.size(), tempMessageId);
        }

        long threadId = SmsThreadUtils.getOrCreateThreadId(context, receiverPhoneNumber);

        for(int i = 0; i < messageParts.size(); i++){
            SmsManager.getDefault().sendTextMessage(receiverPhoneNumber,
                    null,
                    messageParts.get(i),
                    sentIntents.get(i),
                    deliveryIntents.get(i));

            ContentValues insertMessageValues = new ContentValues();
            insertMessageValues.put(Telephony.TextBasedSmsColumns.ERROR_CODE, Errors.ERROR_OK);
            insertMessageValues.put(Telephony.TextBasedSmsColumns.BODY, messageParts.get(i));
            insertMessageValues.put(Telephony.TextBasedSmsColumns.ADDRESS, receiverPhoneNumber);
            insertMessageValues.put(Telephony.TextBasedSmsColumns.THREAD_ID, threadId);

            synchronized (context){
                context.getContentResolver().insert(Telephony.Sms.Sent.CONTENT_URI, insertMessageValues);
            }
        }

        return null;
    }

    private List<PendingIntent> generateSentIntents(int messageParts, String tempMessageId){
        List<PendingIntent> sentIntents = new ArrayList<>();

        Uri uri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(CommunicationType.SMS)
                .appendPath(MessagingResourceType.MESSAGE)
                .appendPath(tempMessageId)
                .build();

        for(int i = 0; i < messageParts; i++){
            Intent intent;

            synchronized (context){
                intent = new Intent(SmsOutgoingSent.ACTION_SENT,
                        uri,
                        context,
                        SmsOutgoingSent.class);

                intent.putExtra(SmsMessagingProvider.SMS_SENT_INTENT_EXTRA_TOTAL_MESSAGE_PARTS, messageParts);
                intent.putExtra(SmsMessagingProvider.SMS_SENT_INTENT_EXTRA_MESSAGE_PART, i + 1);

                sentIntents.add(PendingIntent.getBroadcast(context, 0, intent, 0));
            }
        }

        return sentIntents;
    }

    private List<PendingIntent> generateDeliveryIntents(int messageParts, String tempMessageId){
        List<PendingIntent> deliveryIntents = new ArrayList<>();

        Uri uri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(CommunicationType.SMS)
                .appendPath(MessagingResourceType.MESSAGE)
                .appendPath(tempMessageId)
                .build();

        for(int i = 0; i < messageParts; i++){
            Intent intent;

            synchronized (context){
                intent = new Intent(SmsOutgoingDelivered.ACTION_DELIVERED,
                        uri,
                        context,
                        SmsOutgoingDelivered.class);

                intent.putExtra(SmsMessagingProvider.SMS_SENT_INTENT_EXTRA_TOTAL_MESSAGE_PARTS, messageParts);
                intent.putExtra(SmsMessagingProvider.SMS_SENT_INTENT_EXTRA_MESSAGE_PART, i + 1);

                deliveryIntents.add(PendingIntent.getBroadcast(context, 0, intent, 0));
            }
        }

        return deliveryIntents;
    }
}
