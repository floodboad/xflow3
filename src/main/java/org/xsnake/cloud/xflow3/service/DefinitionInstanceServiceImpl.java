package org.xsnake.cloud.xflow3.service;

import java.util.Date;
import java.util.UUID;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.xsnake.cloud.common.search.Page;
import org.xsnake.cloud.dao.DaoUtil;
import org.xsnake.cloud.xflow3.api.DefinitionInstance;
import org.xsnake.cloud.xflow3.api.DefinitionInstanceCondition;
import org.xsnake.cloud.xflow3.api.IDefinitionInstanceService;
import org.xsnake.cloud.xflow3.api.exception.XflowBusinessException;
import org.xsnake.cloud.xflow3.api.exception.XflowDefinitionException;
import org.xsnake.cloud.xflow3.core.ProcessDefinition;
import org.xsnake.cloud.xflow3.core.context.ApplicationContext;

@Service
@RestController
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class DefinitionInstanceServiceImpl implements IDefinitionInstanceService{
	
	@Autowired
	DaoUtil daoUtil;

	@Autowired
	ApplicationContext applicationContext;
	
	@Override
	public void add(@RequestBody String xml) {
		if(StringUtils.isEmpty(xml)){
			throw new IllegalArgumentException("流程定义XML内容不能为空");
		}
		String name = null;
		String code = null;
		try {
			ProcessDefinition pd = ProcessDefinition.parse(applicationContext,xml);
			name = pd.getName();
			code = pd.getCode();
		} catch (DocumentException e) {
			throw new XflowDefinitionException("流程定义XML有错误：" + e.getMessage());
		}
		
		//生成最新版本的版本号码
		String version = UUID.randomUUID().toString();
		
		DefinitionInstance definitionInstance = new DefinitionInstance();
		definitionInstance.setCode(code);
		definitionInstance.setName(name);
		definitionInstance.setVersion(version);
		definitionInstance.setRemark(null);
		definitionInstance.setStatus(IDefinitionInstanceService.STATUS_NEW);
		definitionInstance.setCreateDate(new Date());
		definitionInstance.setLastUpdateDate(new Date());
		definitionInstance.setXml(xml);
		daoUtil.$update("DEFINITION_INSTANCE_ADD.sql", definitionInstance);
	}

	public void edit(@RequestBody DefinitionInstance definitionInstance) {
		Assert.notNull(definitionInstance);
		
		if(StringUtils.isEmpty(definitionInstance.getCode())){
			throw new IllegalArgumentException("流程定义代码不能为空");
		}
		if(StringUtils.isEmpty(definitionInstance.getXml())){
			throw new IllegalArgumentException("流程定义XML内容不能为空");
		}
		try {
			ProcessDefinition.parse(applicationContext,definitionInstance.getXml());
		} catch (DocumentException e) {
			throw new XflowDefinitionException("流程定义XML有错误：" + e.getMessage());
		}
		//判断状态是否已经发布
		DefinitionInstance _definitionInstance = daoUtil.$queryObject("DEFINITION_INSTANCE_GET.sql", definitionInstance,DefinitionInstance.class);
		
		if(_definitionInstance == null){
			throw new XflowDefinitionException("没有找到要更新的数据");
		}
		
		if(STATUS_RELEASE.equals(_definitionInstance.getStatus())){
			throw new XflowDefinitionException("流程定义实例已经被发布，不能再进行修改操作");
		}
		definitionInstance.setLastUpdateDate(new Date());
		daoUtil.$update("DEFINITION_INSTANCE_EDIT.sql", definitionInstance);
	}

	@Override
	public void release(String code, String version) {
		if(StringUtils.isEmpty(code)){
			throw new IllegalArgumentException("流程定义代码不能为空");
		}
		//将所有该版本的流程重置为未发布状态
		daoUtil.$update("DEFINITION_INSTANCE_RELEASE_1.sql", daoUtil.createMap("code", code));
		//把要设置的流程设置为发布
		int result = daoUtil.$update("DEFINITION_INSTANCE_RELEASE_2.sql", daoUtil.createMap("code", code).put("version", version));
		if(result == 0){
			throw new XflowBusinessException("没有找到要发布的流程定义");
		}
	}

	@Override
	public String getXML(String code, String version) {
		if(StringUtils.isEmpty(code)){
			throw new IllegalArgumentException("流程定义代码不能为空");
		}
		return daoUtil.$queryObject("DEFINITION_INSTANCE_GET_XML.sql", 
				daoUtil.createMap("code", code)
			       .put("version", version),String.class);
	}

	@Override
	public Page<DefinitionInstance> query(@RequestBody DefinitionInstanceCondition definitionInstanceCondition) {
		Assert.notNull(definitionInstanceCondition);
		if(StringUtils.isEmpty(definitionInstanceCondition.getCode())){
			throw new IllegalArgumentException("流程定义代码不能为空");
		}
		return daoUtil.$queryPage("DEFINITION_INSTANCE_SEARCH.sql", definitionInstanceCondition, definitionInstanceCondition.getPage(), definitionInstanceCondition.getRows(),DefinitionInstance.class);
	}

}
