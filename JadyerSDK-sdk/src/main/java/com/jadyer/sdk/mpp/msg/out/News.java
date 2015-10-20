package com.jadyer.sdk.mpp.msg.out;

/**
 * 图文消息
 * @create Oct 18, 2015 5:57:18 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class News {
	/**
	 * 图文消息标题(不是必须)
	 */
	private String title;

	/**
	 * 图文消息描述(不是必须)
	 */
	private String description;

	/**
	 * 图片链接(不是必须,支持JPG/PNG格式,较好的效果为大图360*200,小图200*200)
	 */
	private String picUrl;

	/**
	 * 点击图文消息跳转链接(不是必须)
	 */
	private String url;

	public News(String title, String description, String picUrl, String url) {
		this.title = title;
		this.description = description;
		this.picUrl = picUrl;
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}