package com.example.mingle;

import java.util.ArrayList;

import com.fortysevendeg.swipelistview.SwipeListView;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class OngoingChatFragment extends Fragment {

	public ListView currentlychattinglistview;
	private ArrayList<ChatRoom> chat_room_list;
	private ChatRoomAdapter adapter;
	
	private Activity parent; 
	
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		 System.out.println("ongoing chatview create complete");
		parent = getActivity();
		
		View rootView = inflater.inflate(R.layout.ongoing_chat_fragment, container, false);
		
		currentlychattinglistview= (ListView)(rootView.findViewById(R.id.mingling)) ;
		
        // Stores 
		chat_room_list = ((MingleApplication) parent.getApplication()).currUser.getChatRoomList();;
        adapter = new ChatRoomAdapter(parent, R.layout.chatroom_row, chat_room_list);
        adapter.notifyDataSetChanged();
        
        currentlychattinglistview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // TODO Auto-generated method stub
            	System.out.println(parent + "   " + v + "   " + position + "   " + id);
            }

        });
        // Set the ArrayAdapter as the ListView's adapter.  
        currentlychattinglistview.setAdapter( adapter );        
        
		return rootView;
	}
	
	
}
