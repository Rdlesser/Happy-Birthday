package com.nanit.robertlesser.happybirthday.Activities;

import android.app.Activity;
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
import android.util.Log;

import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.MagicalPermissions;
import com.nanit.robertlesser.happybirthday.Fragments.BirthdayScreenFragment;
import com.nanit.robertlesser.happybirthday.Fragments.DetailsScreenFragment;
import com.nanit.robertlesser.happybirthday.Interfaces.HappyBirthdayFragment;
import com.nanit.robertlesser.happybirthday.R;
import com.nanit.robertlesser.happybirthday.Utilities.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private int RESIZE_PHOTO_PIXELS_PERCENTAGE = 80;

    ArrayList<String> activePermissions;
    private MagicalPermissions magicalPermissions;
    private MagicalCamera magicalCamera;

    String userChosenTask = "";
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activePermissions = new ArrayList<>();

        dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        DetailsScreenFragment fragment = new DetailsScreenFragment();
        fragmentTransaction.add(R.id.fragment_container, fragment, DetailsScreenFragment.TAG);
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
                    findFragmentByTag(DetailsScreenFragment.TAG);
            magicalCamera.resultPhoto(requestCode, resultCode, data);
            Bitmap photo = magicalCamera.getPhoto();
            if (photo.getWidth() > photo.getHeight()){
                magicalCamera.resultPhoto(requestCode, resultCode, data,
                        MagicalCamera.ORIENTATION_ROTATE_90);
                photo = magicalCamera.getPhoto();
            }
            String path = magicalCamera.savePhotoInMemoryDevice(photo, "HappyBirthdayPhoto",
                    Bitmap.CompressFormat.JPEG, false);
            Utility.saveStringInPrefs(this, DetailsScreenFragment.BIRTHDAY_PIC_PATH, path);
            fragment.setImage(photo);
        }

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
        fragmentTransaction.replace(R.id.fragment_container, birthdayFragment, BirthdayScreenFragment.TAG);
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

}
