package com.future.wk.newcontacter.mvp.presenter;

import android.content.Context;

import com.future.wk.newcontacter.base.mvp.presenter.BasePresenter;
import com.future.wk.newcontacter.data.dalex.UserDALEx;
import com.future.wk.newcontacter.mvp.contract.IUserRegisterContract;
import com.future.wk.newcontacter.mvp.model.UserRegisterModel;
import com.future.wk.newcontacter.util.ICallBack;
import com.future.wk.newcontacter.util.ICallBackObject;
import com.future.wk.newcontacter.widget.sweetdialog.OnDismissCallbackListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by samsung on 2017/6/12.
 */

public class UserRegisterPresenter extends BasePresenter<IUserRegisterContract.IUserRegisterView>{
    private IUserRegisterContract.IUserRegisterModel mUserRegisterModel;

    public UserRegisterPresenter(){
        mUserRegisterModel = new UserRegisterModel();
    }

    public void register(Context context, final UserDALEx data){
        if(!mView.checkNet()){
            mView.showNoNet();
            return;
        }
        mView.showLoading("请稍候，正在注册中...");

        mUserRegisterModel.register(context,data, new ICallBackObject<String>() {
            @Override
            public void onSuccess(final String userid) {
                mView.dismissLoading(new OnDismissCallbackListener("注册成功") {
                    @Override
                    public void onCallback() {
                        mView.finishActivity(userid);
                    }
                });
            }

            @Override
            public void onFail(String msg) {
                mView.dismissLoading(new OnDismissCallbackListener(msg, SweetAlertDialog.ERROR_TYPE));
            }
        });
    }
}
