package com.future.wk.newcontacter.util;


/**
 * Created by samsung on 2017/6/9.
 */

public interface ICallBackObject<T> {
    void onSuccess(T mList);
    void onFail(String msg);
}
