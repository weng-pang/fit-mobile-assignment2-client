package edu.monash.mymonashmate;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

public class RESTFULService extends AsyncTask<String,Void,String>{
	private static boolean running = false;
	private MainActivity activity;
	private loginListerer login;
	private mateManager manager;
	private Gson gson = new Gson();
	private String[] type;
	private String message;
	private String address;
	private String responseMessage;
	private final String HOST_ADDRESS = "10.0.2.2";
	private final String BASE_URL ="http://"+HOST_ADDRESS+":8080/MyMonashMate/webresources/edu.monash.mate.";
	
	public RESTFULService(MainActivity activity,mateManager manager){
		this.activity = activity;
		this.manager = manager;
	}
	
	public RESTFULService(MainActivity activity, loginListerer login){
		this.activity = activity;
		this.login = login;
	}
	
	public boolean checkConnection(){
		ConnectivityManager connection = (ConnectivityManager) activity.getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connection.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()){
			return true;
		} else {
			return false;
		}
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) { // Rather than address, this is part of the address for restful service
		this.address = address;
	}

	@Override
	protected String doInBackground(String... connectionType) {
		running = true;
		type = connectionType;
		HttpResponse httpResponse = null;
		responseMessage = "";
		// Connection process to taken here
		try{
			Log.v("MyMonashMateRest","Requested Restful="+type[0].toString());
			Log.v("MyMonashMateRest","ADDR="+BASE_URL+address);
			HttpClient client = new DefaultHttpClient();
			if (type[0].equals("get")){
				HttpGet httpGet = new HttpGet(BASE_URL+address);
				httpGet.setHeader("Content-Type", "application/json");
				httpGet.setHeader("Accept", "application/json");
				Log.v("MyMonashMateRest","Restful Client ESTABLISHED");
				httpResponse = client.execute(httpGet);
				
			} else if (type[0].equals("post")){
				// new content post here
				HttpPost httpPost = new HttpPost(BASE_URL+address);
				StringEntity postString = new StringEntity(message);
				httpPost.setEntity(postString);
				httpPost.setHeader("Content-Type", "application/json");
				httpPost.setHeader("Accept", "application/json");
				httpResponse = client.execute(httpPost);
				
			} else if (type[0].equals("put")){
				// modification goes here
				HttpPut httpPut = new HttpPut(BASE_URL+address);
				StringEntity postString = new StringEntity(message);
				httpPut.setEntity(postString);
				httpPut.setHeader("Content-Type", "application/json");
				httpPut.setHeader("Accept", "application/json");
				httpResponse = client.execute(httpPut);
			}
			Log.v("MyMonashMateRest","Restful Client EXECUTED");				
			Log.v("MyMonashMateRest","CODE="+httpResponse.getStatusLine().getStatusCode());
			if (httpResponse.getStatusLine().getStatusCode() == 200){
			InputStream input = httpResponse.getEntity().getContent();
			Log.v("MyMonashMateRest","Restful Client Content Returned");
				if (input != null){
					BufferedReader bufferReader = new BufferedReader(new InputStreamReader(input));
					String line="";
					while ((line=bufferReader.readLine())!=null){
						responseMessage += line;
					}
				}
			} else if (httpResponse.getStatusLine().getStatusCode() == 500){
				Log.w("MyMonashMateRest","Restful Client Content Returned ERROR"); 
			}
		} catch (Exception e){
			Log.d("MyMonashMateREST","Restful Execution ERROR");
			Log.d("MyMonashMateREST", e.getLocalizedMessage());
		}
		Log.v("MyMonashMateRest","content="+responseMessage);
		Log.v("MyMonashMateRest","Restful Connection Almost COMPLETED");
		return responseMessage;
	} 
	
	@Override
	protected void onPostExecute(String result){
		// completion of communication
		Log.v("MyMonashMateRest","Restful Connection COMPLETED");
		if (type[0].equals("get")){
			if (login != null){ // call relevant after login procedures
				login.afterLogin(responseMessage);
				// The following area handles basic content updates - unit, course and language lists
			} else if (address.equals("units")){
				manager.setUnitList((ArrayList<Unit>) gson.fromJson(responseMessage, new TypeToken<ArrayList<Unit>>(){}.getType()));
			} else if (address.equals("courses")){
				manager.setCourseList((ArrayList<courseId>) gson.fromJson(responseMessage, new TypeToken<ArrayList<courseId>>(){}.getType()));
			} else if (address.equals("languages")){
				manager.setLanguageList((ArrayList<Language>) gson.fromJson(responseMessage, new TypeToken<ArrayList<Language>>(){}.getType()));
			} else if (address.equals("nationalities")){
				manager.setNationList((ArrayList<nationalityId>) gson.fromJson(responseMessage, new TypeToken<ArrayList<nationalityId>>(){}.getType()));
			} else if (address.equals("privacycontrol/"+manager.getStudent().getId())){
				// Feed-IN privacy details
				manager.setStudentPrivacy(gson.fromJson(responseMessage, privacyControl.class));
				// test whether the content has been fed in
				Log.v("MyMonashMateRest","privacycontent="+manager.getStudentPrivacy().getId());
			} else if (address.equals("students/enrolment/"+manager.getStudent().getId())){
				// Feed-In enrolment details
				manager.setStudentUnitList((ArrayList<Unit>) gson.fromJson(responseMessage, new TypeToken<ArrayList<Unit>>(){}.getType()));
			}
			try{
				if (type[1].equals("profile")){
					ArrayList<Student> rawList = gson.fromJson(responseMessage, new TypeToken<ArrayList<Student>>(){}.getType());
					for (Student eachStudent : rawList){
						// the student own profile will not be considered
						if (eachStudent.equals(manager.getStudent())){
							// move to next iteration
							continue;
						}
						manager.getStudentList().add(eachStudent);
						// simply add the student object here first, duplication check will be performed by the manager.
					}
	
				}
			} catch (ArrayIndexOutOfBoundsException e){
				// this is just normal , but it will be reported to control interface
				Log.v("MyMonashMateRest","Type Parameter is insufficient");
			}
		}
		running = false;
		return;
	}

	public static boolean isRunning() {
		return running;
	}

}