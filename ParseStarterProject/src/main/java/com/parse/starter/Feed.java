package com.parse.starter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Feed extends AppCompatActivity {

	ListView feedListView;
	ArrayList<String> usernames, tweets;

	class Tweets {
		private String username;
		private String tweet;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed);

		feedListView = (ListView) findViewById(R.id.feedListView);
		usernames = new ArrayList<>();
		tweets = new ArrayList<>();

		ParseUser user = ParseUser.getCurrentUser();

		ParseQuery<ParseUser> query = ParseUser.getQuery();

		query.whereNotEqualTo("username", user.getUsername());

		query.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> objects, ParseException e) {
				if (e == null) {
					if (objects.size() > 0) {
						for (ParseUser users : objects) {
							String text = users.get("tweets").toString();
							text = text.replaceAll("\\[", "");
							text = text.replaceAll("\\]", "");

							if (text.contains(", ")) {
								String[] split = text.split(", ");
								for (int i = 0; i < split.length; i++) {
									tweets.add(split[i]);
									usernames.add(users.getUsername());
								}
							} else {
								tweets.add(text);
								usernames.add(users.getUsername());
							}
						}
					}
				}
			}
		});
	}
}
