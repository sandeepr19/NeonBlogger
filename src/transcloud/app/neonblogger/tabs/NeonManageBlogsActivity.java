package transcloud.app.neonblogger.tabs;

import java.util.List;

import transcloud.app.neonblogger.NeonApp;
import transcloud.app.neonblogger.NeonBloggerActivity;
import transcloud.app.neonblogger.NeonDashbordActivity;
import transcloud.app.neonblogger.objects.NeonBlog;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class NeonManageBlogsActivity extends ListActivity {

	NeonApp mNeonApp = NeonBloggerActivity.mNeonApp;
	ListView rDashboardListView = null;
	private List<NeonBlog> listOfBlogs;
	private String[] manageArray = {"Update","Delete","View"};
	private final static int DELETE_BUTTON=1;
	private final static int UPDATE_BUTTON=0;
	private final static int VIEW_BUTTON=2;
	
	protected Dialog onCreateDialog(int id) {
		final int blogId=id;
		
		return new AlertDialog.Builder(NeonManageBlogsActivity.this)
				.setTitle("Manage Blogs")
				.setSingleChoiceItems(manageArray, -1,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								SharedPreferences pref = getSharedPreferences("myPreferences",0);;
								Editor editor = pref.edit();
								editor.putLong("blogId", listOfBlogs.get(blogId).getId());
								editor.commit();
								switch(whichButton)
								{
								case DELETE_BUTTON:
									mNeonApp.deletePost(listOfBlogs.get(blogId).getId());
									Intent intent2 = new Intent(getApplicationContext(), NeonDashbordActivity.class);
							    	startActivity(intent2);
									break;
								case UPDATE_BUTTON:
									Intent intent = new Intent(getApplicationContext(), NeonUpdateBlogsActivity.class);
							    	startActivity(intent);
									break;
								case VIEW_BUTTON:
									Intent intent1 = new Intent(getApplicationContext(), NeonViewBlogsActivity.class);
							    	startActivity(intent1);
									break;
								
								}	
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						}).create();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String userId = mNeonApp.getSession().getUserId();
		listOfBlogs = mNeonApp.getBlogPostsForUser(userId);
		setListAdapter(new ArrayAdapter<NeonBlog>(this,
				android.R.layout.simple_list_item_single_choice, listOfBlogs));
		final ListView listView = getListView();
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		showDialog(position);
	}

}
