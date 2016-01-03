package com.jadyer.sdk.demo.weixin.fans;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMapping;

import com.jadyer.sdk.demo.common.constant.Constants;
import com.jadyer.sdk.demo.weixin.fans.model.FansInfo;

@Controller
@RequestMapping(value="/fans")
public class FansController{
	@Resource
	private FansInfoDao fansInfoDao;

	/**
	 * 分页查询粉丝信息
	 * @param page zero-based page index
	 */
	@RequestMapping("/list")
	public String listViaPage(String pageNo, HttpServletRequest request){
		final int uid = (Integer)request.getSession().getAttribute(Constants.UID);
		//排序
		Sort sort = new Sort(Sort.Direction.DESC, "id");
		//分页条件
		Pageable pageable = new PageRequest(StringUtils.isBlank(pageNo)?0:Integer.parseInt(pageNo), Constants.PAGE_SIZE, sort);
		//组合查询条件
		Specification<FansInfo> spec = new Specification<FansInfo>(){
			@Override
			public Predicate toPredicate(Root<FansInfo> root, CriteriaQuery<?> query, CriteriaBuilder builder){
				List<Predicate> list = new ArrayList<Predicate>();
				Path<Integer> _uid = root.get("uid");
				list.add(builder.equal(_uid, uid));
				//list.add(builder.equal(root.get("uid").as(Integer.class), uid));
				//list.add(builder.like(root.<String>get("nickname"), "%"+nickname+"%"));
				return builder.and(list.toArray(new Predicate[list.size()]));
			}
		};
		//执行
		Page<FansInfo> fansPage = fansInfoDao.findAll(spec, pageable);
		request.setAttribute("page", fansPage);
		return "fansList";
	}
}