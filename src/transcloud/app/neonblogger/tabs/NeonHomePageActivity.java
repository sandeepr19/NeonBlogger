package transcloud.app.neonblogger.tabs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import transcloud.app.neonblogger.NeonApp;
import transcloud.app.neonblogger.NeonBloggerActivity;
import transcloud.app.neonblogger.objects.NeonBlog;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class NeonHomePageActivity extends ListActivity {

	NeonApp mNeonApp = NeonBloggerActivity.mNeonApp;
	ListView rDashboardListView = null;
	public String[] listOfTitles;
	public String[] listOfContents;
	public boolean[] listOfExpanded;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		List<NeonBlog> listOfBlogs = mNeonApp.getBlogPosts();
		listOfTitles = new String[listOfBlogs.size()];
		listOfContents = new String[listOfBlogs.size()];
		listOfExpanded = new boolean[listOfBlogs.size()];
		DateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy");
		for (int i = 0; i < listOfBlogs.size(); i++) {
			listOfTitles[i] = listOfBlogs.get(i).getTitle();
			String tags = "";
			for (String tag : listOfBlogs.get(i).getTags()) {
				tags = tags.concat(tag);
			}
			listOfContents[i] = "Posted by "
					+ listOfBlogs.get(i).getAuthor().getUsername() + " On	"
					+ dateFormat.format(listOfBlogs.get(i).getCreated()) + "\n"
					+ "Content:	" + listOfBlogs.get(i).getContent() + "\n"
					+ "Status:" + listOfBlogs.get(i).getStatus();

			listOfExpanded[i] = false;
		}

		setListAdapter(new BlogsListAdapter(this));

	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		((BlogsListAdapter) getListAdapter()).toggle(position);
	}

	private class BlogsListAdapter extends BaseAdapter {
		private Context mContext;

		private String[] mTitles = listOfTitles;
		private String[] mDialogue = listOfContents;
		private boolean[] mExpanded = listOfExpanded;

		public BlogsListAdapter(Context context) {
			mContext = context;
		}

		public int getCount() {
			return mTitles.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			BlogView sv;
			if (convertView == null) {
				sv = new BlogView(mContext, mTitles[position],
						mDialogue[position], mExpanded[position]);
			} else {
				sv = (BlogView) convertView;
				sv.setTitle(mTitles[position]);
				sv.setDialogue(mDialogue[position]);
				sv.setExpanded(mExpanded[position]);
			}

			return sv;
		}

		public void toggle(int position) {
			mExpanded[position] = !mExpanded[position];
			notifyDataSetChanged();
		}

	}

	private class BlogView extends LinearLayout {
		private TextView mTitle;
		private TextView mDialogue;

		public BlogView(Context context, String title, String dialogue,
				boolean expanded) {
			super(context);
			this.setOrientation(VERTICAL);

			mTitle = new TextView(context);
			mTitle.setText(title);
			addView(mTitle, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			mDialogue = new TextView(context);
			mDialogue.setText(dialogue);
			addView(mDialogue, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			mDialogue.setVisibility(expanded ? VISIBLE : GONE);
		}

		public void setTitle(String title) {
			mTitle.setText(title);
		}

		public void setDialogue(String words) {
			mDialogue.setText(words);
		}

		public void setExpanded(boolean expanded) {
			mDialogue.setVisibility(expanded ? VISIBLE : GONE);
		}
	}
}
