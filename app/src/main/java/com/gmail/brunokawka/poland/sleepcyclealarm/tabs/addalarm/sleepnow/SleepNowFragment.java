package com.gmail.brunokawka.poland.sleepcyclealarm.tabs.addalarm.sleepnow;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.tabs.adapters.AddAlarmsAdapter;
import com.gmail.brunokawka.poland.sleepcyclealarm.tabs.addalarm.AddDialogFragment;
import com.gmail.brunokawka.poland.sleepcyclealarm.tabs.ui.EmptyStateRecyclerView;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.AlarmContentUtils;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.itemsbuilder.ItemsBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.itemsbuilder.SleepNowBuildingStrategy;

import org.joda.time.DateTime;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SleepNowFragment extends AddDialogFragment {

    private ItemsBuilder itemsBuilder;

    @BindView(R.id.rv_sleepnow)
    EmptyStateRecyclerView recycler;

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
        setupRecycler();
    }

    private void setupRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        DateTime currentDateFormatted = AlarmContentUtils.getDateConvertedToSimple(DateTime.now());
        List<Item> items = itemsBuilder.getItems(currentDateFormatted, null);
        recycler.setAdapter(new AddAlarmsAdapter(items));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
