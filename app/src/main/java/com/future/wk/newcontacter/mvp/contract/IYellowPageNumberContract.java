package com.future.wk.newcontacter.mvp.contract;

import android.content.Context;

import com.future.wk.newcontacter.base.mvp.model.IBaseModel;
import com.future.wk.newcontacter.base.mvp.view.IBaseView;
import com.future.wk.newcontacter.data.dalex.PhoneNumberDALEx;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kuangminan on 2017/5/14.
 */
public interface IYellowPageNumberContract {

    interface IYellowPageNumberModel extends IBaseModel {
        public List<PhoneNumberDALEx> getYellowPageByID(Context context, String userID);
    }

    interface IYellowPageNumberView extends IBaseView {
       // void showYellowPageDetail();
    }
}
