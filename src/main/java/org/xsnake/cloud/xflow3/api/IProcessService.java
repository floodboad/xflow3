package org.xsnake.cloud.xflow3.api;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xsnake.cloud.common.search.BaseCondition;
import org.xsnake.cloud.common.search.Page;

public interface IProcessService {
	
	@RequestMapping(value="/process/query",method=RequestMethod.GET)
	Page<Process> query(@RequestBody BaseCondition condition);
	
}
