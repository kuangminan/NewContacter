package com.future.wk.newcontacter.base;

import android.content.Context;

import com.future.wk.newcontacter.data.dalex.UserDALEx;
import com.future.wk.newcontacter.util.ICallBack;
import com.future.wk.newcontacter.util.ICallBackObject;

import java.util.List;

/**
 * Created by samsung on 2017/6/9.
 * 管理用户信息
 */

public interface IUserManager {
    /**
     * @Decription 注册
     **/
    void register(final UserDALEx user, final ICallBackObject<UserDALEx> callBack);

    /**
     * @Decription 登陆
     **/
    void login(final Context context, final String name, final String psw, final ICallBackObject<UserDALEx> callBack);

    /**
     * @Decription 根据输入查询用户
     **/
    void queryUsers(String username,int limit,final ICallBackObject<List<UserDALEx>> callBack);

    /**
     * @Decription 更新用户信息
     **/
    void getUserInfo(String userid);

    /**
     * @Decription 查找用户信息
     **/
    void queryUserInfo(String userid, final ICallBackObject<UserDALEx> callBack);

    /**
     * @Decription 更新头像
     **/
    void updateUserIcon(String path,final ICallBackObject<String> callBack);

    /**
     * @Decription 更新名字
     **/
    void updateUserName(String name,final ICallBackObject<String> callBack);

    /**
     * @Decription 更新密码
     **/
    void updateUserPSW(String psw,final ICallBackObject<String> callBack);

    /**
     * @Decription 更新个性签名
     **/
    void updateUserRemark(String remark,final ICallBackObject<String> callBack);
}
