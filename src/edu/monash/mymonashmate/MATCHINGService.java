package edu.monash.mymonashmate;

import java.util.ArrayList;

import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.OverlayItem;

import android.os.AsyncTask;
import android.util.Log;

/*==========================================================================
 *  MATCHINGService
 * =========================================================================
 *  This class aids mateManager class from running some process.
 *  
 *  it is clearly possible that some procedures are a little bit too fast to wait,
 *  thus making some unwanted errors flagging lack of content 
 *  where the pending content is actually arriving.
 *  
 *  This class therefore perform some waits in the background so that the 
 *  application can wait for the matching information
 * 
 * 
 */

public class MATCHINGService extends AsyncTask<String,Void,String>{
	private mateManager manager;
	private RESTFULService rest;
	private MainActivity activity;
	
	public MATCHINGService(mateManager manager, MainActivity activity){
		this.manager = manager;
		this.activity = activity;
	}
	
	@Override
	protected String doInBackground(String... params) {
		Log.v("MATCHINGSerivce","Matching Service called");
		Log.v("MATCHINGSerivce","Matching Service="+params[0]);
		// check whether there are something inside
		if (params[0].equals("course")){
			Log.v("MATCHINGSerivce","Match Student Course Profile Called");
			// use the course id to find all students
			rest = new RESTFULService(activity,manager);
			rest.setAddress("courses/enrolment/"+manager.getStudent().getCourseId().id);
			while (!RESTFULService.isRunning()){
				rest.execute("get","profile","course");
				break; // this is run once only
			}
		} else if (params[0].equals("unit")){
			Log.v("MATCHINGSerivce","Match Student Unit Profile Called");
			// use the unit id to find all students
			for(Unit unit: manager.getStudentUnitList()){
				rest = new RESTFULService(activity,manager);
				rest.setAddress("units/enrolment/"+unit.getId());
				while (!RESTFULService.isRunning()){
					rest.execute("get","profile","unit");
					break; // this is run once only
				}
			}
		}
		
		return null;
	}
	
	public void matchProfiles(){
		// get an empty student list
		ArrayList<Student> newStudentList = new ArrayList<Student>();
		// clear current matching score list
		manager.getMatchList().clear();
		while (!RESTFULService.isRunning()){
			// check for duplicated entry of the current download list, and apply match score (duplicated times)
			for (Student eachStudent: manager.getStudentList()){
				int matchingScore = 0;
				for (Student compareStudent : manager.getStudentList()){
					if (eachStudent.equals(compareStudent)){
						matchingScore++;
						continue;
					} 
					newStudentList.add(eachStudent);
				}
				// comparison completed, apply the matching score
				manager.getMatchList().add(eachStudent.getId(), matchingScore);
			}
			manager.setStudentList(newStudentList);
			break;
		}
		// apply new content to the map
		Log.v("MATCHINGSerivce","Matched Students="+newStudentList.size());
		activity.showMap();
	}
	
	@Override
	protected void onPostExecute(String result){
		// this process perform after finishing everything
		this.matchProfiles();
	}

}
