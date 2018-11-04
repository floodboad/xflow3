package org.xsnake.cloud.xflow3.api;

import java.io.Serializable;

/**
 * 
 * @author Jerry.Zhao
 *
 */
public class Participant implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public Participant(){}
	
	public Participant(String id,String name,String type){
		this.id = id;
		this.name = name;
		this.type = type;
	}

	private String id;
	
	private String name;
	
	private String type;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
