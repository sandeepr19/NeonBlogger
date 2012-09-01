package transcloud.app.neonblogger.tabs;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;

import transcloud.app.neonblogger.NeonApp;
import transcloud.app.neonblogger.NeonBloggerActivity;
import transcloud.app.neonblogger.NeonDashbordActivity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NeonDownloadAcitivity extends ListActivity {
	NeonApp mNeonApp = NeonBloggerActivity.mNeonApp;
	ListView rDashboardListView = null;
	private String[] listOfFiles;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		listOfFiles = mNeonApp.getDownloadedList();
		for (int i = 0; i < listOfFiles.length; i++) {
			listOfFiles[i] = listOfFiles[i].replace("\\", "");
		}
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice, listOfFiles));
		final ListView listView = getListView();
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		String fileName = listOfFiles[position];
		downloadFiles(fileName);
	}

	protected void downloadFiles(String urlStr) {

		HttpURLConnection connection = null;
		URL url;
		try {
			File dir = new File("/sdcard/download/");
			if (dir.exists() == false) {
				dir.mkdirs();
			}

			url = new URL("http://neon.phpfogapp.com//files/"+urlStr.substring(urlStr.lastIndexOf("/")).replace("\"", "")); // you can write here any link
			File file = new File(dir, "downloadedFile");
			long startTime = System.currentTimeMillis();
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			connection.connect();

			
			InputStream is = connection.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(5000);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baf.toByteArray());
			fos.flush();
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		Intent intent = new Intent(getApplicationContext(),
				NeonDashbordActivity.class);
		startActivity(intent);
	}

}
