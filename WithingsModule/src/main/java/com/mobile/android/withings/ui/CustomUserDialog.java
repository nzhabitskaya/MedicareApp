package com.mobile.android.withings.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;

import com.mobile.android.withings.R;

public class CustomUserDialog{
	private Context mContext;
	private ProgressDialog progressDialog;
	private AlertDialog alert;
	private DialogCallback callback;
	
	public CustomUserDialog(Context context){
		mContext = context;
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
		dialogBuilder.setCancelable(false)
				.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if(callback != null)
							callback.notifyOnClick();
						dialog.cancel();
					}
				});
		alert = dialogBuilder.create();
	}

	public void showProgress(int titleId, int messageId) {
		progressDialog = ProgressDialog.show(mContext, mContext.getString(titleId), mContext.getString(messageId));
		progressDialog.setOnKeyListener(onKeyListener);
	}

	public void hideProgress() {
		if(progressDialog != null && progressDialog.isShowing()){
			progressDialog.dismiss();
		}
		//hideUserMessage();
	}

	public void showUserMessage(int messageId) {
		//alert.setTitle(mContext.getString(R.string.title_info));
		alert.setMessage(mContext.getString(messageId));
		alert.show();
	}
	
	public void showUserMessage(String msg) {
		//alert.setTitle(mContext.getString(R.string.title_info));
		alert.setMessage(msg);
		alert.show();
	}
	
	public void hideUserMessage() {
		alert.dismiss();
	}

	public void showErrorMessage(String message) {
		//alert.setTitle(mContext.getString(R.string.title_error));
		alert.setMessage(message);
		alert.show();
	}
	
	public void showErrorMessage(int messageId) {
		showErrorMessage(mContext.getString(messageId));
	}

	public void showWarningMessage(String message) {
		//alert.setTitle(mContext.getString(R.string.title_warning));
		//if(message == null)
			//message = mContext.getString(R.string.message_connection_error);
		alert.setMessage(message);
		alert.show();
	}

	public void showWarningMessage(int messageId) {
		showWarningMessage(mContext.getString(messageId));
	}
	
	public void showInternalErrorMessage(String message) {
		//alert.setTitle(mContext.getString(R.string.title_connection_error));
		alert.setMessage(message);
		alert.show();
	}
	
	public void registerDialogCallback(DialogCallback callback){
		this.callback = callback;
	}
	
	public void unregisterDialogCallback(){
		this.callback = null;
	}
	
	OnKeyListener onKeyListener = new OnKeyListener(){

		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (KeyEvent.KEYCODE_BACK == keyCode){
				hideProgress();
			}
			return false;
		}
		
	};
}
