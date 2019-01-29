package com.parse.starter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;



public class createAccount extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {
	ParseUser user;
	EditText username;
	EditText password;
	RelativeLayout layout;
	ImageView bird;

	@Override
	public void onClick(View view) {
		if (view.getId() == layout.getId() || view.getId() == bird.getId()) {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
	}

	@Override
	public boolean onKey(View view, int i, KeyEvent keyEvent) {
		if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
			createAccount(view);
		}
		return false;
	}

	public void createAccount(View view) {
		user = new ParseUser();
		final String displayName;

		if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
			Toast.makeText(this, "Please enter a valid username and password.", Toast.LENGTH_SHORT).show();
		} else {
			displayName = "@" + username.getText().toString();
			user.put("displayName", displayName);
			user.saveInBackground();
			user.setUsername(username.getText().toString().toLowerCase());
			user.setPassword(password.getText().toString());
			user.signUpInBackground(new SignUpCallback() {
				@Override
				public void done(ParseException e) {
					if (e == null) {
						user.put("displayName", displayName);
						Toast.makeText(createAccount.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
						finish();
					} else {
						Toast.makeText(createAccount.this, e.getMessage(), Toast.LENGTH_LONG).show();
					}
				}
			});
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
		setTitle("Create An Account");
		username = findViewById(R.id.nameEditText);
		password = findViewById(R.id.passswordEditText);
		password.setOnKeyListener(this);
		layout = findViewById(R.id.relativeLayout2);
		bird = findViewById(R.id.birdLogo2);
		layout.setOnClickListener(this);
		bird.setOnClickListener(this);
	}
}
