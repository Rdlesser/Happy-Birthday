package com.nanit.robertlesser.happybirthday.Utilities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.inputmethod.InputMethodManager;

import com.frosquivel.magicalcamera.MagicalPermissions;
import com.nanit.robertlesser.happybirthday.Activities.MainActivity;

import java.util.ArrayList;

/**
 * Created by robertlesser on 20/08/2017.
 */
public class Utility {

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 878;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context, String[] permissions) {

        MainActivity mainActivity = (MainActivity) context;
        ArrayList<String> activePermissions = mainActivity.getActivePermissions();
        ArrayList<String> missingPermissions = new ArrayList<>();

        boolean haveAllPermissions = true;
        // Narrow down the permissions to only the missing ones
        for (String permission : permissions) {
            if (!activePermissions.contains(permission)) {
                missingPermissions.add(permission);
                haveAllPermissions = false;
            }
        }

        //Now ask for the missing permissions
        String[] askingPermissions = missingPermissions.toArray(permissions);
        MagicalPermissions newPermissions = new MagicalPermissions(mainActivity, askingPermissions);
        mainActivity.setMagicalPermissions(newPermissions);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

            }
        };
        for (String permission : askingPermissions) {
            newPermissions.askPermissions(runnable, permission);
        }

        return haveAllPermissions;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void saveStringInPrefs(final Context context, String fieldName, String fieldValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(fieldName, fieldValue);
        editor.commit();
    }
}