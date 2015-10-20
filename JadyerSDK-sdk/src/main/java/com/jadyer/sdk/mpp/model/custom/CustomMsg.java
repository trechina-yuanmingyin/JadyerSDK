package com.jadyer.sdk.mpp.model.custom;

/**
 * 客服接口请求参数的基类
 * @create Oct 18, 2015 10:37:58 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public abstract class CustomMsg {
	/**
	 * 普通用户openid
	 */
	private String touser;

	public CustomMsg(String touser) {
		this.touser = touser;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}
}