package com.future.wk.newcontacter.mvp.contract;

import com.future.wk.newcontacter.base.mvp.model.IBaseModel;
import com.future.wk.newcontacter.base.mvp.view.IBaseView;

import java.util.List;

/**
 * Created by samsung on 2017/5/27.
 */

public interface ISearchContract {

    interface ISearchModel extends IBaseModel{
        List searchLocalContact(String key);
        List searchYPContact(String key);
    }

    interface ISearchView extends IBaseView{

    }
}
