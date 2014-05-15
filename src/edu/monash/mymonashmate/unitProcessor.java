package edu.monash.mymonashmate;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;



public class unitProcessor implements OnClickListener{
	private mateManager manager;
	private MainActivity activity;
	private int enrolmentCount=0;
	private final int LINE_ID_BASE = 100;
	private LinearLayout lstEnrolment;
	private AutoCompleteTextView txtaUnit;
	
	public unitProcessor(mateManager manager, MainActivity activity){
		this.manager = manager;
		this.activity = activity;
	}
	
	public void show(){
		// call out all relevant elements
		Button cmdUnitOK = (Button) activity.findViewById(R.id.cmdUnitOk);
		Button cmdUnitAdd = (Button) activity.findViewById(R.id.cmdUnitAdd);
		lstEnrolment = (LinearLayout) activity.findViewById(R.id.lstEnrolment);
		txtaUnit = (AutoCompleteTextView) activity.findViewById(R.id.txtaUnit);
		// add event listeners
		cmdUnitOK.setOnClickListener(this);
		cmdUnitAdd.setOnClickListener(this);
		// add content here?
		txtaUnit.setAdapter(new ArrayAdapter<Unit>(activity,android.R.layout.simple_dropdown_item_1line,manager.getUnitList()));
		for(Unit unit : manager.getStudentUnitList()){	
			this.add(unit);
		}
	}
	
	public void add(Unit unit){
		// add new unit to the list
		LinearLayout line;
		TextView lblUnit;
		Button cmdUnit;
		
		line = new LinearLayout(activity);
		line.setId(enrolmentCount+LINE_ID_BASE);
		line.setTag(unit.getId());
		line.setOrientation(LinearLayout.HORIZONTAL);
		lblUnit = new TextView(activity);
		lblUnit.setText(unit.getId()+"-"+unit.getName());
		lblUnit.setLayoutParams(new LayoutParams(300,50));
		lblUnit.setId(enrolmentCount+LINE_ID_BASE+LINE_ID_BASE);
		cmdUnit = new Button(activity);
		cmdUnit.setId(enrolmentCount);
		cmdUnit.setText("Remove");
		cmdUnit.setOnClickListener(this);
		// add all child into parent
		line.addView(lblUnit);
		line.addView(cmdUnit);
		// add parent into the list
		lstEnrolment.addView(line);
		enrolmentCount++;
	}
	@Override
	public void onClick(View view) {
		Log.v("UnitProcessor","clickId="+view.getId());
		if (view.getId() == R.id.cmdUnitAdd){
			// add new unit to enrolment list
			// use solely the unit code for entry!
			String unitEntry = txtaUnit.getText().toString();
			String[] unitContent = unitEntry.split("-");
			// check for duplicate entry
			for (Unit unit: manager.getStudentUnitList()){
				if (unit.getId().equals(unitContent[0])){
					// alert user about the duplicated entry
					Toast.makeText(activity, "Unit Repeated", Toast.LENGTH_SHORT).show();
					txtaUnit.setText("");
					return; // leave the process early 
				}
			}
			// look for unit code from the manager's unit list
			for(Unit unit: manager.getUnitList()){
				if (unit.getId().equals(unitContent[0])){
					// alert user about the added entry
					Toast.makeText(activity, "Unit "+txtaUnit.getText()+" Added", Toast.LENGTH_SHORT).show();
					txtaUnit.setText("");
					manager.getStudentUnitList().add(unit);
					this.add(unit);
				}
			}
		} else if (view.getId() == R.id.cmdUnitOk){
			// finalise unit list and back to main page
			// call manager to update the current enrolment list
			manager.updateUnit();
		} else if (view.getId() < LINE_ID_BASE ){
			// perform deletion of a unit
			LinearLayout line = (LinearLayout) activity.findViewById(LINE_ID_BASE+view.getId());
			TextView lblUnit = (TextView) activity.findViewById(view.getId()+LINE_ID_BASE+LINE_ID_BASE);
			Log.v("UnitProcessor","deletingUnitCode=" + lblUnit.getText() );
			// use solely the unit code for entry!
			String unitEntry = lblUnit.getText().toString();
			String[] unitContent = unitEntry.split("-");
			for (Unit unit: manager.getStudentUnitList()){
				if (unit.getId().equals(unitContent[0])){
					manager.getStudentUnitList().remove(unit);
					lstEnrolment.removeView(line);
					// alert user about the deleted entry
					Toast.makeText(activity, "Unit "+lblUnit.getText()+" Deleted", Toast.LENGTH_SHORT).show();
				}
			}
			
		}
	}
	
}

