package transcloud.app.neonblogger.tabs;

import transcloud.app.neonblogger.NeonApp;
import transcloud.app.neonblogger.NeonBloggerActivity;
import transcloud.app.neonblogger.NeonDashbordActivity;
import transcloud.app.neonblogger.NeonSession;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

public class NeonLogoutActivity extends Activity {

	NeonApp mNeonApp = NeonBloggerActivity.mNeonApp;
	NeonSession neonSession = mNeonApp.getSession();
	String userName = neonSession.getUserName();
	ListView rDashboardListView = null;
	private static final int LOGOUT = 1;

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case LOGOUT:
			return new AlertDialog.Builder(NeonLogoutActivity.this)
					.setTitle(" Hi "+userName+", Are you sure you want to logout")
					.setPositiveButton("yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									mNeonApp.setNeonSession(null);
									Intent intent = new Intent(getApplicationContext(), NeonBloggerActivity.class);
							    	startActivity(intent);
									
								}
							})
					.setNegativeButton("no",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									Intent intent = new Intent(getApplicationContext(), NeonDashbordActivity.class);
							    	startActivity(intent);
								}
							}).create();
		}
		return null;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		showDialog(LOGOUT);

	}

}
