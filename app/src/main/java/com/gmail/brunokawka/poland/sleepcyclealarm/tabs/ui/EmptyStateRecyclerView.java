package com.gmail.brunokawka.poland.sleepcyclealarm.tabs.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;

import org.jetbrains.annotations.NotNull;

public class EmptyStateRecyclerView extends RecyclerView {
    @Nullable View emptyView;

    public EmptyStateRecyclerView(Context context) { super(context); }

    public EmptyStateRecyclerView(Context context, AttributeSet attrs) { super(context, attrs); }

    public EmptyStateRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    final @NotNull
    AdapterDataObserver observer = new AdapterDataObserver() {
        @Override public void onChanged() {
            super.onChanged();
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };

    private void checkIfEmpty() {
        if (emptyView != null && getAdapter() != null) {
            boolean showEmptyView = getAdapter().getItemCount() == 0;
            emptyView.setVisibility(showEmptyView ? VISIBLE : GONE);
            setVisibility(showEmptyView ? GONE : VISIBLE);
        }
    }

    @Override public void setAdapter(@Nullable Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
        checkIfEmpty();
    }

    public void setEmptyView(@Nullable View emptyView,
                             int resImage,
                             int resTitle,
                             int resSubtitle) {
        this.emptyView = emptyView;
        Drawable imageDrawable = getResources().getDrawable(resImage);
        ((ImageView) emptyView.findViewById(R.id.iv_empty_state_icon))
                .setImageDrawable(imageDrawable);
        setEmptyView(emptyView, resTitle, resSubtitle);
    }

    public void setEmptyView(@Nullable View emptyView,
                             int resTitle,
                             int resSubtitle) {
        this.emptyView = emptyView;

        String title = getResources().getString(resTitle);
        String subTitle = getResources().getString(resSubtitle);

        ((TextView) emptyView.findViewById(R.id.tv_empty_state_title)).setText(title);
        ((TextView) emptyView.findViewById(R.id.tv_empty_state_subtitle)).setText(subTitle);
        checkIfEmpty();
    }

}