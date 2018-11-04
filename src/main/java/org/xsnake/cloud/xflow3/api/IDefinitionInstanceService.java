package org.xsnake.cloud.xflow3.api;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.xsnake.cloud.common.search.Page;

/**
 * 定义实例的状态有，未发布，已发布。
 * @author Jerry.Zhao
 *
 */
public interface IDefinitionInstanceService {
	
	public final static String STATUS_RELEASE = "RELEASE";
	
	public final static String STATUS_NEW = "NEW";
	
	public final static String STATUS_NOT_RELEASE = "NOT_RELEASE";
	/**
	 * 创建/升级流程定义实例
	 * @param code
	 * @param xml
	 */
	@RequestMapping(value="/definition/add",method=RequestMethod.POST)
	void add(@RequestBody String xml);

	/**
	 * 更新一个未发布的定义实例，如果定义实例一旦被发布，则不允许再被编辑，只能升级
	 * @param code
	 * @param version
	 * @param xml
	 */
	@RequestMapping(value="/definition/edit",method=RequestMethod.POST)
	void edit(@RequestBody DefinitionInstance definitionInstance);
	/**
	 * 发布定义实例未该定义的正式版本，一次只能存在一个正式版本
	 * @param code
	 * @param version
	 */
	@RequestMapping(value="/definition/release",method=RequestMethod.POST)
	void release(@RequestParam(value="code") String code,@RequestParam(value="version") String version);
	
	/**
	 * 获得指定版本的定义实例XML
	 * @param code
	 * @param version
	 * @return
	 */
	@RequestMapping(value="/definition/getXML",method=RequestMethod.GET)
	String getXML(@RequestParam(value="code") String code,@RequestParam(value="version") String version);
	
	/**
	 * 查看定义下所有的定义实例
	 * @param code
	 * @param pageCondition
	 * @return
	 */
	@RequestMapping(value="/definition/query",method=RequestMethod.POST)
	Page<DefinitionInstance> query(@RequestBody DefinitionInstanceCondition definitionInstanceCondition);
	
}
