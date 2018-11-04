package org.xsnake.cloud.dao;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
/**
 * 
 * @author Jerry.Zhao
 * Oracle数据库方言
 *
 */
@Configuration
@ConditionalOnClass(name = "oracle.jdbc.driver.OracleDriver")
public class OracleDialect implements Dialect{

	@Override
	public String getPageSql(String sql, int start, int end) {
        StringBuffer oracleSql = new StringBuffer();
        oracleSql.append("SELECT * FROM  ( SELECT A.*, ROWNUM RN FROM ( ")
                 .append(sql)
                 .append(" ) A WHERE ROWNUM <= ")
                 .append(end)
                 .append(" ) WHERE RN > ")
                 .append(start);
        return oracleSql.toString();
	}

	@Override
	public String getTestSql() {
		return " select '1' from dual";
	}
	
}
