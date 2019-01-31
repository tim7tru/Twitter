package com.parse.twitter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {
	ListView listView;
	ParseUser user;
	Intent intent;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = new MenuInflater(getApplicationContext());
		menuInflater.inflate(R.menu.tweet_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.tweet:
				intent = new Intent(Profile.this, CreateTweet.class);
				startActivity(intent);
				return true;
			case R.id.feed:
				intent = new Intent(Profile.this, Feed.class);
				startActivity(intent);
				return true;
			case R.id.view_profile:
				intent = new Intent(Profile.this, ViewProfile.class);
				startActivity(intent);
				return true;
			case R.id.log_out_of_twitter:
				intent = new Intent(Profile.this, MainActivity.class);
				ParseUser.logOut();
				startActivity(intent);
				return true;
			default:
				return false;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		setTitle("All Users");
		listView = findViewById(R.id.userListView);

		final ArrayList<String> usersFollowing = new ArrayList<>();
		final ArrayList<String> usernamesArray = new ArrayList<>();
		final ArrayList<String> usernamesDisplay = new ArrayList<>();
		user = ParseUser.getCurrentUser();
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

		query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
		query.addAscendingOrder("displayName");
		query.findInBackground(new FindCallback<ParseUser>() {
				@Override
			public void done(List<ParseUser> objects, ParseException e) {
				if (e == null) {
					Log.i("List size", Integer.toString(objects.size()));
					if (objects.size() > 0) {
						for (ParseUser users : objects) {
							usernamesDisplay.add(users.get("displayName").toString());
							usernamesArray.add(users.getUsername());
							Log.i("usernames array", usernamesArray.toString());
						}
						ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Profile.this, android.R.layout.simple_list_item_checked, usernamesDisplay);
						listView.setAdapter(arrayAdapter);
						ArrayList<String> following = (ArrayList<String>) user.get("usersFollowing");
						Log.i("Following", following.toString());

						if (following.size() > 0) {
							for (int i = 0; i < following.size(); i++) {
								for (int j = 0; j < usernamesArray.size(); j++) {
									if (following.get(i).equals(usernamesArray.get(j))) {
										listView.setItemChecked(j, true);
									}
								}
							}
						}
					}
				}
			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
				CheckedTextView checkedTextView = (CheckedTextView) view;

				if (checkedTextView.isChecked()) {
					usersFollowing.add(usernamesArray.get(i));
					user.put("usersFollowing", usersFollowing);
					user.saveInBackground(new SaveCallback() {
						@Override
						public void done(ParseException e) {
							if (e == null) {
								Toast.makeText(Profile.this, "You are now following " + usernamesDisplay.get(i), Toast.LENGTH_SHORT).show();
							} else {
								e.printStackTrace();
							}
						}
					});
				} else {
					usersFollowing.remove(usernamesArray.get(i));
					user.put("usersFollowing", usersFollowing);
					user.saveInBackground(new SaveCallback() {
						@Override
						public void done(ParseException e) {
							if (e == null) {
								Toast.makeText(Profile.this, "You unfollowed " + usernamesDisplay.get(i), Toast.LENGTH_SHORT).show();
							} else {
								e.printStackTrace();
							}
						}
					});
				}
			}
		});

		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
				Intent intent = new Intent(Profile.this, UsersProfile.class);
				intent.putExtra("userProfile", usernamesArray.get(i));
				startActivity(intent);
				return true;
			}
		});

	}
}
