package transcloud.app.neonblogger.tabs;

import transcloud.app.neonblogger.NeonApp;
import transcloud.app.neonblogger.NeonBloggerActivity;
import transcloud.app.neonblogger.NeonDashbordActivity;
import transcloud.app.neonblogger.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class NeonMediaContentActivity extends Activity {

	NeonApp mNeonApp = NeonBloggerActivity.mNeonApp;
	ListView rDashboardListView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.media);
	}

	public void onClickUpload(View view) {
		Intent intent = new Intent(getApplicationContext(), FileChooser.class);
    	startActivity(intent);
	}

	public void onClickDownload(View view) {
		Intent intent = new Intent(getApplicationContext(), NeonDownloadAcitivity.class);
    	startActivity(intent);
	}
}
