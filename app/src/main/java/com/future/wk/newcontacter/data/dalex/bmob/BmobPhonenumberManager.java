package com.future.wk.newcontacter.data.dalex.bmob;

import android.util.Log;

import com.future.wk.newcontacter.base.IPhonenumberManager;
import com.future.wk.newcontacter.data.dalex.PhoneNumberDALEx;
import com.future.wk.newcontacter.util.ICallBackObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by samsung on 2017/6/13.
 */

public class BmobPhonenumberManager implements IPhonenumberManager {

    private static int MaxNum = 40;
    private String TAG = "BmobPhonenumberManager";

    /*保存单独一个phonenumber对象*/
    public void addContacter(PhoneNumberDALEx phonenumber, final ICallBackObject<PhoneNumberDALEx> callBackObject){
        if(phonenumber != null){
            phonenumberBmob phonenumberbomb = new phonenumberBmob();
            phonenumberbomb.ConvertToPhonenumberBmob(phonenumber);
            phonenumberbomb.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if(e == null){
                        callBackObject.onSuccess(null);
                    }else{
                        callBackObject.onFail(e.getMessage());
                    }
                }
            });
        }
    }

    /*保存phonenumber对象list*/
    public void addContacterList(List<PhoneNumberDALEx> phonenumberList, final ICallBackObject<PhoneNumberDALEx> callBackObject){

        List<BmobObject> contactBombList = new ArrayList<BmobObject>();
        if(phonenumberList != null){
            int phoneNumberSize = phonenumberList.size();
            Log.d(TAG,"phoneNumberSize:"+phoneNumberSize);
            int batchNum = phoneNumberSize/MaxNum;
            Log.d(TAG,"batchNum:"+batchNum);
            int startNum = 0;
            for(int j = 1; j <= batchNum; j++) {
                BmobBatch batch = new BmobBatch();
                Log.d(TAG,"startNum:"+startNum);
                Log.d(TAG,"MaxNum:"+j*MaxNum);
                for (int i = startNum; i < j*MaxNum; i++) {
                    phonenumberBmob phonenumber = new phonenumberBmob();
                    contactBombList.add(phonenumber.ConvertToPhonenumberBmob(phonenumberList.get(i)));
                }
                batch.insertBatch(contactBombList);
                batch.doBatch(new QueryListListener<BatchResult>() {
                    @Override
                    public void done(List<BatchResult> list, BmobException e) {
                        if (e == null) {
                            Log.d(TAG,"Successfull");
                            //callBackObject.onSuccess(null);
                        } else {
                            Log.d(TAG,"Save number fail:"+e.getMessage());
                            callBackObject.onFail(e.getMessage());
                        }
                    }
                });
                contactBombList.clear();
                startNum = MaxNum*j;
            }
            //store the last
            Log.d(TAG,"Last startNum:"+startNum);
            BmobBatch batch = new BmobBatch();
            for(int i = startNum; i < phoneNumberSize; i++) {
                phonenumberBmob phonenumber = new phonenumberBmob();
                contactBombList.add(phonenumber.ConvertToPhonenumberBmob(phonenumberList.get(i)));
            }
            batch.insertBatch(contactBombList);
            batch.doBatch(new QueryListListener<BatchResult>() {
                @Override
                public void done(List<BatchResult> list, BmobException e) {
                    if (e == null) {
                        callBackObject.onSuccess(null);
                    } else {
                        Log.d(TAG,"Save number fail:"+e.getMessage());
                        callBackObject.onFail(e.getMessage());
                    }
                }
            });

        }
    }




}
