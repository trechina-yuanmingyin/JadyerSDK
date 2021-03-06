package com.jadyer.sdk.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jadyer.sdk.demo.user.model.UserInfo;

public interface UserInfoDao extends JpaRepository<UserInfo, Integer> {
	@Query("FROM UserInfo WHERE username=?1 AND password=?2")
	UserInfo findByUsernameAndPassword(String username, String password);

	@Query("FROM UserInfo WHERE mptype=1 AND mpid=?1")
	UserInfo findByWxid(String mpid);

	@Query("FROM UserInfo WHERE mptype=2 AND mpid=?1")
	UserInfo findByQqid(String mpid);
}