package com.jadyer.sdk.demo.weixin.fans;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.jadyer.sdk.demo.common.base.ApplicationContextHolder;
import com.jadyer.sdk.demo.user.model.UserInfo;
import com.jadyer.sdk.demo.weixin.fans.model.FansInfo;
import com.jadyer.sdk.weixin.helper.WeixinHelper;
import com.jadyer.sdk.weixin.helper.WeixinTokenHolder;

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
		FansInfo fansInfo = fansInfoDao.findByUidAndOpenid(userInfo.getId(), openid);
		if(null == fansInfo){
			fansInfo = new FansInfo();
		}
//		/**
//		 * 获取并更新accesstoken
//		 */
//		UserInfoDao userInfoDao = (UserInfoDao)ApplicationContextHolder.getBean("userInfoDao");
//		userInfo.setAccessToken(WeixinTokenHolder.getWeixinAccessToken());
//		userI	nfo.setAccessTokenTime(new Date());
//		userInfoDao.saveAndFlush(userInfo);
		/**
		 * 向微信服务器查询粉丝信息
		 */
		com.jadyer.sdk.weixin.model.WeixinFansInfo weixinFansInfo = WeixinHelper.getWeixinFansInfo(WeixinTokenHolder.getWeixinAccessToken(userInfo.getAppid()), openid);
		fansInfo.setUid(userInfo.getId());
		fansInfo.setWxId(userInfo.getMpid());
		fansInfo.setOpenid(openid);
		fansInfo.setSubscribe(String.valueOf(weixinFansInfo.getSubscribe()));
		fansInfo.setSubscribeTime(DateFormatUtils.format(new Date(Long.parseLong(weixinFansInfo.getSubscribe_time())*1000), "yyyy-MM-dd HH:mm:ss"));
		fansInfo.setNickname(weixinFansInfo.getNickname());
		fansInfo.setSex(String.valueOf(weixinFansInfo.getSex()));
		fansInfo.setCity(weixinFansInfo.getCity());
		fansInfo.setCountry(weixinFansInfo.getCountry());
		fansInfo.setProvince(weixinFansInfo.getProvince());
		fansInfo.setLanguage(weixinFansInfo.getLanguage());
		fansInfo.setHeadimgurl(weixinFansInfo.getHeadimgurl());
		fansInfo.setUnionid(weixinFansInfo.getUnionid());
		fansInfo.setRemark(weixinFansInfo.getRemark());
		fansInfo.setGroupid(weixinFansInfo.getGroupid());
		fansInfoDao.saveAndFlush(fansInfo);
	}
}