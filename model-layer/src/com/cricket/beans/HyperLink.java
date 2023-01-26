package com.cricket.beans;


public class HyperLink {
	private String id;
	private String label;
	private String href;
	private String alertText;
	private boolean disabled;
	
	public HyperLink(String label,String href,boolean disabled){
		this.label = label;
		this.href = href;
		this.disabled = disabled;
		this.id = label.replaceAll("\\s","");
	}
	public HyperLink(){
		super();
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getAlertText() {
		return alertText;
	}
	public void setAlertText(String alertText) {
		this.alertText = alertText;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
