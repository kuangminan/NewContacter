package com.future.wk.newcontacter.mvp.contract;

import android.content.Context;

import com.future.wk.newcontacter.base.mvp.model.IBaseModel;
import com.future.wk.newcontacter.base.mvp.view.IBaseView;
import com.future.wk.newcontacter.data.dalex.UserDALEx;
import com.future.wk.newcontacter.util.ICallBack;
import com.future.wk.newcontacter.util.ICallBackObject;
import com.future.wk.newcontacter.widget.sweetdialog.OnDismissCallbackListener;


/**
 * @author wty
 */
public interface IUserRegisterContract {

    interface IUserRegisterModel extends IBaseModel {
        void register(Context context, UserDALEx user, ICallBackObject<String> callBack);
    }

    interface IUserRegisterView extends IBaseView {
        void showLoading(String loadmsg);
        void dismissLoading(OnDismissCallbackListener callback);
        boolean checkNet();
        void showNoNet();
        void finishActivity(String userid);
    }

}
