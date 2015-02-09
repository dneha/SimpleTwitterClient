package com.codepath.apps.MySimpleTweets;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

//import com.codepath.apps.MySimpleTweets.R;
//import com.loopj.android.http.AsyncHttpResponseHandler;
import com.codepath.apps.MySimpleTweets.models.Tweet;
import com.codepath.apps.MySimpleTweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity {

    private final int REQUEST_CODE = 20;
    
    private TwitterClient client;
    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    ListView lvTweets;
    private long max_id;
    private User loggedinUser;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        
        client = TwitterApplication.getRestClient();
        max_id = 0;
        lvTweets = (ListView)findViewById(R.id.lvTweets);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(this, tweets);
        lvTweets.setAdapter(aTweets);

        getLoggedInUser();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#06A9EE")));
        
        populateTimeline();

        
        
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                populateTimeline();
            }
        });
    }

    private void getLoggedInUser() {
        client.getLoggedinUserDetails(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode,  Header[] header, JSONObject json) {
                Log.d("DEBUG",json.toString());
                loggedinUser = User.fromJson(json);
                getSupportActionBar().setTitle("@"+loggedinUser.getScreenName().toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] header, Throwable throwable, JSONObject jsonError) {
                Log.d("DEBUG",jsonError.toString());
            }
        });
    }
    
    //Send API request and fill list view by creating twitter objects from the json
    private void populateTimeline() {
        client.getHomeTimeline(max_id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode,  Header[] header, JSONArray json) {
                Log.d("DEBUG",json.toString());
                //De-serialize json, create models and load data into the list view
                aTweets.addAll(Tweet.fromJsonArray(json));
                int lastIdx = tweets.size()-1;
                Tweet lastTweet = tweets.get(lastIdx);
                max_id = lastTweet.getUid();
            }
            @Override
            public void onFailure(int statusCode, Header[] header, Throwable throwable, JSONObject jsonError) {
                Log.d("DEBUG",jsonError.toString());
                
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }
    
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.miCompose) {
            Intent composeIntent = new Intent(TimelineActivity.this, ComposeActivity.class);
            composeIntent.putExtra("user", loggedinUser);
            startActivityForResult(composeIntent, REQUEST_CODE);
            return true;
        } else if (id == R.id.miRefresh) {
            refreshTimeline();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            refreshTimeline();
        }
    }
    
    private void refreshTimeline() {
        max_id = 0;
        tweets.clear();
        populateTimeline();
        aTweets.notifyDataSetChanged();
        Toast.makeText(this, "Refreshed", Toast.LENGTH_SHORT).show();
    }
}
