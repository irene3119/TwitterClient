package com.codepath.apps.twitterclient.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.codepath.apps.twitterclient.Adapters.TimeLineAdapter;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.Utils.EndlessRecyclerViewScrollListener;
import com.codepath.apps.twitterclient.Utils.NewTweetFragment;
import com.codepath.apps.twitterclient.Utils.TwitterApplication;
import com.codepath.apps.twitterclient.Utils.TwitterClient;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.UserCredential;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements NewTweetFragment.NewTweetFragmentListener{

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private UserCredential userCredential;
    private RecyclerView rvTimeline;
    private TimeLineAdapter adapter;
    private FloatingActionButton composeBtn;
    private NewTweetFragment newTweetFragment;
    private LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        setViews();
        client = TwitterApplication.getRestClient(); //singleton client
        populateTimeline();
        composeTweet();
    }

    private void setViews()
    {
        tweets = new ArrayList<>();
        rvTimeline = (RecyclerView) findViewById(R.id.rvTimeline);
        adapter = new TimeLineAdapter(this, tweets);
        rvTimeline.setAdapter(adapter);
        // Set layout manager to position the items
        linearLayoutManager = new LinearLayoutManager(this);
        rvTimeline.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvTimeline.addOnScrollListener(scrollListener);
        composeBtn = (FloatingActionButton) findViewById(R.id.fbtnCompose);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                Log.e("DEBUG","refresh");
                tweets.clear();
                adapter.notifyDataSetChanged();
                populateTimeline();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

    //send API request to get the timeline json
    //fill the listview by creating tweet object from json
    private void populateTimeline() {

        Long max_id = null;

        if (tweets != null && tweets.size() > 0) {
            max_id = tweets.get(tweets.size() - 1).id;
        }

        client.getHomeTimeLine(max_id, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG", responseString);
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("DEBUG",responseString);
                Gson gson = new Gson();
                Type collectionType = new TypeToken<List<Tweet>>(){}.getType();
                tweets.addAll((Collection<? extends Tweet>) gson.fromJson(responseString.toString(),collectionType));
                adapter.notifyDataSetChanged();
                //tweets = gson.fromJson(responseString,collectionType);
                Log.e("DEBUG", String.valueOf(tweets.size()));
                swipeContainer.setRefreshing(false);

            }
        });
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
            populateTimeline();
    }

    private void getUserCredential() {
        client.getUserCredential(new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG", responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("DEBUG",responseString);
                Gson gson = new Gson();
                Type collectionType = new TypeToken<UserCredential>(){}.getType();
                userCredential = gson.fromJson(responseString,collectionType);
                showComposeDialog();
            }
        });

    }

    private void composeTweet() {

        composeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserCredential();
            }
        });
    }

    private void showComposeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        newTweetFragment = NewTweetFragment.newInstance(userCredential);
        newTweetFragment.show(fm, "compose_fragment");

    }

    @Override
    public void onFinishSettingDialog(Tweet newTweet){
        tweets.add(0, newTweet);
        adapter.notifyItemInserted(0);
        linearLayoutManager.scrollToPosition(0);
    }


}
