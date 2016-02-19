package com.gifsoundroid.moosetown.quickaddcontact;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by alexm_000 on 8/16/2015.
 */
public class ContactStorageManager {

    public static void storeNameAndNumber(String name, String number, Context context) throws ContactStorageException{
        final String methodTag = "storeNameAndNumber";
        //take from http://stackoverflow.com/questions/4744187/how-to-add-new-contacts-in-android

        if(name.isEmpty()){
            throw new ContactStorageException("Name has no value when attempting to save contact");
        }

        if(number.isEmpty()){
            throw new ContactStorageException("Number has no value when attempting to save contact");
        }

        boolean isDebuggable =  ( 0 != ( context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE ) );
        if(isDebuggable) {
            Log.d(context.getPackageName(),"in debugging mode, not saving contact");
            return;
        }


        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //set display name in ContentProviderOPeration
        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        name).build());


        ops.add(ContentProviderOperation.
                newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());

        try {
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e){
            throw new ContactStorageException("applying ContactsContract failed: "+ e.getMessage());
        }

    }




}
