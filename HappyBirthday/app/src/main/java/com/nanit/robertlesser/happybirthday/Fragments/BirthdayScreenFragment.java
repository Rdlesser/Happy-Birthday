package com.nanit.robertlesser.happybirthday.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanit.robertlesser.happybirthday.Activities.MainActivity;
import com.nanit.robertlesser.happybirthday.R;
import com.nanit.robertlesser.happybirthday.Utilities.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.nanit.robertlesser.happybirthday.Fragments.DetailsScreenFragment.BIRTHDAY_DATE;
import static com.nanit.robertlesser.happybirthday.Fragments.DetailsScreenFragment.BIRTHDAY_NAME;

/**
 * A simple {@link Fragment} subclass for the Birthday Screen
 */
public class BirthdayScreenFragment extends Fragment {

    public static final String TAG = BirthdayScreenFragment.class.getSimpleName();

    private View detailsView;
    private ImageView ivPictureImage;
    private ImageView ivBackgroundImage;
    private TextView tvName;
    private ImageView ivAgeView;
    private TextView tvUnits;

    private MainActivity mainActivity;

    private SimpleDateFormat dateFormat;

    public BirthdayScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mainActivity = (MainActivity) getContext();
        // Inflate the layout for this fragment
        detailsView = inflater.inflate(R.layout.fragment_birthday_screen, container, false);

        dateFormat = mainActivity.getDateFormat();

        findViewsById();
        setupBackground();
        setUpViews();

        return detailsView;
    }

    private void setUpViews() {
        SharedPreferences sharedPreferences = mainActivity.getSharedPreferences(mainActivity.getPackageName(),
                Context.MODE_PRIVATE);

        // Setup the name text view
        String name = sharedPreferences.getString(BIRTHDAY_NAME, "");
        Resources resources = getResources();
        String nameText = resources.getString(R.string.birthday_name, name);
        tvName.setText(nameText);

        // Setup the age image
        String birthday = sharedPreferences.getString(BIRTHDAY_DATE, "");
        Date birthdayDate = null;
        try {
            birthdayDate = dateFormat.parse(birthday);
            //catch exception
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int monthDifference = Utility.getDateDiffMonths(birthdayDate, new Date());


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
        tvName = detailsView.findViewById(R.id.name_text_view);
        ivAgeView = detailsView.findViewById(R.id.age_image_view);
        tvUnits = detailsView.findViewById(R.id.units_text_view);
    }

}
