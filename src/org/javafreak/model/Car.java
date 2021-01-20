package org.javafreak.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Car {
	@Id @GeneratedValue
	private int carId;
	private String carName;
	
	/* 
	This is not required for OneToMany(in UserDetails class) to work, but it helps in certain scenarios
	like when we have a Car object and we need UserDetails for it. 
	In that case if we have this Hibernate will populate UserDetails for us
	This is called bi-directional relationship(ManyToOne in this case)
	*/
	@ManyToOne
	private UserDetails userDetails;
	
	public int getCarId() {
		return carId;
	}
	public void setCarId(int carId) {
		this.carId = carId;
	}
	public String getCarName() {
		return carName;
	}
	public void setCarName(String carName) {
		this.carName = carName;
	}
	public UserDetails getUserDetails() {
		return userDetails;
	}
	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}
}
