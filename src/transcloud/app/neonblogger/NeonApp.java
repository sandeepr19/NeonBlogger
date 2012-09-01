package transcloud.app.neonblogger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import transcloud.app.neonblogger.objects.NeonBlog;
import transcloud.app.neonblogger.objects.NeonBlogStatus;
import transcloud.app.neonblogger.objects.NeonConnectionType;
import transcloud.app.neonblogger.objects.NeonUser;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class NeonApp {

	private static final String TAG = "NeonApp";

	private NeonSession mSession;
	private ProgressDialog mProgress;
	private NeonConnection mConnection;
	private INeonAppListener listener;

	private static final int WHAT_AUTHENTICATION = 0;
	private static final int ARG_SUCCESS = 0;
	private static final int ARG_FAILURE = 1;

	private static final String RESPONSE_DELIMITER = ",";

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == WHAT_AUTHENTICATION && msg.arg1 == ARG_SUCCESS) {
				// mProgress.dismiss();
				listener.onSuccessfulAuthentication();
			} else if (msg.what == WHAT_AUTHENTICATION
					&& msg.arg1 == ARG_FAILURE) {
				// mProgress.dismiss();
				listener.onFailedAuthentication();
			}
		}
	};

	public NeonApp(Context context) {
		mSession = new NeonSession(context);
		mProgress = new ProgressDialog(context);
		mProgress.setCancelable(false);
		mConnection = NeonConnection.getConnection();
	}

	public void setListener(INeonAppListener listener) {
		this.listener = listener;
	}

	public void authorize(String userName, String pwd) {
		mProgress.setMessage("Logging In");
		// mProgress.show();
		final String url = "/authentication/login";
		final String[] urlParams = new String[2];
		urlParams[0] = "username=" + userName;
		urlParams[1] = "password=" + pwd;

		new Thread() {
			public void run() {
				Log.d(TAG, "request url = " + url);
				try {
					String resp = mConnection.getResposeForRequest(url,
							urlParams, NeonConnectionType.POST);
					Log.d(TAG, "response = " + resp);
					String response[] = resp.split(RESPONSE_DELIMITER);
					if (response[0].startsWith("success")) {
						String user = response[1].substring(
								response[1].indexOf("=") + 1).trim();
						String userId = response[2].substring(
								response[2].indexOf("=") + 1).trim();
						String token = response[3].substring(
								response[3].indexOf("=") + 1).trim();
						mSession.storeToken(user, userId, token);
						mHandler.sendMessage(mHandler.obtainMessage(
								WHAT_AUTHENTICATION, ARG_SUCCESS, 0));
					} else {
						mHandler.sendMessage(mHandler.obtainMessage(
								WHAT_AUTHENTICATION, ARG_FAILURE, 0));
					}
				} catch (ConnectException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void authorize(String token) {
		mProgress.setMessage("Authenticating");
		mProgress.show();
		final String url = "/authorize";
		final String[] urlParams = new String[1];
		urlParams[0] = "token=" + token;
		new Thread() {
			public void run() {
				Log.d(TAG, "request url = " + url);
				try {
					String resp = mConnection.getResposeForRequest(url,
							urlParams, NeonConnectionType.POST);
					Log.d(TAG, "response = " + resp);
					String response[] = resp.split(RESPONSE_DELIMITER);
					if (response[0].startsWith("success")) {
						String user = response[1].substring(
								response[1].indexOf("=") + 1).trim();
						String userId = response[2].substring(
								response[2].indexOf("=") + 1).trim();
						String token = response[3].substring(
								response[3].indexOf("=") + 1).trim();
						mSession.storeToken(user, userId, token);
						mHandler.sendMessage(mHandler.obtainMessage(
								WHAT_AUTHENTICATION, ARG_SUCCESS, 0));
					} else {
						mHandler.sendMessage(mHandler.obtainMessage(
								WHAT_AUTHENTICATION, ARG_FAILURE, 0));
					}
				} catch (ConnectException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public NeonSession getSession() {
		return this.mSession;
	}

	public void setNeonSession(NeonSession session) {
		this.mSession = session;
	}

	public List<NeonBlog> getBlogPosts() {
		final String url = "/api/posts";
		// new Thread() {
		// public void run() {
		try {
			List<NeonBlog> blogs = new ArrayList<NeonBlog>();
			List<NeonUser> users = new ArrayList<NeonUser>();
			Log.d(TAG, "request url = " + url);
			String resp1 = mConnection.getResposeForRequest("/api/users", null,
					NeonConnectionType.GET);
			JSONObject userObject = (JSONObject) new JSONTokener(resp1)
					.nextValue();
			JSONArray userArray = userObject.getJSONArray("users");
			NeonUser user = null;
			JSONObject userJsonObject = null;
			for (int i = 0; i < userArray.length(); ++i) {
				user = new NeonUser();
				userJsonObject = (JSONObject) userArray.get(i);
				user.setUserID(userJsonObject.getLong("id"));
				user.setUsername(userJsonObject.getString("username"));
				users.add(user);
			}
			String resp = mConnection.getResposeForRequest(url, null,
					NeonConnectionType.GET);
			Log.d(TAG, "response = " + resp);
			JSONObject respObject = (JSONObject) new JSONTokener(resp)
					.nextValue();
			Log.d(TAG, "respObject.length() == " + respObject.length());
			JSONArray blogArray = respObject.getJSONArray("blogs");
			Log.d(TAG, "blogArray.length() == " + blogArray.length());
			NeonBlog blog = null;
			JSONObject blogObject = null;
			for (int i = 0; i < blogArray.length(); ++i) {
				blog = new NeonBlog();
				blogObject = (JSONObject) blogArray.get(i);
				if (blogObject.getInt("status") != 1)
					continue;
				blog.setStatus(NeonBlogStatus.values()[blogObject
						.getInt("status")]);
				blog.setId(blogObject.getLong("id"));
				blog.setTitle(blogObject.getString("title"));
				blog.setContent(blogObject.getString("content"));
				blog.setTags((blogObject.getString("tags").trim())
						.split(RESPONSE_DELIMITER));
				blog.setCreated(new Date(blogObject.getLong("create_time")));
				blog.setLateUpdated(new Date(blogObject.getLong("update_time")));
				blog.setAuthor(users.get(blogObject.getInt("author_id") - 1));
				Log.d(TAG, " --> " + blog);
				blogs.add(blog);
			}
			return blogs;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ConnectException e) {
			e.printStackTrace();
		}
		return null;
		// }
		// }.start();
	}

	public NeonUser getUser(Long userId) {
		return null;
	}

	public String getBackup(String userId, String token) {
		final String url = "/api/posts/backup";
		final String[] urlParams = new String[2];
		urlParams[0] = "userId=" + userId;
		urlParams[1] = "token=" + token;
		new Thread() {
			public void run() {
				Log.d(TAG, "request url = " + url);
				try {
					String resp = mConnection.getResposeForRequest(url,
							urlParams, NeonConnectionType.POST);
					Log.d(TAG, "response = " + resp);
				} catch (ConnectException e) {
					e.printStackTrace();
				}
			}
		}.start();
		return null;
	}

	public boolean createBlog(NeonBlog blog, String token) {
		final String url = "/api/posts";
		String concatenatedTag = "";
		for (String tag : blog.getTags()) {
			concatenatedTag = concatenatedTag.concat(tag).concat(",");
		}
		final String[] urlParams = new String[6];
		urlParams[0] = "id=" + blog.getAuthor().getUserID();
		urlParams[1] = "token=" + token;
		urlParams[2] = "title=" + blog.getTitle();
		urlParams[3] = "content=" + blog.getContent();
		urlParams[4] = "tag=" + concatenatedTag;
		urlParams[5] = "status=" + blog.getStatus().ordinal();

		String resp;
		try {
			resp = mConnection.getResposeForRequest(url, urlParams,
					NeonConnectionType.POST);
			Log.d(TAG, "response = " + resp);
			String response[] = resp.split(RESPONSE_DELIMITER);
			if (response[0].startsWith("success")) {
				return true;
			} else {
				return false;
			}
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	public List<NeonBlog> getBlogPostsForUser(String userId) {
		final String url = "/api/posts/u/" + userId;
		// new Thread() {
		// public void run() {
		try {
			List<NeonBlog> blogs = new ArrayList<NeonBlog>();
			Log.d(TAG, "request url = " + url);
			String resp = mConnection.getResposeForRequest(url, null,
					NeonConnectionType.GET);
			Log.d(TAG, "response = " + resp);
			JSONObject respObject = (JSONObject) new JSONTokener(resp)
					.nextValue();
			Log.d(TAG, "respObject.length() == " + respObject.length());
			JSONArray blogArray = respObject.getJSONArray("blogs");
			Log.d(TAG, "blogArray.length() == " + blogArray.length());
			NeonBlog blog = null;
			JSONObject blogObject = null;
			for (int i = 0; i < blogArray.length(); ++i) {
				blog = new NeonBlog();
				blogObject = (JSONObject) blogArray.get(i);
				blog.setId(blogObject.getLong("id"));
				blog.setTitle(blogObject.getString("title"));
				blog.setContent(blogObject.getString("content"));
				blog.setTags(blogObject.getString("tags").trim()
						.split(RESPONSE_DELIMITER));
				blog.setCreated(new Date(blogObject.getLong("create_time")));
				blog.setLateUpdated(new Date(blogObject.getLong("update_time")));
				Log.d(TAG, " --> " + blog);
				blogs.add(blog);
			}
			return blogs;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ConnectException e) {
			e.printStackTrace();
		}
		return null;
		// }
		// }.start();
	}

	public NeonBlog getBlogPost(Long blogId) {
		final String url = "/api/posts/p/" + blogId;
		// new Thread() {
		// public void run() {
		try {
			List<NeonBlog> blogs = new ArrayList<NeonBlog>();
			Log.d(TAG, "request url = " + url);
			String resp = mConnection.getResposeForRequest(url, null,
					NeonConnectionType.GET);
			Log.d(TAG, "response = " + resp);
			JSONObject respObject = (JSONObject) new JSONTokener(resp)
					.nextValue();
			Log.d(TAG, "respObject.length() == " + respObject.length());
			JSONArray blogArray = respObject.getJSONArray("blogs");
			Log.d(TAG, "blogArray.length() == " + blogArray.length());
			NeonBlog blog = null;
			JSONObject blogObject = null;
			for (int i = 0; i < blogArray.length(); ++i) {
				blog = new NeonBlog();
				blogObject = (JSONObject) blogArray.get(i);
				blog.setStatus(NeonBlogStatus.values()[blogObject
						.getInt("status")]);
				blog.setId(blogObject.getLong("id"));
				blog.setTitle(blogObject.getString("title"));
				blog.setContent(blogObject.getString("content"));
				blog.setTags(blogObject.getString("tags").trim()
						.split(RESPONSE_DELIMITER));
				blog.setCreated(new Date(blogObject.getLong("create_time")));
				blog.setLateUpdated(new Date(blogObject.getLong("update_time")));
				Log.d(TAG, " --> " + blog);
				blogs.add(blog);
			}
			return blogs.get(0);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ConnectException e) {
			e.printStackTrace();
		}
		return null;
		// }
		// }.start();
	}

	public boolean updateBlog(NeonBlog blog, String token) {

		final String url = "/api/posts/11";
		String concatenatedTag = "";
		for (String tag : blog.getTags()) {
			concatenatedTag = concatenatedTag.concat(tag).concat(",");
		}
		final String[] urlParams = new String[7];
		urlParams[0] = "id=" + blog.getAuthor().getUserID();
		urlParams[1] = "token=" + token;
		urlParams[2] = "title=" + blog.getTitle();
		urlParams[3] = "content=" + blog.getContent();
		urlParams[4] = "tags=" + concatenatedTag;
		urlParams[5] = "status=" + blog.getStatus().ordinal();
		urlParams[6] = "postId=" + blog.getId();
		String resp;
		try {
			resp = mConnection.getResposeForRequest(url, urlParams,
					NeonConnectionType.POST);
			Log.d(TAG, "response = " + resp);
			
			if (resp.startsWith("post")) {
				return true;
			} else {
				return false;
			}
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public String getXML(String userID, String token) {
		final String url = "/api/backup";
		final String[] urlParams = new String[2];
		urlParams[0] = "id=" + userID;
		urlParams[1] = "token=" + token;
		try {
			String resp = mConnection.getResposeForRequest(url, urlParams,
					NeonConnectionType.POST);
			return resp;
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String[] getDownloadedList() {
		final String url = "/file/listing";
		try {
			String resp = mConnection.getResposeForRequest(url, null,
					NeonConnectionType.GET);
			String[] listOfDownloadedFiles = resp.split(",");
			return listOfDownloadedFiles;
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void deletePost(Long postId) {
		final String urlStr = "http://neon.phpfogapp.com/index.php/api/posts/"
				+ postId;
		HttpURLConnection connection = null;
		URL url;
		try {
			// Create connection
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("DELETE");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

	}
}
