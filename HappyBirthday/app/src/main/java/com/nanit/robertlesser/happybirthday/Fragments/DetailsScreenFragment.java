package com.nanit.robertlesser.happybirthday.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.R.attr.bitmap;
import static android.R.attr.data;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsScreenFragment extends Fragment implements View.OnFocusChangeListener,
        View.OnClickListener, HappyBirthdayFragment {

    private View detailsView;
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

        dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        setBirthdayField();

        return detailsView;
    }

    private void findViewsById() {
        etName = detailsView.findViewById(R.id.name_view);
        etBirthday = detailsView.findViewById(R.id.birthday_view);
        btnPicture = detailsView.findViewById(R.id.picture_button);
        btnPicture.setOnClickListener(this);
        ivPictureImage = detailsView.findViewById(R.id.picture_image);
        
        btnShowBirthday = detailsView.findViewById(R.id.show_birthday_button);
    }

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
                    break;
                }
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
        }
    }

    private void selectImage() {

        final String[] pictureOptions = mainActivity.getResources().
                getStringArray(R.array.picture_options);
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle(R.string.picture);
        builder.setItems(pictureOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                boolean hasPermission = Utility.checkPermission(mainActivity);
                if (pictureOptions[item].equals(getString(R.string.gallery_select))) {
                    mainActivity.setUserChosenTask(getString(R.string.gallery_select));
                    if (hasPermission) {
                        mainActivity.startGalleryIntent();
                    }
                }
                else if (pictureOptions[item].equals(getString(R.string.take_photo))) {
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
    public void onSelectFromGalleryResult(Intent intent) {
        Bitmap bitmap = null;
        if (intent != null){
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), intent.getData());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        ivPictureImage.setImageBitmap(bitmap);

    }

    @Override
    public void onCaptureImageResult(Intent intent) {
        InputStream stream = null;
        try {
            if (bitmap != null) {
                bitmap.recycle();
            }
            stream = mainActivity.getContentResolver().openInputStream(intent.getData());
            bitmap = BitmapFactory.decodeStream(stream);

            ivPictureImage.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
