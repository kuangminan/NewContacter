package com.future.wk.newcontacter.data.dalex.bmob;

import com.future.wk.newcontacter.data.dalex.ContactDALEx;
import com.future.wk.newcontacter.util.PreferenceUtil;

import cn.bmob.v3.BmobObject;

/**
 * Created by samsung on 2017/6/13.
 */

public class contacterBmob extends BmobObject{
    private String userobjectid; //对应bmob中user表中唯一的id
    private String userid;   //手机联系人数据库中的id
    private String username; // 用户的用户名
    private String yellowpage;  //区别本地联系人和网络号码
    private String usericon; // 头像
    private String userphone; // 手机号码
    private Integer phonecount; //手机号码数量
    private String userjob; // 职业
    private String useremail; // 电子邮箱
    private Integer usersex; // 性别
    private String namepinyin; // 名字拼音
    private String usertel; // 固话

    public static final String TABLENAME = "contacter";

    public contacterBmob(){
        this.setTableName(TABLENAME);
    }

    public void setUserobjectid(String objectid){
        this.userobjectid = objectid;
    }
    public String getUserobjectid(){
        return userobjectid;
    }
    public void setUserid(String userid){
        this.userid = userid;
    }
    public String getUserid(){
        return userid;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return username;
    }
    public void setYellowpage(String yellowpage){
        this.yellowpage = yellowpage;
    }
    public String getYellowpage(){
        return yellowpage;
    }
    public void setUsericon(String usericon){
        this.usericon = usericon;
    }
    public String getUsericon(){
        return usericon;
    }
    public void setUserphone(String userphone){
        this.userphone = userphone;
    }
    public String getUserphone(){
        return userphone;
    }
    public void setPhonecount(Integer phonecount){
        this.phonecount = phonecount;
    }
    public Integer getPhonecount(){
        return phonecount;
    }
    public void setUserjob(String userjob){
        this.userjob = userjob;
    }
    public String getUserjob(){
        return userjob;
    }
    public void setUseremail(String useremail){
        this.useremail = useremail;
    }
    public String getUseremail(){
        return useremail;
    }
    public void setUsersex(Integer usersex){
        this.usersex = usersex;
    }
    public Integer getUsersex(){
        return usersex;
    }
    public void setNamepinyin(String namepinyin){
        this.namepinyin = namepinyin;
    }
    public String getNamepinyin(){
        return namepinyin;
    }
    public void setUsertel(String usertel){
        this.usertel = usertel;
    }
    public String getUsertel(){
        return usertel;
    }

    public ContactDALEx ConvertToContactDALEx(){
        ContactDALEx contact = new ContactDALEx();
        contact.setNamepinyin(this.getNamepinyin());
        contact.setYellowpage(this.getYellowpage());
        contact.setUserid(this.getUserid());
        contact.setUserphone(this.getUserphone());
        contact.setPhonecount(this.getPhonecount());
        contact.setUseremail(this.getUseremail());
        contact.setUsericon(this.getUsericon());
        contact.setUserjob(this.getUserjob());
        contact.setUsersex(this.getUsersex());
        contact.setUsertel(this.getUsertel());
        contact.setUsername(this.getUsername());
        return contact;
    }

    public contacterBmob ConvertTocontacterBmob(ContactDALEx contact){
        contacterBmob contactbmob = new contacterBmob();
        contactbmob.setNamepinyin(contact.getNamepinyin());
        contactbmob.setYellowpage(contact.getYellowPage());
        contactbmob.setUserid(contact.getUserid());
        contactbmob.setUserphone(contact.getUserphone());
        contactbmob.setPhonecount(contact.getPhonecount());
        contactbmob.setUseremail(contact.getUseremail());
        contactbmob.setUsericon(contact.getUsericon());
        contactbmob.setUserjob(contact.getUserjob());
        contactbmob.setUsersex(contact.getUsersex());
        contactbmob.setUsertel(contact.getUsertel());
        contactbmob.setUsername(contact.getUsername());
        contactbmob.setUserobjectid( PreferenceUtil.getInstance().getLastAccount());
        return contactbmob;
    }

}
