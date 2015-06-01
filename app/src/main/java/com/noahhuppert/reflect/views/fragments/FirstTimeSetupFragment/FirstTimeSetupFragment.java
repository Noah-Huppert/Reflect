package com.noahhuppert.reflect.views.fragments.FirstTimeSetupFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.noahhuppert.reflect.R;

public class FirstTimeSetupFragment extends Fragment {
    private ViewFlipper viewFlipper;
    private ImageView[] pageIndicators = new ImageView[3];
    private int currentPageIndex = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first_time_setup, container, false);

        viewFlipper = (ViewFlipper) rootView.findViewById(R.id.fragment_first_time_setup_view_flipper);

        pageIndicators[0] = (ImageView) rootView.findViewById(R.id.fragment_first_time_setup_page_indicator_1);
        pageIndicators[1] = (ImageView) rootView.findViewById(R.id.fragment_first_time_setup_page_indicator_2);
        pageIndicators[2] = (ImageView) rootView.findViewById(R.id.fragment_first_time_setup_page_indicator_3);

        switchPage(0);

        rootView.setOnTouchListener(rootViewOnTouchListener);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onPause() {
        super.onPause();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    private void switchPage(int index){
        currentPageIndex = index;

        for(int i = 0; i < pageIndicators.length; i++){
            if(i == currentPageIndex){
                pageIndicators[i].setAlpha(0.8f);
            } else {
                pageIndicators[i].setAlpha(0.5f);
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
                    if(currentPageIndex + 1 <= 2){
                        switchPage(currentPageIndex + 1);
                    }
                } else if(lastX < event.getX()){//RTL Swipe, backward
                    if(currentPageIndex - 1 >= 0){
                        switchPage(currentPageIndex - 1);
                    }
                }
            }

            return true;
        }
    };
}
