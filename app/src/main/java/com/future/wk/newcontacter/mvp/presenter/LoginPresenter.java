package com.future.wk.newcontacter.mvp.presenter;

import android.content.Context;
import android.util.Log;

import com.future.wk.newcontacter.base.mvp.presenter.BasePresenter;
import com.future.wk.newcontacter.data.dalex.UserDALEx;
import com.future.wk.newcontacter.mvp.contract.ILoginContract;
import com.future.wk.newcontacter.mvp.model.LoginModel;
import com.future.wk.newcontacter.util.ICallBack;
import com.future.wk.newcontacter.util.ICallBackObject;
import com.future.wk.newcontacter.util.PreferenceUtil;
import com.future.wk.newcontacter.widget.sweetdialog.OnDismissCallbackListener;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by samsung on 2017/6/9.
 */

public class LoginPresenter extends BasePresenter<ILoginContract.IUserLoginView> {
    private String TAG = "LoginPresenter";
    private ILoginContract.IUserLoginModel mLoginModel;
    public LoginPresenter(){
        mLoginModel = new LoginModel();
    }
    public void login(final Context context, final String name, final String psw, final boolean isAutoLogin){
        if(!mView.checkNet()){
            mView.showNoNet();
            return;
        }

        mView.showLoading("正在验证用户名...");
        mLoginModel.login(context,name, psw, isAutoLogin,new ICallBackObject<UserDALEx>() {
            @Override
            public void onSuccess(UserDALEx user) {
                Log.d(TAG,"验证账号密码成功");
                mView.showAppToast("登入成功");
                //save the user info to sharedprerefence
                PreferenceUtil.getInstance().writePreferences(PreferenceUtil.IsAutoLogin, mView.getAuto());
                PreferenceUtil.getInstance().writePreferences(PreferenceUtil.LastName, mView.getLastName());
                PreferenceUtil.getInstance().writePreferences(PreferenceUtil.LastPassword, mView.getLastPsw());
                PreferenceUtil.getInstance().writePreferences(PreferenceUtil.LastAccount, user.getUserid());

                mView.finishActivity();
            }

            @Override
            public void onFail(String msg) {
                Log.d(TAG,msg);
                mView.dismissLoading(new OnDismissCallbackListener(msg, SweetAlertDialog.ERROR_TYPE));
            }
        });
    }

    /**
     * 自动登录过程：
     * 直接进入主界面
     **/
    public void loginAuto(final Context context,final UserDALEx user){
        if(!mView.checkNet()){
            mView.showNoNet();
            return;
        }
        mView.finishActivity();
    }
}
