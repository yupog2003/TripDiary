package com.yupog2003.tripdiary.data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.yupog2003.tripdiary.R;

public class PackageHelper {
	public static final String GoogleMapPackageName = "com.google.android.apps.maps";
	public static final String StreetViewPackageNmae = "com.google.android.street";

	public static boolean isAppInstalled(final Context context, final String packageName) {
		PackageManager pm = context.getPackageManager();
		boolean installed = false;
		try {
			pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			installed = false;
		}
		return installed;
	}

	public static void askForInstallApp(final Context context, final String packageName, final String appName) {
		AlertDialog.Builder ab = new AlertDialog.Builder(context);
		ab.setTitle(appName + " " + context.getString(R.string.has_not_been_installed));
		ab.setMessage(context.getString(R.string.do_you_want_to_install));
		ab.setPositiveButton(context.getString(R.string.enter), new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Uri uri = Uri.parse("market://search?q=pname:" + packageName);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				if (context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
					context.startActivity(intent);
				}
			}
		});
		ab.setNegativeButton(context.getString(R.string.cancel), null);
		ab.show();
	}

}
