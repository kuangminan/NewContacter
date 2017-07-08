package com.future.wk.newcontacter.base;

import com.future.wk.newcontacter.data.dalex.PhoneNumberDALEx;
import com.future.wk.newcontacter.util.ICallBackObject;

import java.util.List;

/**
 * Created by samsung on 2017/6/13.
 */

public interface IPhonenumberManager {
    /*保存单独一个phonenumber对象*/
    void addContacter(PhoneNumberDALEx phonenumber, ICallBackObject<PhoneNumberDALEx> callBackObject);

    /*保存phonenumber对象list*/
    void addContacterList(List<PhoneNumberDALEx> phonenumberList, ICallBackObject<PhoneNumberDALEx> callBackObject);
}
