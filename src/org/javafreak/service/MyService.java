package org.javafreak.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.hibernate.Session;
import org.javafreak.dbUtil.DBUtil;
import org.javafreak.model.Address;
import org.javafreak.model.Bike;
import org.javafreak.model.Car;
import org.javafreak.model.Movie;
import org.javafreak.model.UserDetails;
import org.javafreak.model.Vehicle;

public class MyService {
	
	public UserDetails createDummyObject(int count) {
		Address presentAddress=new Address();
		presentAddress.setStreet("Present Street "+count);
		presentAddress.setCity("Present City "+count);
		presentAddress.setState("Present State "+count);
		presentAddress.setPin("Present Pin "+count);
		
		Address permanentAddress=new Address();
		permanentAddress.setStreet("Permanent Street "+count);
		permanentAddress.setCity("Permanent City "+count);
		permanentAddress.setState("Permanent State "+count);
		permanentAddress.setPin("Permanent Pin "+count);
		
		Address previousAddress1=new Address();
		previousAddress1.setStreet("Present Street "+count+ "-1");
		previousAddress1.setCity("Present City "+count+ "-1");
		previousAddress1.setState("Present State "+count+ "-1");
		previousAddress1.setPin("Present Pin "+count+ "-1");		
		Address previousAddress2=new Address();
		previousAddress2.setStreet("Permanent Street "+count+ "-2");
		previousAddress2.setCity("Permanent City "+count+ "-2");
		previousAddress2.setState("Permanent State "+count+ "-2");
		previousAddress2.setPin("Permanent Pin "+count+ "-2");
		Collection<Address> previousAddress=new HashSet<Address>();
		previousAddress.add(previousAddress1);
		previousAddress.add(previousAddress2);
		
		Vehicle favouriteVehicle= new Vehicle();
		favouriteVehicle.setVehicleName("My Vehicle "+count);
		
		Car car1=new Car();
		car1.setCarName("My Car "+count+ "-1");
		Car car2=new Car();
		car2.setCarName("My Car "+count+ "-2");
		Collection<Car> ownedCars=new ArrayList<Car>();
		ownedCars.add(car1);
		ownedCars.add(car2);
		
		Bike bike1=new Bike();
		bike1.setBikeName("My Bike "+count+ "-1");
		Bike bike2=new Bike();
		bike2.setBikeName("My Bike "+count+ "-2");
		Collection<Bike> ownedBikes=new ArrayList<Bike>();
		ownedBikes.add(bike1);
		ownedBikes.add(bike2);
		
		Movie movie1=new Movie();
		movie1.setMovieName("My Movie "+count+ "-1");
		Movie movie2=new Movie();
		movie2.setMovieName("My Movie "+count+ "-2");
		Collection<Movie> movieList=new ArrayList<Movie>();
		movieList.add(movie1);
		movieList.add(movie2);
		
		UserDetails ud1=new UserDetails();
		ud1.setUserId("Vinod"+count);
		ud1.setUserName("Vinod Singh "+count);
		ud1.setUserAge(30+count);
		ud1.setJoinedDate(new Date());
		ud1.setPresentAddress(presentAddress);
		ud1.setPermanentAddress(permanentAddress);
		ud1.setPreviousAddress(previousAddress);
		ud1.setUserNotes("User Notes "+count);
		ud1.setUserImage(new byte[5]);
		ud1.setFavouriteVehicle(favouriteVehicle);
		ud1.setOwnedCars(ownedCars);
		ud1.setOwnedBikes(ownedBikes);
		ud1.setMovieList(movieList);
		
		// Bi-Directional relationships, Optional
		favouriteVehicle.setUserDetails(ud1);
		car1.setUserDetails(ud1);
		car2.setUserDetails(ud1);
		bike1.setUserDetails(ud1);
		bike2.setUserDetails(ud1);
		movie1.setUserList(new ArrayList<UserDetails>(Arrays.asList(ud1)));
		movie2.setUserList(new ArrayList<UserDetails>(Arrays.asList(ud1)));
		
		return ud1;
	}

	public String saveUserDetails(UserDetails userDetails) {
		Session session=DBUtil.createSessionAndOpenTransaction();
		
		/*
		session.save(userDetails); In order to use cascade change it to session.persist
		session.save(userDetails.getFavouriteVehicle()); Done by Cascade
		*/
		session.persist(userDetails);
		
		for(Car car:userDetails.getOwnedCars())
			session.save(car);
		for(Bike bike:userDetails.getOwnedBikes())
			session.save(bike);
		for(Movie movie:userDetails.getMovieList())
			session.save(movie);

		DBUtil.commitCloseTransactionCloseSession(session);
		return "Save Successful";
	}
	public UserDetails getUserDetails() {
		Session session=DBUtil.createSessionAndOpenTransaction();
		
		UserDetails user=session.get(UserDetails.class, 1);

		DBUtil.commitCloseTransactionCloseSession(session);
		return user;
	}

}
