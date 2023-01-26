package com.cricket.dto;

import java.util.Map;
import java.util.TreeMap;

public class CustomPageDto {
	
	private Map<Integer, String> menuMap = new TreeMap<Integer, String>();
	{
		menuMap.put(1, "Players");
		menuMap.put(2, "Matches");
		menuMap.put(3, "Teams");
		menuMap.put(4, "Statistics");
		menuMap.put(5, "Series");
		menuMap.put(6, "Club");
	}
	private int pageId;
	private int pageOrder;
	private int menu;
	private String newMenu;
	private String redirectionUrl;
	private String pageContent;
	private String subMenu;
	private int displayControl;
	private int forAcademy;

	public String getRedirectionUrl() {
		return redirectionUrl;
	}

	public void setRedirectionUrl(String redirectionUrl) {
		this.redirectionUrl = redirectionUrl;
	}

	public int getForAcademy() {
		return forAcademy;
	}

	public void setForAcademy(int forAcademy) {
		this.forAcademy = forAcademy;
	}

	public Map<Integer, String> getMenuMap() {
		return menuMap;
	}

	public void setMenuMap(Map<Integer, String> menuMap) {
		this.menuMap = menuMap;
	}

	public int getDisplayControl() {
		return displayControl;
	}

	public void setDisplayControl(int displayControl) {
		this.displayControl = displayControl;
	}

	public int getPageId() {
		return pageId;
	}

	public void setPageId(int pageId) {
		this.pageId = pageId;
	}

	public int getPageOrder() {
		return pageOrder;
	}

	public void setPageOrder(int pageOrder) {
		this.pageOrder = pageOrder;
	}

	public String getNewMenu() {
		return newMenu;
	}

	public void setNewMenu(String newMenu) {
		this.newMenu = newMenu;
	}

	public String getPageContent() {
		return pageContent;
	}

	public void setPageContent(String pageContent) {
		this.pageContent = pageContent;
	}

	public String getSubMenu() {
		return subMenu;
	}

	public void setSubMenu(String subMenu) {
		this.subMenu = subMenu;
	}

	public int getMenu() {
		return menu;
	}

	public String getMenuDisplay() {
		if (menu > 0 && menu <= 6) {
			return menuMap.get(menu);
		} else if (menu == 9) {
			return this.newMenu;
		}
		return "";
	}

	public void setMenu(int menu) {
		this.menu = menu;
	}

}
