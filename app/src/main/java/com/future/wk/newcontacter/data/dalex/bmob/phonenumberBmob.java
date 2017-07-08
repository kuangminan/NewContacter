package com.future.wk.newcontacter.data.dalex.bmob;

import com.future.wk.newcontacter.data.dalex.PhoneNumberDALEx;

import cn.bmob.v3.BmobObject;

/**
 * Created by samsung on 2017/6/13.
 */

public class phonenumberBmob extends BmobObject {
    private String numberid;
    private String userid;
    private String phonenumber;
    public phonenumberBmob(){
        this.setTableName("phonenumber");
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

    public PhoneNumberDALEx ConvertToPhoneNumberDALEx(){
        PhoneNumberDALEx phonenumber = new PhoneNumberDALEx();
        phonenumber.setNumberid(this.getNumberid());
        phonenumber.setUserid(this.getUserid());
        phonenumber.setPhonenumber(this.getPhonenumber());
        return phonenumber;
    }

    public phonenumberBmob ConvertToPhonenumberBmob(PhoneNumberDALEx phonenumber){
        phonenumberBmob phonenumberbmob = new phonenumberBmob();
        phonenumberbmob.setPhonenumber(phonenumber.getPhonenumber());
        phonenumberbmob.setUserid(phonenumber.getUserid());
        phonenumberbmob.setNumberid(phonenumber.getNumberid());
        return phonenumberbmob;
    }
}
