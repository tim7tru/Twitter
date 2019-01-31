package com.parse.twitter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewProfile extends AppCompatActivity {

	ListView userProfileListView;
	ArrayList<String> tweets, usernames;
	SimpleAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_users_profile);
		Intent intent = getIntent();
		String username = ParseUser.getCurrentUser().getUsername();
		setTitle("Your Tweets");

		userProfileListView = (ListView) findViewById(R.id.userProfileListView);
		tweets = new ArrayList<>();
		usernames = new ArrayList<>();

		ParseQuery<ParseUser> query = ParseUser.getQuery();

		query.whereEqualTo("username", username);

		query.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> objects, ParseException e) {
				if (e == null) {
					if (objects.size() > 0) {
						if (objects.get(0).get("tweets") != null) {
							String text = objects.get(0).get("tweets").toString();
							text = text.replaceAll("\\[", "");
							text = text.replaceAll("]", "");

							if (text.contains(", ")) {
								String[] split = text.split(", ");
								for (int j = 0; j < split.length; j++) {
									tweets.add(split[j]);
									usernames.add(objects.get(0).getString("displayName"));
								}
							} else {
								tweets.add(text);
								usernames.add(objects.get(0).getString("displayName"));
							}
						}
						Log.i("tweet", tweets.toString());
						Log.i("usernames", usernames.toString());
					}
					String[] tweetInfo = new String[]{"tweets", "username"};
					int[] tweetListInfo = new int[]{R.id.tweets, R.id.users};

					List<HashMap<String, String>> fillMaps = new ArrayList<>();

					for (int i = 0; i < tweets.size(); i++) {
						HashMap<String, String> map = new HashMap<>();
						map.put("tweets", tweets.get(i));
						map.put("username", usernames.get(i));
						Log.i("map", map.toString());
						fillMaps.add(map);
					}

					adapter = new SimpleAdapter(ViewProfile.this, fillMaps, R.layout.list_item, tweetInfo, tweetListInfo);
					userProfileListView.setAdapter(adapter);
				}
			}
		});
	}
}
