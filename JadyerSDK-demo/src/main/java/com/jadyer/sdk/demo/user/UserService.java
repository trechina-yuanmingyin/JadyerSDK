package com.jadyer.sdk.demo.user;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import com.jadyer.sdk.demo.user.model.UserInfo;

@Service
public class UserService {
	@Resource
	private UserInfoDao userInfoDao;

	public UserInfo getByUsernameAndPassword(String username, String password){
		String encrypt = DigestUtils.md5Hex(password);
		password = DigestUtils.md5Hex(password.substring(0, 3) + encrypt + password + encrypt + password.substring(password.length()-3));
		return userInfoDao.findByUsernameAndPassword(username, password);
	}
}