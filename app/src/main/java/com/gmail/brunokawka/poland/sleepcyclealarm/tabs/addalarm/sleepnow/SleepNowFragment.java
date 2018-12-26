package com.gmail.brunokawka.poland.sleepcyclealarm.tabs.addalarm.sleepnow;

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
import com.gmail.brunokawka.poland.sleepcyclealarm.tabs.adapters.AddAlarmsAdapter;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.AlarmContentUtils;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.itemsbuilder.ItemsBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.itemsbuilder.SleepNowBuildingStrategy;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SleepNowFragment extends Fragment {

    @BindView(R.id.sleepNowFragmentRecycler) protected RecyclerView recycler;

    private ItemsBuilder itemsBuilder;

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  layoutInflater.inflate(R.layout.fragment_sleep_now, container, false);
        ButterKnife.bind(this, view);

        itemsBuilder = new ItemsBuilder();
        itemsBuilder.setBuildingStrategy(new SleepNowBuildingStrategy());

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
        recycler.setAdapter(
                new AddAlarmsAdapter(itemsBuilder.getItems(getCurrentDateFormattedToSimple(),
                        null)));
    }

    private DateTime getCurrentDateFormattedToSimple() {
        return AlarmContentUtils.getDateConvertedToSimple(DateTime.now());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
