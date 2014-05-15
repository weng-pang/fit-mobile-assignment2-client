package edu.monash.mymonashmate;

public class Student {
	private Integer id;
	private String firstName;
	private String lastName;
	private String nickname;
	private String gender;
	private nationalityId nationalityId;
	private String birthday;
	private String email;
	private courseId courseId;
	private Double latitude;
	private Double longtitude;
	private String mobile;
	private String programmingLang;
	private String food;
	private String movie;
	private String job;
	private String suburb;
	private Language secondLanguage;
	private Language firstLanguage;
	private String password;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getnickName() {
		return nickname;
	}
	public void setnickName(String nickName) {
		this.nickname = nickName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {	
		this.birthday = birthday;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public courseId getCourseId() {
		return courseId;
	}
	public void setCourseId(courseId courseId) {
		this.courseId = courseId;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(Double longtitude) {
		this.longtitude = longtitude;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public nationalityId getNationalityId() {
		return nationalityId;
	}
	public void setNationalityId(nationalityId nationalityId) {
		this.nationalityId = nationalityId;
	}
	public String getProgrammingLang() {
		return programmingLang;
	}
	public void setProgrammingLang(String programmingLang) {
		this.programmingLang = programmingLang;
	}
	public String getFood() {
		return food;
	}
	public void setFood(String food) {
		this.food = food;
	}
	public String getMovie() {
		return movie;
	}
	public void setMovie(String movie) {
		this.movie = movie;
	}
	public String getSuburb() {
		return suburb;
	}
	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}
	public Language getSecondLanguage() {
		return secondLanguage;
	}
	public void setSecondLanguage(Language secondLanguage) {
		this.secondLanguage = secondLanguage;
	}
	public Language getFirstLanguage() {
		return firstLanguage;
	}
	public void setFirstLanguage(Language firstLanguage) {
		this.firstLanguage = firstLanguage;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	
}

class courseId{
	public String id;
	public String name;
	public String toString(){
		return name;
	}
}

class Language{
	public String code;
	public String name;
	public String toString(){
		return name;
	}
}

class nationalityId{
	public String code;
	public String name;
	public String toString(){
		return name;
	}
	
}
