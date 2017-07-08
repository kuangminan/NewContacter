package com.future.wk.newcontacter.base;

import com.future.wk.newcontacter.data.dalex.ContactDALEx;
import com.future.wk.newcontacter.util.ICallBackObject;

import java.util.List;

/**
 * Created by samsung on 2017/6/13.
 */

public interface IContactManager {
    /*保存单独一个contacter对象*/
    void addContacter(ContactDALEx conact, ICallBackObject<ContactDALEx> callBackObject);

    /*保存contacter对象list*/
    void addContacterList(List<ContactDALEx> conactList, ICallBackObject<ContactDALEx> callBackObject);

    /*查找一个contacter对象*/
    void queryContactByID(String objectid, ICallBackObject<ContactDALEx> callBackObject);
}
