package com.example.firestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements View.OnClickListener {
	private long mBackPressed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.btn_data_manage).setOnClickListener(this);
		findViewById(R.id.btn_data_query).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_data_manage:
				startActivity(new Intent(this, ManageActivity.class));
				break;
			case R.id.btn_data_query:
				startActivity(new Intent(this, QueryActivity.class));
				break;
		}
	}

	@Override
	public void onBackPressed() {
		if (mBackPressed + 2000 > System.currentTimeMillis()) {
			super.onBackPressed();
		} else {
			Toast.makeText(this, R.string.exit, Toast.LENGTH_SHORT).show();
			mBackPressed = System.currentTimeMillis();
		}
	}
}