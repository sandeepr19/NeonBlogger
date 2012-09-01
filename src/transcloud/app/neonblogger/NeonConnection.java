package transcloud.app.neonblogger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import transcloud.app.neonblogger.objects.NeonConnectionType;
import android.util.Log;

/**
 * Responses: 1. login success --> (String) - result,username,user_id,token 2.
 * login failure --> (String) - result,errorcode,reason
 * 
 * @author Kanna
 * 
 */
public class NeonConnection {

	private static final String TAG = "NeonConnection";
	// private static final String API =
	// "http://10.0.2.2/neonpad/neon/index.php";
	private static final String API = "http://neon.phpfogapp.com/index.php";
	public static final String CONNECTION_FAILURE = "Connection_Failure";

	private static NeonConnection con;
	HttpURLConnection connection = null;

	private NeonConnection() {
	}

	public static NeonConnection getConnection() {
		if (con == null)
			con = new NeonConnection();
		return con;
	}

	/**
	 * gets the response for the given request. GET - shold have parameters
	 * field as null POST - should have parametes field as NOT null
	 * 
	 * @param urlPart
	 *            - just specify the url that follows
	 *            "http://10.0.2.2/neonpad/neon/index.php".
	 * @param parameters
	 * @param type
	 * @return
	 * @throws ConnectException
	 */
	public String getResposeForRequest(String urlPart, String[] parameters,
			NeonConnectionType type) throws ConnectException {
		String urlStr = API + urlPart;
		if (type.equals(NeonConnectionType.GET)) {
			if (parameters != null) {
				Log.e(TAG, "params for GET request not supported");
				throw new ConnectException("GET request has params!=null");
			}
			Log.d(TAG, "GET url = " + urlStr + ", no_params");
			return getResponseForGet(urlStr);
		} else if (type.equals(NeonConnectionType.POST)) {
			if (parameters == null) {
				Log.e(TAG, "params for POST is not found");
				throw new ConnectException("POST request has params==null");
			}
			String params = getUrlParams(parameters);
			Log.d(TAG, "POST url = " + urlStr + ", params=" + params);
			return getResponseForPost(urlStr, params);
		}
		return null;
	}

	private String getResponseForPost(String urlStr, String params) {
		HttpURLConnection connection = null;
		URL url;
		try {
			// Create connection
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(params.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes(params);
			wr.flush();
			wr.close();

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
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return CONNECTION_FAILURE;
	}

	private String getResponseForGet(String urlStr) {
		HttpURLConnection connection = null;
		URL url;
		try {
			// Create connection
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			connection.connect();

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
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return CONNECTION_FAILURE;
	}

	private String getUrlParams(String[] parameters) {
		StringBuffer urlParams = new StringBuffer();
		String[] params;
		boolean isFirst = true;
		for (String p : parameters) {
			params = p.split("=");
			if (isFirst) { // no &
				urlParams.append(params[0]).append("=")
						.append(URLEncoder.encode(params[1]));
				isFirst = false;
			} else
				// add & at start
				urlParams.append("&").append(params[0]).append("=")
						.append(URLEncoder.encode(params[1]));
		}
		return urlParams.toString();
	}

}
