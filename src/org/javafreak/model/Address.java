package org.javafreak.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/*
	@Embeddable tells hibernate that this object is a value object and will be embedded in some other entity
*/

@Embeddable
public class Address {
	@Column(name="street_name")
	private String street;
	@Column(name="city_name")
	private String city;
	@Column(name="state_name")
	private String state;
	@Column(name="pin_code")
	private String pin;
	
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	@Override
	public String toString() {
		return "Address [street=" + street + ", city=" + city + ", state=" + state + ", pin=" + pin + "]";
	}
	
}
