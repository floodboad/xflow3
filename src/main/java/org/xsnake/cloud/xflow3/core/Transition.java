package org.xsnake.cloud.xflow3.core;

import java.io.Serializable;

import org.dom4j.Element;
import org.springframework.util.StringUtils;
import org.xsnake.cloud.xflow3.api.exception.XflowDefinitionException;

public class Transition implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public Transition(Element transitionElement){
		id = transitionElement.attributeValue("id");
		name = transitionElement.attributeValue("name");
		sourceId = transitionElement.attributeValue("sourceId");
		targetId = transitionElement.attributeValue("targetId");
		
		if(StringUtils.isEmpty(id)){
			throw new XflowDefinitionException("流转定义错误：id不能为空");
		}
		if(StringUtils.isEmpty(name)){
			throw new XflowDefinitionException("流转定义错误：name不能为空");
		}
		if(StringUtils.isEmpty(sourceId)){
			throw new XflowDefinitionException("流转定义错误：sourceId不能为空");
		}
		if(StringUtils.isEmpty(targetId)){
			throw new XflowDefinitionException("流转定义错误：targetId不能为空");
		}
	}
	
	private String id;

	private String name;
	
	private String sourceId;
	
	private String targetId;
	
	Activity sourceActivity;
	
	Activity targetActivity;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

}
