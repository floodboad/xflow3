package org.xsnake.cloud.dao;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @author Jerry.Zhao
 * MySql数据库方言
 *
 */
@Configuration
@ConditionalOnClass(name = "com.mysql.jdbc.Driver")
public class MysqlDialect implements Dialect {

	@Override
	public String getPageSql(String sql, int start, int end) {
		StringBuffer mySql = new StringBuffer();
		mySql.append("SELECT * FROM  (  ").append(sql).append(" ) _A LIMIT ").append(start).append(" , ").append(end);
		return mySql.toString();
	}

	@Override
	public String getTestSql() {
		return "select '1' ";
	}

}
