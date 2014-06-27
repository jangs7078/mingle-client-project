package com.example.mingle;

import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.fortysevendeg.swipelistview.SwipeListView.OnLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.FragmentActivity;
import android.app.ActionBar;
public class HuntActivity extends FragmentActivity implements ActionBar.TabListener	 {

	
	 public Fragment allChatFragment;
	 public Fragment ongoingChatFragment;
		
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hunt);
        
        // Set up the action bar to show tabs.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // for each of the sections in the app, add a tab to the action bar.
        actionBar.addTab(actionBar.newTab().setText(R.string.tab1title)
            .setTabListener(this).setTag(R.string.tab1title));
        actionBar.addTab(actionBar.newTab().setText(R.string.tab2title)
            .setTabListener(this).setTag(R.string.tab2title));
        
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        // Change the current activity to HuntActivity in HttpHelper
        ((MingleApplication) this.getApplication()).initHelper.changeContext(this);

        Context context = getApplicationContext();
        CharSequence text = "User created successfully!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        
        
    }
    
	
	  


	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	
	 @Override
	  public void onRestoreInstanceState(Bundle savedInstanceState) {
	    // Restore the previously serialized current tab position.
	    if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
	      getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
	    }
	  }

	  @Override
	  public void onSaveInstanceState(Bundle outState) {
	    // Serialize the current tab position.
	    outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
	        .getSelectedNavigationIndex());
	  }
	  
	  
	  /* Probably need to fix this method!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.chat, menu);
	    return true;
	  }
	  
	 
	  @Override
	  public void onTabSelected(ActionBar.Tab tab,
	      FragmentTransaction fragmentTransaction) {
	    // When the given tab is selected, show the tab contents in the
	    // container view.
		
		  if(tab.getTag().equals(R.string.tab1title)) {
			  System.out.println("all tab created");
			  if(allChatFragment == null) 
				  allChatFragment  = new AllChatFragment();
		     
			    getFragmentManager().beginTransaction()
			        .replace(R.id.fragment_container, allChatFragment).commit();
			  
		  } else if(tab.getTag().equals(R.string.tab2title)) {
			  System.out.println("mingling tab created");
			  if(ongoingChatFragment == null)
			  	ongoingChatFragment = new OngoingChatFragment();
			  getFragmentManager().beginTransaction()
		        .replace(R.id.fragment_container, ongoingChatFragment).commit();
			  
		  }
	    
	  }
	  
	  
	
}


