DROP TABLE IF EXISTS t_user_info;
CREATE TABLE t_user_info(
id              INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
parentId        INT NOT NULL COMMENT '平台用户所属上一级ID',
username        VARCHAR(16) NOT NULL COMMENT '用户名',
password        VARCHAR(32) NOT NULL COMMENT '登录密码',
uuid            VARCHAR(32) NOT NULL COMMENT '用户唯一标识，用来生成微信Token',
token           VARCHAR(32) COMMENT '微信Token',
wxId            VARCHAR(32) COMMENT '微信原始ID',
wxNo            VARCHAR(32) COMMENT '微信号',
wxName          VARCHAR(32) COMMENT '微信名称',
appId           VARCHAR(32) COMMENT '微信应用ID',
appSecret       VARCHAR(64) COMMENT '微信应用密钥',
appAESKey       VARCHAR(64) COMMENT '微信消息加解密密钥',
appAESStatus    CHAR(1) COMMENT '微信消息加解密方式：0--明文模式，1--兼容模式，2--安全模式',
accessToken     VARCHAR(1024) COMMENT '微信access_token',
accessTokenTime TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00' COMMENT '获取微信access_token的时间',
bindStatus      CHAR(1) COMMENT '微信绑定状态：0--未绑定，1--已绑定',
createTime TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
updateTime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
UNIQUE INDEX unique_index_wxId(wxId)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8 COMMENT='平台用户表';


DROP TABLE IF EXISTS t_fans_info;
CREATE TABLE t_fans_info(
id            INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
uid           INT NOT NULL COMMENT '平台用户ID，对应t_user#id',
wxId          VARCHAR(32) COMMENT '微信原始ID',
openid        VARCHAR(64) NOT NULL COMMENT '粉丝的openid',
name          VARCHAR(16) COMMENT '粉丝的真实姓名',
idCard        VARCHAR(18) COMMENT '粉丝的身份证号',
phoneNo       CHAR(11) COMMENT '粉丝的手机号',
subscribe     CHAR(1) NOT NULL COMMENT '关注状态：0--未关注，其它为已关注',
nickname      VARCHAR(32) COMMENT '粉丝的昵称',
sex           CHAR(1) COMMENT '粉丝的性别：0--未知，1--男，2--女',
city          VARCHAR(32) COMMENT '粉丝所在城市',
country       VARCHAR(32) COMMENT '粉丝所在国家',
province      VARCHAR(32) COMMENT '粉丝所在省份',
language      VARCHAR(32) COMMENT '粉丝的语言，简体中文为zh_CN',
headimgurl    VARCHAR(256) COMMENT '粉丝的头像，简体中文为zh_CN',
subscribeTime VARCHAR(19) COMMENT '粉丝最后一次关注的时间戳',
unionid       VARCHAR(64) COMMENT '只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段',
remark        VARCHAR(64) COMMENT '公众号运营者对粉丝的备注',
groupid       VARCHAR(16) COMMENT '粉丝用户所在的分组ID',
createTime    TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
updateTime    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
UNIQUE INDEX unique_index_uid_openid(uid, openid)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8 COMMENT='粉丝表';


DROP TABLE IF EXISTS t_reply_info;
CREATE TABLE t_reply_info(
id         INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
uid        INT NOT NULL COMMENT '平台用户ID，对应t_user#id',
category   CHAR(1) NOT NULL COMMENT '回复的类别：0--通用的回复，1--关注后回复，2--关键字回复',
type       CHAR(1) NOT NULL COMMENT '回复的类型：0--文本，1--图文，2--图片，3--活动，4--转发到多客服',
keyword    VARCHAR(16) COMMENT '关键字',
content    VARCHAR(2048) COMMENT '回复的内容',
pluginId   INT COMMENT '活动插件ID，对应t_plugin#id',
createTime TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
updateTime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
UNIQUE INDEX unique_index_keyword(keyword)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8 COMMENT='统一回复设置表';


DROP TABLE IF EXISTS t_menu_info;
CREATE TABLE t_menu_info(
id         INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
uid        INT NOT NULL COMMENT '平台用户ID，对应t_user#id',
parentId   INT NOT NULL COMMENT '上一级菜单的ID，一级菜单情况下为0',
level      CHAR(1) NOT NULL COMMENT '菜单级别：0--未知，1--一级菜单，2--二级菜单',
type       CHAR(1) NOT NULL COMMENT '菜单类型：0--占位，1--CLICK，2--VIEW',
name       VARCHAR(16) NOT NULL COMMENT '菜单名称',
viewURL    VARCHAR(256) COMMENT 'type=2时用到',
replyId    INT COMMENT 'type=1时用到，对应t_reply_info#id',
createTime TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
updateTime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
)ENGINE=InnoDB DEFAULT CHARSET=UTF8 COMMENT='自定义菜单表';


INSERT INTO t_reply_info(uid, category, type) VALUES(2, '0', '4');
INSERT INTO t_user_info(id, parentId, username, password, uuid, bindStatus) VALUES(1, 0, 'admin', '38fa7ad72e2b908b9921238eb2601981', REPLACE(UUID(),'-',''), '0');
INSERT INTO t_user_info(id, parentId, username, password, uuid, bindStatus) VALUES(2, 0, 'test', '398f2a4045f3caa202735395d58dc92f', REPLACE(UUID(),'-',''), '0');