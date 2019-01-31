package com.parse.twitter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Feed extends AppCompatActivity {

	ListView feedListView;
	ArrayList<String> usernames, tweets, following;
	ParseUser user;
	ParseQuery<ParseUser> tweetsQuery;
	ParseQuery<ParseUser> followingQuery;
	SimpleAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed);

		setTitle("Feed");
		feedListView = (ListView) findViewById(R.id.feedListView);
		usernames = new ArrayList<>();
		tweets = new ArrayList<>();
		following = new ArrayList<>();
		user = ParseUser.getCurrentUser();
		tweetsQuery = ParseUser.getQuery();
		followingQuery = ParseUser.getQuery();
		followingQuery.whereEqualTo("username", user.getUsername());
		followingQuery.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> objects, ParseException e) {
				if (e == null) {
					if (objects.size() > 0) {
						String usersFollowing = user.get("usersFollowing").toString();
						usersFollowing = usersFollowing.replaceAll("\\[", "");
						usersFollowing = usersFollowing.replaceAll("]", "");

						if (usersFollowing.contains(", ")) {
							String[] split = usersFollowing.split(", ");
							for (int i = 0; i < split.length; i++) {
								following.add(split[i]);
							}
						} else {
							following.add(usersFollowing);
						}
						Log.i("users following", following.toString());

						tweetsQuery.whereNotEqualTo("username", user.getUsername());
						tweetsQuery.findInBackground(new FindCallback<ParseUser>() {
							@Override
							public void done(List<ParseUser> objects, ParseException e) {
								if (e == null) {
									if (objects.size() > 0) {
										for (ParseUser users : objects) {
											for (int i = 0; i < following.size(); i++) {
												if (users.getUsername().equals(following.get(i))) {
													if (users.get("tweets") != null) {
														String text = users.get("tweets").toString();
														text = text.replaceAll("\\[", "");
														text = text.replaceAll("]", "");

														if (text.contains(", ")) {
															String[] split = text.split(", ");
															for (int j = 0; j < split.length; j++) {
																tweets.add(split[j]);
																usernames.add(users.getString("displayName"));
															}
														} else {
															tweets.add(text);
															usernames.add(users.getString("displayName"));
														}
													}
													Log.i("tweet", tweets.toString());
													Log.i("usernames", usernames.toString());
												}
											}
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

										adapter = new SimpleAdapter(Feed.this, fillMaps, R.layout.list_item, tweetInfo, tweetListInfo);
										feedListView.setAdapter(adapter);
									}
								}
							}
						});
					}
				}
			}
		});
	}
}
