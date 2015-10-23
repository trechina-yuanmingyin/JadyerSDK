package com.jadyer.sdk.demo.mpp.reply;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jadyer.sdk.demo.mpp.reply.model.ReplyInfo;

public interface ReplyInfoDao extends JpaRepository<ReplyInfo, Integer> {
	/**
	 * 根据分类查询回复内容
	 * @param uid      平台用户ID
	 * @param category 回复的类别：0--通用的回复，1--关注后回复，2--关键字回复
	 */
	@Query("FROM ReplyInfo WHERE uid=?1 AND category=?2")
	public List<ReplyInfo> findByCategory(int uid, String category);

	/**
	 * 查询指定关键字的信息
	 */
	@Query("FROM ReplyInfo WHERE uid=?1 AND keyword=?2")
	public ReplyInfo findByKeyword(int uid, String keyword);
}