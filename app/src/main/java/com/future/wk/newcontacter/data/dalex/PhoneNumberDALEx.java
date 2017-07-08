package com.future.wk.newcontacter.data.dalex;

import android.util.Log;

import com.future.wk.newcontacter.base.store.db.SqliteBaseDALEx;
import com.future.wk.newcontacter.base.store.db.annotation.DatabaseField;
import com.future.wk.newcontacter.base.store.db.annotation.DatabaseField.FieldType;
import com.future.wk.newcontacter.base.store.db.annotation.SqliteDao;

import java.util.List;

/**
 * Created by samsung on 2017/5/26.
 */

public class PhoneNumberDALEx extends SqliteBaseDALEx {

    private String TAG = "PhoneNumberDALEx";

    private static final String USERID = "userid";
    private static final String PHONENUMBER = "phonenumber";

    @DatabaseField(Type = FieldType.VARCHAR, primaryKey = true)
    private String numberid;
    @DatabaseField(Type = FieldType.VARCHAR)
    private String userid;
    @DatabaseField(Type = FieldType.VARCHAR)
    private String phonenumber;

    public static PhoneNumberDALEx get(){
        return  SqliteDao.getDao(PhoneNumberDALEx.class);
    }

    public String getNumberid(){
        return numberid;
    }
    public void setNumberid(String id){
        this.numberid = id;
    }
    public String getUserid(){
        return userid;
    }
    public void setUserid(String userid){
        this.userid = userid;
    }
    public String getPhonenumber(){
        return phonenumber;
    }
    public void setPhonenumber(String phonenumber){
        this.phonenumber = phonenumber;
    }

    public List<PhoneNumberDALEx> getPhoneNumberByID(String userid){
        //通过userID 来查找对应的所有电话号码
        /*String sql1 = String.format("select * from %s",TABLE_NAME);
        List<PhoneNumberDALEx> mList1 = findList(sql1);
        Log.d(TAG,"mList1 size:"+mList1.size());*/
        String sql = String.format("select * from %s where %s = '%s'", TABLE_NAME,USERID,userid);
        Log.d(TAG, sql);
        List<PhoneNumberDALEx> mList = findList(sql);
        Log.d(TAG,"mList:"+(mList.size()));
        return mList;
    }


}
