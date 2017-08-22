package com.nanit.robertlesser.happybirthday.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.MagicalPermissions;
import com.nanit.robertlesser.happybirthday.Interfaces.HappyBirthdayFragment;
import com.nanit.robertlesser.happybirthday.R;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int SELECT_FILE = 1;
    private static final int REQUEST_CAMERA = 0;
    private int RESIZE_PHOTO_PIXELS_PERCENTAGE = 80;

    private String[] permissions;
    private MagicalPermissions magicalPermissions;
    private MagicalCamera magicalCamera;

    String userChosenTask = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if(userChosenTask.equals(getString(R.string.gallery_select)))
//                        startGalleryIntent();
//                    else if(userChosenTask.equals(getString(R.string.take_photo)))
//                        startCameraIntent();
//                } else {
//                    //code for deny
//                }
//                break;
//        }
        Map<String, Boolean> map = magicalPermissions.permissionResult(requestCode, permissions, grantResults);
        for (String permission : map.keySet()) {
            Log.d("PERMISSIONS", permission + " was: " + map.get(permission));

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            HappyBirthdayFragment fragment = (HappyBirthdayFragment) getSupportFragmentManager().
                    findFragmentById(R.id.details_screen_fragment);
            magicalCamera.resultPhoto(requestCode, resultCode, data);
            Bitmap photo = magicalCamera.getPhoto();
            fragment.onSelectFromGalleryResult(photo);
        }

    }

    public void startCameraIntent() {
        if (magicalCamera == null) {
            magicalCamera = new MagicalCamera(this, RESIZE_PHOTO_PIXELS_PERCENTAGE, magicalPermissions);
        }
        magicalCamera.takePhoto();
    }

    public void startGalleryIntent() {
        if (magicalCamera == null) {
            magicalCamera = new MagicalCamera(this, RESIZE_PHOTO_PIXELS_PERCENTAGE, magicalPermissions);
        }
        magicalCamera.selectedPicture(getString(R.string.gallery_select));
    }

    public String getUserChosenTask() {
        return userChosenTask;
    }

    public String[] getPermissions() {
        return permissions;
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

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }
}
