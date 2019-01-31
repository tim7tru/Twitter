package com.parse.starter;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class CreateTweet extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {
	Button cancelButton, tweetButton;
	EditText tweetEditText;
	List<String> usersTweets;
	ParseUser user;
	ConstraintLayout layout;
	ImageView imageView;
	String currentTweet;
	@Override
	public boolean onKey(View view, int i, KeyEvent keyEvent) {
		if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
			keyBoardDown(view);
		}
		return false;
	}

	public void keyBoardDown(View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == layout.getId() || view.getId() == imageView.getId()) {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
	}

	public void cancel(View view) {
		finish();
	}

	public void tweet(View view) {
		if (!tweetEditText.getText().toString().isEmpty() && tweetEditText.getText().toString().length() < 140) {
			currentTweet = "";
			user = ParseUser.getCurrentUser();
			ParseQuery<ParseUser> query = ParseUser.getQuery();
			Log.i("User", user.getUsername());
			query.whereEqualTo("username", user.getUsername());
			query.findInBackground(new FindCallback<ParseUser>() {
				@Override
				public void done(List<ParseUser> objects, ParseException e) {
					if (e == null) {
						if (objects.size() > 0) {
							if (objects.get(0).get("tweets") != null) {
								Log.i("tweets", objects.get(0).get("tweets").toString());
								// Regex to get all past tweets
                                String text = objects.get(0).get("tweets").toString();
                                Log.i("text", text);
								text = text.replaceAll("\\[", "");
								text = text.replaceAll("\\]", "");

								if (text.contains(", ")) {
									String[] split = text.split(", ");
									for (int i = 0; i < split.length; i++) {
										usersTweets.add(split[i]);
									}
								} else {
									usersTweets.add(text);
								}

								usersTweets.add(tweetEditText.getText().toString());

							} else {
								usersTweets.add(tweetEditText.getText().toString());
							}

							user.put("tweets", usersTweets);
							user.saveInBackground(new SaveCallback() {
								@Override
								public void done(ParseException e) {
									if (e == null) {
										Toast.makeText(CreateTweet.this, "Tweet sent out!", Toast.LENGTH_SHORT).show();
										finish();
									} else {
										e.printStackTrace();
									}
								}
							});
						} else {
							Toast.makeText(CreateTweet.this, "Please ensure your tweet is less than 140 characters, or isn't empty.", Toast.LENGTH_LONG).show();
						}
					}
				}
			});
		}
	}

	@Override
	protected void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_tweet);
		setTitle("Tweet");
		cancelButton = findViewById(R.id.tweetCancelButton);
		tweetButton = findViewById(R.id.tweetButton);
		tweetEditText = findViewById(R.id.tweetEditText);
		layout = findViewById(R.id.constraintLayout1);
		imageView = findViewById(R.id.birdLogo4);
		tweetEditText.setOnKeyListener(this);
		layout.setOnClickListener(this);
		imageView.setOnClickListener(this);
		usersTweets = new ArrayList<>();
	}
}
