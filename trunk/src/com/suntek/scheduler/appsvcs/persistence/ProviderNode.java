package com.suntek.scheduler.appsvcs.persistence;

public class ProviderNode {
	
	private int providerId;
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getProviderId() {
		return providerId;
	}
	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}

	public String toString(){
		return name;
	}
}
