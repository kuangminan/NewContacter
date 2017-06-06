package com.future.wk.newcontacter.mvp.model;

import com.future.wk.newcontacter.data.dalex.ContactDALEx;
import com.future.wk.newcontacter.mvp.contract.ISearchContract;

import java.util.List;

/**
 * Created by samsung on 2017/5/27.
 */

public class SearchContactModel implements ISearchContract.ISearchModel {

    public List<ContactDALEx> searchLocalContact(String key){
        return ContactDALEx.get().findLocalContacterByKey(key);
    }

    public List<ContactDALEx> searchYPContact(String key){
        return ContactDALEx.get().findYPContacterByKey(key);
    }
}
