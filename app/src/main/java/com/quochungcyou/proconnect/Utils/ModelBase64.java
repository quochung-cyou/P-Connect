package com.quochungcyou.proconnect.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class ModelBase64 {
    public static String base64Image;

    public static Bitmap decodeImage(String data) {
        byte[] b = Base64.decode(data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }


}


