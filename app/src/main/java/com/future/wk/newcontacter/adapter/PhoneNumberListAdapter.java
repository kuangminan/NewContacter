package com.future.wk.newcontacter.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.future.wk.newcontacter.R;
import com.future.wk.newcontacter.data.dalex.PhoneNumberDALEx;
import com.future.wk.newcontacter.widget.xrecyclerview.adapter.BaseRecyclerViewAdapter;
import com.future.wk.newcontacter.widget.xrecyclerview.adapter.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by samsung on 2017/5/26.
 */

public class PhoneNumberListAdapter extends BaseRecyclerViewAdapter<PhoneNumberDALEx> {
    private String TAG = "PNListAdapter";

    ImageView iv_phone;
    ImageView iv_message;

    public PhoneNumberListAdapter(Context mContext, List<PhoneNumberDALEx> data){
        super(mContext, R.layout.item_number_list, data);
    }

    @Override
    protected void convert(BaseRecyclerViewHolder holder, final PhoneNumberDALEx contact, final int position) {

        iv_phone = holder.getView(R.id.item_contact_phone);
        iv_message = holder.getView(R.id.item_contact_message);

        TextView tv_number = holder.getView(R.id.item_contact_number);
        Log.d(TAG,"name:"+contact.getPhonenumber());
        tv_number.setText(contact.getPhonenumber());

        iv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:"+contact.getPhonenumber()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        iv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+contact.getPhonenumber()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });


    }

}
