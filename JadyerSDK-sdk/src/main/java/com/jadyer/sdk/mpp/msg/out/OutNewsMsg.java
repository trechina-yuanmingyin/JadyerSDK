package com.jadyer.sdk.mpp.msg.out;

import java.util.ArrayList;
import java.util.List;

import com.jadyer.sdk.mpp.msg.in.InMsg;

/**
 * 回复图文消息
 * @create Oct 18, 2015 6:04:05 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class OutNewsMsg extends OutMsg {
	private List<News> articles = new ArrayList<News>();

	public OutNewsMsg(InMsg inMsg) {
		super(inMsg);
		this.msgType = "news";
	}

	public int getArticleCount() {
		return articles.size();
	}

	public List<News> getArticles() {
		return articles;
	}

//	public OutNewsMsg addNews(List<News> articles) {
//		if(null != articles){
//			this.articles.addAll(articles);
//		}
//		return this;
//	}
//	
//	public OutNewsMsg addNews(News news) {
//		this.articles.add(news);
//		return this;
//	}

	public OutNewsMsg addNews(String title, String description, String picUrl, String url) {
		this.articles.add(new News(title, description, picUrl, url));
		return this;
	}
}