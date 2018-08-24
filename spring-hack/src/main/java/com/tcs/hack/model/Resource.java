package com.tcs.hack.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Resource {
	@Id
	private int resourceId;
	private String resourceName;
	public Resource() {
    }
	public Resource(int resourceId) {
		super();
		this.resourceId = resourceId;
	}
	public Resource(int resourceId, String resourceName) {
		super();
		this.resourceId = resourceId;
		this.resourceName = resourceName;
	}
	
	public int getResourceId() {
		return resourceId;
	}
	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	
}
