package org.xsnake.cloud.common.service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;

/**
 * 
 * @author Jerry.Zhao
 * 将正常返回的参数编码为{status='error',message='返回值'}的形式
 * 
 */
public class JsonMessageConverter extends FastJsonHttpMessageConverter4 {
	@Override
	protected void writeInternal(Object obj, Type type, HttpOutputMessage outputMessage) 
			throws IOException, HttpMessageNotWritableException {
		Map<String,Object> r = new HashMap<String,Object>();
		r.put("status", "success");
		r.put("message", obj);
		super.writeInternal(r, type, outputMessage);
	}
	
}
