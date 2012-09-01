package transcloud.app.neonblogger.tabs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import transcloud.app.neonblogger.NeonApp;
import transcloud.app.neonblogger.NeonBloggerActivity;
import transcloud.app.neonblogger.NeonDashbordActivity;
import transcloud.app.neonblogger.R;
import transcloud.app.neonblogger.objects.NeonBlog;
import transcloud.app.neonblogger.objects.NeonBlogStatus;
import transcloud.app.neonblogger.objects.NeonUser;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class NeonUpdateBlogsActivity extends Activity {

	NeonApp mNeonApp = NeonBloggerActivity.mNeonApp;
	ListView rDashboardListView = null;
	private Long blogId;
	private EditText titleUpdate;
	private EditText contentUpdate;
	private EditText tagsUpdate;
	private Spinner statusUpdate;
	private static final String EMPTY_STRING = "";
	private String[] statusValues = { "DRAFT", "PUBLISHED", "ARCHIVED",
			"PENDING_APPROVAL", "APPROVED " };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_blog);
		SharedPreferences pref = getSharedPreferences("myPreferences", 0);
		blogId = pref.getLong("blogId", 0);
		DateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy");
		String concatenatedTag = "";
		NeonBlog blog = mNeonApp.getBlogPost(blogId);
		for (String tag : blog.getTags()) {
			concatenatedTag = concatenatedTag.concat(tag).concat(",");
		}
		titleUpdate = (EditText) findViewById(R.id.titleUpdate);
		titleUpdate.setText(blog.getTitle());
		contentUpdate = (EditText) findViewById(R.id.contentUpdate);
		contentUpdate.setText(blog.getContent());
		ArrayAdapter adapter = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, statusValues);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		statusUpdate = (Spinner) findViewById(R.id.statusSpinnerUpdate);
		statusUpdate.setAdapter(adapter);
		statusUpdate.setSelection(blog.getStatus().ordinal());
		tagsUpdate = (EditText) findViewById(R.id.tagsUpdate);
		tagsUpdate.setText(concatenatedTag);

	}

	public void onClickUpdate(View view) {

		String title = titleUpdate.getText().toString();
		String content = contentUpdate.getText().toString();
		String tagsText = tagsUpdate.getText().toString();
		int status = statusUpdate.getSelectedItemPosition();
		String[] tags = null;
		if (!tagsUpdate.getText().toString().equalsIgnoreCase("")) {
			tags = tagsUpdate.getText().toString().split(",");
		}
		if (!authenticate(title, content,tagsText)) {
			if (title.equalsIgnoreCase("")) {
				Toast.makeText(getApplicationContext(), "Please enter title",
						Toast.LENGTH_SHORT).show();
			}
			if (content.equalsIgnoreCase("")) {
				Toast.makeText(getApplicationContext(), "Please enter content",
						Toast.LENGTH_SHORT).show();
			}
			if (tagsText.equalsIgnoreCase("")) {
				Toast.makeText(getApplicationContext(), "Please enter tags",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			NeonBlog blog = new NeonBlog();
			NeonUser user = new NeonUser();
			user.setUserID(Long.parseLong(mNeonApp.getSession().getUserId()));
			blog.setId(blogId);
			blog.setAuthor(user);
			blog.setTitle(title);
			blog.setContent(content);
			blog.setTags(tags);
			blog.setStatus(NeonBlogStatus.values()[status]);
			if (mNeonApp.updateBlog(blog, mNeonApp.getSession().getToken())) {
				Intent intent = new Intent(getApplicationContext(),
						NeonDashbordActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(),
						"Sorry, record could not be updated",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void onClickHome(View view) {
		Intent intent = new Intent(getApplicationContext(),
				NeonDashbordActivity.class);
		startActivity(intent);
	}

	public boolean authenticate(String title, String content, String tagsText) {
		if (title.equalsIgnoreCase(EMPTY_STRING)
				|| content.equalsIgnoreCase(EMPTY_STRING) || tagsText.equalsIgnoreCase(EMPTY_STRING))
			return false;
		else
			return true;
	}
}
