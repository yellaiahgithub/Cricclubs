/*
 * Created on Mar 14, 2010
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cricket.dto;

import java.io.Serializable;

import com.cricket.utility.CommonUtility;

/**
 * @author ganesh
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GroundDto implements Serializable {

	private static final long serialVersionUID = -5392943945844015089L;
	
	private int groundId;
	private String name;
	private String shortName;
	private String address;
	private String information;
	private String mapLink ="";
	private String email;
	private int showGroundAvailability;
	private String sourceSite;
	private int sourceId;
	private String imagePaths;
	
	public int getShowGroundAvailability() {
		return showGroundAvailability;
	}
	public void setShowGroundAvailability(int showGroundAvailability) {
		this.showGroundAvailability = showGroundAvailability;
	}
	public int getGroundId() {
		return groundId;
	}
	public void setGroundId(int groundId) {
		this.groundId = groundId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortName() {
		return CommonUtility.isNullOrEmpty(shortName)?name:shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getInformation() {
		return information;
	}
	public void setInformation(String information) {
		this.information = information;
	}
	public String getMapLink() {
		return mapLink;
	}
	public void setMapLink(String mapLink) {
		this.mapLink = mapLink;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}	
	@Override
    public boolean equals(Object obj) {
		GroundDto ground = (GroundDto) obj;
        return this.groundId == ground.getGroundId() && this.hashCode() == ground.hashCode();
    }
	public String getSourceSite() {
		return sourceSite;
	}
	public void setSourceSite(String sourceSite) {
		this.sourceSite = sourceSite;
	}
	public int getSourceId() {
		return sourceId;
	}
	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}
	public String getImagePaths() {
		return imagePaths;
	}
	public void setImagePaths(String imagePaths) {
		this.imagePaths = imagePaths;
	}
}
