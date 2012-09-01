package transcloud.app.neonblogger.tabs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import transcloud.app.neonblogger.NeonApp;
import transcloud.app.neonblogger.NeonBloggerActivity;
import transcloud.app.neonblogger.NeonDashbordActivity;
import transcloud.app.neonblogger.R;
import transcloud.app.neonblogger.objects.NeonBlog;
import transcloud.app.neonblogger.objects.NeonBlogStatus;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class NeonViewBlogsActivity  extends Activity {
	
	NeonApp mNeonApp = NeonBloggerActivity.mNeonApp;
	ListView rDashboardListView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_blog);
		SharedPreferences pref = getSharedPreferences("myPreferences",0);
		Long blogId=pref.getLong("blogId", 0);
		DateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy");
		String concatenatedTag = "";
		NeonBlog blog = mNeonApp.getBlogPost(blogId);
		for (String tag : blog.getTags()) {
			concatenatedTag = concatenatedTag.concat(tag).concat(",");
		}
		TextView titleView = (TextView)findViewById(R.id.titleView);
		titleView.setText(blog.getTitle());
		TextView contentView = (TextView)findViewById(R.id.contentView);
		contentView.setText(blog.getContent());
		TextView statusView = (TextView)findViewById(R.id.statusView);
		statusView.setText(blog.getStatus().name());
		TextView createdTimeView = (TextView)findViewById(R.id.createdTimeView);
		createdTimeView.setText(dateFormat.format(blog.getCreated()).toString());
		TextView updatedTimeView = (TextView)findViewById(R.id.updatedTimeView);
		updatedTimeView.setText(dateFormat.format(blog.getLateUpdated()).toString());
		TextView tagsView = (TextView)findViewById(R.id.tagsView);
		tagsView.setText(concatenatedTag);
		
	}

	public void onClickHome(View view) {
		Intent intent = new Intent(getApplicationContext(), NeonDashbordActivity.class);
    	startActivity(intent);
	}
}
