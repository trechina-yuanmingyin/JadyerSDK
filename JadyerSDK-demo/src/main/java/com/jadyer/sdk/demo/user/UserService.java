package com.jadyer.sdk.demo.user;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jadyer.sdk.demo.user.model.MenuInfo;
import com.jadyer.sdk.demo.user.model.UserInfo;

@Service
@Transactional(rollbackFor=Exception.class)
public class UserService {
	@Resource
	private UserInfoDao userInfoDao;
	@Resource
	private MenuInfoDao menuInfoDao;

	@Transactional(readOnly=true)
	public UserInfo findByUsernameAndPassword(String username, String password){
		String encrypt = DigestUtils.md5Hex(password);
		password = DigestUtils.md5Hex(password.substring(0, 3) + encrypt + password + encrypt + password.substring(password.length()-3));
		return userInfoDao.findByUsernameAndPassword(username, password);
	}

	@Transactional(readOnly=true)
	public UserInfo findByWxId(String wxId){
		return userInfoDao.findByWxId(wxId);
	}

	public UserInfo save(UserInfo userInfo){
		return userInfoDao.saveAndFlush(userInfo);
	}

	/**
	 * 查询指定平台用户的微信菜单资料
	 */
	public List<MenuInfo> findMenuList(int uid){
		return menuInfoDao.findMenuListByUID(uid);
	}
	
	public boolean updateMenu(int uid){
		menuInfoDao.deleteByUID(uid);
		return false;
	}
}