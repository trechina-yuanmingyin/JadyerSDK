package com.jadyer.sdk.demo.mpp.fans;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jadyer.sdk.demo.mpp.fans.model.FansInfo;

public interface FansInfoDao extends JpaRepository<FansInfo, Integer> {}