package com.future.wk.newcontacter.mvp.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.future.wk.newcontacter.adapter.ContactListAdapter;
import com.future.wk.newcontacter.data.dalex.ContactDALEx;
import com.future.wk.newcontacter.mvp.contract.ILocalContract;
import com.future.wk.newcontacter.mvp.presenter.LocalPresenter;
import com.future.wk.newcontacter.R;
import com.future.wk.newcontacter.base.BaseFragment;
import com.future.wk.newcontacter.util.CoreCommonUtil;
import com.future.wk.newcontacter.util.ICallBack;
import com.future.wk.newcontacter.widget.common.SearchView;
import com.future.wk.newcontacter.widget.common.SideBar;
import com.future.wk.newcontacter.widget.processDialog.processDialog;
import com.future.wk.newcontacter.widget.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 本地通讯录
 **/
public class LocalContacterFragment extends BaseFragment<LocalPresenter> implements ILocalContract.ILocalView{

    @Bind(R.id.contactListView)
    XRecyclerView listview;
    @Bind(R.id.filter_letters)
    SideBar filter_letters;
    @Bind(R.id.tv_letter)
    TextView tv_letter;
    @Bind(R.id.blankTextView)
    TextView tv_blank;

    private ContactListAdapter adapter;
    private List<ContactDALEx> mDataList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private String TAG = "LocalContacterFragment";

    @Override
    public LocalPresenter getPresenter(){
        return new LocalPresenter();
    }

    @Override
    public void onInitView(Bundle savedInstanceState){
        Log.d(TAG,"onInitView");
        filter_letters.setTextView(tv_letter);
        adapter = new ContactListAdapter(getContext(),mDataList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        listview.setLayoutManager(linearLayoutManager);
        listview.addItemDecoration(new XRecyclerView.DivItemDecoration(2, true));
        listview.setLoadingMoreEnabled(false);
        listview.setAdapter(adapter);

        filter_letters.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                Log.d(TAG,"onTouch Letter:"+s);

                if (s != null && s.trim().length() > 0) {
                    tv_letter.setText(s);
                    if (adapter.getAlphaIndexer().get(s) != null) {
                        int position = adapter.getAlphaIndexer().get(s);
                        Log.d(TAG,"position:"+position);
                        linearLayoutManager.scrollToPositionWithOffset(position+1, 0);
                    }
                }
            }
        });

        listview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG,"onRefresh()");
                int localContactLength = mPresenter.numOfLocalContact(getActivity());
                int networkContactLenght = mPresenter.getNormalContact(getActivity()).size();
                if(localContactLength == networkContactLenght){  //已经同步到最新
                    Log.d(TAG,"NO need update");
                    activity.showAppToast("无需更新");
                }else if(localContactLength > networkContactLenght){  //本地增加了联系人
                    Log.d(TAG,"Need update to network");
                    SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
                    dialog.setContentText("将新增的本地联系人同步到网络？");
                    dialog.setTitleText("需要同步");
                    dialog.setConfirmText("确定");
                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            activity.showLoading("同步中...");
                            sweetAlertDialog.cancel();
                            mPresenter.updateNetworkContact(getActivity(),new ICallBack<ContactDALEx>(){
                                @Override
                                public void onSuccess(List<ContactDALEx> mList){
                                    mDataList.addAll(mList);
                                    adapter.init();
                                    adapter.notifyDataSetChanged();
                                    activity.dismissLoading();
                                    activity.showAppToast("更新成功");
                                }
                                @Override
                                public void onFail(String msg){
                                    activity.dismissLoading();
                                    activity.showAppToast("更新失败:"+msg);
                                }
                            });
                        }
                    });
                    dialog.setCancelText("取消");
                    dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.cancel();
                        }
                    });
                    dialog.show();
                }
                listview.refreshComplete("");
            }

            @Override
            public void onLoadMore() {

            }
        });
        checkLocalContact();

    }

    public void checkLocalContact(){
        List<ContactDALEx> mList = mPresenter.getNormalContact(getActivity());
        if(mList.size() == 0){
            tv_blank.setVisibility(View.VISIBLE);
            showDialogAddContact();
        }else{
            mDataList.addAll(mList);
            adapter.init();
            adapter.notifyDataSetChanged();
        }
    }

    public void showDialogAddContact(){
        SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
        dialog.setContentText("同步本地联系人到网络？");
        dialog.setTitleText("未同步");
        dialog.setConfirmText("确定");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                activity.showLoading("同步中...");
                sweetAlertDialog.cancel();
                Log.d(TAG,"show 同步中。。。");
                mPresenter.getFromContactList(getActivity(),new ICallBack<ContactDALEx>(){
                    @Override
                    public void onSuccess(List<ContactDALEx> mList){
                        mDataList.addAll(mList);
                        adapter.init();
                        adapter.notifyDataSetChanged();
                        activity.dismissLoading();
                    }
                    @Override
                    public void onFail(String msg){
                        Log.d(TAG,"同步失败："+msg);
                        activity.dismissLoading();
                    }
                });


                tv_blank.setVisibility(View.GONE);
            }
        });
        dialog.setCancelText("取消");
        dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
            }
        });
        dialog.show();
    }


    @Override
    public int getLayoutResource(){
        return R.layout.fragment_local;
    }

    @Override
    public void showContactList(){
    }

    SearchView.OnSearchListener searchListener = new SearchView.OnSearchListener() {

        @Override
        public void onSearchEmpty() {
            filter_letters.setLettersList(adapter.getAlphaList());
        }

        @Override
        public void onSearchChange(String content) {
            filter_letters.setLettersList(adapter.getAlphaList());
        }
    };

}
