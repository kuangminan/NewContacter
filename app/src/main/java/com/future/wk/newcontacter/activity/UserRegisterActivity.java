package com.future.wk.newcontacter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;


import com.future.wk.newcontacter.R;
import com.future.wk.newcontacter.base.BaseActivity;
import com.future.wk.newcontacter.data.dalex.UserDALEx;
import com.future.wk.newcontacter.mvp.contract.IUserRegisterContract;
import com.future.wk.newcontacter.mvp.presenter.UserRegisterPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * @author wty
 * @Description 用户注册页面
 **/
public class UserRegisterActivity extends BaseActivity<UserRegisterPresenter> implements IUserRegisterContract.IUserRegisterView {

    public static final String USERID = "userid";

    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_psw)
    EditText etPsw;
    @Bind(R.id.et_psw_check)
    EditText etPswCheck;

    @OnClick(R.id.btn_sign)
    void sign(){
        //注册
        if(super.submit()){
            mPresenter.register(UserRegisterActivity.this,getSubmitData());
        }
    }

    private String path_header = "";

    public static void startUserRegisterActivity(Activity activity,int RequestCode) {
        Intent intent = new Intent(activity, UserRegisterActivity.class);
        activity.startActivityForResult(intent,RequestCode);
    }

    @Override
    public UserRegisterPresenter getPresenter() {
        return new UserRegisterPresenter();
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        getDefaultNavigation().setTitle("注册账号");
        getDefaultNavigation().getLeftButton().setText("返回");

    }

    @Override
    protected boolean isEnableStatusBar() {
        return true;
    }

    @Override
    protected List<String> validate() {
        List<String> list = super.validate();
        if(TextUtils.isEmpty(etName.getText().toString())){
            list.add("请填写昵称");
        }else if(TextUtils.isEmpty(etPsw.getText().toString())){
            list.add("请填写密码");
        }else if(TextUtils.isEmpty(etPswCheck.getText().toString())){
            list.add("请填写确认密码");
        }else if(!(etPsw.getText().toString()).equals(etPswCheck.getText().toString())){
            list.add("两次密码不一样，请重新输入");
        }
        return list;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_register;
    }


    private UserDALEx getSubmitData(){
        UserDALEx user = new UserDALEx();
        user.setNickname(etName.getText().toString());
        user.setPassword(etPsw.getText().toString());
        user.setMobilePhoneNumber(etPhone.getText().toString());
        return user;
    }

    @Override
    public void finishActivity(String userid) {
        Intent intent = new Intent();
        intent.putExtra(USERID,userid);
        setResult(RESULT_OK, intent);
        finish();
    }
}
