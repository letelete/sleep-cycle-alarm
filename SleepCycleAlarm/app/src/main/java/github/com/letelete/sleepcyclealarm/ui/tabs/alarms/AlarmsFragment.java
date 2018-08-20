package github.com.letelete.sleepcyclealarm.ui.tabs.alarms;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import github.com.letelete.sleepcyclealarm.R;
import github.com.letelete.sleepcyclealarm.model.DataHelper;
import github.com.letelete.sleepcyclealarm.model.alarms.Alarm;
import github.com.letelete.sleepcyclealarm.model.alarms.AlarmsParent;
import io.realm.Realm;

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
        View view = layoutInflater.inflate(R.layout.fragment_alarms, container, false);
        realm = Realm.getDefaultInstance();
        recyclerView = view.findViewById(R.id.alarmsList);
        setUpRecyclerView();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
        recyclerView.setAdapter(null);
    }

    private void setUpRecyclerView() {
        adapter = new AlarmsRecyclerViewAdapter(realm.where(AlarmsParent.class).findFirst().getItemList());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        TouchHelperCallback touchHelperCallback = new TouchHelperCallback();
        ItemTouchHelper touchHelper = new ItemTouchHelper(touchHelperCallback);
        touchHelper.attachToRecyclerView(recyclerView);
    }
}
