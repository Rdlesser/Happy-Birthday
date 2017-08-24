package com.nanit.robertlesser.happybirthday.Fragments;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nanit.robertlesser.happybirthday.Activities.MainActivity;
import com.nanit.robertlesser.happybirthday.R;
import com.nanit.robertlesser.happybirthday.Utilities.Utility;

/**
 * A simple {@link Fragment} subclass for the Birthday Screen
 */
public class BirthdayScreenFragment extends Fragment {

    public static final String TAG = BirthdayScreenFragment.class.getSimpleName();

    private View detailsView;
    private ImageView ivPictureImage;
    private ImageView ivBackgroundImage;

    private MainActivity mainActivity;

    public BirthdayScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mainActivity = (MainActivity) getContext();
        // Inflate the layout for this fragment
        detailsView = inflater.inflate(R.layout.fragment_birthday_screen, container, false);
        findViewsById();
        setupBackground();

        return detailsView;
    }

    private void setupBackground() {
        // Randomly choose a number between 0 and 2 to be used to randomly set the background
        int randomInt = Utility.randomIntInRange(0, 2);
        switch (randomInt) {
            case 0:
                // Set background to "Elephant"
                ivBackgroundImage.setImageDrawable(ContextCompat.getDrawable(
                        mainActivity,
                        R.drawable.android_elephant_popup));
                ivPictureImage.setImageDrawable(ContextCompat.getDrawable(
                        mainActivity,
                        R.drawable.default_place_holder_yellow));
                break;
            case 1:
                // Set background to "Fox"
                ivBackgroundImage.setImageDrawable(ContextCompat.getDrawable(
                        mainActivity,
                        R.drawable.android_fox_popup));
                ivPictureImage.setImageDrawable(ContextCompat.getDrawable(
                        mainActivity,
                        R.drawable.default_place_holder_green));
                break;
            case 2:
                // Set background to "Pelican"
                ivBackgroundImage.setImageDrawable(ContextCompat.getDrawable(
                        mainActivity,
                        R.drawable.android_pelican_popup));
                ivPictureImage.setImageDrawable(ContextCompat.getDrawable(
                        mainActivity,
                        R.drawable.default_place_holder_blue));
                break;
        }
    }

    private void findViewsById() {
        ivPictureImage = detailsView.findViewById(R.id.picture_image);
        ivBackgroundImage = detailsView.findViewById(R.id.background_image);
    }

}
