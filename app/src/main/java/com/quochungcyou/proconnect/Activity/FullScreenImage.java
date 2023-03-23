package com.quochungcyou.proconnect.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.quochungcyou.proconnect.R;
import com.quochungcyou.proconnect.Utils.ModelBase64;

public class FullScreenImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        ImageView imgFullImage = findViewById(R.id.fullScreenImage);

        Bundle bundle = getIntent().getExtras();
        String image = ModelBase64.base64Image;
        Bitmap bitmap = ModelBase64.decodeImage(image);
        imgFullImage.setImageBitmap(bitmap);
    }
}