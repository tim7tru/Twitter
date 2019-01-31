package com.parse.twitter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.content.Intent;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class logIn extends AppCompatActivity implements  View.OnKeyListener, View.OnClickListener{
	EditText username, password;
	RelativeLayout layout;
	ImageView bird;
	Intent profileIntent;

	public void onClick(View view) {
		if (view.getId() == layout.getId() || view.getId() == bird.getId()) {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
	}

	@Override
	public boolean onKey(View view, int i, KeyEvent keyEvent) {
		if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
			loginClicked(view);
		}
		return false;
	}

	public void loginClicked(View view) {
		ParseUser user = new ParseUser();

		if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
			Toast.makeText(this, "Please enter a valid username and password", Toast.LENGTH_SHORT).show();
		} else {
			user.logInInBackground(username.getText().toString().toLowerCase(), password.getText().toString(), new LogInCallback() {
				@Override
				public void done(ParseUser user, ParseException e) {
					if (e == null) {
						Toast.makeText(logIn.this, "Logged in successfully!", Toast.LENGTH_SHORT).show();
						startActivity(profileIntent);
					} else {
						Toast.makeText(logIn.this, e.getMessage(), Toast.LENGTH_LONG).show();
					}
				}
			});
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);
		setTitle("Log In");
		username = findViewById(R.id.nameEditText2);
		password = findViewById(R.id.passswordEditText2);
		layout = findViewById(R.id.relativeLayout3);
		bird = findViewById(R.id.birdLogo3);
		password.setOnKeyListener(this);
		layout.setOnClickListener(this);
		bird.setOnClickListener(this);
		profileIntent = new Intent(getApplicationContext(), Profile.class);
	}
}
