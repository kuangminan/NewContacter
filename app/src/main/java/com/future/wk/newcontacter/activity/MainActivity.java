package com.future.wk.newcontacter.activity;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;

import com.future.wk.newcontacter.base.BaseFragment;
import com.future.wk.newcontacter.base.NCApplication;
import com.future.wk.newcontacter.base.NCObjectCache;
import com.future.wk.newcontacter.mvp.view.LocalContacterFragment;
import com.future.wk.newcontacter.R;
import com.future.wk.newcontacter.base.BaseActivity;
import com.future.wk.newcontacter.base.mvp.presenter.BasePresenter;
import com.future.wk.newcontacter.mvp.view.MyselfFragment;
import com.future.wk.newcontacter.mvp.view.NetworkContacterFragment;
import com.future.wk.newcontacter.mvp.view.NetworkFragment;
import com.future.wk.newcontacter.util.PreferenceUtil;
import com.future.wk.newcontacter.widget.common.TabStripView;
import com.future.wk.newcontacter.widget.navigation.NavigationText;

import butterknife.Bind;

public class MainActivity extends BaseActivity {

    private String TAG = "MainActivity";

    @Bind(R.id.navigateTabBar)
    TabStripView navigateTabBar;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    public String leftButtonString = "设置";
    public String rightButtonString = "搜索";
    public NavigationText mNavigationText;

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
    @Override
    protected boolean isEnableStatusBar() {
        return true;
    }

    public static void startMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }


    @Override
    public void onInitView(Bundle savedInstanceState) {
        //EventBus.getDefault().register(this);

        //init the action bar layout
        mNavigationText = getDefaultNavigation();
        setLeftButton();
        setRightButton();

        // Set up the navigation drawer.
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            View headview = navigationView.getHeaderView(0);
            TextView headText = (TextView)headview.findViewById(R.id.nav_text_view);
            headText.setText(PreferenceUtil.getInstance().getLastName());
            setupDrawerContent(navigationView);
        }


        //对应xml中的containerId
        navigateTabBar.setFrameLayoutId(R.id.main_container);
        //对应xml中的navigateTabTextColor
        navigateTabBar.setTabTextColor(getResources().getColor(R.color.gray_font_3));
        //对应xml中的navigateTabSelectedTextColor
        navigateTabBar.setSelectedTabTextColor(getResources().getColor(R.color.colorPrimary));
        //恢复选项状态
        navigateTabBar.onRestoreInstanceState(savedInstanceState);

        navigateTabBar.addTab(LocalContacterFragment.class, new TabStripView.TabParam(R.mipmap.ic_tab_local_normal, R.mipmap.ic_tab_local_pressed, NCApplication.LocalTag));
        navigateTabBar.addTab(NetworkFragment.class, new TabStripView.TabParam(R.mipmap.ic_tab_network_normal, R.mipmap.ic_tab_network_pressed, NCApplication.NetworkTag));
    }

    public BaseFragment getFragment(String fragmentTag){
        return navigateTabBar.getFragment(fragmentTag);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //保存当前选中的选项状态
        navigateTabBar.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG,"Drawer Layout show:"+ mDrawerLayout.isShown());
            if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                mDrawerLayout.closeDrawers();
            }else {
                Timer tExit;
                if (!isExit) {
                    isExit = true; // 准备退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    tExit = new Timer();
                    tExit.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            isExit = false; // 取消退出
                        }
                    }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
                } else {
                    finish();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        NCObjectCache.getInstance().clearCache();
        NCObjectCache.getInstance().closeAllDB();
        NCObjectCache.getInstance().finishAllActivity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                Log.d(TAG,"Item select home");
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setLeftButton(){
        mNavigationText.setLefttButton(R.mipmap.female, leftButtonString, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
    public void setRightButton(){
        mNavigationText.setRightButton(R.mipmap.ic_search, "", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start the search activity
                Intent YPDetailIntent = new Intent(MainActivity.this, SearchContactActivity.class);
                startActivity(YPDetailIntent);
            }
        });
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.notify_navigation_menu_item:
                                Log.d(TAG,"notify_navigation_menu_item");
                                Toast.makeText(MainActivity.this, "点击通知栏", Toast.LENGTH_SHORT);
                                break;
                            case R.id.feedback_navigation_menu_item:
                                Log.d(TAG,"feedback_navigation_menu_item");
                                Toast.makeText(MainActivity.this, "点击反馈栏", Toast.LENGTH_SHORT);
                                break;
                            case R.id.setting_navigation_menu_item:
                                Log.d(TAG,"setting_navigation_menu_item");
                                Toast.makeText(MainActivity.this, "点击设置栏", Toast.LENGTH_SHORT);
                                Intent mySettingIntent = new Intent(MainActivity.this, MySettingActivity.class);
                                startActivity(mySettingIntent);
                                break;
                            case R.id.aboutus_navigation_menu_item:
                                Log.d(TAG,"aboutus_navigation_menu_item");
                                Intent AboutUsIntent = new Intent(MainActivity.this, AboutUsActivity.class);
                                startActivity(AboutUsIntent);
                                break;
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }


}
