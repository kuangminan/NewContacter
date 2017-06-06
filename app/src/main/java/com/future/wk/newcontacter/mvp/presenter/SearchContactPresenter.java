package com.future.wk.newcontacter.mvp.presenter;

import com.future.wk.newcontacter.base.mvp.presenter.BasePresenter;
import com.future.wk.newcontacter.data.dalex.ContactDALEx;
import com.future.wk.newcontacter.mvp.contract.ISearchContract;
import com.future.wk.newcontacter.mvp.model.SearchContactModel;

import java.util.List;

/**
 * Created by samsung on 2017/5/27.
 */

public class SearchContactPresenter extends BasePresenter<ISearchContract.ISearchView> {
    private ISearchContract.ISearchModel mSearchContactModel;

    public SearchContactPresenter(){
        mSearchContactModel = new SearchContactModel();
    }

    public List<ContactDALEx> findLocalContact(String key){
        return mSearchContactModel.searchLocalContact(key);
    }

    public List<ContactDALEx> findYPContact(String key){
        return mSearchContactModel.searchYPContact(key);
    }
}
