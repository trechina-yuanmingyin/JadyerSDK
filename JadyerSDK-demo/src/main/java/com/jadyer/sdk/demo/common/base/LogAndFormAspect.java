package com.jadyer.sdk.demo.common.base;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jadyer.sdk.demo.common.util.IPUtil;
import com.jadyer.sdk.demo.common.util.LogUtil;

/**
 * 日志记录和表单验证的切面器
 * @see 完整版见https://github.com/jadyer/JadyerEngine/blob/master/JadyerEngine-common/src/main/java/com/jadyer/engine/common/base/LogAndFormAspect.java
 * @create Apr 18, 2015 9:49:12 AM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
@Aspect
@Component
public class LogAndFormAspect {
	@Around("execution(* com.jadyer.sdk.demo..*Controller.*(..))")
	public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
		Object respData = null;
		long startTime = System.currentTimeMillis();
		String className = joinPoint.getTarget().getClass().getSimpleName(); //获取类名(这里只切面了Controller类)
		String methodName = joinPoint.getSignature().getName();              //获取方法名
		String methodInfo = className + "." + methodName;                    //组织类名.方法名
		//Object[] objs = joinPoint.getArgs();                               //获取方法参数
		//String paramInfo = com.alibaba.fastjson.JSON.toJSONString(args);
		/**
		 * 打印Controller入参
		 */
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		LogUtil.getAppLogger().info(methodInfo + "()被调用, 客户端IP=" + IPUtil.getClientIP(request) + ", 入参为" + buildStringFromMapWithStringArray(request.getParameterMap()));
		/**
		 * 执行Controller的方法
		 */
		respData = joinPoint.proceed();
		long endTime = System.currentTimeMillis();
		String returnInfo = null;
		if(null!=respData && respData.getClass().isAssignableFrom(ResponseEntity.class)){
			returnInfo = "ResponseEntity";
		}else{
			returnInfo = JSON.toJSONStringWithDateFormat(respData, JSON.DEFFAULT_DATE_FORMAT, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullBooleanAsFalse);
		}
		LogUtil.getAppLogger().info(methodInfo + "()被调用, 出参为" + returnInfo + ", Duration[" + (endTime-startTime) + "]ms");
		LogUtil.getAppLogger().info("---------------------------------------------------------------------------------------------");
		return respData;
	}


	/**
	 * 获取Map中的属性
	 * @see 该方法的参数适用于打印Map<String, String[]>的情况
	 * @see 由于Map.toString()打印出来的参数值对,是横着一排的...参数多的时候,不便于查看各参数值
	 * @see 故此仿照commons-lang3.jar中的ReflectionToStringBuilder.toString()编写了本方法
	 * @return String key11=value11 \n key22=value22 \n key33=value33 \n......
	 */
	private static String buildStringFromMapWithStringArray(Map<String, String[]> map){
		StringBuilder sb = new StringBuilder();
		sb.append(map.getClass().getName()).append("@").append(map.hashCode()).append("[");
		for(Map.Entry<String,String[]> entry : map.entrySet()){
			sb.append("\n").append(entry.getKey()).append("=").append(Arrays.toString(entry.getValue()));
		}
		return sb.append("\n]").toString();
	}
}