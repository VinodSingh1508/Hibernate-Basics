package org.javafreak.model;

import java.util.Collection;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;

/*
Hibernate will not automatically save static or Transient property as
	static will have same value accross all instances hence its pointless to persist
	transient means do not persist
Embedded is used for value objects whereas OneToOne, OneToMany & ManyToMany are used for independent objects.
Make sure to save all referenced Entity class explicetly. In our case classes like Car are also entity which
	is referenced from UserDetails, so while saving UserDetails we need to explicetly save Car objects as well.
	If we don't want to do the save of referenced objects explicetly and want hibernate to do it implicetly for us 
	then we can use cascade=CascadeType.PERSIST
	Can be used with OneToOne, OneToMany & ManyToMany
	Also in order to use cascade we need to use session.persist and not session.save
	
Annotations
	@Entity - To mark it as entity, if name not provided then entityName = className
		If @Table not provided then entityName = className = tableName
	@Table - to provide table name
	
	@Id - to mark a property as the primary key of the table
	@GeneratedValue - Hibernate creates a surrogate key for us to use as primary key
		By default hibernate uses SEQUENCE "select nextval ('hibernate_sequence')" to generate unique id
		strategy=AUTO means hibernate will decide which strategy to use depending on the DB (mostly its SEQUENCE), 
			this is the default if no strategy is provided
		strategy=IDENTITY means it uses identity columns to comeup with a unique id
		strategy=SEQUENCE means it uses the sequence object of db to comeup with a unique id
		strategy=TABLE means hibernate will create a table and will use it to maintain and provide  unique ids
		
	@Column - to provide column name
	
	@Basic - to tell hibernate to treat the property as column and persist it
		applied over property name and is the default behaviour, 
		we dont necessarily have to provide it unless we want to override some property
		
	@Temporal - to configure date/time column
		If @Temporal not provided then default is timestamp without time zone
		
	@Lob - to mark a property as large object
		Hibernate will create the column of type CLOB(character large object) / BLOB(binary large object) depending on java data type
		For String/char[] CLOB, text in postgres
		For byte[] BLOB, oid in postgres
		
	@Embedded to tell hibernate to embed the property of the class in the current table
		not mandatory, will be applicable by default
	@AttributeOverrides to group @AttributeOverride
	@AttributeOverride to override the default properties
		we need it because we cant have 2 street_name columns(one for present add and other for permanent) in the same table
		We could have left one of the address with its default, but overridden just for consistency
	
	@EmbeddedId - to create a primary key out of embedded object
		Combination of @Id and @Embedded
		@AttributeOverrides can be used with this
	
	@ElementCollection to persist a collection
		fetch type Lazy is the default
		Hibernate uses proxy class concept to enable lazy fetching
		Check the video at below path for explanation on proxy class
		D:\Studies\Hibernate\1\14.Hibernate Tutorial 12 - Proxy Objects and Eager and Lazy Fetch Types.mp4
	@JoinTable to provide referenced table details
		not mandatory, if not provided defaults will be used
	@JoinColumn to provide foreign key column name
	@GenericGenerator - to define hibernate specific unique id generator
	@CollectionId to provide primary key to the referenced table
	
	@OneToOne - to create one to one relation between tables
		Relationship between tables(UserDetails & Vehicle) will be created by a foreign key column in UserDetails table
	@OneToMany - to create one to many mapping relation between tables
		Relationship between tables(UserDetails & Car) will be created by a mapping table
		Note: Instead of having a mapping table to achieve OneToMany, we can also go other way round and have reference of
		UserDetails table in Car table to achieve the same result (Check Bike class for example)
	@ManyToMany - to create many to many relationship using mapping table
		We should use mappedBy in one of the 2 ManyToMany annotations otherwise Hibernate will create 2 mapping tables
	@NotFound(action=NotFoundAction.IGNORE) - to tell hibernate to ignore the mapping if mapped data is not found
		Can be used with OneToOne, OneToMany & ManyToMany
	cascade=CascadeType.PERSIST - to tell hibernate to persist referenced objects implecitely while saving the main object
		Can be used with OneToOne, OneToMany & ManyToMany
*/

@Entity(name="User_Details")
@Table(name="UserDetails")
public class UserDetails {
	//@EmbeddedId
	//private ComplexID complexID;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private int serialID;
	@Column(name="user_id")
	private String userId;
	@Column(name="user_name")
	private String userName;
	@Column(name="user_age")
	private int userAge;
	@Column(name="join_date")
	@Temporal(TemporalType.DATE)
	private Date joinedDate;
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "street", column = @Column(name="present_street")),
		@AttributeOverride(name = "city", column = @Column(name="present_city")),
		@AttributeOverride(name = "state", column = @Column(name="present_state")),
		@AttributeOverride(name = "pin", column = @Column(name="present_pin"))
	})
	private Address presentAddress;
	
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "street", column = @Column(name="permanent_street")),
		@AttributeOverride(name = "city", column = @Column(name="permanent_city")),
		@AttributeOverride(name = "state", column = @Column(name="permanent_state")),
		@AttributeOverride(name = "pin", column = @Column(name="permanent_pin"))
	})
	private Address permanentAddress;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@JoinTable(name="UserAddress",
		joinColumns=@JoinColumn(name="user_serialID")
	)
	@GenericGenerator(name="increment-gen", strategy="increment")
	@CollectionId(columns = { @Column(name="Address_Id") }, generator = "increment-gen", type = @Type(type = "long"))
	private Collection<Address> previousAddress;
	
	@OneToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="favouriteVehicle")
	private Vehicle favouriteVehicle;
	
	@OneToMany
	@JoinTable(name="UserCarMapping",
		joinColumns=@JoinColumn(name="UserDetails"), // Column Name for this table
		inverseJoinColumns=@JoinColumn(name="Car") // Column Name for this referencing table
	)
	private Collection<Car> ownedCars;
	
	@OneToMany(mappedBy="userDetails")
	private Collection<Bike> ownedBikes;
	
	@ManyToMany()
	@JoinTable(name="UserMovieMapping",
		joinColumns=@JoinColumn(name="UserDetails"),
		inverseJoinColumns=@JoinColumn(name="Movie")
	)
	@NotFound(action=NotFoundAction.IGNORE)
	private Collection<Movie> movieList;
	
	@Column(name="user_notes")
	@Lob
	private String userNotes;
	@Lob
	@Column(name="user_image")
	private byte[] userImage;
	
	@Transient
	private String transientProperty;
	public static String staticProperty;
	
	public int getSerialID() {
		return serialID;
	}
	public void setSerialID(int serialID) {
		this.serialID = serialID;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getUserAge() {
		return userAge;
	}
	public void setUserAge(int userAge) {
		this.userAge = userAge;
	}
	public Date getJoinedDate() {
		return joinedDate;
	}
	public void setJoinedDate(Date joinedDate) {
		this.joinedDate = joinedDate;
	}
	public Address getPresentAddress() {
		return presentAddress;
	}
	public void setPresentAddress(Address presentAddress) {
		this.presentAddress = presentAddress;
	}
	public Address getPermanentAddress() {
		return permanentAddress;
	}
	public void setPermanentAddress(Address permanentAddress) {
		this.permanentAddress = permanentAddress;
	}
	public Vehicle getFavouriteVehicle() {
		return favouriteVehicle;
	}
	public void setFavouriteVehicle(Vehicle favouriteVehicle) {
		this.favouriteVehicle = favouriteVehicle;
	}
	public Collection<Car> getOwnedCars() {
		return ownedCars;
	}
	public void setOwnedCars(Collection<Car> ownedCars) {
		this.ownedCars = ownedCars;
	}
	public Collection<Bike> getOwnedBikes() {
		return ownedBikes;
	}
	public void setOwnedBikes(Collection<Bike> ownedBikes) {
		this.ownedBikes = ownedBikes;
	}
	public Collection<Movie> getMovieList() {
		return movieList;
	}
	public void setMovieList(Collection<Movie> movieList) {
		this.movieList = movieList;
	}
	public String getUserNotes() {
		return userNotes;
	}
	public void setUserNotes(String userNotes) {
		this.userNotes = userNotes;
	}
	public byte[] getUserImage() {
		return userImage;
	}
	public void setUserImage(byte[] userImage) {
		this.userImage = userImage;
	}
	public Collection<Address> getPreviousAddress() {
		return previousAddress;
	}
	public void setPreviousAddress(Collection<Address> previousAddress) {
		this.previousAddress = previousAddress;
	}
}
