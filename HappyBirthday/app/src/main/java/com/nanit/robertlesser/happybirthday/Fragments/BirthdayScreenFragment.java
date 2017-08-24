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

import com.nanit.robertlesser.happybirthday.R;

/**
 * A simple {@link Fragment} subclass for the Birthday Screen
 */
public class BirthdayScreenFragment extends Fragment {

    private View detailsView;
    private ImageView ivPictureImage;
    private ImageView ivBackgroundImage;

    public static final String TAG = BirthdayScreenFragment.class.getSimpleName();

    public BirthdayScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        detailsView = inflater.inflate(R.layout.fragment_birthday_screen, container, false);
        findViewsById();

//        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.default_place_holder_blue);
//        ivPictureImage.setImageDrawable(drawable);

        return detailsView;
    }

    private void findViewsById() {
        ivPictureImage = detailsView.findViewById(R.id.picture_image);
        ivBackgroundImage = detailsView.findViewById(R.id.background_image);
    }

}
