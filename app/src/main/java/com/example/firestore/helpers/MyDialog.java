package com.example.firestore.helpers;

import android.app.Dialog;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.firestore.R;

public class MyDialog {
	private static Dialog mDialog;

	public static void showDialog(Context context) {
		mDialog = new Dialog(context, R.style.CustomProgressDialog);
		mDialog.addContentView(
				new ProgressBar(context),
				new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
		);
		mDialog.setCancelable(true);
		if (!mDialog.isShowing()) {
			mDialog.show();
		}
	}

	public static void dismissDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}
}
