package org.xsnake.cloud.xflow3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.xsnake.cloud.common.search.BaseCondition;
import org.xsnake.cloud.common.search.Page;
import org.xsnake.cloud.dao.DaoUtil;
import org.xsnake.cloud.xflow3.api.IProcessService;
import org.xsnake.cloud.xflow3.api.Process;

@Service
@RestController
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class ProcessServiceImpl implements IProcessService {

	@Autowired
	DaoUtil daoUtil;
	
	@Override
	public Page<Process> query(BaseCondition condition) {
		return daoUtil.$queryPage("PROCESS_QUERY.sql", condition, condition.getPage(), condition.getRows(),Process.class);
	}

}
