package com.nanit.robertlesser.happybirthday.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.MagicalPermissions;
import com.nanit.robertlesser.happybirthday.Fragments.BirthdayScreenFragment;
import com.nanit.robertlesser.happybirthday.Fragments.DetailsScreenFragment;
import com.nanit.robertlesser.happybirthday.Interfaces.HappyBirthdayFragment;
import com.nanit.robertlesser.happybirthday.R;
import com.nanit.robertlesser.happybirthday.Utilities.Utility;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.nanit.robertlesser.happybirthday.Utilities.Utility.getScreenShot;
import static com.nanit.robertlesser.happybirthday.Utilities.Utility.store;

public class MainActivity extends AppCompatActivity {

    private int RESIZE_PHOTO_PIXELS_PERCENTAGE = 80;
    private static String FRAGMENT_TAG = "HappyBirthdayFragment";
    private static String PHOTO_FILE_NAME = "HappyBirthdayPhoto";
    private static String SCREENSHOT_FILE_NAME = "HappyBirthdayScreen.jpg";
    private static String DATE_FORMAT = "dd.MM.yyyy";

    ArrayList<String> activePermissions;
    private MagicalPermissions magicalPermissions;
    private MagicalCamera magicalCamera;

    Context context;

    String userChosenTask = "";
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        activePermissions = new ArrayList<>();

        dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        DetailsScreenFragment fragment = new DetailsScreenFragment();
        fragmentTransaction.add(R.id.fragment_container, fragment, FRAGMENT_TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Map<String, Boolean> map = magicalPermissions.permissionResult(requestCode, permissions, grantResults);
        boolean allGranted = true;
        for (String permission : map.keySet()) {
            if (map.get(permission)) {
                activePermissions.add(permission);
            }
            else {
                allGranted = map.get(permission);
            }
        }
        if (allGranted) {
            if (userChosenTask.equals(getString(R.string.gallery_select))) {
                startGalleryIntent();
            }
            else if (userChosenTask.equals(getString(R.string.take_photo))) {
                startCameraIntent();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            HappyBirthdayFragment fragment = (HappyBirthdayFragment) getSupportFragmentManager().
                    findFragmentByTag(FRAGMENT_TAG);
            magicalCamera.resultPhoto(requestCode, resultCode, data);
            Bitmap photo = magicalCamera.getPhoto();
            if (photo.getWidth() > photo.getHeight()){
                magicalCamera.resultPhoto(requestCode, resultCode, data,
                        MagicalCamera.ORIENTATION_ROTATE_90);
                photo = magicalCamera.getPhoto();
            }
            String path = magicalCamera.savePhotoInMemoryDevice(photo, PHOTO_FILE_NAME,
                    Bitmap.CompressFormat.JPEG, false);
            Utility.saveStringInPrefs(this, DetailsScreenFragment.BIRTHDAY_PIC_PATH, path);
            fragment.setImage(photo);
        }

    }

    /**
     * Method for selecting the image for the preview image view
     */
    public void selectImage() {

        final String[] pictureOptions = getResources().
                getStringArray(R.array.picture_options);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.picture);
        builder.setItems(pictureOptions, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                if (pictureOptions[item].equals(getString(R.string.gallery_select))) {
                    String[] askedPermissions = new String[] {
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    };
                    boolean hasPermission = Utility.checkPermission(
                            context, askedPermissions);
                    setUserChosenTask(getString(R.string.gallery_select));
                    if (hasPermission) {
                        startGalleryIntent();
                    }
                }
                else if (pictureOptions[item].equals(getString(R.string.take_photo))) {
                    String[] askedPermissions = new String[] {
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    };
                    boolean hasPermission = Utility.checkPermission(
                            context, askedPermissions);
                    setUserChosenTask(getString(R.string.take_photo));
                    if (hasPermission){
                        startCameraIntent();
                    }
                }
                else if (pictureOptions[item].equals(getString(R.string.cancel))) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }

    public void startCameraIntent() {
        setupMagicalCameraFields();
        magicalCamera.takePhoto();
    }

    public void startGalleryIntent() {
        setupMagicalCameraFields();
        magicalCamera.selectedPicture(getString(R.string.gallery_select));
    }

    private void setupMagicalCameraFields() {
        if (magicalCamera == null) {
            if (magicalPermissions == null) {
                magicalPermissions = new MagicalPermissions(this,
                        activePermissions.toArray(new String[]{}));
            }
            magicalCamera = new MagicalCamera(this, RESIZE_PHOTO_PIXELS_PERCENTAGE, magicalPermissions);
        }
    }

    public void displayBirthdayScreen() {
        // Create new fragment and transaction
        Fragment birthdayFragment = new BirthdayScreenFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        fragmentTransaction.replace(R.id.fragment_container, birthdayFragment, FRAGMENT_TAG);
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();
    }

    public String getUserChosenTask() {
        return userChosenTask;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public ArrayList<String> getActivePermissions() {
        if (activePermissions.size() == 0) {
            //Either we did not ask for permissions yet, or we restarted the app
            ArrayList<String> granted = new ArrayList<>();
            try {
                PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(),
                        PackageManager.GET_PERMISSIONS);
                for (int i = 0; i < pi.requestedPermissions.length; i++) {
                    if ((pi.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                        granted.add(pi.requestedPermissions[i]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            activePermissions = granted;
            return activePermissions;
        }
        else {
            return activePermissions;
        }
    }

    public MagicalPermissions getMagicalPermissions() {
        return magicalPermissions;
    }

    public void setUserChosenTask(String userChosenTask) {
        this.userChosenTask = userChosenTask;
    }

    public void setMagicalPermissions(MagicalPermissions magicalPermissions) {
        this.magicalPermissions = magicalPermissions;
    }

    public void shareScreen(View[] disableViews) {
        Utility.changeViewsVisibility(disableViews, INVISIBLE);
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        Bitmap screenshot = getScreenShot(rootView);
        Utility.changeViewsVisibility(disableViews, VISIBLE);
        File file = store(screenshot, SCREENSHOT_FILE_NAME);
        Utility.shareImage(this, file);
    }


}
