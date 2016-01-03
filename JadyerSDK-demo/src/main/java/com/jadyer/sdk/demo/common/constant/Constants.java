package com.jadyer.sdk.demo.common.constant;

public interface Constants {
	/**
	 * 分页参数
	 */
	int PAGE_NO = 0;
	int PAGE_SIZE = 10;

	/**
	 * 平台用户ID
	 */
	String UID = "uid";
	
	/**
	 * 平台用户对象
	 */
	String USERINFO = "userInfo";
	
	/**
	 * 平台一级菜单和当前菜单标记
	 */
	String MENU_SYS = "menu_sys";
	String MENU_FANS = "menu_fans";
	String MENU_REPLY = "menu_reply";
	String MENU_PLUGIN = "menu_plugin";
	String MENU_VIEW_CURRENT = "currentMenu";
	
	/**
	 * 微信或QQ公众平台绑定时发送的文本指令
	 */
	String MPP_BIND_TEXT = "我是玄玉";
}