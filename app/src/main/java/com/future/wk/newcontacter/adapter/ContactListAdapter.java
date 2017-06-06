package com.future.wk.newcontacter.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.future.wk.newcontacter.R;
import com.future.wk.newcontacter.activity.YellowPageDetailActivity;
import com.future.wk.newcontacter.activity.YellowPageNumberDetailActivity;
import com.future.wk.newcontacter.data.dalex.ContactDALEx;
import com.future.wk.newcontacter.data.dalex.PhoneNumberDALEx;
import com.future.wk.newcontacter.mvp.view.NetworkContacterFragment;
import com.future.wk.newcontacter.util.CorePinYinUtil;
import com.future.wk.newcontacter.widget.roundedimageview.RoundedImageView;
import com.future.wk.newcontacter.widget.xrecyclerview.adapter.BaseRecyclerViewAdapter;
import com.future.wk.newcontacter.widget.xrecyclerview.adapter.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.view.View.GONE;
import com.future.wk.newcontacter.data.dalex.ContactDALEx;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 通讯录适配器
 */

public class ContactListAdapter extends BaseRecyclerViewAdapter<ContactDALEx> {

    private String TAG = "ContactListAdapter";

    //是否支持字母列表
    private boolean isAlphaSearch = true;
    //字母列表
    private Map<String, Integer> alphaIndexer;

    ImageView iv_phone;
    ImageView iv_add;
    ImageView iv_message;

    public ContactListAdapter(Context context, List<ContactDALEx> data) {
        super(context, R.layout.item_contact_list, data);
//        init();
    }

    @Override
    protected void convert(BaseRecyclerViewHolder holder, final ContactDALEx contact, final int position) {
        RoundedImageView iv_icon = holder.getView(R.id.item_contact_icon);
        iv_phone = holder.getView(R.id.item_contact_phone);
        iv_add = holder.getView(R.id.item_add_contact);
        iv_message = holder.getView(R.id.item_contact_message);
        TextView tv_name = holder.getView(R.id.item_contact_name);
        TextView tv_number = holder.getView(R.id.item_contact_number);
        TextView tv_alpha = holder.getView(R.id.item_contact_alpha);

        if((mContext instanceof YellowPageDetailActivity) && (ContactDALEx.get().findYPContacterByNumber(contact.getUserphone().split(";")[0]).size() == 0)){
            Log.d(TAG,"set add picture");
            iv_message.setVisibility(View.GONE);
            iv_add.setVisibility(View.VISIBLE);
        }else{
            Log.d(TAG,"set message picture");
            iv_message.setVisibility(View.VISIBLE);
            iv_add.setVisibility(View.GONE);
        }

        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(mContext instanceof YellowPageDetailActivity)) {
                    Intent YPNDetailIntent = new Intent(mContext, YellowPageNumberDetailActivity.class);
                    YPNDetailIntent.putExtra("userid", contact.getUserid());
                    YPNDetailIntent.putExtra("username", contact.getUsername());
                    mContext.startActivity(YPNDetailIntent);
                }else{
                    return ;
                }
            }
        });

        iv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:"+contact.getUserphone()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
            });

        iv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+contact.getUserphone()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SweetAlertDialog dialog = new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE);
                dialog.setTitleText("确认添加到常用号码中吗？");
                dialog.setConfirmText("确定");
                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                        String mulUserPhonenumber = contact.getUserphone();//处理json中一个联系人存在多个号码的情况
                        String[] number = mulUserPhonenumber.split(";");
                        contact.setUserphone(number[0]);
                        contact.setPhonecount(number.length);
                        contact.setUserid(UUID.randomUUID().toString());
                        contact.saveOrUpdate();
                        for (int k = 0; k < number.length; k++) {
                            PhoneNumberDALEx mphonenumberDALEX = new PhoneNumberDALEx();
                            mphonenumberDALEX.setUserid(contact.getUserid());
                            mphonenumberDALEX.setPhonenumber(number[k]);
                            mphonenumberDALEX.setNumberid(UUID.randomUUID().toString());
                            mphonenumberDALEX.saveOrUpdate();
                        }
                        new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("已添加")
                                .show();

                        ContactListAdapter.this.notifyDataSetChanged();
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
            };
        });
        tv_name.setText(contact.getUsername());
        Log.d(TAG,"contact name:"+contact.getUsername());
        if(contact.getUserphone() != null) {
            Log.d(TAG,"user phone:"+(contact.getUserphone().split(";")[0]));
            tv_number.setText(contact.getUserphone().split(";")[0]); //当contact对象是直接从json文件读取出来时，可能会包含几个号码用；隔开，默认在list中显示第一个号码
        }else{
            tv_number.setVisibility(GONE);
        }
        tv_alpha.setText(CorePinYinUtil.getPinyinFirstAlpha(contact.getNamepinyin()));
        iv_icon.setImageResource(R.mipmap.img_contact_default);
        if (!TextUtils.isEmpty(contact.getUsericon())) {
            if (contact.getUsericon().startsWith("content")) {
            } else {
            }
        }

        if (position > 0) {
            // 不是第一行，控制pinyin栏
            ContactDALEx lastContact = getItem(position - 1);
            String lastAlpha = CorePinYinUtil.getPinyinFirstAlpha(lastContact.getNamepinyin());
            String nowAlpha = CorePinYinUtil.getPinyinFirstAlpha(contact.getNamepinyin());
            if (lastAlpha.equals(nowAlpha)) {
                tv_alpha.setVisibility(GONE);
            } else {
                tv_alpha.setVisibility(View.VISIBLE);
            }
        } else {
            tv_alpha.setVisibility(View.VISIBLE);
        }

    }

    public void init() {

        if (isAlphaSearch) {
            //构建字母列表索引
            if (alphaIndexer == null) {
                alphaIndexer = new LinkedHashMap<>();
            } else {
                alphaIndexer.clear();
            }

            for (int index = 0; index < getData().size(); index++) {
                String alpha = CorePinYinUtil.getPinyinFirstAlpha(getData().get(index).getNamepinyin());
                if (!alphaIndexer.containsKey(alpha)) {
                    alphaIndexer.put(alpha, index);
                }
            }
        }
    }

    @Override
    public void refreshList(List<ContactDALEx> data) {
        super.refreshList(data);
        init();
    }

    public Map<String, Integer> getAlphaIndexer() {
        if (alphaIndexer == null) {
            alphaIndexer = new HashMap<String, Integer>();
        }
        return alphaIndexer;
    }

    public List<String> getAlphaList() {
        List<String> list = new ArrayList<>();
        if (alphaIndexer == null) {
            init();
        }
        list.addAll(alphaIndexer.keySet());
        return list;
    }
}
