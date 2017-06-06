package com.future.wk.newcontacter.mvp.presenter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.future.wk.newcontacter.R;
import com.future.wk.newcontacter.data.dalex.ContactDALEx;
import com.future.wk.newcontacter.data.dalex.PhoneNumberDALEx;
import com.future.wk.newcontacter.mvp.contract.ILocalContract;
import com.future.wk.newcontacter.mvp.model.LocalModel;
import com.future.wk.newcontacter.base.mvp.presenter.BasePresenter;
import com.future.wk.newcontacter.util.ICallBack;
import com.future.wk.newcontacter.widget.processDialog.processDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by samsung on 2017/5/3.
 */

public class LocalPresenter extends BasePresenter<ILocalContract.ILocalView> {
    private ILocalContract.ILocalModel mLocalModel;
    private String TAG = "LocalPresenter";

    Uri CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    String CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
    String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    String ID = ContactsContract.Contacts._ID;
    String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
    String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
    String SORT_KEY = ContactsContract.Contacts.SORT_KEY_PRIMARY;

    //使用两个MAP来保存从contentprovider中读出来的数据
    Map<String, ContactDALEx> mContactMap =  new HashMap<String, ContactDALEx>();   //contact id 作为Key进行存储
    Map<String, List<PhoneNumberDALEx>> mNumberMap = new HashMap<String, List<PhoneNumberDALEx>>();

    public LocalPresenter(){
        mLocalModel = new LocalModel();
    }

    public void getFromContactList(Context mcontext, ICallBack<ContactDALEx> callBack){
        List<ContactDALEx> mList = new ArrayList<>();
        List<PhoneNumberDALEx> mNumberList = new ArrayList<>();

        //init the number into Map
        Cursor phoneCursor = mLocalModel.getAllNumberFromCP(mcontext);
        if(phoneCursor.moveToFirst()) {
            do{
                PhoneNumberDALEx mNumber = new PhoneNumberDALEx();
                String contact_id = phoneCursor.getString(phoneCursor.getColumnIndex(CONTACT_ID));
                Log.d(TAG,"contact_id:"+contact_id);
                mNumber.setPhonenumber(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                mNumber.setUserid(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)));
                if(mNumberMap.containsKey(contact_id)) {
                    mNumberMap.get(contact_id).add(mNumber);
                }else{
                    List<PhoneNumberDALEx> NumberList = new ArrayList<>();
                    NumberList.add(mNumber);
                    mNumberMap.put(contact_id, NumberList);
                }
                mNumber.setNumberid(UUID.randomUUID().toString());
                mNumberList.add(mNumber);
            }while (phoneCursor.moveToNext());
        }
        mLocalModel.saveContactNumber(mNumberList);//将号码存入数据库中

        //init the contact list into Map
        Cursor cursor = mLocalModel.getFromContactList(mcontext);
        if(!cursor.moveToFirst()){
            return ;
        }
        do{
            ContactDALEx mContact = new ContactDALEx();
            mContact.setUsername(cursor.getString(cursor.getColumnIndex(DISPLAY_NAME)));
            mContact.setPhonecount(cursor.getInt(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
            mContact.setNamepinyin(getSortKey(cursor.getString(cursor.getColumnIndex(SORT_KEY))));
            mContact.setYellowpage(ContactDALEx.INDEXFORNORMAL);
            mContact.setUserid(cursor.getString(cursor.getColumnIndex(ID)));
            String contractID = cursor.getString(cursor.getColumnIndex(ID));
            mContactMap.put(contractID,mContact);
            if((mNumberMap.get(contractID) != null) && (mNumberMap.get(contractID).size() >0)){
                Log.d(TAG,"contractID:"+contractID);
                Log.d(TAG, "Phone number:"+ mNumberMap.get(contractID).get(0).getPhonenumber());
                Log.d(TAG,"Number size:"+mNumberMap.get(contractID).size());
                mContact.setUserphone(mNumberMap.get(contractID).get(0).getPhonenumber());
            }
            Log.d(TAG,"Name :"+ cursor.getString(cursor.getColumnIndex(DISPLAY_NAME)));
            mList.add(mContact);  //保存联系人list给view
        }while (cursor.moveToNext());


        mLocalModel.saveContactList(mList); //保存联系人到数据库中
        if(mList.size() > 0) {
            callBack.onSuccess(mList);
        }else{
            callBack.onFail("存储失败...");
        }
    }

    private static String getSortKey(String sortKeyString) {
        String key = sortKeyString.substring(0, 1).toUpperCase();
        if (key.matches("[A-Z]")) {
            return key;
        }
        return "#";
    }


    public List<ContactDALEx> getNormalContact(Context context){
        return mLocalModel.getNormalContactList(context);
    }

}
