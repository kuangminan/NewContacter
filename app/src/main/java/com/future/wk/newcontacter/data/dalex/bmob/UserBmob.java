package com.future.wk.newcontacter.data.dalex.bmob;


import com.future.wk.newcontacter.data.dalex.UserDALEx;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * 用户信息
 * @author wty
 */
public class UserBmob extends BmobUser {

	private int sex; // 性别  1男 0女

	private String pinyin; // 名字拼音

	private int age;//年龄


	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}


	public void save(final UserBmob bmob){
		UserDALEx dalex = new UserDALEx();
		dalex.setUserid(bmob.getObjectId());
		dalex.setNickname(bmob.getUsername());
		dalex.setEmail(bmob.getEmail());
		dalex.setMobilePhoneNumber(bmob.getMobilePhoneNumber());
		dalex.setAge(bmob.getAge());
		dalex.setPinyin(bmob.getPinyin());
		dalex.setCreateAt(bmob.getCreatedAt());
		dalex.setUpdateAt(bmob.getUpdatedAt());
		dalex.setSex(bmob.getSex());
		dalex.saveOrUpdate();
	}

	public static UserDALEx convertToDALEx(final UserBmob bmob){
		UserDALEx dalex = new UserDALEx();
		dalex.setUserid(bmob.getObjectId());
		dalex.setNickname(bmob.getUsername());
		dalex.setEmail(bmob.getEmail());
		dalex.setMobilePhoneNumber(bmob.getMobilePhoneNumber());
		dalex.setAge(bmob.getAge());
		dalex.setPinyin(bmob.getPinyin());
		dalex.setCreateAt(bmob.getCreatedAt());
		dalex.setUpdateAt(bmob.getUpdatedAt());
		dalex.setSex(bmob.getSex());
		return dalex;
	}

	public List<UserDALEx> convert(final List<UserBmob> list){
		List<UserDALEx> localdalex = bmobToLocal(list);
		return localdalex;
	}

	public List<UserDALEx> bmobToLocal(List<UserBmob> list){
		List<UserDALEx> localdalex = new ArrayList<UserDALEx>();
		for(UserBmob bmob:list){
			UserDALEx dalex = new UserDALEx();
			dalex.setUserid(bmob.getObjectId());
			dalex.setNickname(bmob.getUsername());
			dalex.setEmail(bmob.getEmail());
			dalex.setMobilePhoneNumber(bmob.getMobilePhoneNumber());
			dalex.setAge(bmob.getAge());
			dalex.setPinyin(bmob.getPinyin());
			dalex.setCreateAt(bmob.getCreatedAt());
			dalex.setUpdateAt(bmob.getUpdatedAt());
			dalex.setSex(bmob.getSex());
			localdalex.add(dalex);
		}
		return localdalex;
	}

	public void setAnnotationField(UserDALEx user){
		setMobilePhoneNumber(user.getMobilePhoneNumber());
		setUsername(user.getNickname());
		setPassword(user.getPassword());
		//setPinyin(HanziToPinyinUtil.getShortPinyin(user.getNickname()));
		//setAge(user.getAge());
		//setSex(user.getSex());
		//setEmail(user.getEmail());
	}
}
