package transcloud.app.neonblogger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NeonBloggerActivity extends Activity {

	private static final String TAG = "NeonBloggerActivity"; 
	private EditText rEditTextUserName;
	private EditText rEditTextPassword;
	public static NeonApp mNeonApp;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mNeonApp = new NeonApp(getApplicationContext());
        INeonAppListener listener = new INeonAppListener() {
			
			@Override
			public void onSuccessfulAuthentication() {
				Log.d(TAG, "Authentication Success");
				Toast.makeText(getApplicationContext(), "Authentication Success", Toast.LENGTH_SHORT).show();
				onLoginSuccess();
			}
			
			@Override
			public void onFailedAuthentication() {
				Toast.makeText(getApplicationContext(), "Authentication Failure", Toast.LENGTH_SHORT).show();
			}
		};
		mNeonApp.setListener(listener);
        
        rEditTextUserName = (EditText) findViewById(R.id.et_un);
        rEditTextPassword = (EditText) findViewById(R.id.et_pw);
    }
    
    public void onClickLogin(View view) {
    	String username = rEditTextUserName.getText().toString();
    	String password = rEditTextPassword.getText().toString();
    			
    	Log.d(TAG, "logging in - u="+username+", p="+password);
    	mNeonApp.authorize(username, password);
    }
    
    public void onLoginSuccess(){
    	Intent intent = new Intent(getApplicationContext(), NeonDashbordActivity.class);
    	startActivity(intent);
    }
    
}