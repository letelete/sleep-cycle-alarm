package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.sleepnow;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.AlarmDAO;
import com.gmail.brunokawka.poland.sleepcyclealarm.events.ItemInListClickedEvent;
import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.ListAdapter;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.itemsbuilder.ItemsBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.itemsbuilder.SleepNowBuildingStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SleepNowFragment extends Fragment {

    private ItemsBuilder itemsBuilder;
    private AlarmDAO alarmDAO;

    @BindView(R.id.sleepNowFragmentRecycler)
    protected RecyclerView recycler;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSetAlarmEvent(ItemInListClickedEvent itemInListClickedEvent) {
        // TODO: show dialog that dialog has been added and will ring at: <hour>:<minute>
        alarmDAO.generateAlarmAndSaveItToRealm(itemInListClickedEvent.getItem());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  layoutInflater.inflate(R.layout.fragment_sleep_now, container, false);
        ButterKnife.bind(this, view);

        itemsBuilder = new ItemsBuilder();
        itemsBuilder.setBuildingStrategy(new SleepNowBuildingStrategy());

        alarmDAO = new AlarmDAO();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpRecycler();
    }

    private void setUpRecycler() {
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setItemAnimator(new DefaultItemAnimator());

        recycler.setAdapter(new ListAdapter(itemsBuilder.getItems(getCurrentDateFormattedToSimple(), null)));
    }

    private DateTime getCurrentDateFormattedToSimple() {
        return ItemContentBuilder.getDateConvertedToSimple(DateTime.now());
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        alarmDAO.cleanUp();
        super.onDestroyView();
    }
}
