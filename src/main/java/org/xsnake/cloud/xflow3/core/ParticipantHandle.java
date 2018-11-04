package org.xsnake.cloud.xflow3.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.xsnake.cloud.xflow3.api.Participant;
import org.xsnake.cloud.xflow3.core.context.ProcessInstanceContext;

public abstract class ParticipantHandle {

	protected Map<String,String> attributes = new HashMap<String,String>();
	
	protected ParticipantActivity activity;
	
	public static enum Type {
		
		assign("assign"), 
		
		creator("creator"),
		
		runtimeAssign("runtimeAssign"),
		
		expand("expand"),
		
		sql("sql"),
		
		formVariable("formVariable");
		
		String type;

		private Type(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}
		
	}
	
	public ParticipantHandle(Element participantElement){
		parseAttributes(participantElement);
	}
	
	private void parseAttributes(final Element participantElement) {
		Element propsElement = participantElement.element(DefinitionConstant.ELEMENT_ATTRIBUTES);
		if(propsElement == null){
			return;
		}
		@SuppressWarnings("unchecked")
		List<Element> propertyList = propsElement.elements(DefinitionConstant.ELEMENT_ATTRIBUTES_ATTRIBUTE);
		if(propertyList == null){
			return;
		}
		for(Element propertyElement : propertyList){
			attributes.put(propertyElement.attributeValue(DefinitionConstant.ELEMENT_ATTRIBUTES_ATTRIBUTE_KEY), propertyElement.getText());
		}
	}
	
	protected abstract List<Participant> findParticipantList(ProcessInstanceContext context);
	
	public final List<Participant> _findParticipantList(ProcessInstanceContext context){
		List<Participant> list = null;
		try{
			list = findParticipantList(context);
		}catch (Exception e) {
			//TODO 记录出错的参与者处理器，可以让管理员尽快解决错误。同时把这个任务转交到默认管理员处理
		}
		
		if(list == null || list.isEmpty() ){
			//如果没有任何参与者，这里可以读取配置使用一个默认管理员处理没有找到参与者的环节
		}
		return list;
	}

}
