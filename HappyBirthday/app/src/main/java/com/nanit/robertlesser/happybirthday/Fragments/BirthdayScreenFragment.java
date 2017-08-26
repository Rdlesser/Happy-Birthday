package com.nanit.robertlesser.happybirthday.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.nanit.robertlesser.happybirthday.Fragments.DetailsScreenFragment.BIRTHDAY_DATE;
import static com.nanit.robertlesser.happybirthday.Fragments.DetailsScreenFragment.BIRTHDAY_NAME;
import static com.nanit.robertlesser.happybirthday.Fragments.DetailsScreenFragment.BIRTHDAY_PIC_PATH;

/**
 * A simple {@link Fragment} subclass for the Birthday Screen
 */
public class BirthdayScreenFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = BirthdayScreenFragment.class.getSimpleName();

    private static String AGE_IMAGE_PREFIX = "num";
    private static String IMAGE_TYPE = "drawable";

    private View detailsView;
    private ImageView ivBtnClose;
    private ImageView ivPictureImage;
    private ImageView ivBackgroundImage;
    private TextView tvName;
    private ImageView ivAgeView;
    private ImageView ivTensImageView;
    private ImageView ivUnitsImageView;
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

        // Setup the age layout
        String birthday = sharedPreferences.getString(BIRTHDAY_DATE, "");
        setAgeView(birthday);
        setUnitsTextView();

        // Setup the picture
        String path = sharedPreferences.getString(BIRTHDAY_PIC_PATH, "");
        setPictureImageView(path);

    }

    /**
     * Set up the image for ivPictureImage with the picture chosen in the details screen if
     * exists
     * @param path The path to the image chosen in the details screen
     */
    private void setPictureImageView(String path) {
        File imgFile = new  File(path);
        Bitmap bitmap = null;
        if (imgFile.exists()) {
            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        if (bitmap != null) {
            Bitmap pictureBitmap = ((BitmapDrawable)ivPictureImage.getDrawable()).getBitmap();
            int height = pictureBitmap.getHeight();
            int width = pictureBitmap.getWidth();
            ivPictureImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap, width, height, false));
        }
    }

    /**
     * Set up the units text view according to monthDifference
     */
    private void setUnitsTextView() {
        if (monthDifference <= 12) {
            // Case we're dealing with months
            String suffix = monthDifference == 1? "" : "s";
            String text = mainActivity.getString(R.string.month_old, suffix);
            tvUnits.setText(text);
        }
        else {
            // Case we're dealing with years
            String suffix = (monthDifference > 12 && monthDifference < 18)? "" : "s";
            String text = mainActivity.getString(R.string.year_old, suffix);
            tvUnits.setText(text);
        }
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
        ivBtnClose = detailsView.findViewById(R.id.close_button);
        ivBtnClose.setOnClickListener(this);
        ivPictureImage = detailsView.findViewById(R.id.picture_image);
        ivBackgroundImage = detailsView.findViewById(R.id.background_image);
        tvName = detailsView.findViewById(R.id.name_text_view);
        ivAgeView = detailsView.findViewById(R.id.age_image_view);
        tvUnits = detailsView.findViewById(R.id.units_text_view);
        smallAgeLayout = detailsView.findViewById(R.id.small_age_layout);
        bigAgeLayout = detailsView.findViewById(R.id.big_age_layout);
        ivTensImageView = detailsView.findViewById(R.id.tens_image_view);
        ivUnitsImageView = detailsView.findViewById(R.id.units_image_view);
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
    private void setAgeView(String birthday) {
        Date birthdayDate = null;
        try {
            birthdayDate = dateFormat.parse(birthday);
            //catch exception
        } catch (ParseException e) {
            e.printStackTrace();
        }
        monthDifference = Utility.getDateDiffMonths(birthdayDate, new Date());
        if (monthDifference >= 18 && monthDifference < 24) {
            // Special case of 1.5 years old
            switchViewVisibility(smallAgeLayout, bigAgeLayout);
            setImageViewResource("1_half", ivAgeView);
        }
        else {
            // We'll be working with age and not monthDifference as we'll be using monthDifference again
            // for the units TextView
            int age = monthDifference;
            // Case we need to start counting in years as we've passed 12 months
            if (monthDifference > 12) {
                age /= 12;
            }
            if (age > 12) {
                // If the person is older than 12 - we'll need to work with 2 imageViews for the age
                switchViewVisibility(bigAgeLayout, smallAgeLayout);
                int tens = age / 10;
                int units = age % 10;
                setImageViewResource(String.valueOf(tens), ivTensImageView);
                setImageViewResource(String.valueOf(units), ivUnitsImageView);

            }
            else {
                // Age of person is less than 13 - we'll work with smallAgeLayout
                switchViewVisibility(smallAgeLayout, bigAgeLayout);
                setImageViewResource(String.valueOf(age), ivAgeView);
            }
        }
    }

    /**
     * Switch between 2 views' visibility
     * @param toShow The View that's visibility we would like to change to Visible
     * @param toHide The View that's visibility we would like to change to GONE
     */
    private void switchViewVisibility(View toShow, View toHide) {
        toHide.setVisibility(View.GONE);
        toShow.setVisibility(View.VISIBLE);
    }

    /**
     * Set the image resource of an ImageView according to a given string
     * @param imageIdentifier The identifier used for identify the image name
     * @param viewToSet The view that we would like to set
     */
    private void setImageViewResource(String imageIdentifier, ImageView viewToSet) {
        String imageId = AGE_IMAGE_PREFIX + imageIdentifier;
        int id = mainActivity.getResources().
                getIdentifier(imageId, IMAGE_TYPE, mainActivity.getPackageName());
        viewToSet.setImageResource(id);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_button:
                mainActivity.onBackPressed();
                break;
        }
    }
}
