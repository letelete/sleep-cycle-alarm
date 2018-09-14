package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.wakeupat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.ListAdapter;

import org.joda.time.DateTime;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WakeUpAtFragment extends Fragment
    implements WakeUpAtPresenter.ViewContract {
    private static final String TAG = "WakeUpAtFragmentLog";

    private DateTime lastExecutionDate;

    ArrayList<Item> items;

    @BindView(R.id.wakeUpAtRoot)
    ViewGroup root;

    @BindView(R.id.wakeUpAtListCardView)
    CardView listCardView;

    @BindView(R.id.wakeUpAtFragmentRecycler)
    RecyclerView recycler;

    static WakeUpAtPresenter wakeUpAtPresenter;
    AlertDialog dialog;

    @BindView(R.id.wakeUpAtFloatingActionButtonExtended)
    Button floatingActionButtonExtended;

    @OnClick(R.id.wakeUpAtFloatingActionButtonExtended)
    public void onFloatingActionButtonClick() {
        if (wakeUpAtPresenter != null) {
            wakeUpAtPresenter.showTimeDialog();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_wake_up_at, container, false);
        ButterKnife.bind(this, view);
        wakeUpAtPresenter = new WakeUpAtPresenter();
        wakeUpAtPresenter.bindView(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lastExecutionDate = getExecutionDateFromPreferences();
        setUpRecycler();
        setUpAdapterAndCheckForContentUpdate();
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
    }

    @Override
    public void showSetTimeDialog() {
        final View content = getLayoutInflater().inflate(R.layout.dialog_set_hour_to_wake_up_at, root, false);
        final DialogContract dialogContract = (DialogContract) content;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(content)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        wakeUpAtPresenter.generateList(dialogContract);
                        wakeUpAtPresenter.dismissTimeDialog();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        wakeUpAtPresenter.dismissTimeDialog();
                        dialog.dismiss();
                    }
                });
        dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    public void showList() {

    }

    @Override
    public void hideList() {

    }

    @Override
    public void showEmptyListHint() {

    }

    @Override
    public void hideEmptyListHint() {

    }

    @Override
    public void showListHelper() {

    }

    @Override
    public void hideListHelper() {

    }

    @Override
    public void generateList(DateTime executionDate) {
        if (executionDate != null) {
            updateLastExecutionDate(executionDate);
            setUpAdapterAndCheckForContentUpdate();
        } else {
            Log.e(TAG, "executionDate is null");
        }
    }

    private void updateLastExecutionDate(DateTime newDate) {
        if (lastExecutionDate == null || lastExecutionDate != newDate) {
            lastExecutionDate = newDate;
            saveExecutionDateToPreferencesAsString();
        }
    }

    private void saveExecutionDateToPreferencesAsString() {
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                .putString(getString(R.string.key_last_execution_date), lastExecutionDate.toString())
                .apply();
    }

    private DateTime getExecutionDateFromPreferences() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (pref.contains(getString(R.string.key_last_execution_date))) {
            return DateTime.parse(pref.getString(getString(R.string.key_last_execution_date), null));
        }

        return null;
    }

    private void setUpRecycler() {
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setItemAnimator(new DefaultItemAnimator());
    }

    private void setUpAdapterAndCheckForContentUpdate() {
        if (lastExecutionDate != null) {
            items = WakeUpAtItemsBuilder.getItemsForExecutionDate(getExecutionDateFromPreferences());
            recycler.setAdapter(new ListAdapter(items, recycler));
        }
    }
}
