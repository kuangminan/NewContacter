package com.future.wk.newcontacter.mvp.contract;

import android.content.Context;

import com.future.wk.newcontacter.base.mvp.model.IBaseModel;
import com.future.wk.newcontacter.base.mvp.view.IBaseView;
import com.future.wk.newcontacter.data.dalex.UserDALEx;
import com.future.wk.newcontacter.util.ICallBack;
import com.future.wk.newcontacter.util.ICallBackObject;
import com.future.wk.newcontacter.widget.sweetdialog.OnDismissCallbackListener;

/**
 * Created by samsung on 2017/6/9.
 */

public interface ILoginContract {
    interface IUserLoginModel extends IBaseModel {
        void login(Context context, String name, String psw, boolean isAutoLogin, ICallBackObject<UserDALEx> callBack);
    }

    interface IUserLoginView extends IBaseView {
        void showLoading(String loadmsg);
        void dismissLoading(OnDismissCallbackListener callback);
        boolean checkNet();
        void showNoNet();
        void finishActivity();
        void showFailed(String msg);
        void showSuccess(String msg);
        void showAppToast(String msg);
        String getLastName();
        String getLastPsw();
        Boolean getAuto();
    }

}
