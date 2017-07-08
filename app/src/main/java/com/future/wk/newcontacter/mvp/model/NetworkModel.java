package com.future.wk.newcontacter.mvp.model;

import android.content.Context;

import com.future.wk.newcontacter.data.dalex.ContactDALEx;
import com.future.wk.newcontacter.mvp.contract.INetworkContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samsung on 2017/5/3.
 */

public class NetworkModel implements INetworkContract.INetworkModel {
    @Override
    public List getNetworkContactList() {
        return ContactDALEx.get().findAllNormalContacter();
    }

    @Override
    public List getAddYellowPageList(Context mContext){
        return ContactDALEx.get().findAllYPContacter();
    }

    @Override
    public void removeYellowPageByID(String userid){
        ContactDALEx.get().deleteById(userid);
    }

}
