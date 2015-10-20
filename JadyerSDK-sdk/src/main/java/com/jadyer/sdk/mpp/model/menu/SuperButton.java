package com.jadyer.sdk.mpp.model.menu;

/**
 * 封装父菜单项
 * @see 1.这类菜单项有两个固定属性name和sub_button
 * @see   而sub_button是一个可能为SubClickButton或SubViewButton子菜单项数组
 * @see 2.这里的父菜单指的是包含有二级菜单项的一级菜单
 * @create Oct 18, 2015 8:55:19 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class SuperButton extends Button {
	private Button[] sub_button;
	
	public SuperButton(String name, Button[] sub_button) {
		super(name);
		this.sub_button = sub_button;
	}

	public Button[] getSub_button() {
		return sub_button;
	}

	public void setSub_button(Button[] sub_button) {
		this.sub_button = sub_button;
	}
}