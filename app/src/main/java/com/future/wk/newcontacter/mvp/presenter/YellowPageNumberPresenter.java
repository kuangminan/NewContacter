package com.future.wk.newcontacter.mvp.presenter;

import android.content.Context;
import android.widget.Toast;

import com.future.wk.newcontacter.base.mvp.presenter.BasePresenter;
import com.future.wk.newcontacter.data.dalex.ContactDALEx;
import com.future.wk.newcontacter.data.dalex.PhoneNumberDALEx;
import com.future.wk.newcontacter.mvp.contract.IYellowPageContract;
import com.future.wk.newcontacter.mvp.contract.IYellowPageNumberContract;
import com.future.wk.newcontacter.mvp.model.YellowPageModel;
import com.future.wk.newcontacter.mvp.model.YellowPageNumberModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuangminan on 2017/5/14.
 */
public class YellowPageNumberPresenter extends BasePresenter<IYellowPageNumberContract.IYellowPageNumberView> {

    private IYellowPageNumberContract.IYellowPageNumberModel mYellowPageNumberModel;

    public YellowPageNumberPresenter(){
        mYellowPageNumberModel = new YellowPageNumberModel();
    }

    public List<PhoneNumberDALEx> getYellowPageById(Context context, String userid){
        List<PhoneNumberDALEx> mNumberList = mYellowPageNumberModel.getYellowPageByID(context, userid);
        return mNumberList;
    }

}
