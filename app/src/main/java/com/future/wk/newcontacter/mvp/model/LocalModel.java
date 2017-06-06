package com.future.wk.newcontacter.mvp.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.future.wk.newcontacter.data.dalex.ContactDALEx;
import com.future.wk.newcontacter.data.dalex.PhoneNumberDALEx;
import com.future.wk.newcontacter.mvp.contract.ILocalContract;

import java.util.List;

/**
 * Created by samsung on 2017/5/3.
 */

public class LocalModel implements ILocalContract.ILocalModel {
    @Override
    public Cursor getFromContactList(Context mcontext) {
        ContentResolver contentResolver = mcontext.getContentResolver();
        // 获取手机联系人
        Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/contacts"), null, null, null, ContactsContract.Contacts.SORT_KEY_PRIMARY);
        return cursor;
    }

    public List<ContactDALEx> getNormalContactList(Context mcontext){
        return  ContactDALEx.get().findAllNormalContacter();
    }

    public void saveContactList(List<ContactDALEx> mList){
        ContactDALEx.get().saveOrUpdate(mList);
    }

    public void saveContactNumber(List<PhoneNumberDALEx> mList){
        PhoneNumberDALEx.get().saveOrUpdate(mList);
    }

    public Cursor getNumberByContactID(Context mcontext, String ContactID){
        ContentResolver contentResolver = mcontext.getContentResolver();
        Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{ContactID}, null);
        return phoneCursor;
    }

    public Cursor getAllNumberFromCP(Context mcontext){
        ContentResolver contentResolver = mcontext.getContentResolver();
        Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        return phoneCursor;
    }
}
