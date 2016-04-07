package com.jadyer.sdk.demo.mpp.fans;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.jadyer.sdk.demo.common.base.ApplicationContextHolder;
import com.jadyer.sdk.demo.mpp.fans.model.FansInfo;
import com.jadyer.sdk.demo.user.model.UserInfo;
import com.jadyer.sdk.qq.helper.QQHelper;
import com.jadyer.sdk.qq.helper.QQTokenHolder;
import com.jadyer.sdk.util.SDKUtil;
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
		/**
		 * 向QQ服务器或微信服务器查询粉丝信息
		 */
		if("1".equals(userInfo.getMptype())){
			com.jadyer.sdk.weixin.model.WeixinFansInfo weixinFansInfo = WeixinHelper.getWeixinFansInfo(WeixinTokenHolder.getWeixinAccessToken(userInfo.getAppid()), openid);
			fansInfo.setUid(userInfo.getId());
			fansInfo.setWxId(userInfo.getMpid());
			fansInfo.setOpenid(openid);
			fansInfo.setSubscribe(String.valueOf(weixinFansInfo.getSubscribe()));
			fansInfo.setSubscribeTime(DateFormatUtils.format(new Date(Long.parseLong(weixinFansInfo.getSubscribe_time())*1000), "yyyy-MM-dd HH:mm:ss"));
			fansInfo.setNickname(SDKUtil.escapeEmoji(weixinFansInfo.getNickname()));
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
		}else{
			com.jadyer.sdk.qq.model.QQFansInfo qqFansInfo = QQHelper.getQQFansInfo(QQTokenHolder.getQQAccessToken(userInfo.getAppid()), openid);
			fansInfo.setUid(userInfo.getId());
			fansInfo.setWxId(userInfo.getMpid());
			fansInfo.setOpenid(openid);
			fansInfo.setSubscribe(String.valueOf(qqFansInfo.getSubscribe()));
			fansInfo.setSubscribeTime(DateFormatUtils.format(new Date(Long.parseLong(qqFansInfo.getSubscribe_time())*1000), "yyyy-MM-dd HH:mm:ss"));
			fansInfo.setNickname(qqFansInfo.getNickname());
			fansInfo.setSex(String.valueOf(qqFansInfo.getSex()));
			fansInfo.setCity(qqFansInfo.getCity());
			fansInfo.setCountry(qqFansInfo.getCountry());
			fansInfo.setProvince(qqFansInfo.getProvince());
			fansInfo.setLanguage(qqFansInfo.getLanguage());
			fansInfo.setHeadimgurl(qqFansInfo.getHeadimgurl());
			fansInfo.setUnionid(qqFansInfo.getUnionid());
			fansInfo.setRemark(qqFansInfo.getRemark());
			fansInfo.setGroupid(qqFansInfo.getGroupid());
			fansInfoDao.saveAndFlush(fansInfo);
		}
	}
}