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
import android.widget.LinearLayout;
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
    private LinearLayout smallAgeLayout;
    private LinearLayout bigAgeLayout;

    private MainActivity mainActivity;

    private SimpleDateFormat dateFormat;
    int monthDifference;

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

    /**
     * Set up the UI views according to the user's choices
     */
    private void setUpViews() {
        SharedPreferences sharedPreferences = mainActivity.getSharedPreferences(mainActivity.getPackageName(),
                Context.MODE_PRIVATE);

        // Setup the name text view
        String name = sharedPreferences.getString(BIRTHDAY_NAME, "");
        setName(name);

        // Setup the age image
        String birthday = sharedPreferences.getString(BIRTHDAY_DATE, "");
        setAge(birthday);
        Date birthdayDate = null;
        try {
            birthdayDate = dateFormat.parse(birthday);
            //catch exception
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        monthDifference = Utility.getDateDiffMonths(birthdayDate, new Date());


    }

    /**
     * Randomly set up the background based on 1 of 3 options:
     * - Elephant
     * - Fox
     * - Pelican
     */
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

    /**
     * Perform all the "findViewById()" calls
     */
    private void findViewsById() {
        ivPictureImage = detailsView.findViewById(R.id.picture_image);
        ivBackgroundImage = detailsView.findViewById(R.id.background_image);
        tvName = detailsView.findViewById(R.id.name_text_view);
        ivAgeView = detailsView.findViewById(R.id.age_image_view);
        tvUnits = detailsView.findViewById(R.id.units_text_view);
        smallAgeLayout = detailsView.findViewById(R.id.small_age_layout);
        bigAgeLayout = detailsView.findViewById(R.id.big_age_layout);
    }

    /**
     * Set tvName's text according to parameter
     * @param name A string representing the name to be used
     */
    private void setName(String name) {
        Resources resources = getResources();
        String nameText = resources.getString(R.string.birthday_name, name);
        tvName.setText(nameText);
    }

    /**
     * Set the age image according to the given birthday
     * @param birthday
     */
    private void setAge(String birthday) {
        Date birthdayDate = null;
        try {
            birthdayDate = dateFormat.parse(birthday);
            //catch exception
        } catch (ParseException e) {
            e.printStackTrace();
        }
        monthDifference = Utility.getDateDiffMonths(birthdayDate, new Date());
        // We'll be working with age and not monthDifference as we'll be using monthDifference again
        // for the units TextView
        int age = monthDifference;
        // Case we need to start counting in years as we've passed 12 months
        if (monthDifference > 12) {
            age /= 12;
        }
        if (age > 12) {
            // If the person is older than 12 - we'll need to work with 2 imageViews for the age

        }
        else {
            // Age of person is less than 13 - we'll work with smallAgeLayout
            bigAgeLayout.setVisibility(View.GONE);
            smallAgeLayout.setVisibility(View.VISIBLE);
            String imageId = "num" + age;
            int id = mainActivity.getResources().
                    getIdentifier(imageId, "drawable", mainActivity.getPackageName());
            ivAgeView.setImageResource(id);
        }
    }
}
