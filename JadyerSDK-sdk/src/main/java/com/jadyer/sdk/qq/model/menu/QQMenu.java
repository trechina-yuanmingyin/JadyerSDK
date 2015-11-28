package com.jadyer.sdk.qq.model.menu;

/**
 * 封装整个菜单对象
 * @create Nov 28, 2015 8:58:27 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class QQMenu {
	private QQButton[] button;

	public QQMenu(QQButton[] button) {
		this.button = button;
	}

	public QQButton[] getButton() {
		return button;
	}

	public void setButton(QQButton[] button) {
		this.button = button;
	}
}