package com.jadyer.sdk.demo.mpp.reply;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor=Exception.class)
public class ReplyService {
//	@Resource
//	private ReplyInfoDao replyInfoDao;
}