package com.jadyer.sdk.mpp.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jadyer.sdk.mpp.msg.InMsgParser;
import com.jadyer.sdk.mpp.msg.OutMsgXmlBuilder;
import com.jadyer.sdk.mpp.msg.in.InImageMsg;
import com.jadyer.sdk.mpp.msg.in.InLinkMsg;
import com.jadyer.sdk.mpp.msg.in.InLocationMsg;
import com.jadyer.sdk.mpp.msg.in.InMsg;
import com.jadyer.sdk.mpp.msg.in.InTextMsg;
import com.jadyer.sdk.mpp.msg.in.event.InCustomServiceEventMsg;
import com.jadyer.sdk.mpp.msg.in.event.InFollowEventMsg;
import com.jadyer.sdk.mpp.msg.in.event.InMenuEventMsg;
import com.jadyer.sdk.mpp.msg.out.OutMsg;

/**
 * 接收微信服务器消息,自动解析成com.jadyer.sdk.mpp.msg.in.InMsg
 * 并分发到相应的处理方法,得到处理后的com.jadyer.sdk.mpp.msg.out.OutMsg并回复给微信服务器
 * @create Oct 18, 2015 12:37:47 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public abstract class WeixinMsgController {
	private static final Logger logger = LoggerFactory.getLogger(WeixinMsgController.class);

	@RequestMapping(value="/{token}")
	public void index(@PathVariable String token, HttpServletRequest request, HttpServletResponse response) throws IOException {
		//验签
		token = DigestUtils.md5Hex(token + "http://blog.csdn.net/jadyer" + token);
		if(!this.verifySignature(token, request)){
			PrintWriter out = response.getWriter();
			out.write("verify signature failed");
			out.flush();
			out.close();
			return;
		}
		//GET过来的请求表示更新开发者服务器URL
		if("GET".equalsIgnoreCase(request.getMethod())){
			PrintWriter out = response.getWriter();
			out.write(request.getParameter("echostr"));
			out.flush();
			out.close();
			return;
		}
		//POST过来的请求表示微信服务器请求通信
		InMsg inMsg = this.getInMsg(request);
		OutMsg outMsg = new OutMsg();
		if(inMsg instanceof InTextMsg){
			outMsg = this.processInTextMsg((InTextMsg)inMsg);
		}
		if(inMsg instanceof InImageMsg){
			outMsg = this.processInImageMsg((InImageMsg)inMsg);
		}
		if(inMsg instanceof InLocationMsg){
			outMsg = this.processInLocationMsg((InLocationMsg)inMsg);
		}
		if(inMsg instanceof InLinkMsg){
			outMsg = this.processInLinkMsg((InLinkMsg)inMsg);
		}
		if(inMsg instanceof InFollowEventMsg){
			outMsg = this.processInFollowEventMsg((InFollowEventMsg)inMsg);
		}
		if(inMsg instanceof InMenuEventMsg){
			outMsg = this.processInMenuEventMsg((InMenuEventMsg)inMsg);
		}
		if(inMsg instanceof InCustomServiceEventMsg){
			outMsg = this.processInCustomServiceEventMsg((InCustomServiceEventMsg)inMsg);
		}
		String outMsgXml = OutMsgXmlBuilder.build(outMsg);
		PrintWriter out = response.getWriter();
		out.write(outMsgXml);
		out.flush();
		out.close();
		logger.info("应答给微信服务器消息为{}", outMsgXml);
		return;
	}


	/**
	 * 验签
	 * @create Oct 18, 2015 1:16:24 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	private boolean verifySignature(String token, HttpServletRequest request){
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		if(StringUtils.isBlank(signature) || StringUtils.isBlank(timestamp) || StringUtils.isBlank(nonce)){
			return false;
		}
		String[] signPlains = new String[]{token, nonce, timestamp};
		Arrays.sort(signPlains);
		if(!signature.equals(DigestUtils.sha1Hex(signPlains[0]+signPlains[1]+signPlains[2]))){
			return false;
		}
		return true;
	}


	/**
	 * 解析微信服务器请求过来的xml报文为InMsg对象
	 * @create Oct 18, 2015 3:38:57 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	private InMsg getInMsg(HttpServletRequest request){
		String inMsgXml = null;
		BufferedReader br = null;
		try{
			StringBuilder sb = new StringBuilder();
			br = request.getReader();
			for(String line=null; (line=br.readLine())!=null;){
				sb.append(line).append("\n");
			}
			inMsgXml = sb.toString();
		}catch(IOException e){
			throw new RuntimeException(e);
		}finally {
			if(null != br){
				IOUtils.closeQuietly(br);
			}
		}
		//解密消息
		//String aeskey = "WhyEzMHrSKfV5HbsVf5DjZfV3yx7bsJ7TUivKdeeH22";
		//decrypt(aeskey);
		logger.info("收到微信服务器消息为{}", inMsgXml);
		return InMsgParser.parse(inMsgXml);
	}


	/**
	 * 处理收到的文本消息
	 */
	protected abstract OutMsg processInTextMsg(InTextMsg inTextMsg);


	/**
	 * 处理收到的图片消息
	 */
	protected abstract OutMsg processInImageMsg(InImageMsg inImageMsg);


	/**
	 * 处理收到的地址位置消息
	 */
	protected abstract OutMsg processInLocationMsg(InLocationMsg inLocationMsg);


	/**
	 * 处理收到的链接消息
	 */
	protected abstract OutMsg processInLinkMsg(InLinkMsg inLinkMsg);


	/**
	 * 处理收到的关注/取消关注事件
	 */
	protected abstract OutMsg processInFollowEventMsg(InFollowEventMsg inFollowEventMsg);


	/**
	 * 处理自定义菜单拉取消息/跳转链接的事件
	 * @see 经测试,对于VIEW类型的URL跳转类,不会推到开发者服务器而是直接跳过去
	 */
	protected abstract OutMsg processInMenuEventMsg(InMenuEventMsg inMenuEventMsg);


	/**
	 * 处理多客服接入会话/关闭会话/转接会话的事件
	 */
	protected abstract OutMsg processInCustomServiceEventMsg(InCustomServiceEventMsg inCustomServiceEventMsg);
}