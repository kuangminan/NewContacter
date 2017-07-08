package com.future.wk.newcontacter.data.dalex;



import android.util.Log;

import com.future.wk.newcontacter.base.store.db.SqliteBaseDALEx;
import com.future.wk.newcontacter.base.store.db.annotation.DatabaseField;
import com.future.wk.newcontacter.base.store.db.annotation.DatabaseField.FieldType;
import com.future.wk.newcontacter.base.store.db.annotation.SqliteDao;

import java.util.List;

/**
 * Created by samsung on 2017/6/8.
 */
public class UserDALEx extends SqliteBaseDALEx {

	public static final String PINYIN = "pinyin";
	public static final String USERNAME = "username";
	public static final String SEX = "sex";
	public static final String AGE = "age";
	public static final String ROLE = "role";
	public static final String LOGOURL = "logourl";
	private String TAG = "UserDALEX";

	@DatabaseField(primaryKey = true,Type = FieldType.VARCHAR)
	private String userid;

	@DatabaseField(Type = FieldType.VARCHAR)
	private String username; // 用户的用户名

	@DatabaseField(Type = FieldType.VARCHAR)
	private String password;

	@DatabaseField(Type = FieldType.VARCHAR)
	private String email;

	@DatabaseField(Type = FieldType.VARCHAR)
	private String mobilePhoneNumber;

	@DatabaseField(Type = FieldType.INT)
	private int sex; // 性别  1男 0女

	@DatabaseField(Type = FieldType.VARCHAR)
	private String pinyin; // 名字拼音

	@DatabaseField(Type = FieldType.INT)
	private int age;//年龄

	@DatabaseField(Type = FieldType.VARCHAR)
	private String createAt;//创建时间

	@DatabaseField(Type = FieldType.VARCHAR)
	private String updateAt;//修改时间

	@DatabaseField(Type = FieldType.VARCHAR)
	private String logourl;//头像

	public static UserDALEx get() {
		return SqliteDao.getDao(UserDALEx.class);
	}

	/**
	 * 获取我的好友列表
	 **/
	public List<UserDALEx> findAllFriend(){
		String sql = String.format("select * from %s where exists (select null from %s where %s.userid = friendid and status = 1) order by %s asc",
				TABLE_NAME,TABLE_NAME,TABLE_NAME,PINYIN);
		Log.d(TAG,sql);
		return findList(sql);
	}

	/**
	 * 根据关键字搜索朋友
	 **/
	public List<UserDALEx> findAllFriendByFilter(String filterString){
		String sql = String.format("select * from %s where %s or %s and exists (select null from %s where %s.userid = friendid and status = 1) order by %s asc",
				TABLE_NAME,PINYIN + " like " + "'%" + filterString + "%'",USERNAME + " like " + " '%" + filterString + "%' ",TABLE_NAME,TABLE_NAME,PINYIN);
		Log.d(TAG,sql);
		return findList(sql
		);

	}

	public String getCreateAt() {
		return createAt;
	}

	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}

	public String getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(String updateAt) {
		this.updateAt = updateAt;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getNickname() {
		return username;
	}

	public void setNickname(String nickname) {
		this.username = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLogourl() {
		return logourl;
	}

	public void setLogourl(String logourl) {
		this.logourl = logourl;
	}
}
