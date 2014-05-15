package edu.monash.mymonashmate;

public class privacyControl {
	private boolean birthday;
	private boolean email;
	private boolean firstLanguage;
	private boolean food;
	private boolean gender;
	private int id;
	private boolean job;
	private boolean mobile = false;
	private boolean movie;
	private boolean nationalityId;
	private boolean programmingLang;
	private boolean secondLanguage;
	private boolean suburb = false;
	
	
	public boolean isBirthday() {
		return birthday;
	}
	public void setBirthday(boolean birthday) {
		this.birthday = birthday;
	}
	public boolean isEmail() {
		return email;
	}
	public void setEmail(boolean email) {
		this.email = email;
	}
	public boolean isFirstLanguage() {
		return firstLanguage;
	}
	public void setFirstLanguage(boolean firstLanguage) {
		this.firstLanguage = firstLanguage;
	}
	public boolean isFood() {
		return food;
	}
	public void setFood(boolean food) {
		this.food = food;
	}
	public boolean isGender() {
		return gender;
	}
	public void setGender(boolean gender) {
		this.gender = gender;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isJob() {
		return job;
	}
	public void setJob(boolean job) {
		this.job = job;
	}
	public boolean isMobile() {
		return mobile;
	}
	public void setMobile(boolean mobile) {
		this.mobile = mobile;
	}
	public boolean isMovie() {
		return movie;
	}
	public void setMovie(boolean movie) {
		this.movie = movie;
	}
	public boolean isNationalityId() {
		return nationalityId;
	}
	public void setNationalityId(boolean nationalityId) {
		this.nationalityId = nationalityId;
	}
	public boolean isProgrammingLang() {
		return programmingLang;
	}
	public void setProgrammingLang(boolean programmingLang) {
		this.programmingLang = programmingLang;
	}
	public boolean isSecondLanguage() {
		return secondLanguage;
	}
	public void setSecondLanguage(boolean secondLanguage) {
		this.secondLanguage = secondLanguage;
	}
	public boolean isSuburb() {
		return suburb;
	}
	public void setSuburb(boolean suburb) {
		this.suburb = suburb;
	}
}
