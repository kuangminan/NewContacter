package com.future.wk.newcontacter.mvp.model;

import android.content.Context;

import com.future.wk.newcontacter.base.CloudManager;
import com.future.wk.newcontacter.data.dalex.UserDALEx;
import com.future.wk.newcontacter.mvp.contract.ILoginContract;
import com.future.wk.newcontacter.util.ICallBack;
import com.future.wk.newcontacter.util.ICallBackObject;
import com.future.wk.newcontacter.util.PreferenceUtil;

import java.util.List;

/**
 * Created by samsung on 2017/6/9.
 */

public class LoginModel implements ILoginContract.IUserLoginModel {
    @Override
    public void login(final Context context, final String name, final String psw, final boolean isAutoLogin, final ICallBackObject<UserDALEx> callBack) {

        CloudManager.getInstance().getUserManager().login(context, name, psw, new ICallBackObject<UserDALEx>() {
            @Override
            public void onSuccess(UserDALEx user) {
                callBack.onSuccess(user);
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    /**
     * @Decription 保存用户数据到preference
     **/
    private void saveUserPreference(boolean isAutoLogin,String psw,UserDALEx user){
        PreferenceUtil.getInstance().writePreferences(PreferenceUtil.LastName, user.getNickname());
        PreferenceUtil.getInstance().writePreferences(PreferenceUtil.LastAccount, user.getUserid());
        PreferenceUtil.getInstance().writePreferences(PreferenceUtil.LogoUrl, user.getLogourl());
        if(isAutoLogin){
            PreferenceUtil.getInstance().writePreferences(PreferenceUtil.IsAutoLogin, true);
            PreferenceUtil.getInstance().writePreferences(PreferenceUtil.LastPassword,psw);
        }else{
            PreferenceUtil.getInstance().writePreferences(PreferenceUtil.IsAutoLogin, false);
            PreferenceUtil.getInstance().writePreferences(PreferenceUtil.LastPassword, "");
        }
    }

}
