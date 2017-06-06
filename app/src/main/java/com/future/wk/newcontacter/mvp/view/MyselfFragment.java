package com.future.wk.newcontacter.mvp.view;

import android.os.Bundle;

import com.future.wk.newcontacter.R;
import com.future.wk.newcontacter.base.BaseFragment;
import com.future.wk.newcontacter.mvp.contract.ILocalContract;
import com.future.wk.newcontacter.mvp.contract.ISettingContract;
import com.future.wk.newcontacter.mvp.presenter.LocalPresenter;
import com.future.wk.newcontacter.mvp.presenter.SettingPresenter;

/**
 * Created by samsung on 2017/5/3.
 */

public class MyselfFragment extends BaseFragment<SettingPresenter> implements ISettingContract.ISettingView{

    @Override
    public SettingPresenter getPresenter(){
        return new SettingPresenter();
    }

    @Override
    public void onInitView(Bundle savedInstanceState){
        //...
    }

    @Override
    public int getLayoutResource(){
        return R.layout.fragment_setting;
    }

    @Override
    public void showSettingList(){
        //...
    }

}
