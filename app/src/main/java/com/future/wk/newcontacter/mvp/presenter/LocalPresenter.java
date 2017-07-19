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
import com.future.wk.newcontacter.util.ICallBackObject;
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
    Map<String, ContactDALEx> mNetworkContactMap =  new HashMap<String, ContactDALEx>();   //contact id 作为Key进行存储,该map用于更新的时候存储网络的contact
    Map<String, List<PhoneNumberDALEx>> mNumberMap = new HashMap<String, List<PhoneNumberDALEx>>();

    public LocalPresenter(){
        mLocalModel = new LocalModel();
    }

    public int numOfLocalContact(Context mcontext){
        Cursor contactCursor = mLocalModel.getFromContactList(mcontext);
        int length = contactCursor.getCount();
        Log.d(TAG,"cursor count:"+length);
        return length;
    }

    public void updateNetworkContact(Context mcontext, final ICallBack<ContactDALEx> callBack){
        List<ContactDALEx> mUpdateList = new ArrayList<>();  //需要更新的contact  list
        final List<ContactDALEx> mList = new ArrayList<>();    //返回给adapter的所有的contact list
        List<PhoneNumberDALEx> mUpdateNumberList = new ArrayList<>();  //需要更新的number list

        //init the number into Map
        Log.d(TAG,"init the number into Map");
        initNumberMap(mcontext);
        mList.addAll(initContactMap(mcontext));
        initNetworkContactMap(mcontext);
        Log.d(TAG,"Number map size:"+mNumberMap.size());
        Log.d(TAG,"Contact map size:"+mContactMap.size());

        List<ContactDALEx> contactList = mLocalModel.getNormalContactList(mcontext);
        Log.d(TAG,"contactList size:"+contactList.size());
        for(int i = 0; i < mList.size(); i++){
            String contact_id = mList.get(i).getUserid();
            if(mNetworkContactMap.get(contact_id) == null){  //find the contact need add into network
                mUpdateList.add(mContactMap.get(contact_id)); //增加需要更新的contact list
                mUpdateNumberList.addAll(mNumberMap.get(contact_id));  //增加需要更新的Number list
            }
        }
        //define the save number callback
        ICallBackObject<PhoneNumberDALEx> callBackObject = new ICallBackObject<PhoneNumberDALEx>() {
            @Override
            public void onSuccess(PhoneNumberDALEx List) {
            }
            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        };
        Log.d(TAG,"Update number list size:"+mUpdateNumberList.size());
        mLocalModel.saveContactNumber(mUpdateNumberList,callBackObject);//将号码存入数据库和网络服务器中

        //define the save contact callback
        ICallBackObject<ContactDALEx> callBackObject1 = new ICallBackObject<ContactDALEx>() {
            @Override
            public void onSuccess(ContactDALEx contact) {
                Log.d(TAG,"onSuccess");
                callBack.onSuccess(mList);
            }
            @Override
            public void onFail(String msg) {
                Log.d(TAG,"onFail");
                callBack.onFail(msg);
            }
        };
        Log.d(TAG,"Update contact size:"+mUpdateList.size());
        mLocalModel.saveContactList(mUpdateList, callBackObject1); //保存联系人到数据库中
    }

    public List<PhoneNumberDALEx> initNumberMap(Context mcontext){
        List<PhoneNumberDALEx> mNumberList = new ArrayList<>();
        Cursor phoneCursor = mLocalModel.getAllNumberFromCP(mcontext);
        mNumberMap.clear();
        if(phoneCursor.moveToFirst()) {
            do{
                PhoneNumberDALEx mNumber = new PhoneNumberDALEx();
                String contact_id = phoneCursor.getString(phoneCursor.getColumnIndex(CONTACT_ID));
                Log.d(TAG,"contact_id:"+contact_id);
                mNumber.setPhonenumber(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                mNumber.setUserid(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)));
                if(mNumberMap.containsKey(contact_id)) {  //一个联系人几个号码的情况
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
        return mNumberList;
    }

    public List<ContactDALEx> initContactMap(Context mcontext){
        List<ContactDALEx> mList = new ArrayList<>();
        Cursor cursor = mLocalModel.getFromContactList(mcontext);
        mContactMap.clear();
        if(!cursor.moveToFirst()){
            return null;
        }
        do{
            ContactDALEx mContact = new ContactDALEx();
            mContact.setUsername(cursor.getString(cursor.getColumnIndex(DISPLAY_NAME)));
            mContact.setPhonecount(cursor.getInt(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
            mContact.setNamepinyin(getSortKey(cursor.getString(cursor.getColumnIndex(SORT_KEY))));
            mContact.setYellowpage(ContactDALEx.INDEXFORNORMAL);
            mContact.setUserid(cursor.getString(cursor.getColumnIndex(ID)));
            String contractID = cursor.getString(cursor.getColumnIndex(ID));
            if((mNumberMap.get(contractID) != null) && (mNumberMap.get(contractID).size() >0)){
                Log.d(TAG,"contractID:"+contractID);
                Log.d(TAG, "Phone number:"+ mNumberMap.get(contractID).get(0).getPhonenumber());
                Log.d(TAG,"Number size:"+mNumberMap.get(contractID).size());
                mContact.setUserphone(mNumberMap.get(contractID).get(0).getPhonenumber());
            }
            mContactMap.put(contractID,mContact);
            Log.d(TAG,"Name :"+ cursor.getString(cursor.getColumnIndex(DISPLAY_NAME)));
            mList.add(mContact);  //保存联系人list给view
        }while (cursor.moveToNext());
        return mList;
    }

    public List<ContactDALEx> initNetworkContactMap(Context mcontext){
        mNetworkContactMap.clear();
        List<ContactDALEx> mContactList = mLocalModel.getNormalContactList(mcontext);
        for(int i = 0; i < mContactList.size(); i++){
            mNetworkContactMap.put(mContactList.get(i).getUserid(),mContactList.get(i));
        }
        return mContactList;
    }


    public void getFromContactList(Context mcontext, final ICallBack<ContactDALEx> callBack){
        final List<ContactDALEx> mList = new ArrayList<>();
        ICallBackObject<PhoneNumberDALEx> callBackObject = new ICallBackObject<PhoneNumberDALEx>() {
            @Override
            public void onSuccess(PhoneNumberDALEx List) {
            }
            @Override
            public void onFail(String msg) {
                Log.d(TAG,"PhoneNumber fail:"+msg);
                callBack.onFail(msg);
            }
        };
        //init the number into Map
        Log.d(TAG,"Get number from provider...");
        mLocalModel.saveContactNumber(initNumberMap(mcontext),callBackObject);//将号码存入数据库和网络服务器中

        //init the contact list into Map
        ICallBackObject<ContactDALEx> callBackObject1 = new ICallBackObject<ContactDALEx>() {
            @Override
            public void onSuccess(ContactDALEx contact) {
                 Log.d(TAG,"onSuccess");
                callBack.onSuccess(mList);
            }
            @Override
            public void onFail(String msg) {
                Log.d(TAG,"contact onFail:"+msg);
                callBack.onFail(msg);
            }
        };
        Log.d(TAG,"start back up contact...");
        mList.addAll(initContactMap(mcontext));
        mLocalModel.saveContactList(mList, callBackObject1); //保存联系人到数据库中
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
