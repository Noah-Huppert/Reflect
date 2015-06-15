package com.noahhuppert.reflect.messaging.providers.SmsMessagingProvider;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;

import com.noahhuppert.reflect.exceptions.InvalidIdException;
import com.noahhuppert.reflect.exceptions.NoTelephonyManagerException;
import com.noahhuppert.reflect.messaging.CommunicationType;
import com.noahhuppert.reflect.messaging.models.ReflectContact;
import com.noahhuppert.reflect.threading.ResultHandlerThread;
import com.noahhuppert.reflect.threading.ThreadResultHandler;
import com.noahhuppert.reflect.utils.TelephonyUtils;

public class SmsGetContactRunnable extends ResultHandlerThread<ReflectContact> {
    private long contactId;
    private Context context;

    public SmsGetContactRunnable(long contactId, Context context, ThreadResultHandler<ReflectContact> threadResultHandler) {
        super(threadResultHandler);
        this.contactId = contactId;
        this.context = context;
    }

    @Override
    protected ReflectContact execute() throws Exception {
        //TODO Figure out why displayName and Uri are not getting retrieved
        Cursor cursor = null;

        try{
            Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);

            synchronized (context){
                cursor = context.getContentResolver().query(contactUri,
                        SmsMessagingProvider.SMS_CONTACT_PROJECTION,
                        null, null, null);
            }

            if(cursor.getCount() == 0){
                throw new InvalidIdException("The provided contact id does not point to any contacts", contactId + "");
            }

            if(cursor.getCount() > 1){
                throw new InvalidIdException("The provided contact id points to more than one contact", contactId + "");
            }

            return reflectContactFromCursor(cursor);
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }

    private ReflectContact reflectContactFromCursor(Cursor cursor) throws NoTelephonyManagerException{
        ReflectContact reflectContact = new ReflectContact();

        cursor.moveToFirst();

        String id = cursor.getString(0);
        String displayName = cursor.getString(1);

        String avatarUriString = cursor.getString(2);
        Uri avatarUri = avatarUriString != null ? Uri.parse(avatarUriString) : null;

        Uri uri = getContactUri(id);

        reflectContact.setId(id);
        reflectContact.setProtocol(CommunicationType.SMS);
        reflectContact.setAvatarUrl(avatarUri);
        reflectContact.setUri(uri);

        return reflectContact;
    }

    private Uri getContactUri(String lookupKey) throws NoTelephonyManagerException{
        Cursor cursor = null;

        try{
            String[] contactDataProjection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

            String contactDataQuery = ContactsContract.Data.MIMETYPE + " = ? " +
                                      "AND " + ContactsContract.Data.LOOKUP_KEY + " = ?";
            String[] contactDataQueryArgs = {ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, lookupKey};

            synchronized (context){
                cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                        contactDataProjection,
                        contactDataQuery,
                        contactDataQueryArgs,
                        null);
            }

            if(cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            String phoneNumber = cursor.getString(0);

            String defaultCountryCode;

            synchronized (context){
                defaultCountryCode = TelephonyUtils.GetTelephonyManager(context).getSimCountryIso();
            }

            phoneNumber = PhoneNumberUtils.formatNumber(phoneNumber, defaultCountryCode);

            return new Uri.Builder()
                    .scheme(SmsMessagingProvider.SMS_URI_SCHEME)
                    .authority(phoneNumber)
                    .build();
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }
}
