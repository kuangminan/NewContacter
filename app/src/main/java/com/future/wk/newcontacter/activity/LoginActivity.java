package com.future.wk.newcontacter.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.future.wk.newcontacter.R;
import com.future.wk.newcontacter.base.BaseActivity;
import com.future.wk.newcontacter.base.NCApplication;
import com.future.wk.newcontacter.data.dalex.UserDALEx;
import com.future.wk.newcontacter.mvp.contract.ILoginContract;
import com.future.wk.newcontacter.mvp.presenter.LoginPresenter;
import com.future.wk.newcontacter.util.CoreCommonUtil;
import com.future.wk.newcontacter.util.PreferenceUtil;
import com.future.wk.newcontacter.widget.login.LoginInputView;
import com.tbruyelle.rxpermissions.RxPermissions;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * @author wty
 * @Description 注册/登陆界面
 **/
public class LoginActivity extends BaseActivity<LoginPresenter> implements ILoginContract.IUserLoginView{

    @Bind(R.id.login_icon)
    ImageView mloginIcon;
    @Bind(R.id.login_inputview)
    LoginInputView mloginInputview;
    @Bind(R.id.login_version)
    TextView tv_version;
    @Bind(R.id.rl_content)
    RelativeLayout contentlayout;
    @Bind(R.id.img_launch)
    ImageView mImgLaunch;

    private String userName;
    private String userPhone;
    private String userPsw;
    private Boolean isAuto;

    @OnClick(R.id.login_signup)
    void goToRegisterActivity(){
        UserRegisterActivity.startUserRegisterActivity(this, NCApplication.Request_Register);
    }
    /*@OnClick(R.id.login_forgetpsw)
    void showForgetPswMenu(){
        UserEmailResetPSWActivity.startUserEmailResetPSWActivity(LoginActivity.this);
    }*/

    private String TAG = "LoginActivity";
    private String userid;

    public static void startLoginActivity(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public LoginPresenter getPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        getDefaultNavigation().setTitle("");
        getDefaultNavigation().getRootView().setBackgroundColor(Color.TRANSPARENT);
        getDefaultNavigation().getLeftButton().hide();

        final boolean isAutoLogin = PreferenceUtil.getInstance().isAutoLogin();
        String name = PreferenceUtil.getInstance().getLastName();
        String psw = PreferenceUtil.getInstance().getLastPassword();
        String logourl = PreferenceUtil.getInstance().getLogoUrl();
        String userid = PreferenceUtil.getInstance().getLastAccount();
        Log.d(TAG,"isFirstLogin:"+(PreferenceUtil.getInstance().isFirstLogin()));
        Log.d(TAG,"sdk:"+Build.VERSION.SDK_INT);

        if(PreferenceUtil.getInstance().isFirstLogin() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //如果是第一次安装应用程序 适配android6.0动态权限
            RxPermissions.getInstance(this)
                    .request(Manifest.permission.WRITE_CONTACTS)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            if(aBoolean){
                                // All requested permissions are granted
                            }else{
                                finish();
                            }
                        }
                    });
            PreferenceUtil.getInstance().writePreferences(PreferenceUtil.IsFirstLogin,false);
        }

        //点击登陆后做的事情
        mloginInputview.setOnLoginAction(new LoginInputView.OnLoginActionListener() {
            @Override
            public void onLogin() {
                CoreCommonUtil.keyboardControl(LoginActivity.this, false, mloginInputview.getAccountInput());
                if (submit()) {
                    userName = mloginInputview.getAccount().toString();
                    userPsw = mloginInputview.getPassword().toString();
                    isAuto = mloginInputview.isRememberPsw();
                    mPresenter.login(NCApplication.getInstance(),userName, userPsw, isAuto);
                }
            }
        });

        tv_version.setText("V"+ CoreCommonUtil.getVersion(this)+"."+CoreCommonUtil.getVersionCode(this));

        if (name != null) {
            mloginInputview.setAccount(name);
            mloginInputview.setPassword(psw);
            if (!TextUtils.isEmpty(psw)) {
                mloginInputview.setIsRememberPsw(true);
            } else {
                mloginInputview.setIsRememberPsw(false);
            }
        } else {// 第一次使用，默认不记住密码
            mloginInputview.setIsRememberPsw(false);
        }

        if(isAutoLogin){//自动登录就调整到主页面
            UserDALEx user = new UserDALEx();
            user.setUserid(userid);
            user.setNickname(name);
            user.setLogourl(logourl);

            contentlayout.setVisibility(View.GONE);
            mImgLaunch.setVisibility(View.VISIBLE);
            mPresenter.loginAuto(LoginActivity.this,user);
        }else{
            mImgLaunch.setVisibility(View.GONE);
            contentlayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected List<String> validate() {
        List<String> list = super.validate();
        if(!TextUtils.isEmpty(mloginInputview.validata()))
            list.add(mloginInputview.validata());
        return list;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_login;
    }


    @Override
    public void finishActivity() {

        new Handler().postDelayed(new Runnable(){
            public void run() {
                MainActivity.startMainActivity(LoginActivity.this);
                finish();
            }
        }, 500);

    }

    @Override
    public void showNoNet() {
        mImgLaunch.setVisibility(View.GONE);
        contentlayout.setVisibility(View.VISIBLE);
        super.showNoNet();
    }

    @Override
    public void showFailed(String msg) {
        mImgLaunch.setVisibility(View.GONE);
        contentlayout.setVisibility(View.VISIBLE);
        super.showFailed(msg);
    }

    @Override
    public String getLastName(){
        return userName;
    }
    @Override
    public String getLastPsw(){
        return userPsw;
    }
    @Override
    public Boolean getAuto(){
        return isAuto;
    }
}
