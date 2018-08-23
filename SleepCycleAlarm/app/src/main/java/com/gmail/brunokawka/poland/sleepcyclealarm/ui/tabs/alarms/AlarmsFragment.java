package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.alarms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.realm.Realm;
import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.model.DataHelper;
import com.gmail.brunokawka.poland.sleepcyclealarm.model.alarms.Alarm;
import com.gmail.brunokawka.poland.sleepcyclealarm.model.alarms.AlarmsParent;

public class AlarmsFragment extends Fragment {
    private static final String TAG = "AlarmsFragmentLog";

    private Realm realm;
    private RecyclerView recyclerView;
    private AlarmsRecyclerViewAdapter adapter;

    private class TouchHelperCallback extends ItemTouchHelper.SimpleCallback {

        TouchHelperCallback() {
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            DataHelper.deleteItemAsync(realm, viewHolder.getItemId());
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();

        View view = layoutInflater.inflate(R.layout.fragment_alarms, container, false);

        recyclerView = view.findViewById(R.id.alarmsList);
        setUpRecyclerView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
        recyclerView.setAdapter(null);
    }

    private void setUpRecyclerView() {
        AlarmsParent parent = realm.where(AlarmsParent.class).findFirst();
        if(parent != null) {
            adapter = new AlarmsRecyclerViewAdapter(parent.getItemList());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);

            TouchHelperCallback touchHelperCallback = new TouchHelperCallback();
            ItemTouchHelper touchHelper = new ItemTouchHelper(touchHelperCallback);
            touchHelper.attachToRecyclerView(recyclerView);
        } else {
            Log.e(TAG, "Parent equals null");
        }
    }
}

// TODO: create unmanaged object, and then use realm.insertOrUpdate method which will check for existence of an object with the same key, and update it if exists or create a new one if it doesn't:
