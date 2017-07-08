package com.future.wk.newcontacter.mvp.model;

import android.content.Context;
import android.text.TextUtils;


import com.future.wk.newcontacter.base.CloudManager;
import com.future.wk.newcontacter.data.dalex.UserDALEx;
import com.future.wk.newcontacter.mvp.contract.IUserRegisterContract;
import com.future.wk.newcontacter.util.ICallBack;
import com.future.wk.newcontacter.util.ICallBackObject;

import java.io.File;
import java.util.List;

/**
 * @author wty
 */
public class UserRegisterModel implements IUserRegisterContract.IUserRegisterModel {

    /**
     * @Decription 注册用户
     **/
    public void register(Context context,final UserDALEx user, final ICallBackObject<String> callBack){
        CloudManager.getInstance().getUserManager().register(user, new ICallBackObject<UserDALEx>() {
            @Override
            public void onSuccess(UserDALEx user) {
                //注册成功之后设置一下当前数据库名字
                //OrmModuleManager.getInstance().setCurrentDBName(user.get(0).getUserid());
                user.saveOrUpdate();
                callBack.onSuccess(user.getUserid());
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }

}
