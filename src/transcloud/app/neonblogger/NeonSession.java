package transcloud.app.neonblogger;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class NeonSession {
	
	private static final String TAG = "NeonSession";
	private static final String PREFERENCES_NAME = "Neon_Preferences";
	private static final String TOKEN = "Neon_Token";
	private static final String USERNAME = "Neon_UserName";
	private static final String USERID = "Neon_UserId";
	
	private SharedPreferences preferences;
	private Editor editor;
	
	public NeonSession(Context context) {
		preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		editor = preferences.edit();
	}
	
	public void storeToken(String username, String userId, String token) {
		Log.d(TAG, "Storing token - user=" + username + ", userId=" + userId + " , token=" + token);
		editor.putString(TOKEN, token);
		editor.putString(USERNAME, username);
		editor.putString(USERID, userId);
		editor.commit();
	}
	
	public void clearToken(String username, String token) {
		editor.putString(TOKEN, null);
		editor.putString(USERNAME, null);
		editor.putString(USERID, null);
		editor.commit();
	}
	
	public String getUserName() {
		return preferences.getString(USERNAME, null);
	}
	
	public String getToken() {
		return preferences.getString(TOKEN, null);
	}
	
	public String getUserId() {
		return preferences.getString(USERID, null);
	}

}
