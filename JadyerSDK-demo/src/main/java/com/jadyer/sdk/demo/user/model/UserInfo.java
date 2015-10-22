package com.jadyer.sdk.demo.user.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name="t_user_info")
public class UserInfo {
	@Id
	@GeneratedValue
	private int id;
	private int parentId;
	private String username;
	private String password;
	private String uuid;
	private String token;
	private String wxId;
	private String wxNo;
	private String wxName;
	private String appId;
	private String appSecret;
	private String appAESKey;
	private String appAESStatus;
	private String accessToken;
	private Date accessTokenTime;
	private String bindStatus;
	@Basic(fetch=FetchType.LAZY)
	private Date createTime = new Date();
	@Basic(fetch=FetchType.LAZY)
	private Date updateTime;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getWxId() {
		return wxId;
	}
	public void setWxId(String wxId) {
		this.wxId = wxId;
	}
	public String getWxNo() {
		return wxNo;
	}
	public void setWxNo(String wxNo) {
		this.wxNo = wxNo;
	}
	public String getWxName() {
		return wxName;
	}
	public void setWxName(String wxName) {
		this.wxName = wxName;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	public String getAppAESKey() {
		return appAESKey;
	}
	public void setAppAESKey(String appAESKey) {
		this.appAESKey = appAESKey;
	}
	public String getAppAESStatus() {
		return appAESStatus;
	}
	public void setAppAESStatus(String appAESStatus) {
		this.appAESStatus = appAESStatus;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public Date getAccessTokenTime() {
		return accessTokenTime;
	}
	public void setAccessTokenTime(Date accessTokenTime) {
		this.accessTokenTime = accessTokenTime;
	}
	public String getBindStatus() {
		return bindStatus;
	}
	public void setBindStatus(String bindStatus) {
		this.bindStatus = bindStatus;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}