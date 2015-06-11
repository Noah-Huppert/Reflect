package com.noahhuppert.reflect.intents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.Set;

public class SmsIncomingReceived extends BroadcastReceiver {
    private static final String TAG = SmsIncomingReceived.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] messageParts = Telephony.Sms.Intents.getMessagesFromIntent(intent);

        for(int i = 0; i < messageParts.length; i++){
            Log.d(TAG, "(" + (i + 1) + "/" + messageParts.length + ") " + messageParts[i].getOriginatingAddress());
        }
        //TODO Handle incoming sms messages
        //How do I figure out what this new message is?
        //There has to be a good way, there are tons of apps
        /*
        TODO See TextSecure way
        GET THREAD ID
        https://github.com/WhisperSystems/TextSecure/blob/master/src/org/thoughtcrime/securesms/jobs/SmsReceiveJob.java ln 71#insertMessageInbox
        |
        v
        https://github.com/WhisperSystems/TextSecure/blob/e130d0a3e62d833e799f32a463de01e42f28b5ca/src/org/thoughtcrime/securesms/database/SmsDatabase.java ln 387#getThreadIdFor
        |
        v
        https://github.com/WhisperSystems/TextSecure/blob/e130d0a3e62d833e799f32a463de01e42f28b5ca/src/org/thoughtcrime/securesms/database/ThreadDatabase.java ln 354#getThreadIdFor

        GET MESSAGE ID
        https://github.com/WhisperSystems/TextSecure/blob/e130d0a3e62d833e799f32a463de01e42f28b5ca/src/org/thoughtcrime/securesms/database/SmsDatabase.java ln 408#db.insert
         */
    }
}
