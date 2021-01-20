package org.javafreak.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Vehicle {
	@Id @GeneratedValue
	private int vehicleId;
	private String vehicleName;
	
	/* 
	This is not required for OneToOne(in UserDetails class) to work, but it helps in certain scenarios
	like when we have a Vehicle object and we need UserDetails for it. 
	In that case if we have this Hibernate will populate UserDetails for us
	This is called bi-directional relationship
	*/
	@OneToOne
	private UserDetails userDetails;
	
	public int getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getVehicleName() {
		return vehicleName;
	}
	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}
	public UserDetails getUserDetails() {
		return userDetails;
	}
	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}
}
