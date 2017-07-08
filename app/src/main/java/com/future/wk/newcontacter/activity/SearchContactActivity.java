package com.future.wk.newcontacter.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.future.wk.newcontacter.R;
import com.future.wk.newcontacter.adapter.MyExpandableListViewAdapter;
import com.future.wk.newcontacter.base.BaseActivity;
import com.future.wk.newcontacter.data.dalex.ContactDALEx;
import com.future.wk.newcontacter.mvp.presenter.SearchContactPresenter;
import com.future.wk.newcontacter.util.CoreCommonUtil;
import com.future.wk.newcontacter.widget.navigation.NavigationText;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by samsung on 2017/5/27.
 */

public class SearchContactActivity extends BaseActivity<SearchContactPresenter> {
    @Bind(R.id.expendlist)
    ExpandableListView expandableListView;
    @Bind(R.id.blankTextView)
    TextView blankView;

    private String TAG = "SearchContactActivity";
    private List<String> group_list;
    private List<List<ContactDALEx>> item_list;
    private MyExpandableListViewAdapter adapter;



    public NavigationText mNavigationText;

    @Override
    public SearchContactPresenter getPresenter(){
        return new SearchContactPresenter();
    }
    @Override
    protected boolean isEnableStatusBar() {
        return true;
    }
    @Override
    public int getLayoutResource() {
        return R.layout.activity_search;
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        initActionBar();
        initGroupList();
    }

    public void initGroupList(){
        group_list = new ArrayList<String>();
        group_list.add("本地联系人");
        group_list.add("网络联系人");

        item_list = new ArrayList<List<ContactDALEx>>();

        expandableListView.setGroupIndicator(null);
        // 监听组点击
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
        {
            @SuppressLint("NewApi")
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
            {
                if (item_list.get(groupPosition).isEmpty())
                {
                    return true;
                }
                return false;
            }
        });

        // 监听每个分组里子控件的点击事件
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                Intent YPNDetailIntent = new Intent(SearchContactActivity.this, YellowPageNumberDetailActivity.class);
                YPNDetailIntent.putExtra("userid", item_list.get(groupPosition).get(childPosition).getUserid());
                YPNDetailIntent.putExtra("username", item_list.get(groupPosition).get(childPosition).getUsername());
                SearchContactActivity.this.startActivity(YPNDetailIntent);
                return true;
            }
        });
        adapter = new MyExpandableListViewAdapter(this, group_list, item_list);
        expandableListView.setAdapter(adapter);
    }

    public void initActionBar(){

        /*init the left view */
        mNavigationText = getDefaultNavigation();
        mNavigationText.setLefttButton(R.mipmap.actionbar_back, "", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchContactActivity.this.finish();
            }
        });

        /*init the center view*/
        mNavigationText.showSearchEditText();

        /*init the right view*/
        mNavigationText.setRightButton( "搜索", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int i = 0; i < group_list.size(); i++){
                    if(expandableListView.isGroupExpanded(i)){
                        expandableListView.collapseGroup(i);
                    }
                }
                item_list.clear();
                adapter.notifyDataSetChanged();

                String searchKey = mNavigationText.getSearchValue();
                List<ContactDALEx> NormalList = mPresenter.findLocalContact(searchKey);
                List<ContactDALEx> YPList = mPresenter.findYPContact(searchKey);
                if((NormalList.size() > 0) || (YPList.size() >0)) {
                    item_list.add(NormalList);
                    item_list.add(YPList);
                    blankView.setVisibility(View.GONE);
                    expandableListView.setVisibility(View.VISIBLE);
                }else{
                    if(expandableListView.getVisibility() == View.VISIBLE){
                        expandableListView.setVisibility(View.GONE);
                        blankView.setVisibility(View.VISIBLE);
                    }
                }
                //hide keyboard
                CoreCommonUtil.keyboardControl(SearchContactActivity.this, false, view);
            }
        });
    }

}
