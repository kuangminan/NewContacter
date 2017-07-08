package com.future.wk.newcontacter.base;

/**
 * Created by samsung on 2017/6/9.
 *管理第三方云存储模块
 */

public class CloudManager {
    private IFileManager fileManager;
    private IUserManager userManager;
    private IContactManager contactManager;
    private IPhonenumberManager phonenumberManager;

    private static volatile CloudManager sInstance = new CloudManager();
    public static CloudManager getInstance(){
        return sInstance;
    }

    public IFileManager getFileManager() {
        return fileManager;
    }

    public void setFileManager(IFileManager fileManager) {
        this.fileManager = fileManager;
    }

    public IUserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    public IContactManager getContactManager(){
        return contactManager;
    }

    public void setContactManager(IContactManager contactManager){
        this.contactManager = contactManager;
    }

    public IPhonenumberManager getPhonenumberManager(){
        return phonenumberManager;
    }

    public void setPhonenumberManager(IPhonenumberManager phonenumberManager){
        this.phonenumberManager = phonenumberManager;
    }



}
