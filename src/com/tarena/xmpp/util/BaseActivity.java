package com.tarena.xmpp.util;

import com.tarena.xmpp.model.TApplication;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TApplication.allActivities.add(this);
		LogUtils.i("exit app", "add to TApplication"+this);
	}
}
