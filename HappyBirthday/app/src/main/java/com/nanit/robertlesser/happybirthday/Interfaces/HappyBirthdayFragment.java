package com.nanit.robertlesser.happybirthday.Interfaces;

import android.content.Intent;
import android.graphics.Bitmap;

/**
 * Created by robertlesser on 20/08/2017.
 */

public interface HappyBirthdayFragment {

    void onSelectFromGalleryResult(Bitmap bitmap);

    void onCaptureImageResult(Bitmap bitmap);
}
