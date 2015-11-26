package com.jadyer.sdk.demo.weixin.reply;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jadyer.sdk.demo.common.base.CommonResult;
import com.jadyer.sdk.demo.common.constant.Constants;
import com.jadyer.sdk.demo.weixin.reply.model.ReplyInfo;

@Controller
@RequestMapping(value="/reply")
public class ReplyController{
	@Resource
	private ReplyInfoDao replyInfoDao;

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


//	/**
//	 * 查询关键字回复列表
//	 */
//	@RequestMapping("/keyword/list")
//	public String listKeyword(HttpServletRequest request){
//		int uid = (Integer)request.getSession().getAttribute(Constants.UID);
//		List<ReplyInfo> replyInfoList = replyInfoDao.findByCategory(uid, "2");
//		request.setAttribute("replyInfoList", replyInfoList);
//		return "reply/keywordList";
//	}


	/**
	 * 分页查询关键字回复列表
	 * @param page zero-based page index
	 */
	@RequestMapping("/keyword/list")
	public String listViaPage(String pageNo, HttpServletRequest request){
		final int uid = (Integer)request.getSession().getAttribute(Constants.UID);
		//排序
		Sort sort = new Sort(Sort.Direction.DESC, "id");
		//分页条件
		Pageable pageable = new PageRequest(StringUtils.isBlank(pageNo)?0:Integer.parseInt(pageNo), Constants.PAGE_SIZE, sort);
		//组合查询条件
		Specification<ReplyInfo> spec = new Specification<ReplyInfo>(){
			@Override
			public Predicate toPredicate(Root<ReplyInfo> root, CriteriaQuery<?> query, CriteriaBuilder builder){
				Path<Integer> _uid = root.get("uid");
				Path<Integer> _category = root.get("category");
				query.where(builder.equal(_uid, uid)).where(builder.equal(_category, "2"));
				return null;
			}
		};
		//执行
		Page<ReplyInfo> keywordPage = replyInfoDao.findAll(spec, pageable);
		request.setAttribute("page", keywordPage);
		return "reply/keywordList";
	}


	/**
	 * 查询关键字回复的内容
	 */
	@ResponseBody
	@RequestMapping("/keyword/get/{id}")
	public CommonResult getKeyword(@PathVariable int id){
		return new CommonResult(replyInfoDao.findOne(id));
	}


	/**
	 * delete关键字
	 */
	@ResponseBody
	@RequestMapping("/keyword/delete/{id}")
	public CommonResult deleteKeyword(@PathVariable int id){
		replyInfoDao.delete(id);
		return new CommonResult();
	}


	/**
	 * saveOrUpdate关键字
	 */
	@ResponseBody
	@RequestMapping("/keyword/save")
	public CommonResult saveKeyword(ReplyInfo replyInfo){
		replyInfoDao.saveAndFlush(replyInfo);
		return new CommonResult();
	}
}