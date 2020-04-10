package com.mobile.android.ebabynotebook.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.mobile.android.ebabynotebook.R;

public class DialogUtil {
	public static void showErrorDialog(Context context, int message) {
		final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(R.string.warning);
		alertDialog.setMessage(context.getString(message));
		alertDialog.setButton(context.getString(R.string.close),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						alertDialog.dismiss();
					}
				});
		alertDialog.show();
	}
}
