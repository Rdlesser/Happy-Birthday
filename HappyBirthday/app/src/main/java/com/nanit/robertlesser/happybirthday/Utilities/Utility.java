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
import java.util.Random;

/**
 * Created by robertlesser on 20/08/2017.
 */
public class Utility {

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 878;

    /**
     * Check if the permissions needed have already been granted to the app.
     * If not - ask for the permissions that are missing
     * @param context The context of the app (MainActivity)
     * @param permissions A string array of the permissions needed
     * @return True or false depending on whether or not the app has the needed permissions
     */
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

        if (!haveAllPermissions) {
            String[] askingPermissions = missingPermissions.toArray(permissions);
            MagicalPermissions newPermissions = new MagicalPermissions(mainActivity, askingPermissions);
            mainActivity.setMagicalPermissions(newPermissions);

            //Now ask for the missing permissions
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                }
            };
            for (String permission : askingPermissions) {
                newPermissions.askPermissions(runnable, permission);
            }
        }

        return haveAllPermissions;
    }

    /**
     * Hide the soft keyboard from screen
     * @param activity The activity from which to hide the keyboard from
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * Save a string value in SharedPreferences
     * @param context The context of the SharedPreferences instance
     * @param fieldName The field to be used in the SharedPreferences to save the value in
     * @param fieldValue The value of the field used to save
     */
    public static void saveStringInPrefs(final Context context, String fieldName, String fieldValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(fieldName, fieldValue);
        editor.commit();
    }

    /**
     * Produce a random int in range [min, max]
     * @param min The minimum value for the random range (inclusive)
     * @param max The maximum value of the random range (inclusive)
     * @return A random int in the range
     */
    public static int randomIntInRange(int min, int max) {
        Random random = new Random();
        int randomInt = random.nextInt(max - min + 1) + min;
        return randomInt;
    }
}