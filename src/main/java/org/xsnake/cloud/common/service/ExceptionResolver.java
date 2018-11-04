package org.xsnake.cloud.common.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @author Jerry.Zhao
 * 异常消息编码
 * 将异常封装为{status='error',message='错误原因'}的格式
 *
 */
public class ExceptionResolver extends SimpleMappingExceptionResolver {

	@Override
    public ModelAndView doResolveException(HttpServletRequest request,  
            HttpServletResponse response, Object handler, Exception ex) {
		
		Map<String,Object> result = new HashMap<>();
		result.put("status", "error");
		result.put("message", ex.getMessage());
		
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.print(JSON.toJSONString(result));
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(writer !=null){
				writer.close();
			}
		}
		
		return null;
	}

}

