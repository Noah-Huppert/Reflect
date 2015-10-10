package com.noahhuppert.reflect.views.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * A {@link RecyclerView} adapter that contains the appropriate code for adding a
 * {@link RecyclerViewOnItemClickListener}
 * @param <ViewHolderType> The {@link android.support.v7.widget.RecyclerView.ViewHolder} being used
 *                        for the adapter
 */
public abstract class RecyclerViewOnItemClickAdapter<ViewHolderType extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<ViewHolderType> {
    /**
     * The {@link RecyclerViewOnItemClickListener} to use when a {@link RecyclerView} is clicked
     */
    private RecyclerViewOnItemClickListener onClickListener;

    /**
     * Used to set the {@link RecyclerViewOnItemClickListener}
     * @param onClickListener The {@link RecyclerViewOnItemClickListener}
     */
    public void setOnItemClickListener(RecyclerViewOnItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolderType holder, final int position) {
        final ViewHolderType viewHolder = holder;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener != null) {
                    onClickListener.onClick(viewHolder, position);
                }
            }
        });
    }
}
