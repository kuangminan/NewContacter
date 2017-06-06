package com.future.wk.newcontacter.mvp.contract;

import android.content.Context;
import android.database.Cursor;

import com.future.wk.newcontacter.base.mvp.model.IBaseModel;
import com.future.wk.newcontacter.base.mvp.view.IBaseView;
import com.future.wk.newcontacter.data.dalex.ContactDALEx;
import com.future.wk.newcontacter.data.dalex.PhoneNumberDALEx;

import java.util.List;

/**
 * Created by samsung on 2017/5/3.
 */

public interface ILocalContract {
    interface ILocalModel extends IBaseModel{
        Cursor getFromContactList(Context mcontext);
        List<ContactDALEx> getNormalContactList(Context mcontext);
        void saveContactList(List<ContactDALEx> mList);
        void saveContactNumber(List<PhoneNumberDALEx> mList);
        Cursor getNumberByContactID(Context mcontext, String ContactID);
        Cursor getAllNumberFromCP(Context mcontext);
        //...
    }
    interface ILocalView extends IBaseView{
        void showContactList();
        // ...
    }
}
