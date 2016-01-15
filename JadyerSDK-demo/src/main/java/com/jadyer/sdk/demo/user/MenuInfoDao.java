package com.jadyer.sdk.demo.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.jadyer.sdk.demo.user.model.MenuInfo;

public interface MenuInfoDao extends JpaRepository<MenuInfo, Integer> {
	@Query("FROM MenuInfo WHERE uid=?1")
	List<MenuInfo> findMenuListByUID(int uid);

	@Modifying
	@Transactional
	@Query("UPDATE MenuInfo SET menuJson=?1 WHERE type=3 AND uid=?2")
	public int updateJson(String menuJson, int uid);
}