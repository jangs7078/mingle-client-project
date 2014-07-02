package com.example.mingle;

import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.app.ActionBar;
public class HuntActivity extends FragmentActivity implements ActionBar.TabListener	 {
	
	 public AllChatFragment allChatFragment;			//Fragment for list of chattable users
	 public OngoingChatFragment ongoingChatFragment;	//Fragment for list of users whom current user is chatting with
		
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hunt);
        System.out.println("hunt on create");
        // Set up the action bar to show tabs.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // for each of the sections in the app, add a tab to the action bar.
        actionBar.addTab(actionBar.newTab().setText(R.string.tab1title)
            .setTabListener(this).setTag(R.string.tab1title));
        actionBar.addTab(actionBar.newTab().setText(R.string.tab2title)
            .setTabListener(this).setTag(R.string.tab2title));
        
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        System.out.println("hunt navigation set");
        // Change the current activity to HuntActivity in HttpHelper
        ((MingleApplication) this.getApplication()).connectHelper.changeContext(this);

        //Load first 10 chattable users
        allChatFragment.loadNewMatches(this);
        
        ((MingleApplication) this.getApplication()).connectHelper.connectSocket();
    }
	
	//?????
    public String getMyUid(){
    	return ((MingleApplication) this.getApplication()).currUser.getUid();
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
		  System.out.println("ontabsel called");
		  // When the given tab is selected, show the tab contents in the
		  // container view.
		  if(tab.getTag().equals(R.string.tab1title)) {
			  if(allChatFragment == null) allChatFragment  = new AllChatFragment();
			 
			  System.out.println("chat fragment on view");
			  getFragmentManager().beginTransaction()
			        .replace(R.id.fragment_container, allChatFragment).commit();
			  
		  } else if(tab.getTag().equals(R.string.tab2title)) {
			  if(ongoingChatFragment == null) ongoingChatFragment = new OngoingChatFragment();
			  
			  getFragmentManager().beginTransaction()
		        .replace(R.id.fragment_container, ongoingChatFragment).commit();
		  }	    
	  }
	  
	  @Override
	  public void onRestart(){
	        super.onRestart();
	        // Change the current activity to HuntActivity in HttpHelper
	        System.out.println(this);
	        ((MingleApplication) this.getApplication()).connectHelper.changeContext(this);
	 }
}


