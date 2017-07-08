package com.future.wk.newcontacter.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.future.wk.newcontacter.R;
import com.future.wk.newcontacter.base.BaseActivity;
import com.future.wk.newcontacter.data.dalex.ContactDALEx;
import com.future.wk.newcontacter.mvp.presenter.YellowPagePresenter;
import com.future.wk.newcontacter.util.CheckVersionTask;
import com.future.wk.newcontacter.util.CoreCommonUtil;
import com.future.wk.newcontacter.util.ICallBack;
import com.future.wk.newcontacter.util.UpdataInfo;
import android.app.DownloadManager;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import butterknife.Bind;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by samsung on 2017/6/6.
 */
public class AboutUsActivity extends BaseActivity<YellowPagePresenter> {

    @Bind(R.id.checkUpdate)
    TextView checkUpdate;

    String LocalVersion;
    String ServerVersion;


    private UpdataInfo info;
    private String TAG = "AboutUsActivity";
    private static final int UPDATA_CLIENT = 0;
    private static final int GET_UNDATAINFO_ERROR = 1;
    private static final int DOWN_ERROR = 2;

    @Override
    public YellowPagePresenter getPresenter() {
        return new YellowPagePresenter();
    }
    @Override
    protected boolean isEnableStatusBar() {
        return true;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_aboutus;
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        getDefaultNavigation().setTitle("关于我们");

        checkUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"onClick");
                Toast.makeText(AboutUsActivity.this, "click on update", Toast.LENGTH_SHORT);
                if(checkNet()){
                    CheckVersionTask checkVersionTask = new CheckVersionTask(AboutUsActivity.this);
                    new Thread(checkVersionTask).start();
                }
            }
        });
    }


}
