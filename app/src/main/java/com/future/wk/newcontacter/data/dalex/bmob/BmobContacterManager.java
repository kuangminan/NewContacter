package com.future.wk.newcontacter.data.dalex.bmob;

import android.util.Log;

import com.future.wk.newcontacter.base.IContactManager;
import com.future.wk.newcontacter.data.dalex.ContactDALEx;
import com.future.wk.newcontacter.util.ICallBackObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

import static com.future.wk.newcontacter.data.dalex.bmob.contacterBmob.TABLENAME;

/**
 * Created by samsung on 2017/6/13.
 */

public class BmobContacterManager implements IContactManager {

    private String TAG = "BmobContacterManager";



    public void addContacter(ContactDALEx conact, final ICallBackObject<ContactDALEx> callBackObject){
        contacterBmob contacter = new contacterBmob();
        if(conact != null){
            contacter = contacter.ConvertTocontacterBmob(conact);
            contacter.save(new SaveListener<String>() {
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

    public void addContacterList(final List<ContactDALEx> conactList, final ICallBackObject<ContactDALEx> callBackObject){
        Log.d(TAG,"addContacterList size:"+conactList.size());
        BmobBatch batch = new BmobBatch();
        List<BmobObject> contactBombList = new ArrayList<BmobObject>();
        if(conactList != null) {
            for (int i = 0; i < conactList.size(); i++) {
                contacterBmob contactbmob = new contacterBmob();
                contactBombList.add(contactbmob.ConvertTocontacterBmob(conactList.get(i)));
            }
            Log.d(TAG,"contactBombList size:"+contactBombList.size());
            batch.insertBatch(contactBombList);
            batch.doBatch(new QueryListListener<BatchResult>() {
                @Override
                public void done(List<BatchResult> list, BmobException e) {
                    if (e == null) {
                        for(int i = 0; i<list.size(); i++){
                            BatchResult result = list.get(i);
                            BmobException ex = result.getError();
                            if(ex == null){
                                Log.d(TAG,"第"+i+"个数据添加成功");
                            }else{
                                Log.d(TAG, "第"+ i +"个数据添加失败 "+ex.getMessage());
                            }
                        }

                        Log.d(TAG,"onSuccess");
                        callBackObject.onSuccess(null);
                    } else {
                        Log.d(TAG,"onFail:"+e.getMessage());
                        callBackObject.onFail(e.getMessage());
                    }
                }
            });
        }else{
            Log.d(TAG,"存储数据为空");
            callBackObject.onFail("存储数据为空");
        }
    }

    /*查找一个contacter对象*/
    public void queryContactByID(String objectid, final ICallBackObject<ContactDALEx> callBackObject){
        BmobQuery<contacterBmob> query = new BmobQuery<contacterBmob>(TABLENAME);
        query.getObject(objectid, new QueryListener<contacterBmob>() {
            @Override
            public void done(contacterBmob contacterbmob, BmobException e) {
                if(e == null){
                    ContactDALEx contact = new ContactDALEx();
                    if(contacterbmob != null){
                        contact = contacterbmob.ConvertToContactDALEx();
                        callBackObject.onSuccess(contact);
                    }
                }else{
                    callBackObject.onFail(e.getMessage());
                }
            }
        });

    }


}
