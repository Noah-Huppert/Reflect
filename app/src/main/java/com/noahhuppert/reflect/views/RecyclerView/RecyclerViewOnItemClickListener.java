package com.noahhuppert.reflect.views.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * A click listener interface for RecyclerView items
 */
public interface RecyclerViewOnItemClickListener {
    /**
     * Called when a {@link RecyclerView} item is clicked
     * @param viewHolder The {@link android.support.v7.widget.RecyclerView.ViewHolder} of the item
     *                   that was clicked
     * @param index The index of the item that was clicked
     */
    void onClick(RecyclerView.ViewHolder viewHolder, int index);
}
