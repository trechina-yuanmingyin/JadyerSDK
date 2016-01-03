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
	
	private String buildEncryptPassword(String password){
		String encrypt = DigestUtils.md5Hex(password);
		return DigestUtils.md5Hex(password.substring(0, 1) + encrypt + password + encrypt + password.substring(password.length()-1));
	}

	@Transactional(readOnly=true)
	public UserInfo findByUsernameAndPassword(String username, String password){
		return userInfoDao.findByUsernameAndPassword(username, buildEncryptPassword(password));
	}

	@Transactional(readOnly=true)
	public UserInfo findOne(int id){
		return userInfoDao.findOne(id);
	}

	@Transactional(readOnly=true)
	public List<UserInfo> findAll(){
		return userInfoDao.findAll();
	}

	@Transactional(readOnly=true)
	public UserInfo findByWxId(String wxId){
		return userInfoDao.findByWxId(wxId);
	}

	public UserInfo save(UserInfo userInfo){
		return userInfoDao.saveAndFlush(userInfo);
	}

	/**
	 * 修改密码
	 * @param userInfo    HttpSession中的当前登录用户信息
	 * @param oldPassword 用户输入的旧密码
	 * @param newPassword 用户输入的新密码
	 * @create Oct 25, 2015 2:58:33 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public UserInfo passwordUpdate(UserInfo userInfo, String oldPassword, String newPassword){
		if(!userInfo.getPassword().equals(buildEncryptPassword(oldPassword))){
			return null;
		}
		userInfo.setPassword(buildEncryptPassword(newPassword));
		return userInfoDao.saveAndFlush(userInfo);
	}

	/**
	 * 查询指定平台用户的微信菜单资料
	 */
	@Transactional(readOnly=true)
	public List<MenuInfo> findMenuList(int uid){
		return menuInfoDao.findMenuListByUID(uid);
	}

	/**
	 * 暂未使用到该方法
	 * @create Oct 25, 2015 3:02:19 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	@Deprecated
	public boolean updateMenu(int uid){
		menuInfoDao.deleteByUID(uid);
		return false;
	}
}