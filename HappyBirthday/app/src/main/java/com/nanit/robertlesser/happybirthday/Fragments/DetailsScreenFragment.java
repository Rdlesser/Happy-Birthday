package com.nanit.robertlesser.happybirthday.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.nanit.robertlesser.happybirthday.Activities.MainActivity;
import com.nanit.robertlesser.happybirthday.Interfaces.HappyBirthdayFragment;
import com.nanit.robertlesser.happybirthday.R;
import com.nanit.robertlesser.happybirthday.Utilities.Utility;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsScreenFragment extends Fragment implements View.OnFocusChangeListener,
        View.OnClickListener, HappyBirthdayFragment, TextWatcher {

    public static final String BIRTHDAY_NAME = "name";
    public static final String BIRTHDAY_DATE = "date";
    public static final String BIRTHDAY_PIC_PATH = "picture_path";

    private View detailsView;
    private ConstraintLayout detailsScreenLayout;
    private ImageView ivBottomImage;
    private EditText etName;
    private EditText etBirthday;
    private Button btnPicture;
    private ImageView ivPictureImage;
    private Button btnShowBirthday;

    private DatePickerDialog birthdayDatePickerDialog;

    private SimpleDateFormat dateFormat;
    private MainActivity mainActivity;

    private Bitmap bitmap;


    public DetailsScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mainActivity = (MainActivity) getContext();
        // Inflate the layout for this fragment
        detailsView = inflater.inflate(R.layout.fragment_details_screen, container, false);

        findViewsById();
        setupBackground();
        setupTouchListener(detailsView);

        dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        setBirthdayField();
        setupFields();
        setupListeners();
        return detailsView;
    }

    private void setupBackground() {
        // Randomly choose a number between 0 and 2 to be used to randomly set the background
        int randomInt = Utility.randomIntInRange(0, 2);
        switch (randomInt) {
            case 0:
                // Set background to "Elephant"
                detailsScreenLayout.setBackgroundColor(ContextCompat.getColor(
                        mainActivity, R.color.elephant_background));
                ivBottomImage.setImageDrawable(ContextCompat.getDrawable(
                        mainActivity,
                        R.drawable.elephant_activity));
                break;
            case 1:
                // Set background to "Fox"
                detailsScreenLayout.setBackgroundColor(ContextCompat.getColor(
                        mainActivity, R.color.fox_background));
                ivBottomImage.setImageDrawable(ContextCompat.getDrawable(
                        mainActivity,
                        R.drawable.fox_activity));
                break;
            case 2:
                // Set background to "Pelican"
                detailsScreenLayout.setBackgroundColor(ContextCompat.getColor(
                        mainActivity, R.color.pelican_background));
                ivBottomImage.setImageDrawable(ContextCompat.getDrawable(
                        mainActivity,
                        R.drawable.pelican_activity));
                break;
        }
    }

    private void setupListeners() {
        etName.addTextChangedListener(this);
        etBirthday.addTextChangedListener(this);
        btnPicture.setOnClickListener(this);
        btnShowBirthday.setOnClickListener(this);

    }

    /**
     * Set up fields according to previous app launches
     */
    private void setupFields() {
        SharedPreferences sharedPreferences = mainActivity.getSharedPreferences(mainActivity.getPackageName(),
                Context.MODE_PRIVATE);
        String name = sharedPreferences.getString(BIRTHDAY_NAME, "");
        etName.setText(name);
        String date = sharedPreferences.getString(BIRTHDAY_DATE, "");
        etBirthday.setText(date);
        String path = sharedPreferences.getString(BIRTHDAY_PIC_PATH, "");
        File imgFile = new  File(path);
        Bitmap bitmap = null;
        if (imgFile.exists()) {
            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        if (bitmap != null) {
            ivPictureImage.setImageBitmap(bitmap);
        }

        enableShowBirthdayButtonIfReady();
    }

    /**
     * Perform all the "findViewById()" calls
     */
    private void findViewsById() {
        detailsScreenLayout = detailsView.findViewById(R.id.details_screen_layout);
        ivBottomImage = detailsView.findViewById(R.id.bottom_image);
        etName = detailsView.findViewById(R.id.name_view);
        etBirthday = detailsView.findViewById(R.id.birthday_view);
        btnPicture = detailsView.findViewById(R.id.picture_button);
        ivPictureImage = detailsView.findViewById(R.id.picture_image);

        btnShowBirthday = detailsView.findViewById(R.id.show_birthday_button);
    }

    /**
     * Set up the date picker dialog for the birhday field
     */
    private void setBirthdayField() {

        etBirthday.setOnFocusChangeListener(this);
        etBirthday.setOnClickListener(this);
        etBirthday.setInputType(InputType.TYPE_NULL);

        Calendar newCalendar = Calendar.getInstance();
        birthdayDatePickerDialog = new DatePickerDialog(getContext(), new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etBirthday.setText(dateFormat.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
        birthdayDatePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
    }

    @Override
    public void onFocusChange(View view, boolean b) {

        switch (view.getId()){

            case R.id.birthday_view:
                if (b) {
                    birthdayDatePickerDialog.show();

                }
                break;
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.birthday_view:
                birthdayDatePickerDialog.show();
                break;

            case R.id.picture_button:
                selectImage();
                break;

            case R.id.show_birthday_button:
                mainActivity.displayBirthdayScreen();
                break;
        }
    }

    /**
     * Method for selecting the image for the preview image view
     */
    private void selectImage() {

        final String[] pictureOptions = mainActivity.getResources().
                getStringArray(R.array.picture_options);
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle(R.string.picture);
        builder.setItems(pictureOptions, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                if (pictureOptions[item].equals(getString(R.string.gallery_select))) {
                    String[] askedPermissions = new String[] {
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    };
                    boolean hasPermission = Utility.checkPermission(
                            mainActivity, askedPermissions);
                    mainActivity.setUserChosenTask(getString(R.string.gallery_select));
                    if (hasPermission) {
                        mainActivity.startGalleryIntent();
                    }
                }
                else if (pictureOptions[item].equals(getString(R.string.take_photo))) {
                    String[] askedPermissions = new String[] {
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    };
                    boolean hasPermission = Utility.checkPermission(
                            mainActivity, askedPermissions);
                    mainActivity.setUserChosenTask(getString(R.string.take_photo));
                    if (hasPermission){
                        mainActivity.startCameraIntent();
                    }
                }
                else if (pictureOptions[item].equals(getString(R.string.cancel))) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }

    @Override
    public void setImage(Bitmap bitmap) {
        ivPictureImage.setImageBitmap(bitmap);

    }

    /**
     * Set up touch listener for non-text box views to hide keyboard.
     * @param view The ViewGroup containing all the views
     */
    public void setupTouchListener(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    Utility.hideSoftKeyboard(mainActivity);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupTouchListener(innerView);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        String birthdayName = etName.getText().toString();
        Utility.saveStringInPrefs(mainActivity, BIRTHDAY_NAME, birthdayName);

        String birthdayDate = etBirthday.getText().toString();
        Utility.saveStringInPrefs(mainActivity, BIRTHDAY_DATE, birthdayDate);

        enableShowBirthdayButtonIfReady();

    }

    /**
     * Enable/Disable the 'show birthday button' when name and birthday have been inserted/removed
     */
    private void enableShowBirthdayButtonIfReady() {
        boolean isReady = (etName.getText().length() > 0) &&
                (etBirthday.getText().length() > 0);
        btnShowBirthday.setEnabled(isReady);
    }
}
