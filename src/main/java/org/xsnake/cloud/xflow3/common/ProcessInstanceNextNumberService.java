package org.xsnake.cloud.xflow3.common;

import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xsnake.cloud.dao.DaoTemplate;
import org.xsnake.cloud.dao.DaoTemplate.InProcedureParam;
import org.xsnake.cloud.dao.DaoTemplate.OutProcedureParam;
import org.xsnake.cloud.dao.DaoTemplate.ProcedureParam;

@Component
public class ProcessInstanceNextNumberService {

	@Autowired
	DaoTemplate daoTemplate;
	
	private String nextNumber(String type, String prefix, int len, String pk) {
		String sql = "DECLARE  l_retval varchar2(30);BEGIN  l_retval := cux_fnd_doc_seq_util_pkg.next_seq_number(:1,:2,:3,:4 );  :5 := l_retval;END;";
		List<Object> result = daoTemplate.executeCall(sql,
				 new ProcedureParam[]{new InProcedureParam(type), new InProcedureParam(prefix) ,
						 new InProcedureParam(len) , new InProcedureParam(pk),new OutProcedureParam(Types.VARCHAR)});
		String nextNumber = (String) result.get(4);
		return nextNumber;
	}

	
	public Long nextNumber(String processInstanceId) {
		String number = nextNumber(processInstanceId,null,3,null);
		return Long.valueOf(number);
	}
}
