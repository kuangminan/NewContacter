package com.future.wk.newcontacter.activity;

import android.os.Bundle;

import com.future.wk.newcontacter.R;
import com.future.wk.newcontacter.base.BaseActivity;
import com.future.wk.newcontacter.mvp.presenter.YellowPagePresenter;

/**
 * Created by samsung on 2017/6/6.
 */

public class AboutUsActivity extends BaseActivity<YellowPagePresenter> {

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
    }
}
