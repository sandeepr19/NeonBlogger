package transcloud.app.neonblogger;

import transcloud.app.neonblogger.tabs.NeonBackUpActivity;
import transcloud.app.neonblogger.tabs.NeonCreateBlogsActivity;
import transcloud.app.neonblogger.tabs.NeonHomePageActivity;
import transcloud.app.neonblogger.tabs.NeonLogoutActivity;
import transcloud.app.neonblogger.tabs.NeonManageBlogsActivity;
import transcloud.app.neonblogger.tabs.NeonMediaContentActivity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TabHost;

public class NeonDashbordActivity extends TabActivity {

	NeonApp mNeonApp = NeonBloggerActivity.mNeonApp;
	ListView rDashboardListView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final TabHost neonTabs = getTabHost();
		neonTabs.addTab(neonTabs.newTabSpec("homeTab").setIndicator("Home")
				.setContent(new Intent(this, NeonHomePageActivity.class)));
		neonTabs.addTab(neonTabs.newTabSpec("createBlogsTab")
				.setIndicator("Create")
				.setContent(new Intent(this, NeonCreateBlogsActivity.class)));
		neonTabs.addTab(neonTabs.newTabSpec("manageBlogsTab")
				.setIndicator("Manage")
				.setContent(new Intent(this, NeonManageBlogsActivity.class)));
		neonTabs.addTab(neonTabs.newTabSpec("manageMediaContent")
				.setIndicator("Media")
				.setContent(new Intent(this, NeonMediaContentActivity.class)));
		neonTabs.addTab(neonTabs.newTabSpec("backupTab").setIndicator("Backup")
				.setContent(new Intent(this, NeonBackUpActivity.class)));
		neonTabs.addTab(neonTabs.newTabSpec("logoutTab").setIndicator("Logout")
				.setContent(new Intent(this, NeonLogoutActivity.class)));
		
		
		
	}

}
