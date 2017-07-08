package com.future.wk.newcontacter.base;

import android.content.Context;
import com.future.wk.newcontacter.util.ICallBack;

/**
 * Created by samsung on 2017/6/9.
 * 长传下载文件
 */

public interface IFileManager {
    /**
     * @Decription 上传文件
     **/
    void uploadFile(final Context context, final String uploadname, final String filepath, final ICallBack<String> callBack);

}
