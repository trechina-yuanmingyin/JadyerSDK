package com.jadyer.sdk.mpp.model.menu;

/**
 * 封装整个菜单对象
 * @create Oct 18, 2015 8:57:46 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class Menu {
	private Button[] button;

	public Menu(Button[] button) {
		this.button = button;
	}

	public Button[] getButton() {
		return button;
	}

	public void setButton(Button[] button) {
		this.button = button;
	}
}