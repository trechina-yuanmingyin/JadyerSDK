package com.jadyer.sdk.mpp.model.menu;

/**
 * 按钮的基类
 * @see 所有一级菜单、二级菜单都有一个相同的name属性
 * @create Oct 18, 2015 8:51:25 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public abstract class Button {
	private String name;

	public Button(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}