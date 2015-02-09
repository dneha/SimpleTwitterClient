package com.codepath.apps.MySimpleTweets;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.MySimpleTweets.R;
import com.codepath.apps.MySimpleTweets.models.Tweet;
import com.codepath.apps.MySimpleTweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class ComposeActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        getSupportActionBar().setTitle("Compose Tweet");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#06A9EE")));//#0ee0f0
        
        User user = (User) getIntent().getSerializableExtra("user");
        ImageView ivUserImageView = (ImageView)findViewById(R.id.ivComposeUserImage);
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivUserImageView);
        TextView tvUserName = (TextView)findViewById(R.id.tvComposeUserName);
        tvUserName.setText(user.getName().toString());
        TextView tvUserAlias = (TextView)findViewById(R.id.tvComposeUserAlias);
        tvUserAlias.setText("@"+user.getScreenName().toString());
    }

    public void onCancelComposeClicked(View v) {
        finish();
    }
    
    public void onTweetClicked(View v) {

        EditText etTweet = (EditText)findViewById(R.id.etComposeTweet);
        String tweetStr = etTweet.getText().toString();
        TwitterClient client;
        client = TwitterApplication.getRestClient();

        client.postTweet(tweetStr, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode,  Header[] header, JSONObject json) {
                Log.d("DEBUG", json.toString());
                Toast.makeText(ComposeActivity.this, "Tweet Posted Successfully", Toast.LENGTH_SHORT).show();
                //De-serialize json, create models and load data into the list view
                Intent doneCompose = new Intent();
                // Pass relevant data back as a result
                //doneCompose.putExtra("name", etName.getText().toString());
                // Activity finished ok, return the data
                setResult(RESULT_OK, doneCompose); // set result code and bundle data for response
                finish(); // closes the activity, pass data to parent
                
            }
            @Override
            public void onFailure(int statusCode, Header[] header, Throwable throwable, JSONObject jsonError) {
                Log.d("DEBUG",jsonError.toString());
                Toast.makeText(ComposeActivity.this, "Error Posting Tweet. Please try again", Toast.LENGTH_SHORT).show();

            }
        });
        
        
    }
    
    //public void
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
