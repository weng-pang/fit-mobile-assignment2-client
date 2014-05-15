package edu.monash.mymonashmate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.AlertDialog;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class mateManager implements loginListerer{
	private Student student;
	private privacyControl studentPrivacy;
	private ArrayList<Unit> studentUnitList;
	private ArrayList<Student> studentList = new ArrayList<Student>();
	private ArrayList<Integer> matchList = new ArrayList<Integer>(); // this attribute stores student id and match count
	private boolean matchCourse = true;
	private boolean matchUnit = false;
	private ArrayList<privacyControl> studentPrivacyList;
	private ArrayList<courseId> courseList;
	private ArrayList<nationalityId> nationList;
	private ArrayList<Language> languageList;
	private ArrayList<Unit> unitList;
	private RESTFULService rest;
	private MATCHINGService match;
	private MainActivity activity;
	private AlertDialog.Builder msgBox;
	public final double[] LOCATION_1 = {-37.875992,145.043492};
	public final double[] LOCATION_2 = {-37.878982,145.048093};
	private Gson gson = new Gson();
	
	public mateManager(MainActivity activity){
		rest = new RESTFULService(activity,this); // prepare the service 
		if (rest.checkConnection()){
			Log.v("MyMonashMateMan","REST Connection OK");
		} else {
			Log.w("MyMonashMateMan","REST Connection NOT OK");
		}
		rest =null;
		this.activity = activity; // store the activity for possible use
		msgBox = new AlertDialog.Builder(activity); // prepare an empty dialog box
	}
	
	public void createStudent(){
		rest = new RESTFULService(activity,this); // prepare the service 
		Log.v("MyMonashMateMan","Create Student Called");
		Student newStudent = new Student(); // Get a student object ready
		// fetch form information
		EditText txtResPassword = (EditText) activity.findViewById(R.id.txtResPassword);
		EditText txtResNickname = (EditText) activity.findViewById(R.id.txtResNickname);
		EditText txtResFirstName = (EditText) activity.findViewById(R.id.txtResFirstName);
		EditText txtResLastName = (EditText) activity.findViewById(R.id.txtResLastName);
		EditText txtResBirthday = (EditText) activity.findViewById(R.id.txtResBirthday);
		EditText txtResEmail = (EditText) activity.findViewById(R.id.txtResEmail);
//		EditText txtResMobile = (EditText) activity.findViewById(R.id.txtResMobile);
		EditText txtResProgramming = (EditText) activity.findViewById(R.id.txtResProgramming);
		EditText txtResFood = (EditText) activity.findViewById(R.id.txtResFood);
		EditText txtResMovie = (EditText) activity.findViewById(R.id.txtResMovie);
		EditText txtResJob = (EditText) activity.findViewById(R.id.txtResJob);
		AutoCompleteTextView txtaNationality = (AutoCompleteTextView) activity.findViewById(R.id.txtaResNationality);
		AutoCompleteTextView txtaCourse = (AutoCompleteTextView) activity.findViewById(R.id.txtaResCourse);
		AutoCompleteTextView txtaFirstLanguage = (AutoCompleteTextView) activity.findViewById(R.id.txtaResFirstLanguage);
		AutoCompleteTextView txtaSecondLanugage = (AutoCompleteTextView) activity.findViewById(R.id.txtaResSecondLanguage);
//		RadioButton radResMale = (RadioButton) activity.findViewById(R.id.radResMale);
		RadioButton radResFemale = (RadioButton) activity.findViewById(R.id.radResFemale);
		// This area checks entry status of each dynamic field
		// perform search in each of the list, if find, append the relevant object into the student object
		Log.v("MyMonashMateMan","Nation="+txtaNationality.getText().toString());
		nationalityId selectNation =null;
		for (nationalityId nation: nationList){
			if (txtaNationality.getText().toString().equalsIgnoreCase(nation.toString())){
				selectNation = nation;
				break;
			}
		}
		if (selectNation == null){
			Log.v("MyMonashMateMan","NationId=NULL");
		} else {
			Log.v("MyMonashMateMan","NationId="+selectNation.code);
		}
		//=================================================================================================
		courseId selectCourseId =null;
		for (courseId course: courseList){
			if (txtaCourse.getText().toString().equalsIgnoreCase(course.toString())){
				selectCourseId = course;
				break;
			}
		}
		if (selectCourseId == null){
			Log.v("MyMonashMateMan","CourseId=NULL");
		} else {
			Log.v("MyMonashMateMan","CourseId="+selectCourseId.id);
		}
		//=================================================================================================
		Language selectFirstLanguage = null;
		for (Language language: languageList){
//			Log.v("MyMonashMateMan","selectFirstLanguageId="+txtaFirstLanguage.getText().toString()+"currentid="+language.toString());
			if (txtaFirstLanguage.getText().toString().equalsIgnoreCase(language.toString())){
				selectFirstLanguage = language;
				break;
			}
		}
		if (selectFirstLanguage == null){
			Log.v("MyMonashMateMan","selectFirstLanguageId=NULL");
		} else {
			Log.v("MyMonashMateMan","selectFirstLanguageId="+selectFirstLanguage.code);
		}
		//=================================================================================================
		Language selectSecondLanguage = null;
		for (Language language: languageList){
			if (txtaSecondLanugage.getText().toString().equalsIgnoreCase(language.toString())){
				selectSecondLanguage = language;
				break;
			}
		}
		if (selectSecondLanguage == null){
			// this allows student with single language entering the form 
			selectSecondLanguage = selectFirstLanguage;
			Log.v("MyMonashMateMan","selectSecondLanguageId=NULL");
		} else {
			Log.v("MyMonashMateMan","selectSecondLanguageId="+selectSecondLanguage.code);
		}
		//=================================================================================================
		String gender = "M";
		if (radResFemale.isSelected()){
			gender = "F";
		}
		//=================================================================================================
		// if not prompt for error message
		// validation
		// check the email address first
		boolean correctEmail = true;
		if (txtResEmail.getText().toString().isEmpty()){
			Toast.makeText(activity, "Please Enter Email", Toast.LENGTH_SHORT).show();
		} else {
			String[] emailComponent = txtResEmail.getText().toString().split("@");
			try{
				if (!emailComponent[1].equalsIgnoreCase("student.monash.edu")){
					correctEmail = false;
				}
			} catch (ArrayIndexOutOfBoundsException e){
				correctEmail = false;
			}
		}
		// check the date
		boolean correctDate = true;
		SimpleDateFormat theDate = new SimpleDateFormat("dd/MM/yyyy");
		String newDate = null;
		theDate.setLenient(false);
		if (!txtResBirthday.getText().toString().isEmpty()){
			try {
				Date d = theDate.parse(txtResBirthday.getText().toString());
				theDate.applyPattern("yyyy-MM-dd");
				newDate = theDate.format(d);
			} catch (ParseException e) {
				Log.v("MyMonashMateMan","WRONG Date Inpected" + txtResBirthday.getText().toString());
				correctDate = false;
			}
		} else {
			Log.v("MyMonashMateMan","EMPTY Date Inpected");
			correctDate = false;
		}
		//================================================================
		// NON-mandatory fields
		//================================================================
		if (txtResProgramming.getText().toString().isEmpty()){
			txtResProgramming.setText(",");
		} 
		if (txtResFood.getText().toString().isEmpty()){
			txtResFood.setText(",");
		}
		if (txtResMovie.getText().toString().isEmpty()){
			txtResMovie.setText(",");
		} 
		if (selectFirstLanguage == null){
			Toast.makeText(activity, "Please Select Language", Toast.LENGTH_SHORT).show();
		}
		if (txtResNickname.getText().toString().isEmpty()){
			txtResNickname.setText(" ");
		}
		if (txtResJob.getText().toString().isEmpty()){
			txtResJob.setText("student");
		}
		//==================================================================
		// Mandatory fields
		//==================================================================
		if (txtResPassword.getText().toString().isEmpty()){
			Toast.makeText(activity, "Please Enter Password", Toast.LENGTH_SHORT).show();
		} else if (txtResFirstName.getText().toString().isEmpty()){
			Toast.makeText(activity, "Please Enter First Name", Toast.LENGTH_SHORT).show();
		} else if (txtResLastName.getText().toString().isEmpty()){
			Toast.makeText(activity, "Please Enter Last Name", Toast.LENGTH_SHORT).show();
		} else if (selectNation == null){
			Toast.makeText(activity, "Please Select Nation", Toast.LENGTH_SHORT).show();
		} else if (!correctEmail || txtResEmail.getText().toString().isEmpty()){
			Toast.makeText(activity, "Only Monash Student Email Accepted", Toast.LENGTH_SHORT).show();
		} else if (!correctDate){
			Log.v("MyMonashMateMan","WRONG Date Entered");
			Toast.makeText(activity, "Please Enter Birthday", Toast.LENGTH_SHORT).show();			
//		} else if (txtResMobile.getText().toString().isEmpty()){
//			Toast.makeText(activity, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();			
		}  else {
			// check for empty inputs - append an empty space if necessary
			// location input -- provide a random spot around the Monash Caulfield Campus
			Random randomGen = new Random();
			double range_1 = LOCATION_1[0] - LOCATION_2[0];
			double range_2 = LOCATION_1[1] - LOCATION_2[1];
			double latitude = 0;
			double longtitude = 0;
			if (LOCATION_1[0] > 0){
				latitude = range_1*randomGen.nextDouble() + LOCATION_1[0];
			} else {
				latitude = - range_1*randomGen.nextDouble() + LOCATION_1[0];
			}
			if (LOCATION_1[1] > 0){
				longtitude = range_2*randomGen.nextDouble()+LOCATION_1[1];
			} else {
				longtitude = - range_2*randomGen.nextDouble()+LOCATION_1[1];
			}
			// change the date format
			// append the content into student object
			newStudent.setFirstName(txtResFirstName.getText().toString());
			newStudent.setLastName(txtResLastName.getText().toString());
			newStudent.setnickName(txtResNickname.getText().toString());
			newStudent.setPassword(txtResPassword.getText().toString());
			newStudent.setGender(gender);
			newStudent.setNationalityId(selectNation);
			newStudent.setBirthday(newDate.toString()+"T00:00:00");
			newStudent.setEmail(txtResEmail.getText().toString());
			newStudent.setCourseId(selectCourseId);
//			newStudent.setMobile(txtResMobile.getText().toString());
			newStudent.setMobile("10101010");
			newStudent.setProgrammingLang(txtResProgramming.getText().toString());
			newStudent.setFood(txtResFood.getText().toString());
			newStudent.setMovie(txtResMovie.getText().toString());
			newStudent.setJob(txtResJob.getText().toString());
			newStudent.setFirstLanguage(selectFirstLanguage);
			newStudent.setSecondLanguage(selectSecondLanguage);
			newStudent.setSuburb("caulfield"); // hardcoded in this version
			newStudent.setLatitude(latitude);
			newStudent.setLongtitude(longtitude);
			
			// show the json result
			String inputJson = gson.toJson(newStudent,Student.class);
			Log.v("MyMonashMateMan","Generate JSON="+inputJson);
			// call restful service for post 
			rest.setAddress("students");
			rest.setMessage(inputJson);
			rest.execute("post");
			// redirect back to login page
			activity.loginPage();
			// show message
			Toast.makeText(activity, "Please use your email to Login", Toast.LENGTH_SHORT).show();
		}
	}
	public void saveProfile(){
		// This area updates profile for current students
		// This is very similar to create profile - may be merged in next version
		rest = new RESTFULService(activity,this); // prepare the service 
		Log.v("MyMonashMateMan","Modify Student Called");
		// fetch form information
		EditText txtPassword = (EditText) activity.findViewById(R.id.txtPassword);
		EditText txtNickname = (EditText) activity.findViewById(R.id.txtNickname);
		EditText txtFirstName = (EditText) activity.findViewById(R.id.txtFirstName);
		EditText txtLastName = (EditText) activity.findViewById(R.id.txtLastName);
		EditText txtBirthday = (EditText) activity.findViewById(R.id.txtBirthday);
		EditText txtEmail = (EditText) activity.findViewById(R.id.txtEmail);
//		EditText txtMobile = (EditText) activity.findViewById(R.id.txtMobile);
		EditText txtProgramming = (EditText) activity.findViewById(R.id.txtProgramming);
		EditText txtFood = (EditText) activity.findViewById(R.id.txtFood);
		EditText txtMovie = (EditText) activity.findViewById(R.id.txtMovie);
		EditText txtJob = (EditText) activity.findViewById(R.id.txtJob);
		AutoCompleteTextView txtaNationality = (AutoCompleteTextView) activity.findViewById(R.id.txtaNationality);
		AutoCompleteTextView txtaCourse = (AutoCompleteTextView) activity.findViewById(R.id.txtaCourse);
		AutoCompleteTextView txtaFirstLanguage = (AutoCompleteTextView) activity.findViewById(R.id.txtaFirstLanguage);
		AutoCompleteTextView txtaSecondLanugage = (AutoCompleteTextView) activity.findViewById(R.id.txtaSecondLanguage);
//		RadioButton radMale = (RadioButton) activity.findViewById(R.id.radMale);
		RadioButton radFemale = (RadioButton) activity.findViewById(R.id.radFemale);
		// This area checks entry status of each dynamic field
		// perform search in each of the list, if find, append the relevant object into the student object
		Log.v("MyMonashMateMan","Nation="+txtaNationality.getText().toString());
		nationalityId selectNation =null;
		for (nationalityId nation: nationList){
			if (txtaNationality.getText().toString().equalsIgnoreCase(nation.toString())){
				selectNation = nation;
				break;
			}
		}
		if (selectNation == null){
			Log.v("MyMonashMateMan","NationId=NULL");
		} else {
			Log.v("MyMonashMateMan","NationId="+selectNation.code);
		}
		//=================================================================================================
		courseId selectCourseId =null;
		for (courseId course: courseList){
			if (txtaCourse.getText().toString().equalsIgnoreCase(course.toString())){
				selectCourseId = course;
				break;
			}
		}
		if (selectCourseId == null){
			Log.v("MyMonashMateMan","CourseId=NULL");
		} else {
			Log.v("MyMonashMateMan","CourseId="+selectCourseId.id);
		}
		//=================================================================================================
		Language selectFirstLanguage = null;
		for (Language language: languageList){
//			Log.v("MyMonashMateMan","selectFirstLanguageId="+txtaFirstLanguage.getText().toString()+"currentid="+language.toString());
			if (txtaFirstLanguage.getText().toString().equalsIgnoreCase(language.toString())){
				selectFirstLanguage = language;
				break;
			}
		}
		if (selectFirstLanguage == null){
			Log.v("MyMonashMateMan","selectFirstLanguageId=NULL");
		} else {
			Log.v("MyMonashMateMan","selectFirstLanguageId="+selectFirstLanguage.code);
		}
		//=================================================================================================
		Language selectSecondLanguage = null;
		for (Language language: languageList){
			if (txtaSecondLanugage.getText().toString().equalsIgnoreCase(language.toString())){
				selectSecondLanguage = language;
				break;
			}
		}
		if (selectSecondLanguage == null){
			// this allows student with single language entering the form 
			selectSecondLanguage = selectFirstLanguage;
			Log.v("MyMonashMateMan","selectSecondLanguageId=NULL");
		} else {
			Log.v("MyMonashMateMan","selectSecondLanguageId="+selectSecondLanguage.code);
		}
		//=================================================================================================
		String gender = "M";
		if (radFemale.isSelected()){
			gender = "F";
		}
		//=================================================================================================
		// if not prompt for error message
		// validation
		// check the email address first
		boolean correctEmail = true;
		if (txtEmail.getText().toString().isEmpty()){
			Toast.makeText(activity, "Please Enter Email", Toast.LENGTH_SHORT).show();
		} else {
			String[] emailComponent = txtEmail.getText().toString().split("@");
			try{
				if (!emailComponent[1].equalsIgnoreCase("student.monash.edu")){
					correctEmail = false;
				}
			} catch (ArrayIndexOutOfBoundsException e){
				correctEmail = false;
			}
		}
		// check the date
		boolean correctDate = true;
		SimpleDateFormat theDate = new SimpleDateFormat("dd/MM/yyyy");
		String newDate = null;
		theDate.setLenient(false);
		if (!txtBirthday.getText().toString().isEmpty()){
			try {
				Date d = theDate.parse(txtBirthday.getText().toString());
				theDate.applyPattern("yyyy-MM-dd");
				newDate = theDate.format(d);
			} catch (ParseException e) {
				Log.v("MyMonashMateMan","WRONG Date Inpected" + txtBirthday.getText().toString());
				correctDate = false;
			}
		} else {
			Log.v("MyMonashMateMan","EMPTY Date Inpected");
			correctDate = false;
		}
		//================================================================
		// NON-mandatory fields
		//================================================================
		if (txtProgramming.getText().toString().isEmpty()){
			txtProgramming.setText(",");
		} 
		if (txtFood.getText().toString().isEmpty()){
			txtFood.setText(",");
		}
		if (txtMovie.getText().toString().isEmpty()){
			txtMovie.setText(",");
		} 
		if (selectFirstLanguage == null){
			Toast.makeText(activity, "Please Select Language", Toast.LENGTH_SHORT).show();
		}
		if (txtNickname.getText().toString().isEmpty()){
			txtNickname.setText(" ");
		}
		if (txtJob.getText().toString().isEmpty()){
			txtJob.setText("student");
		}
		//==================================================================
		// Mandatory fields
		//==================================================================
		if (txtFirstName.getText().toString().isEmpty()){
			Toast.makeText(activity, "Please Enter First Name", Toast.LENGTH_SHORT).show();
		} else if (txtLastName.getText().toString().isEmpty()){
			Toast.makeText(activity, "Please Enter Last Name", Toast.LENGTH_SHORT).show();
		} else if (selectNation == null){
			Toast.makeText(activity, "Please Select Nation", Toast.LENGTH_SHORT).show();
		} else if (!correctEmail || txtEmail.getText().toString().isEmpty()){
			Toast.makeText(activity, "Only Monash Student Email Accepted", Toast.LENGTH_SHORT).show();
		} else if (!correctDate){
			Log.v("MyMonashMateMan","WRONG Date Entered");
			Toast.makeText(activity, "Please Enter Birthday", Toast.LENGTH_SHORT).show();			
//		} else if (txtResMobile.getText().toString().isEmpty()){
//			Toast.makeText(activity, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();			
		}  else {
			// check for empty inputs - append an empty space if necessary
			// change the date format
			// append the content into student object
			student.setFirstName(txtFirstName.getText().toString());
			student.setLastName(txtLastName.getText().toString());
			student.setnickName(txtNickname.getText().toString());
			if (!txtPassword.getText().toString().isEmpty()){
				student.setPassword(txtPassword.getText().toString());
			}
			student.setGender(gender);
			student.setNationalityId(selectNation);
			student.setBirthday(newDate.toString()+"T00:00:00");
			student.setEmail(txtEmail.getText().toString());
			student.setCourseId(selectCourseId);
//			student.setMobile(txtResMobile.getText().toString());
			student.setMobile("10101010");
			student.setProgrammingLang(txtProgramming.getText().toString());
			student.setFood(txtFood.getText().toString());
			student.setMovie(txtMovie.getText().toString());
			student.setJob(txtJob.getText().toString());
			student.setFirstLanguage(selectFirstLanguage);
			student.setSecondLanguage(selectSecondLanguage);
			
			// show the json result
			String inputJson = gson.toJson(student,Student.class);
			Log.v("MyMonashMateMan","Generate JSON="+inputJson);
			// call restful service for post 
			rest.setAddress("students/"+student.getId());
			rest.setMessage(inputJson);
			rest.execute("put");
			// redirect back to map page
			activity.showMap();
			// display message
			Toast.makeText(activity, "Profile Saved", Toast.LENGTH_SHORT).show();
		}
	}
	public void getBasicInfo(){
		// This method calls the restful to obtain some basic information 
		// information included: UNIT, COURSE, LANGUAGE, NATIONALITY
		//===============================================================
		rest = new RESTFULService(activity,this);
		rest.setAddress("units");
		rest.execute("get");
		//===============================================================
		rest = new RESTFULService(activity,this);
		rest.setAddress("courses");
		rest.execute("get");
		//===============================================================
		rest = new RESTFULService(activity,this);
		rest.setAddress("languages");
		rest.execute("get");
		//===============================================================
		rest = new RESTFULService(activity,this);
		rest.setAddress("nationalities");
		rest.execute("get");
		//===============================================================
		
	}
	
	public void login(String email, String password){
		rest = new RESTFULService(activity,(loginListerer)this); // prepare the service 
		// call restful for login 
		rest.setAddress("students/login/"+email+"/"+password);
		rest.execute("get");
		
	}

	@Override
	public void afterLogin(String result) {
		// check whether the attempt was successful
		if (!result.equals("")){
			Log.v("MyMonashMateMan","LOGIN OK");
				// if OK , apply the student content into object
			student = gson.fromJson(result, Student.class);
			// apply privacy settings
			rest = new RESTFULService(activity, this);
			rest.setAddress("privacycontrol/"+student.getId().toString());
			rest.execute("get");
			// apply enrolment settings
			rest = new RESTFULService(activity, this);
			rest.setAddress("students/enrolment/"+student.getId().toString());
			rest.execute("get");

			// redirect the user to map page
			activity.showMap();
			activity.logIn();
			// display message
			Toast.makeText(activity, "Welcome "+student.getFirstName()+" !", Toast.LENGTH_SHORT).show();
		} else{
			// otherwise , consider as no success
			// finally if there is no success in login process
					// display any error message to user (like wrong password)
					// tell the interface about the failure
			Log.v("MyMonashMateMan","LOGIN FALIED");
				msgBox.setTitle("Login Failed");
				msgBox.setMessage("Please check your email or password, and try again");
				msgBox.setPositiveButton("OK", null);
				msgBox.create().show();
		}
		
	}
	
	public void configurePrivacy(){
		// prepare all elements
		CheckBox ckbBirthday = (CheckBox) activity.findViewById(R.id.ckbBirthday);
		CheckBox ckbEmail = (CheckBox) activity.findViewById(R.id.ckbEmail);
		CheckBox ckbFirstLanguage = (CheckBox) activity.findViewById(R.id.ckbFirstLang);
		CheckBox ckbFood = (CheckBox) activity.findViewById(R.id.ckbFood);
		CheckBox ckbGender = (CheckBox) activity.findViewById(R.id.ckbGender);
		CheckBox ckbJob = (CheckBox) activity.findViewById(R.id.ckbJob);
		CheckBox ckbMovie = (CheckBox) activity.findViewById(R.id.ckbMovie);
		CheckBox ckbNationality = (CheckBox) activity.findViewById(R.id.ckbNationality);
		CheckBox ckbProgram = (CheckBox) activity.findViewById(R.id.ckbProgram);
		CheckBox ckbSecondLanguage = (CheckBox) activity.findViewById(R.id.ckbSecondLang);
		// update the content accordingly
		studentPrivacy.setBirthday(ckbBirthday.isChecked());
		studentPrivacy.setEmail(ckbEmail.isChecked());
		studentPrivacy.setFirstLanguage(ckbFirstLanguage.isChecked());
		studentPrivacy.setFood(ckbFood.isChecked());
		studentPrivacy.setGender(ckbGender.isChecked());
		studentPrivacy.setJob(ckbJob.isChecked());
		studentPrivacy.setMovie(ckbMovie.isChecked());
		studentPrivacy.setNationalityId(ckbNationality.isChecked());
		studentPrivacy.setProgrammingLang(ckbProgram.isChecked());
		studentPrivacy.setSecondLanguage(ckbSecondLanguage.isChecked());
		// prepare restful service
		rest = new RESTFULService(activity,this); // prepare the service 
		Log.v("MyMonashMateMan","Modify Privacy Called");
		rest.setAddress("privacycontrol/"+student.getId());
		rest.setMessage(gson.toJson(studentPrivacy, privacyControl.class));
		rest.execute("put");
		// return to map page
		activity.showMap();
	}
	public void updateUnit(){
		// apply the current enrolment unit into the database
		// prepare restful service
		rest = new RESTFULService(activity, this);
		Log.v("MyMonashMateMan","Modify Student Enrolment Called");
		rest.setAddress("students/enrolment/"+student.getId().toString());
		rest.setMessage(gson.toJson(studentUnitList, new TypeToken<ArrayList<Unit>>(){}.getType()));
		rest.execute("put");
		// return to map page
		activity.showMap();
		// display message
		Toast.makeText(activity, "Units Saved", Toast.LENGTH_SHORT).show();
	}
	
	public void matchProfile(){
		// logging in first time - arrange the auto profile match
		if (matchCourse){
			match = new MATCHINGService(this, activity);
			match.execute("course");
		}
		if (matchUnit){
			match = new MATCHINGService(this, activity);
			match.execute("unit");
		}
	}
	public void changeMatchProfile(){
		CheckBox ckbUnit = (CheckBox) activity.findViewById(R.id.ckbMatchUnit);
		CheckBox ckbCourse = (CheckBox) activity.findViewById(R.id.ckbMatchCourse);
		// apply the content information
		matchUnit = ckbUnit.isChecked();
		matchCourse = ckbCourse.isChecked();
		// return to map page
		activity.showMap();
		// display message
		Toast.makeText(activity, "Match Profile Saved", Toast.LENGTH_SHORT).show();
	}
 // ==============================================================================================================
	// GETTERs AND SETTERs
	//============================================================================================================
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public ArrayList<Unit> getStudentUnitList() {
		return studentUnitList;
	}

	public void setStudentUnitList(ArrayList<Unit> studentUnitList) {
		this.studentUnitList = studentUnitList;
	}

	public ArrayList<Student> getStudentList() {
		return studentList;
	}

	public void setStudentList(ArrayList<Student> studentList) {
		this.studentList = studentList;
	}

	public ArrayList<courseId> getCourseList() {
		return courseList;
	}

	public void setCourseList(ArrayList<courseId> courseList) {
		this.courseList = courseList;
	}

	public boolean isMatchCourse() {
		return matchCourse;
	}

	public void setMatchCourse(boolean matchCourse) {
		this.matchCourse = matchCourse;
	}

	public boolean isMatchUnit() {
		return matchUnit;
	}

	public void setMatchUnit(boolean matchUnit) {
		this.matchUnit = matchUnit;
	}

	public ArrayList<Language> getLanguageList() {
		return languageList;
	}

	public void setLanguageList(ArrayList<Language> languageList) {
		this.languageList = languageList;
	}

	public ArrayList<Unit> getUnitList() {
		return unitList;
	}

	public void setUnitList(ArrayList<Unit> unitList) {
		this.unitList = unitList;
	}

	public ArrayList<nationalityId> getNationList() {
		return nationList;
	}

	public void setNationList(ArrayList<nationalityId> nationList) {
		this.nationList = nationList;
	}

	public privacyControl getStudentPrivacy() {
		return studentPrivacy;
	}

	public void setStudentPrivacy(privacyControl studentPrivacy) {
		this.studentPrivacy = studentPrivacy;
	}

	public ArrayList<privacyControl> getStudentPrivacyList() {
		return studentPrivacyList;
	}

	public void setStudentPrivacyList(ArrayList<privacyControl> studentPrivacyList) {
		this.studentPrivacyList = studentPrivacyList;
	}

	public ArrayList<Integer> getMatchList() {
		return matchList;
	}

	public void setMatchList(ArrayList<Integer> matchList) {
		this.matchList = matchList;
	}
}

interface loginListerer{
	public void afterLogin(String result);
}