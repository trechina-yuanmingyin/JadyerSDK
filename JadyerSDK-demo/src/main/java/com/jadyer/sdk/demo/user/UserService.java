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
	public UserInfo findByWxid(String mpid){
		return userInfoDao.findByWxid(mpid);
	}

	@Transactional(readOnly=true)
	public UserInfo findByQqid(String mpid){
		return userInfoDao.findByQqid(mpid);
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
	 * 查询当前登录用户关联的公众平台自定义菜单JSON信息
	 */
	public String getMenuJson(int uid){
		List<MenuInfo> menuList = menuInfoDao.findMenuListByUID(uid);
		for(MenuInfo obj : menuList){
			if("3".equals(obj.getType())){
				return obj.getMenuJson();
			}
		}
		return null;
	}

	/**
	 * 存储微信或QQ公众号自定义菜单的JSON字符串
	 * @param menuJson 微信或QQ公众号自定义菜单数据的JSON字符串
	 * @return 返回本次存储在数据库的自定义菜单内容
	 */
	public MenuInfo menuJsonSave(int uid, String menuJson){
		MenuInfo info = new MenuInfo();
		info.setUid(uid);
		info.setType("3");
		info.setName("json");
		info.setMenuJson(menuJson);
		return menuInfoDao.saveAndFlush(info);
	}
}