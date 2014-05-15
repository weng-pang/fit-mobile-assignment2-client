package edu.monash.mymonashmate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.mapquest.android.maps.AnnotationView;
import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.ItemizedOverlay;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.OverlayItem;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	private mateManager manager;
	private eventProcessor events;
	private unitProcessor units;
	private Menu menu;
	private AnnotationView annotation;
	private DefaultItemizedOverlay poiOverlay=null;
	private ArrayList<Student> mapList = new ArrayList<Student>();
	
	MenuItem menLogout;
	MenuItem menMatch;
	MenuItem menPrivacy;
	MenuItem menProfile;
	MenuItem menUnit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		manager = new mateManager(this);
		// After starting up, get all essential directories
		manager.getBasicInfo();
		events = new eventProcessor(manager,this);
		this.setUnits(new unitProcessor(manager,this));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		loginPage();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		this.menu = menu;
		menLogout = (MenuItem) findViewById(R.id.menLogout);
		menMatch = (MenuItem) findViewById(R.id.menMatch);
		menPrivacy = (MenuItem) findViewById(R.id.menPrivacy);
		menProfile = (MenuItem) findViewById(R.id.menProfile);
		menUnit = (MenuItem) this.findViewById(R.id.menUnits);
		return true;
	}
	//===============================================================
	// MENU Click Handler
	// Unlike all buttons, menu event handling is taken right here
	//===============================================================
	public boolean onOptionsItemSelected(MenuItem item){
		Log.v("MAINActivity","The Menu is clicked");
		switch (item.getItemId()){
			case R.id.menLogout:
				Log.v("MAINActivity","Logout");
				this.logOut();
			break;
			case R.id.menPrivacy:
				Log.v("MAINActivity","Privacy");
				this.showPrivacy();
			break;
			case R.id.menUnits:
				Log.v("MAINActivity","Unit");
				this.showUnit();
			break;
			case R.id.menMatch:
				Log.v("MAINActivity","Match");
				this.showMatch();
			break;
			case R.id.menProfile:
				Log.v("MAINActivity","Profile");
				this.showProfile();
			break;
		}
		return true;
	}

	@Override
	public void onClick(View view) {
		
		
	}
	//=======================================================
	// This area handles all CHANGE-OF-VIEW activities
	//=======================================================
	public void loginPage(){
		this.setContentView(R.layout.login);
		// Re-establish the listeners
		// Dynamic content goes here ... or somewhere else
		Button cmdLogin = (Button) findViewById(R.id.cmdLogin);
		Button cmdRegister = (Button) findViewById(R.id.cmdRegister);
		
		// Register to the event processor
		cmdLogin.setOnClickListener(events);
		cmdRegister.setOnClickListener(events);
	}
	public void logOut(){
		// This method listens to logout command, it simply returns user back to login page
		setContentView(R.layout.login); // Return to login page
		menu.findItem(R.id.menLogout).setVisible(false);
		menu.findItem(R.id.menMatch).setVisible(false);
		menu.findItem(R.id.menPrivacy).setVisible(false);
		menu.findItem(R.id.menProfile).setVisible(false);
		menu.findItem(R.id.menUnits).setVisible(false);
		this.loginPage();
	}
	public void logIn(){
		// This method allows restricted elements displayed after login
		menu.findItem(R.id.menLogout).setVisible(true);
		menu.findItem(R.id.menMatch).setVisible(true);
		menu.findItem(R.id.menPrivacy).setVisible(true);
		menu.findItem(R.id.menProfile).setVisible(true);
		menu.findItem(R.id.menUnits).setVisible(true);
	}
	
	public void showRegister(){
		// Lead to register page
		this.setContentView(R.layout.register);
		// call out all relevant elements
		Button cmdResReturn = (Button) this.findViewById(R.id.cmdResReturn);
		Button cmdResSubmit = (Button) this.findViewById(R.id.cmdResSubmit);
		AutoCompleteTextView txtaResNationality = (AutoCompleteTextView) this.findViewById(R.id.txtaResNationality);
		AutoCompleteTextView txtaResCourse = (AutoCompleteTextView) this.findViewById(R.id.txtaResCourse);
		AutoCompleteTextView txtaResFirstLanguage = (AutoCompleteTextView) this.findViewById(R.id.txtaResFirstLanguage);
		AutoCompleteTextView txtaResSecondLanguage = (AutoCompleteTextView) this.findViewById(R.id.txtaResSecondLanguage);
		// add "predefined" list contents
		txtaResNationality.setAdapter(new ArrayAdapter<nationalityId>(this,android.R.layout.simple_dropdown_item_1line,manager.getNationList()));
		txtaResCourse.setAdapter(new ArrayAdapter<courseId>(this,android.R.layout.simple_dropdown_item_1line,manager.getCourseList()));
		txtaResFirstLanguage.setAdapter(new ArrayAdapter<Language>(this,android.R.layout.simple_dropdown_item_1line,manager.getLanguageList()));
		txtaResSecondLanguage.setAdapter(new ArrayAdapter<Language>(this,android.R.layout.simple_dropdown_item_1line,manager.getLanguageList()));
		// add event listeners
		cmdResReturn.setOnClickListener(events);
		cmdResSubmit.setOnClickListener(events);
	}
	
	public void showProfile(){
		// lead to profile page
		this.setContentView(R.layout.profile);
		// call out all relevant elements
		Button cmdReturn = (Button) this.findViewById(R.id.cmdReturn);
		Button cmdSubmit = (Button) this.findViewById(R.id.cmdSubmit);
		AutoCompleteTextView txtaNationality = (AutoCompleteTextView) this.findViewById(R.id.txtaNationality);
		AutoCompleteTextView txtaCourse = (AutoCompleteTextView) this.findViewById(R.id.txtaCourse);
		AutoCompleteTextView txtaFirstLanguage = (AutoCompleteTextView) this.findViewById(R.id.txtaFirstLanguage);
		AutoCompleteTextView txtaSecondLanguage = (AutoCompleteTextView) this.findViewById(R.id.txtaSecondLanguage);
//		EditText txtPassword = (EditText) this.findViewById(R.id.txtPassword);
		EditText txtNickname = (EditText) this.findViewById(R.id.txtNickname);
		EditText txtFirstName = (EditText) this.findViewById(R.id.txtFirstName);
		EditText txtLastName = (EditText) this.findViewById(R.id.txtLastName);
		EditText txtBirthday = (EditText) this.findViewById(R.id.txtBirthday);
		EditText txtEmail = (EditText) this.findViewById(R.id.txtEmail);
//		EditText txtMobile = (EditText) this.findViewById(R.id.txtMobile);
		EditText txtProgramming = (EditText) this.findViewById(R.id.txtProgramming);
		EditText txtFood = (EditText) this.findViewById(R.id.txtFood);
		EditText txtMovie = (EditText) this.findViewById(R.id.txtMovie);
		EditText txtJob = (EditText) this.findViewById(R.id.txtJob);
		RadioButton radMale = (RadioButton) this.findViewById(R.id.radMale);
		RadioButton radFemale = (RadioButton) this.findViewById(R.id.radFemale);
		// add "predefined" list contents
		txtaNationality.setAdapter(new ArrayAdapter<nationalityId>(this,android.R.layout.simple_dropdown_item_1line,manager.getNationList()));
		txtaCourse.setAdapter(new ArrayAdapter<courseId>(this,android.R.layout.simple_dropdown_item_1line,manager.getCourseList()));
		txtaFirstLanguage.setAdapter(new ArrayAdapter<Language>(this,android.R.layout.simple_dropdown_item_1line,manager.getLanguageList()));
		txtaSecondLanguage.setAdapter(new ArrayAdapter<Language>(this,android.R.layout.simple_dropdown_item_1line,manager.getLanguageList()));
		// event listeners
		cmdReturn.setOnClickListener(events);
		cmdSubmit.setOnClickListener(events);
		// set current student content 
		txtaNationality.setText(manager.getStudent().getNationalityId().name);
		txtaCourse.setText(manager.getStudent().getCourseId().name);
		txtaFirstLanguage.setText(manager.getStudent().getFirstLanguage().name);
		txtaSecondLanguage.setText(manager.getStudent().getSecondLanguage().name);
//		txtPassword.setText(manager.getStudent().getPassword());
		txtNickname.setText(manager.getStudent().getnickName());
		txtFirstName.setText(manager.getStudent().getFirstName());
		txtLastName.setText(manager.getStudent().getLastName());
		SimpleDateFormat theDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {
			Date d = theDate.parse(manager.getStudent().getBirthday());
			theDate.applyPattern("dd/MM/yyyy");
			txtBirthday.setText(theDate.format(d));
		} catch (ParseException e) {
			Log.e("MAINActivity","Something wrong with the date");
		}
		txtEmail.setText(manager.getStudent().getEmail());
		txtProgramming.setText(manager.getStudent().getProgrammingLang());
		txtFood.setText(manager.getStudent().getFood());
		txtMovie.setText(manager.getStudent().getMovie());
		txtJob.setText(manager.getStudent().getJob());
		if (manager.getStudent().getGender().equalsIgnoreCase("M")){
			radMale.setSelected(true);
		} else {
			radFemale.setSelected(true);
			radMale.setSelected(false);
		}
	}
	public void showPrivacy(){
		// lead to privacy control page
		this.setContentView(R.layout.privacy);
		// call out all relevant elements
		Button cmdPrivacyOK = (Button) this.findViewById(R.id.cmdPrivacyOK);
		CheckBox ckbBirthday = (CheckBox) this.findViewById(R.id.ckbBirthday);
		CheckBox ckbEmail = (CheckBox) this.findViewById(R.id.ckbEmail);
		CheckBox ckbFirstLanguage = (CheckBox) this.findViewById(R.id.ckbFirstLang);
		CheckBox ckbFood = (CheckBox) this.findViewById(R.id.ckbFood);
		CheckBox ckbGender = (CheckBox) this.findViewById(R.id.ckbGender);
		CheckBox ckbJob = (CheckBox) this.findViewById(R.id.ckbJob);
		CheckBox ckbMovie = (CheckBox) this.findViewById(R.id.ckbMovie);
		CheckBox ckbNationality = (CheckBox) this.findViewById(R.id.ckbNationality);
		CheckBox ckbProgram = (CheckBox) this.findViewById(R.id.ckbProgram);
		CheckBox ckbSecondLanguage = (CheckBox) this.findViewById(R.id.ckbSecondLang);
		
		// add event listeners
		cmdPrivacyOK.setOnClickListener(events);
		// apply content  here
		ckbBirthday.setChecked(manager.getStudentPrivacy().isBirthday());
		ckbEmail.setChecked(manager.getStudentPrivacy().isEmail());
		ckbFirstLanguage.setChecked(manager.getStudentPrivacy().isFirstLanguage());
		ckbFood.setChecked(manager.getStudentPrivacy().isFood());
		ckbGender.setChecked(manager.getStudentPrivacy().isGender());
		ckbJob.setChecked(manager.getStudentPrivacy().isJob());
		ckbMovie.setChecked(manager.getStudentPrivacy().isMovie());
		ckbNationality.setChecked(manager.getStudentPrivacy().isNationalityId());
		ckbProgram.setChecked(manager.getStudentPrivacy().isProgrammingLang());
		ckbSecondLanguage.setChecked(manager.getStudentPrivacy().isSecondLanguage());
	}
	public void showUnit(){
		// lead to unit enrolment page
		this.setContentView(R.layout.unit_profile);
		units.show();
	}
	
	public void showMatch(){
		// lead to match configuration page
		this.setContentView(R.layout.match);
		// call out all relevant elements
		Button cmdMatchOK = (Button) this.findViewById(R.id.cmdMatchOK);
		CheckBox ckbUnit = (CheckBox) this.findViewById(R.id.ckbMatchUnit);
		CheckBox ckbCourse = (CheckBox) this.findViewById(R.id.ckbMatchCourse);
		// add event listeners
		cmdMatchOK.setOnClickListener(events);
		// apply matching profile settings
		ckbUnit.setChecked(manager.isMatchUnit());
		ckbCourse.setChecked(manager.isMatchCourse());
	}
	public void showMap(){
		this.setContentView(R.layout.map);
		// call out to all relevant elements
		Button cmdMatch = (Button) this.findViewById(R.id.cmdMatch);
		// add event listeners
		cmdMatch.setOnClickListener(events);
		// apply map parameters
		MapView map = (MapView) this.findViewById(R.id.map);
		map.getController().setZoom(16);
		// get the centre point from manager
		double x = 0;
		double y = 0;
		if (Math.abs(manager.LOCATION_1[0]) > Math.abs(manager.LOCATION_2[0])){
			x = manager.LOCATION_1[0]+(manager.LOCATION_1[0] - manager.LOCATION_2[0])/2;
		} else {
			x = manager.LOCATION_1[0]-(manager.LOCATION_1[0] - manager.LOCATION_2[0])/2;
		}
		if (Math.abs(manager.LOCATION_1[1]) >Math.abs(manager.LOCATION_2[1])){
			y = manager.LOCATION_1[1]+(manager.LOCATION_1[1] - manager.LOCATION_2[1])/2;
		} else {
			y = manager.LOCATION_1[1]-(manager.LOCATION_1[1] - manager.LOCATION_2[1])/2;
		}
		map.getController().setCenter(new GeoPoint(x,y));
		map.setBuiltInZoomControls(true);
		// plot the student "own" location
		Drawable icon = this.getResources().getDrawable(R.drawable.location_marker);
		poiOverlay = new DefaultItemizedOverlay(icon);
		OverlayItem studentPoint = new OverlayItem(new GeoPoint(manager.getStudent().getLatitude(),manager.getStudent().getLongtitude()),"YOU","");
		// add further point here...
		for (Student foundStudent: manager.getStudentList()){
			Log.v("MAINActivity","Adding Point x="+foundStudent.getLatitude()+" y="+foundStudent.getLongtitude());
			OverlayItem studentPoint1 = new OverlayItem(new GeoPoint(foundStudent.getLatitude(),foundStudent.getLongtitude()),foundStudent.getFirstName()+" "+foundStudent.getLastName(),"");
			poiOverlay.addItem(studentPoint1);
			
//			mapList.add(index, foundStudent);
		}
		poiOverlay.addItem(studentPoint);
		annotation = new AnnotationView(map);
		// add map specific event listeners
		poiOverlay.setTapListener(new ItemizedOverlay.OverlayTapListener(){
			@Override
			public void onTap(GeoPoint arg0, MapView arg1) {
				int lastTouchedIndex = poiOverlay.getLastFocusedIndex();
				if (lastTouchedIndex > -1){
					OverlayItem tapped = poiOverlay.getItem(lastTouchedIndex);
					annotation.showAnnotationView(tapped);
					// use alert box to show further information here
					
				}
				
			}

		});
		map.getOverlays().add(poiOverlay);
	}
	public void clearMap(){
		// clear current overlay
		MapView map = (MapView) this.findViewById(R.id.map);
		map.getOverlay().clear();
		poiOverlay.clear();
	}
	
	
	//============================================================================
	// GETTERs AND SETTERs
	//============================================================================
	public Menu getMenu() {
		return menu;
	}

	public eventProcessor getEvents() {
		return events;
	}

	public void setEvents(eventProcessor events) {
		this.events = events;
	}

	public unitProcessor getUnits() {
		return units;
	}

	public void setUnits(unitProcessor units) {
		this.units = units;
	}

	public DefaultItemizedOverlay getPOI_OVERLAY() {
		return poiOverlay;
	}

}
