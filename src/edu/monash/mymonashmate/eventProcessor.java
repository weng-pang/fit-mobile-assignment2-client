package edu.monash.mymonashmate;

import android.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class eventProcessor implements OnClickListener{
	private mateManager manager;
	private MainActivity activity;
	
	public eventProcessor(mateManager manager, MainActivity activity){
		this.manager = manager; // map the manager object to this class
		this.activity = activity;
	}
	
	@Override
	public void onClick(View view) {
		Log.v("MyMonashMateEvent","clickBy="+view.getId());
		AlertDialog.Builder msgBox = new AlertDialog.Builder(activity);
		msgBox.setTitle("MyMonashMate");
		msgBox.setPositiveButton("OK", null);
		
		// All event will be processed here
		// login button is clicked
		if (view.getId() == R.id.cmdLogin){
			EditText txtEmail = (EditText)activity.findViewById(R.id.txtLoginEmail);
			EditText txtPassword = (EditText)activity.findViewById(R.id.txtLoginPassword);
			// get the form content
			String email = txtEmail.getText().toString();
			String password = txtPassword.getText().toString();
			// validate the content -- ensure all fields are not empty
			// if any of them is empty, a message will be shown to the user
			if (email.isEmpty()){
				msgBox.setMessage("Email Field is empty");
				msgBox.create().show();
			} else if (password.isEmpty()){
				msgBox.setMessage("Password Field is empty");
				msgBox.create().show();
			} else {
				Log.v("MyMonashMateEvent","email="+email+" password="+password);
				// call manager for login
				manager.login(email, password);
			}

		} else 
		// register button is clicked
		if (view.getId() == R.id.cmdRegister){
			// redirect to register view
			activity.showRegister();
			
		} else if (view.getId() == R.id.cmdResReturn){
			// back to login page
			activity.loginPage();
		} else if (view.getId() == R.id.cmdResSubmit){
			// process the register page
			// call the manager for processing
			manager.createStudent();
		} else if (view.getId() == R.id.cmdPrivacyOK){
			// process the new privacy settings
			manager.configurePrivacy();
		} else if (view.getId() == R.id.cmdMatch){
			// process matching profile for student
			manager.matchProfile();
		} else if (view.getId() == R.id.cmdMatchOK){
			// process the update match pattern
			manager.changeMatchProfile();
		} else if (view.getId() == R.id.cmdReturn){
			// return to main map page
			activity.showMap();
		} else if (view.getId() == R.id.cmdSubmit){
			manager.saveProfile();
		}
		
	}

}
