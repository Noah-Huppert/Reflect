package com.noahhuppert.reflect.views.fragments.FirstTimeSetupFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import com.noahhuppert.reflect.R;

public class FirstTimeSetupFragment extends Fragment implements FirstTimeSetupPageSwitcher {
    public static final String ACTION_FRAGMENT_FIRST_TIME_SETUP_SET_PAGE= "com.noahhuppert.reflect.intent.actions.ACTION_FRAGMENT_FIRST_TIME_SETUP_SET_PAGE";
    public static final String EXTRA_PAGE_NUMBER = "page_number";

    /**
     * A key for the page direction in a {@link FirstTimeSetupFragment#ACTION_FRAGMENT_FIRST_TIME_SETUP_SET_PAGE} intent
     * If the value is positive then it is forward, if the value is negative then is is backwards
     */
    public static final String EXTRA_SET_PAGE_DIRECTION = "set_page_direction";

    private ViewFlipper viewFlipper;
    private View[] pageIndicators = new View[3];
    private int currentPageIndex = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first_time_setup, container, false);

        viewFlipper = (ViewFlipper) rootView.findViewById(R.id.fragment_first_time_setup_view_flipper);

        pageIndicators[0] = rootView.findViewById(R.id.fragment_first_time_setup_page_indicator_1);
        pageIndicators[1] = rootView.findViewById(R.id.fragment_first_time_setup_page_indicator_2);
        pageIndicators[2] = rootView.findViewById(R.id.fragment_first_time_setup_page_indicator_3);

        switchPage(0);

        rootView.setOnTouchListener(rootViewOnTouchListener);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onPause() {
        super.onPause();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    public void switchPage(int index){
        if(index < 0 || index > pageIndicators.length){
            return;
        }

        currentPageIndex = index;

        for(int i = 0; i < pageIndicators.length; i++){
            if(i == currentPageIndex){
                pageIndicators[i].setAlpha(0.8f);
            } else {
                pageIndicators[i].setAlpha(0.2f);
            }
        }

        viewFlipper.setDisplayedChild(currentPageIndex);
    }

    private View.OnTouchListener rootViewOnTouchListener = new View.OnTouchListener() {
        private float lastX;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                lastX = event.getX();
            } else if(event.getAction() == MotionEvent.ACTION_UP){
                if(lastX > event.getX()){//LTR Swipe, forward
                    switchPage(currentPageIndex + 1);
                } else if(lastX < event.getX()){//RTL Swipe, backward

                    switchPage(currentPageIndex - 1);
                }
            }

            return true;
        }
    };

    /* Getters */

    @Override
    public int getCurrentPageIndex() {
        return currentPageIndex;
    }
}
