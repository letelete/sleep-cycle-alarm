package com.gmail.brunokawka.poland.sleepcyclealarm.tabs.addalarm.wakeupat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.events.SetHourButtonClickedEvent;
import com.gmail.brunokawka.poland.sleepcyclealarm.tabs.adapters.AddAlarmsAdapter;
import com.gmail.brunokawka.poland.sleepcyclealarm.tabs.addalarm.AddDialogFragment;
import com.gmail.brunokawka.poland.sleepcyclealarm.tabs.ui.EmptyStateRecyclerView;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.AlarmContentUtils;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.itemsbuilder.ItemsBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.itemsbuilder.WakeUpAtBuildingStrategy;

import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WakeUpAtFragment extends AddDialogFragment
    implements WakeUpAtContract.WakeUpAtView, DialogInterface.OnDismissListener {

    private static WakeUpAtPresenter wakeUpAtPresenter;
    
    private ItemsBuilder itemsBuilder;
    private AlertDialog dialog;
    private DateTime lastExecutionDate;
    private DateTime currentDate;

    @BindView(R.id.wakeUpAtRoot)
    ViewGroup root;

    @BindView(R.id.rv_wakeupat)
    EmptyStateRecyclerView recycler;

    @BindView(R.id.i_wakeupat_empty_state)
    View vEmptyView;

    @BindView(R.id.tv_fragment_wakeupat_action_description)
    TextView tvActionDescription;

    @BindView(R.id.tv_wakeupat_list_hint)
    TextView tvListHint;

    @Subscribe
    public void onWakeUpAtActivityClicked(SetHourButtonClickedEvent setHourButtonClickedEvent) {
        if (wakeUpAtPresenter != null) {
            wakeUpAtPresenter.handleFloatingActionButtonClicked();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = layoutInflater
                .inflate(R.layout.fragment_wake_up_at, container, false);
        ButterKnife.bind(this, view);

        wakeUpAtPresenter = new WakeUpAtPresenter();
        wakeUpAtPresenter.bindView(this);

        itemsBuilder = new ItemsBuilder();
        itemsBuilder.setBuildingStrategy(new WakeUpAtBuildingStrategy());

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        wakeUpAtPresenter.setUpEnvironment();
        setupUi();
    }

    private void setupUi() {
        updateCurrentDate();
        setLastExecutionDateFromPreferences();
        setupRecycler();
        updateDescription();
    }

    @Override
    public void updateCurrentDate() {
        currentDate = DateTime.now();
    }

    @Override
    public void setLastExecutionDateFromPreferences() {
        SharedPreferences pref = getActivity()
                .getSharedPreferences(getString(R.string.wakeupat_preferences_name),
                        Context.MODE_PRIVATE);

        if (pref.contains(getString(R.string.key_last_execution_date))) {
            String notFormattedDate
                    = pref.getString(getString(R.string.key_last_execution_date), null);

            if (!TextUtils.isEmpty(notFormattedDate)) {
                setLastExecutionDate(DateTime.parse(notFormattedDate));
            }
        }
    }

    @Override
    public void setLastExecutionDate(DateTime newDate) {
        lastExecutionDate = newDate;
    }

    private void setupRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter();
        recycler.setEmptyView(vEmptyView,
                R.drawable.ic_empty_wakeupat_list,
                R.string.wake_up_at_empty_list_title,
                R.string.wake_up_at_empty_list_summary);
        recycler.addViewToHideIfListEmpty(tvActionDescription);
        recycler.addViewToHideIfListEmpty(tvListHint);
    }

    @Override
    public void setupAdapter() {
        List<Item> items = lastExecutionDate != null
                ? itemsBuilder.getItems(currentDate, lastExecutionDate)
                : new ArrayList<Item>();
        recycler.setAdapter(new AddAlarmsAdapter(items));
    }

    @Override
    public void saveExecutionDateToPreferencesAsString() {
        Log.d(getClass().getName(), "Saving execution date to pref_alarm");
        SharedPreferences pref = getActivity()
                .getSharedPreferences(getString(R.string.wakeupat_preferences_name),
                        Context.MODE_PRIVATE);
        pref.edit()
                .putString(getString(R.string.key_last_execution_date), lastExecutionDate.toString())
                .apply();
    }

    @Override
    public void showSetTimeDialog() {
        final View content = getLayoutInflater()
                .inflate(R.layout.dialog_set_hour_to_wake_up_at, root, false);
        final WakeUpAtContract.WakeUpAtView.DialogContract dialogContract
                = (WakeUpAtContract.WakeUpAtView.DialogContract) content;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(content)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        DateTime newChosenExecutionDate = dialogContract.getDateTime();
                        wakeUpAtPresenter.tryToGenerateAListWithGivenValues(newChosenExecutionDate,
                                currentDate, lastExecutionDate);
                        wakeUpAtPresenter.dismissTimeDialog();
                    }
                })
                .setOnDismissListener(this)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        wakeUpAtPresenter.dismissTimeDialog();
                    }
                });
        dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    public void updateDescription() {
        if (lastExecutionDate != null) {
            String baseString = getString(R.string.wakeupat_view_description);
            String simpleExecutionDate = AlarmContentUtils.getTitle(lastExecutionDate);
            String formattedDescription = String.format(baseString, simpleExecutionDate);
            tvActionDescription.setText(formattedDescription);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (wakeUpAtPresenter != null) {
            wakeUpAtPresenter.unbindView();
        }

        if (dialog != null) {
            dialog.dismiss();
        }

        if (lastExecutionDate != null) {
            lastExecutionDate = null;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        wakeUpAtPresenter.dismissTimeDialog();
    }
}
