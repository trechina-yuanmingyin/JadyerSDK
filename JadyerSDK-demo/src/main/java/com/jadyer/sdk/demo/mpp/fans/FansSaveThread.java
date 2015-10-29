package com.jadyer.sdk.demo.mpp.fans;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.jadyer.sdk.demo.common.base.ApplicationContextHolder;
import com.jadyer.sdk.demo.mpp.fans.model.FansInfo;
import com.jadyer.sdk.demo.user.UserInfoDao;
import com.jadyer.sdk.demo.user.model.UserInfo;
import com.jadyer.sdk.mpp.util.MPPUtil;
import com.jadyer.sdk.mpp.util.TokenHolder;

public class FansSaveThread implements Runnable {
	private UserInfo userInfo;
	private String openid;

	public FansSaveThread(UserInfo userInfo, String openid){
		this.userInfo = userInfo;
		this.openid = openid;
	}

	@Override
	public void run() {
		FansInfoDao fansInfoDao = (FansInfoDao)ApplicationContextHolder.getBean("fansInfoDao");
		UserInfoDao userInfoDao = (UserInfoDao)ApplicationContextHolder.getBean("userInfoDao");
		FansInfo fansInfo = fansInfoDao.findByUidAndOpenid(userInfo.getId(), openid);
		if(null == fansInfo){
			fansInfo = new FansInfo();
		}
		/**
		 * 获取并更新accesstoken
		 */
		String accesstoken = TokenHolder.getWeixinAccessToken(userInfo.getAppId(), userInfo.getAppSecret());
		userInfo.setAccessToken(accesstoken);
		userInfo.setAccessTokenTime(new Date());
		userInfoDao.saveAndFlush(userInfo);
		/**
		 * 向微信服务器查询粉丝信息
		 */
		com.jadyer.sdk.mpp.model.FansInfo _fansInfo = MPPUtil.getFansInfo(accesstoken, openid);
		fansInfo.setUid(userInfo.getId());
		fansInfo.setWxId(userInfo.getWxId());
		fansInfo.setOpenid(openid);
		fansInfo.setSubscribe(String.valueOf(_fansInfo.getSubscribe()));
		fansInfo.setSubscribeTime(DateFormatUtils.format(new Date(Long.parseLong(_fansInfo.getSubscribe_time())*1000), "yyyy-MM-dd HH:mm:ss"));
		fansInfo.setNickname(_fansInfo.getNickname());
		fansInfo.setSex(String.valueOf(_fansInfo.getSex()));
		fansInfo.setCity(_fansInfo.getCity());
		fansInfo.setCountry(_fansInfo.getCountry());
		fansInfo.setProvince(_fansInfo.getProvince());
		fansInfo.setLanguage(_fansInfo.getLanguage());
		fansInfo.setHeadimgurl(_fansInfo.getHeadimgurl());
		fansInfo.setUnionid(_fansInfo.getUnionid());
		fansInfo.setRemark(_fansInfo.getRemark());
		fansInfo.setGroupid(_fansInfo.getGroupid());
		fansInfoDao.saveAndFlush(fansInfo);
	}
}