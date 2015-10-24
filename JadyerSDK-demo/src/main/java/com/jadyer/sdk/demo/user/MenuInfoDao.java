package com.jadyer.sdk.demo.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.jadyer.sdk.demo.user.model.MenuInfo;

public interface MenuInfoDao extends JpaRepository<MenuInfo, Integer> {
	@Query("FROM MenuInfo WHERE uid=?1")
	public List<MenuInfo> findMenuListByUID(int uid);

	@Modifying
	@Transactional(timeout=10)
	@Query("DELETE FROM MenuInfo WHERE uid=?1")
	public int deleteByUID(int uid);
}