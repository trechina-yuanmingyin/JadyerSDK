package com.jadyer.sdk.demo.mpp.fans;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.jadyer.sdk.demo.mpp.fans.model.FansInfo;

public interface FansInfoDao extends JpaRepository<FansInfo, Integer> {
	/**
	 * 查询某个粉丝的信息
	 */
	@Query("FROM FansInfo WHERE uid=?1 AND openid=?2")
	public FansInfo findByUidAndOpenid(int uid, String openid);

	/**
	 * 更新粉丝的关注状态
	 */
	@Modifying
	@Transactional(timeout=10)
	@Query("UPDATE FansInfo SET subscribe=?1 WHERE uid=?2 AND openid=?3")
	public int updateSubscribe(String subscribe, int uid, String openid);
}