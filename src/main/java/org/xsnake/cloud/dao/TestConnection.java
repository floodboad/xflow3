package org.xsnake.cloud.dao;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author Jerry.Zhao
 * 启动时测试数据库是否连接正常
 */

@Configuration
@RefreshScope
public class TestConnection implements InitializingBean {

	private static final Log logger = LogFactory.getLog(TestConnection.class);
	
	@Autowired
	DaoUtil daoUtil;
	
	@Value(value = "${jdbc.testSQL}")
	String testSQL;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		try{
			if(!StringUtils.isEmpty(testSQL)){
				logger.info("开始测试数据库连接");
				daoUtil.queryObject(testSQL, new HashMap<>(),String.class);
				logger.info("测试数据库连接正常");
			}else{
				logger.info("无测试SQL执行");
			}
		}catch (Exception e) {
			logger.error("测试数据库连接异常" + e.getMessage());
			throw e;
		}
	}

}
