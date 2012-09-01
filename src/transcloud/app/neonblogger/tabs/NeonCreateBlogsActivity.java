package transcloud.app.neonblogger.tabs;

import transcloud.app.neonblogger.NeonApp;
import transcloud.app.neonblogger.NeonBloggerActivity;
import transcloud.app.neonblogger.NeonDashbordActivity;
import transcloud.app.neonblogger.R;
import transcloud.app.neonblogger.objects.NeonBlog;
import transcloud.app.neonblogger.objects.NeonBlogStatus;
import transcloud.app.neonblogger.objects.NeonUser;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class NeonCreateBlogsActivity extends Activity {

	NeonApp mNeonApp = NeonBloggerActivity.mNeonApp;
	ListView rDashboardListView = null;
	private EditText titleEditText;
	private EditText contentEditText;
	private EditText tagEditText;
	private Spinner statusSpinner;
	private String[] statusValues = { "DRAFT", "PUBLISHED", "ARCHIVED",
			"PENDING_APPROVAL", "APPROVED " };
	private static final String EMPTY_STRING = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_blog);
		statusSpinner = (Spinner) findViewById(R.id.statusSpinner);
		ArrayAdapter adapter = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, statusValues);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		statusSpinner.setAdapter(adapter);
		titleEditText = (EditText) findViewById(R.id.title);
		contentEditText = (EditText) findViewById(R.id.content);
		tagEditText = (EditText) findViewById(R.id.tags);
	}

	public void onClickCreate(View view) {

		String title = titleEditText.getText().toString();
		String content = contentEditText.getText().toString();
		String tagsText = tagEditText.getText().toString();
		int status = statusSpinner.getSelectedItemPosition();

		if (!authenticate(title, content, tagsText)) {
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
			String[] tags = null;
			if (!tagEditText.getText().toString().equalsIgnoreCase("")) {
				tags = tagEditText.getText().toString().split(",");
			}
			NeonBlog blog = new NeonBlog();
			NeonUser user = new NeonUser();
			user.setUserID(Long.parseLong(mNeonApp.getSession().getUserId()));
			blog.setAuthor(user);
			blog.setTitle(title);
			blog.setContent(content);
			blog.setTags(tags);
			blog.setStatus(NeonBlogStatus.values()[status]);
			if (mNeonApp.createBlog(blog, mNeonApp.getSession().getToken())) {
				Intent intent = new Intent(getApplicationContext(),
						NeonDashbordActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(),
						"Sorry, record could not be inserted",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void onClickReset(View view) {
		EditText title = (EditText) findViewById(R.id.title);
		title.setText("");
		EditText content = (EditText) findViewById(R.id.content);
		content.setText("");
		EditText tags = (EditText) findViewById(R.id.tags);
		tags.setText("");
	}

	public boolean authenticate(String title, String content, String tags) {
		if (title.equalsIgnoreCase(EMPTY_STRING)
				|| content.equalsIgnoreCase(EMPTY_STRING)
				|| tags.equalsIgnoreCase(EMPTY_STRING))
			return false;
		else
			return true;
	}

}
