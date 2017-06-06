package com.future.wk.newcontacter.mvp.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.future.wk.newcontacter.R;
import com.future.wk.newcontacter.adapter.ContactListAdapter;
import com.future.wk.newcontacter.base.BaseFragment;
import com.future.wk.newcontacter.data.dalex.ContactDALEx;
import com.future.wk.newcontacter.mvp.contract.ILocalContract;
import com.future.wk.newcontacter.mvp.contract.INetworkContract;
import com.future.wk.newcontacter.mvp.presenter.LocalPresenter;
import com.future.wk.newcontacter.mvp.presenter.NetworkPresenter;
import com.future.wk.newcontacter.util.CoreCommonUtil;
import com.future.wk.newcontacter.util.OnRecyclerItemClickListener;
import com.future.wk.newcontacter.widget.common.SearchView;
import com.future.wk.newcontacter.widget.common.SideBar;
import com.future.wk.newcontacter.widget.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by samsung on 2017/5/3.
 */

public class NetworkContacterFragment extends BaseFragment<NetworkPresenter> implements INetworkContract.INetworkView{

    private String TAG = "NetworkContactFragment";

    @Bind(R.id.netcontactListView)
    XRecyclerView netListview;
    @Bind(R.id.net_filter_letters)
    SideBar net_filter_letters;
    @Bind(R.id.net_tv_letter)
    TextView net_tv_letter;
    @Bind(R.id.blankTextView)
    TextView tv_blankView;

    public ContactListAdapter netAdapter;
    private List<ContactDALEx> mNetDataList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    @Override
    public NetworkPresenter getPresenter(){
        return new NetworkPresenter();
    }

    @Override
    public void onResume() {
        Log.d(TAG,"onResume()");
        super.onResume();
        mNetDataList.clear();
        List<ContactDALEx> mList = mPresenter.getAddYellowPageList(getContext());
        if(mList.size() == 0){
            tv_blankView.setVisibility(View.VISIBLE);
        }else {
            tv_blankView.setVisibility(View.GONE);
            mNetDataList.addAll(mList);
            netAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onInitView(Bundle savedInstanceState){
        Log.d(TAG,"onInitView()");

        net_filter_letters.setTextView(net_tv_letter);
        netAdapter = new ContactListAdapter(getContext(),mNetDataList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        netListview.setLayoutManager(linearLayoutManager);
        netListview.addItemDecoration(new XRecyclerView.DivItemDecoration(2, true));
        netListview.setLoadingMoreEnabled(false);
        netListview.setAdapter(netAdapter);
        netListview.addOnItemTouchListener(new OnRecyclerItemClickListener(netListview) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder) {
                Toast.makeText(getActivity(), "click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLOngClick(final RecyclerView.ViewHolder viewHolder) {

                SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
                dialog.setTitleText("确定删除该号码吗");
                dialog.setConfirmText("确定");
                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Log.d(TAG,"position:"+viewHolder.getLayoutPosition());
                        Log.d(TAG,"position:"+viewHolder.getAdapterPosition());
                        String userID = mNetDataList.get(viewHolder.getLayoutPosition()).getUserid();
                        String name = mNetDataList.get(viewHolder.getLayoutPosition()).getUsername();
                        String userID2 = mNetDataList.get(viewHolder.getAdapterPosition()).getUserid();
                        String name2 = mNetDataList.get(viewHolder.getAdapterPosition()).getUsername();
                        mPresenter.removeYellowPageByID(userID);
                        mNetDataList.remove(viewHolder.getLayoutPosition());
                        netListview.removeViewAt(viewHolder.getLayoutPosition());
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("已删除")
                                .show();
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
        });

        net_filter_letters.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                if (s != null && s.trim().length() > 0) {
                    net_tv_letter.setText(s);
                    if (netAdapter.getAlphaIndexer().get(s) != null) {
                        int position = netAdapter.getAlphaIndexer().get(s);
                        linearLayoutManager.scrollToPositionWithOffset(position+1, 0);
                    }
                }
            }
        });



        netListview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                netListview.refreshComplete("");
            }

            @Override
            public void onLoadMore() {

            }
        });
        //mNetDataList.addAll(mPresenter.getAddYellowPageList(getContext()));
        //netAdapter.notifyDataSetChanged();
    }

    @Override
    public int getLayoutResource(){
        return R.layout.fragment_network;
    }

    @Override
    public void showNetworkContactList(){
        //...
    }

}
