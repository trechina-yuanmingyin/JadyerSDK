package com.jadyer.sdk.demo.mpp.reply;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jadyer.sdk.demo.common.base.CommonResult;
import com.jadyer.sdk.demo.common.constant.Constants;
import com.jadyer.sdk.demo.mpp.reply.model.ReplyInfo;

@Controller
@RequestMapping(value="/reply")
public class ReplyController{
	@Resource
	private ReplyInfoDao replyInfoDao;

	/**
	 * 查询关注后回复的内容
	 */
	@RequestMapping("/follow/get")
	public String getFollow(HttpServletRequest request){
		int uid = (Integer)request.getSession().getAttribute(Constants.UID);
		List<ReplyInfo> replyInfoList = replyInfoDao.findByCategory(uid, "1");
		if(!replyInfoList.isEmpty()){
			request.setAttribute("replyInfo", replyInfoList.get(0));
		}
		return "reply/follow";
	}

	/**
	 * 更新关注后回复的内容
	 */
	@ResponseBody
	@RequestMapping("/follow/save")
	public CommonResult saveFollow(ReplyInfo replyInfo, HttpServletRequest request){
		replyInfo.setUid((Integer)request.getSession().getAttribute(Constants.UID));
		replyInfo.setCategory("1");
		replyInfo.setType("0");
		return new CommonResult(replyInfoDao.saveAndFlush(replyInfo));
	}

	/**
	 * 查询通用的回复内容
	 */
	@RequestMapping("/common/get")
	public String getCommon(HttpServletRequest request){
		int uid = (Integer)request.getSession().getAttribute(Constants.UID);
		List<ReplyInfo> replyInfoList = replyInfoDao.findByCategory(uid, "0");
		if(!replyInfoList.isEmpty()){
			request.setAttribute("replyInfo", replyInfoList.get(0));
		}
		return "reply/common";
	}

	/**
	 * 查询关键字回复列表
	 */
	@RequestMapping("/keyword/list")
	public String listKeyword(HttpServletRequest request){
		int uid = (Integer)request.getSession().getAttribute(Constants.UID);
		List<ReplyInfo> replyInfoList = replyInfoDao.findByCategory(uid, "2");
		request.setAttribute("replyInfoList", replyInfoList);
		return "reply/keyword";
	}
	
	/**
	 * 查询关键字回复的内容
	 */
	@RequestMapping("/keyword/get/{id}")
	public String getKeyword(@PathVariable int id, HttpServletRequest request){
		request.setAttribute("reply", replyInfoDao.findOne(id));
		return "reply/keyword_get";
	}

	/**
	 * 跳转到更新关键字页面
	 */
	@RequestMapping("/keyword/toupdate/{id}")
	public String updateKeyword(@PathVariable int id, HttpServletRequest request){
		request.setAttribute("replyInfo", replyInfoDao.findOne(id));
		return "reply/keyword_save";
	}

	/**
	 * saveOrUpdate关键字
	 */
	@RequestMapping("/keyword/save")
	public String saveKeyword(ReplyInfo replyInfo, HttpServletRequest request){
		replyInfoDao.saveAndFlush(replyInfo);
		return this.listKeyword(request);
	}
}