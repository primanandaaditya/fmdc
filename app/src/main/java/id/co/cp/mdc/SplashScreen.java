package id.co.cp.mdc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class SplashScreen extends Activity {

	// Splash screen timer
	private static int SPLASH_TIME_OUT = 1500;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		OneSignal.startInit(this).setNotificationOpenedHandler(new NotificationHandler())
				.autoPromptLocation(true)
				.init();
		setContentView(R.layout.activity_splash);

		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity
				Intent i = new Intent(SplashScreen.this, LoginActivity.class);
				startActivity(i);

				// close this activity
				finish();
			}
		}, SPLASH_TIME_OUT);
	}
	private class NotificationHandler implements OneSignal.NotificationOpenedHandler {
		@Override
		public void notificationOpened(OSNotificationOpenResult result) {
			OSNotificationAction.ActionType actionType = result.action.type;
			JSONObject data = result.notification.payload.additionalData;
			String customKey;

			if (data != null) {
				customKey = data.optString("customkey", null);
				if (customKey != null)
					Log.i("OneSignalExample", "customkey set with value: " + customKey);
			}

			if (actionType == OSNotificationAction.ActionType.ActionTaken)
				Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);

			// The following can be used to open an Activity of your choice.
			// Replace - getApplicationContext() - with any Android Context.
			 Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
			 startActivity(intent);

			// Add the following to your AndroidManifest.xml to prevent the launching of your main Activity
			//   if you are calling startActivity above.

		}
	}
}
