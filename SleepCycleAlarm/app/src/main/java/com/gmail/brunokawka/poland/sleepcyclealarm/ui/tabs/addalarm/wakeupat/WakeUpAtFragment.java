package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.wakeupat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.ListAdapter;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WakeUpAtFragment extends Fragment
    implements WakeUpAtPresenter.ViewContract {
    private static final String TAG = "WakeUpAtFragmentLog";

    private DateTime lastExecutionDate = null;

    @BindView(R.id.wakeUpAtRoot)
    ViewGroup root;

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
    public void generateList(DateTime executionDate) {
        if (executionDate != null) {
            if (lastExecutionDate == null || lastExecutionDate != executionDate) {
                lastExecutionDate = executionDate;
            }

            Log.d(TAG, lastExecutionDate.toString());

            setUpRecycler();
            recycler.setAdapter(new ListAdapter(WakeUpAtItemsBuilder.getItemsForExecutionDate(lastExecutionDate), recycler));
        } else {
            Log.e(TAG, "executionDate is null");
        }
    }

    private void setUpRecycler() {
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setItemAnimator(new DefaultItemAnimator());
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
}
