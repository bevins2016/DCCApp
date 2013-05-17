package com.example.dcc.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.dcc.R;
import com.example.dcc.helpers.Member;
import com.example.dcc.helpers.ObjectStorage;
import com.example.dcc.helpers.mysql.HttpConnection;

import java.util.List;


public class MembersListFragment extends Fragment implements OnClickListener{

	ListView listview;

	ArrayAdapter<Spanned> adapter;
	List<Member> members;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
        setRetainInstance(true);

		View view = inflater.inflate(R.layout.members_list, container, false);
		
		new GetMembersTask().execute((Void) null);
		
		try {
			Thread.currentThread().sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        adapter = new ArrayAdapter<Spanned>(getActivity(), R.layout.member_item);
		listview = (ListView)view.findViewById(R.id.membersList);

        listview.setAdapter(adapter);

		for(Member m : members){
			adapter.add(Html.fromHtml(m.toString()));
		}
		return view;
	}

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        List<String> memberList = ObjectStorage.getMemberActivity();
        Log.e("Something", "Saving State");
        if(!members.isEmpty()){
            memberList.clear();
            savedInstanceState.clear();
            for(Member m : members){
                memberList.add(m.getHandle());
                savedInstanceState.putSerializable(m.getHandle(), m);
            }
        }
    }

	@Override
	public void onClick(View v) {
	}

	public class GetMembersTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			members  = HttpConnection.getMembers();
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {

		}

		@Override
		protected void onCancelled() {
		}
	}
	
}
