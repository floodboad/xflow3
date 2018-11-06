package org.xsnake.cloud.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 数据源配置类，数据源的来源为配置中心
 * @author Jerry.Zhao
 *
 */
@Configuration
public class DataSourceConfiguration {

	@Value(value = "${jdbc.url}")
	String url;

	@Value(value = "${jdbc.driver}")
	String driver;
	
	@Value(value = "${jdbc.username}")
	String username;
	
	@Value(value = "${jdbc.password}")
	String password;
	
	@Bean
	public DataSource dataSource(){
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(url);
		dataSource.setDriverClassName(driver);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setMinIdle(10);
		dataSource.setMaxActive(20);
		return dataSource;
	}
	
}
