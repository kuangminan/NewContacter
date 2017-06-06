package com.future.wk.newcontacter.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.future.wk.newcontacter.R;
import com.future.wk.newcontacter.adapter.PhoneNumberListAdapter;
import com.future.wk.newcontacter.base.BaseActivity;
import com.future.wk.newcontacter.data.dalex.PhoneNumberDALEx;
import com.future.wk.newcontacter.mvp.presenter.YellowPageNumberPresenter;
import com.future.wk.newcontacter.widget.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by samsung on 2017/5/26.
 */

public class YellowPageNumberDetailActivity extends BaseActivity<YellowPageNumberPresenter> {

    private String TAG = "YPNumberDetailActivity";

    @Bind(R.id.img_user_bg)
    ImageView ImgUserBg;
    @Bind(R.id.img_user_icon)
    ImageView ImgUserIcon;
    @Bind(R.id.tv_user_name)
    TextView TvUserName;
    @Bind(R.id.numberlistview)
    XRecyclerView NumberListView;

    public PhoneNumberListAdapter PNLAdapter;
    private List<PhoneNumberDALEx> PhoneNumberDataList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private String userName;

    @Override
    public YellowPageNumberPresenter getPresenter() {
        return new YellowPageNumberPresenter();
    }
    @Override
    protected boolean isEnableStatusBar() {
        return true;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_yellowpage_number;
    }

    @Override
    public void onInitView(Bundle savedInstanceState){
        getDefaultNavigation().setTitle("详情");

        PNLAdapter = new PhoneNumberListAdapter(this,PhoneNumberDataList);
        linearLayoutManager = new LinearLayoutManager(this);
        NumberListView.setLayoutManager(linearLayoutManager);
        NumberListView.addItemDecoration(new XRecyclerView.DivItemDecoration(2, true));
        NumberListView.setLoadingMoreEnabled(false);
        NumberListView.setAdapter(PNLAdapter);
        Log.d(TAG,"userid:"+getIntent().getStringExtra("userid"));
        //List<PhoneNumberDALEx> all = PhoneNumberDALEx.
        userName = getIntent().getStringExtra("username");
        TvUserName.setText(userName);
        List<PhoneNumberDALEx> mList = mPresenter.getYellowPageById(this, getIntent().getStringExtra("userid"));
        Log.d(TAG, "mList lenght:"+mList.size());

        PhoneNumberDataList.addAll(mList);
        Log.d(TAG,"PhoneNumberDataList size:"+PhoneNumberDataList.size());
        PNLAdapter.notifyDataSetChanged();
    }


}
