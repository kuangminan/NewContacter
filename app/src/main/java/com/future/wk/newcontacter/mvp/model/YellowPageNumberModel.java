package com.future.wk.newcontacter.mvp.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.future.wk.newcontacter.data.dalex.PhoneNumberDALEx;
import com.future.wk.newcontacter.mvp.contract.IYellowPageNumberContract;


import java.util.List;

/**
 * Created by kuangminan on 2017/5/14.
 */
public class YellowPageNumberModel implements IYellowPageNumberContract.IYellowPageNumberModel {

    private String TAG = "YellowPageModel";

    @Override
    public List<PhoneNumberDALEx> getYellowPageByID(Context context, String userID){
        return PhoneNumberDALEx.get().getPhoneNumberByID(userID);
    }


}
