package com.future.wk.newcontacter.base;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.future.wk.newcontacter.base.store.preference.NCPreference;
import com.future.wk.newcontacter.data.dalex.bmob.BmobContacterManager;
import com.future.wk.newcontacter.data.dalex.bmob.BmobFileManager;
import com.future.wk.newcontacter.data.dalex.bmob.BmobPhonenumberManager;
import com.future.wk.newcontacter.data.dalex.bmob.BmobUserManager;
import com.future.wk.newcontacter.util.PreferenceUtil;
import com.orhanobut.logger.Logger;

import cn.bmob.v3.Bmob;


/**
 * 应用 Application
 **/
public class NCApplication extends Application {


	public static final String LocalTag = "通讯录";
	public static final String NetworkTag = "微服务";
	public static final String MyselfTag = "设置";


	public final static int Request_Camera = 100;
	public final static int Request_Image = 101;
	public final static int Request_Preview = 102;
	public final static int Request_Register = 103;
	public final static int Request_Crop = 104;


	private static Context mApplication;

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this.getApplicationContext();

		NCAppContext.setContext(getApplicationContext());
		//创建一下公共数据库
		NCObjectCache.getInstance().getDBFromUserAccunt(NCAppContext.DB_COMMON);

		//检查数据库升级
		String lastOriginalAccount = NCPreference.getInstance(this).getLastOriginalAccount();
		if(!TextUtils.isEmpty(lastOriginalAccount)){
			NCObjectCache.getInstance().getDBFromUserAccunt(lastOriginalAccount);
		}

		//初始化云数据库
		IUserManager userManager = new BmobUserManager();
		IFileManager fileManager = new BmobFileManager();
		IContactManager contactManager = new BmobContacterManager();
		IPhonenumberManager phonenumberManager = new BmobPhonenumberManager();
		Bmob.initialize(this, NCAppContext.ApplicationID);
		CloudManager.getInstance().setUserManager(userManager);
		CloudManager.getInstance().setFileManager(fileManager);
		CloudManager.getInstance().setContactManager(contactManager);
		CloudManager.getInstance().setPhonenumberManager(phonenumberManager);

		//初始化preference
		PreferenceUtil.init(mApplication);


		Logger.init("NewContacter")               // default tag : PRETTYLOGGER or use just init()
                .methodCount(10)
				.hideThreadInfo();  // default : LogLevel.FULL
	}

	/**
	 * 功能描述：获得一个全局的application对象
	 **/
	public static Context getInstance(){
		return mApplication;
	}
}
