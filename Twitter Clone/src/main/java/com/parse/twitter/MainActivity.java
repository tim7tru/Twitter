/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.twitter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.parse.ParseAnalytics;
import com.parse.ParseUser;


public class MainActivity extends AppCompatActivity {
	Intent userIntent, createAccountIntent, logInIntent;

	public void createAccount(View view) {
		startActivity(createAccountIntent);
	}

	public void logIn(View view) {
		startActivity(logInIntent);
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Twitter");
        setContentView(R.layout.activity_main);
		createAccountIntent = new Intent(getApplicationContext(), createAccount.class);
		logInIntent = new Intent(getApplicationContext(), logIn.class);
        userIntent = new Intent(getApplicationContext(), Profile.class);
		if (ParseUser.getCurrentUser().getUsername() != null) {
        	startActivity(userIntent);
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}