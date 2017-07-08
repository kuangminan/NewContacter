package com.future.wk.newcontacter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.future.wk.newcontacter.R;
import com.future.wk.newcontacter.base.BaseActivity;
import com.future.wk.newcontacter.base.NCApplication;
import com.future.wk.newcontacter.base.mvp.presenter.BasePresenter;
import com.future.wk.newcontacter.util.PreferenceUtil;

import butterknife.Bind;

/**
 * Created by samsung on 2017/7/7.
 */

public class MySettingActivity extends BaseActivity {
    @Bind(R.id.et_name)
    TextView TV_name;
    @Bind(R.id.et_phone)
    TextView TV_phone;
    @Bind(R.id.btn_logout)
    Button Button_logout;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_mysetting;
    }

    @Override
    public BasePresenter getPresenter(){
        return null;
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        String name = PreferenceUtil.getInstance().getLastName();
        String number  = PreferenceUtil.getInstance().getLastAccount();
        TV_name.setText(name);
        TV_phone.setText(number);

        Button_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PreferenceUtil.getInstance().writePreferences(PreferenceUtil.LastAccount, "");
                PreferenceUtil.getInstance().writePreferences(PreferenceUtil.LastName, "");
                PreferenceUtil.getInstance().writePreferences(PreferenceUtil.LogoUrl, "");
                PreferenceUtil.getInstance().writePreferences(PreferenceUtil.LastPassword, "");
                PreferenceUtil.getInstance().writePreferences(PreferenceUtil.IsAutoLogin, false);
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        LoginActivity.startLoginActivity(MySettingActivity.this);
                        finish();
                    }
                }, 500);
            }
        });
    }
}
