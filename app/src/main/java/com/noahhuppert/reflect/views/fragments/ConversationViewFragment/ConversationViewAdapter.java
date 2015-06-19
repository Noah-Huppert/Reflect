package com.noahhuppert.reflect.views.fragments.ConversationViewFragment;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.noahhuppert.reflect.R;
import com.noahhuppert.reflect.threading.MainThreadPool;
import com.noahhuppert.reflect.threading.ThreadResultHandler;

public class ConversationViewAdapter extends RecyclerView.Adapter<ConversationViewAdapter.ConversationViewViewHolder> {
    private static final String TAG = ConversationViewAdapter.class.getSimpleName();

    public static final int MESSAGE_BODY_INDEX = 0;
    public static final int MESSAGE_OURS_INDEX = 1;
    public static final int MESSAGE_SENDER_NAME_INDEX = 2;

    public static final String MESSAGE_OURS_TRUE_VALUE = "1";
    public static final String MESSAGE_OURS_FALSE_VALUE = "0";

    private String[][] messages;

    public ConversationViewAdapter(final Activity activity, final String threadId, final String senderName, final RecyclerView recyclerView) {
        messages = new String[0][3];

        GetMessagesForAdapterRunnable getMessagesForAdapterRunnable = new GetMessagesForAdapterRunnable(activity, threadId, new ThreadResultHandler<String[][]>() {
            @Override
            public void onDone(String[][] data) {
                messages = data;

                for(int i = 0; i < messages.length; i++){
                    messages[i][MESSAGE_SENDER_NAME_INDEX] = senderName;
                }

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                        recyclerView.scrollToPosition(getItemCount());
                    }
                });
            }

            @Override
            public void onError(Exception exception) {
                Log.e(TAG, "Exception fetching message for " + threadId, exception);
            }
        });

        MainThreadPool.getInstance().getPool().submit(getMessagesForAdapterRunnable);
    }

    public static final class ConversationViewViewHolder extends RecyclerView.ViewHolder{
        public TextView body;

        public ConversationViewViewHolder(View itemView) {
            super(itemView);
            body = (TextView) itemView.findViewById(R.id.fragment_conversation_view_view_holder_body);
        }
    }

    @Override
    public ConversationViewViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_conversation_view_view_holder, viewGroup, false);

        return new ConversationViewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ConversationViewViewHolder conversationViewViewHolder, int i) {

        if(messages[i][MESSAGE_OURS_INDEX].equals(MESSAGE_OURS_TRUE_VALUE)) {
            conversationViewViewHolder.body.setText(messages[i][MESSAGE_BODY_INDEX] + " <You");
            conversationViewViewHolder.body.setGravity(Gravity.END);
        } else{
            conversationViewViewHolder.body.setText(messages[i][MESSAGE_SENDER_NAME_INDEX] + "> " + messages[i][MESSAGE_BODY_INDEX]);
            conversationViewViewHolder.body.setGravity(Gravity.START);
        }
    }

    @Override
    public int getItemCount() {
        return messages.length;
    }
}
