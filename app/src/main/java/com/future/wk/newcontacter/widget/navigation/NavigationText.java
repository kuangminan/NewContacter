package com.future.wk.newcontacter.widget.navigation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.future.wk.newcontacter.R;
import com.future.wk.newcontacter.widget.common.SearchView;

/**
 *	默认文字标题actionbar
 */
public class NavigationText extends NavigationContainer{
	
	public NavigationText(Context context) {
		super(context);
	}
	
	public NavigationText(Context context, AttributeSet attr) {
		super(context,attr);
	}

	TextView tv_title;
	SearchView et_title;

	@Override
	public View initCenterView() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.navigation_text, null);
		tv_title = (TextView)view.findViewById(R.id.navigation_text_title);
		et_title = (SearchView)view.findViewById(R.id.navigation_search_text);
		return view;
	}
	
	public NavigationText setTitle(String text){
		tv_title.setText(text);
		return this;
	}
	public void showSearchEditText(){
		tv_title.setVisibility(GONE);
		et_title.setVisibility(VISIBLE);
	}
	public String getSearchValue(){
		return et_title.getEditText().getText().toString();
	}
}