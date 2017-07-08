package com.future.wk.newcontacter.data.dalex.bmob;

import android.content.Context;

import com.future.wk.newcontacter.base.IFileManager;
import com.future.wk.newcontacter.util.ICallBack;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by samsung on 2017/6/9.
 */

public class BmobFileManager implements IFileManager {

    private static BmobFileManager ourInstance = new BmobFileManager();

    public static BmobFileManager getInstance() {
        return ourInstance;
    }



    @Override
    public void uploadFile(Context context, String uploadname, String filepath, final ICallBack<String> callBack) {
        final BmobFile bmobFile = new BmobFile(new File(filepath));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    //上传成功 返回上传文件的完整地址
                    //callBack.onSuccess(bmobFile.getFileUrl());
                }else{
                    //上传失败
                    callBack.onFail("上传失败:"+e.getMessage());
                }
            }

            @Override
            public void onProgress(Integer value) {
                super.onProgress(value);
            }
        });
    }

}
