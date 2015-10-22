package com.jadyer.sdk.demo.mpp.menu;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jadyer.sdk.demo.mpp.menu.model.MenuInfo;

public interface MenuInfoDao extends JpaRepository<MenuInfo, Integer> {}