package com.future.wk.newcontacter.util;

import java.util.List;

/**
 * Created by samsung on 2017/6/2.
 */

public interface ICallBack<T> {
    void onSuccess(List<T> mList);
    void onFail(String msg);
}
