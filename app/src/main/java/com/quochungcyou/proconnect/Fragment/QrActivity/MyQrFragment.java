package com.quochungcyou.proconnect.Fragment.QrActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.quochungcyou.proconnect.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class MyQrFragment extends Fragment {

    private DatabaseReference databaseReference;
    TextView username, shareimage, saveimage, useruidtext;
    ImageView qrimage;
    Bitmap globalqrcode;
    ImageView backButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_qr, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initVar(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        initVar(getView());
    }

    public void initVar(View view) {
        qrimage =  view.findViewById(R.id.qr_image);
        username = view.findViewById(R.id.username);
        shareimage = view.findViewById(R.id.share_image);
        saveimage = view.findViewById(R.id.save_image);
        useruidtext = view.findViewById(R.id.useruid);
        backButton = view.findViewById(R.id.back_button);

        backButton.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });


        useruidtext.setText("User UID " + FirebaseAuth.getInstance().getCurrentUser().getUid());

        updateData();
        generateQr();


        saveimage.setOnClickListener(v -> {
            if (globalqrcode != null) {
                askPermission();
                Log.d("mediaStorage", "saving image");
                storeImage(globalqrcode);
            } else {
                MotionToast.Companion.createColorToast(getActivity(),
                        "Error",
                        "Can't save this image",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_TOP,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(getActivity(), R.font.opensanlight));
            }
        });

        shareimage.setOnClickListener(v -> {
            if (globalqrcode != null) {
                // save bitmap to cache directory
                try {

                    File cachePath = new File(getActivity().getCacheDir(), "images");
                    cachePath.mkdirs(); // don't forget to make the directory
                    FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
                    globalqrcode.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    stream.close();

                    File imagePath = new File(getActivity().getCacheDir(), "images");
                    File newFile = new File(imagePath, "image.png");
                    Uri contentUri = FileProvider.getUriForFile(getActivity(), "com.quochungcyou.proconnect.fileprovider", newFile);

                    if (contentUri != null) {
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
                        shareIntent.setDataAndType(contentUri, getActivity().getContentResolver().getType(contentUri));
                        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                        startActivity(Intent.createChooser(shareIntent, "Choose an app"));
                    }

                } catch (IOException e) {
                    MotionToast.Companion.createColorToast(getActivity(),
                            "Error",
                            "Can't share this image",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_TOP,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(getActivity(), R.font.opensanlight));
                }
            }
        });
    }

    private void updateData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users" + "/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                if (name == null || name.isEmpty()) {
                    name = "Guess";
                    databaseReference.child("name").setValue("Guess");
                }
                username.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ProfileFragment", "onCancelled: " + error.getMessage());
            }
        });
    }


    private void askPermission() {
        Log.d("mediaStorage", "asking permission");
        String[] permissionsStorage = {Manifest.permission.READ_EXTERNAL_STORAGE};
        int requestExternalStorage = 1;
        int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), permissionsStorage, requestExternalStorage);
        }

    }


    private void generateQr() {
        String useruid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = writer.encode(useruid, BarcodeFormat.QR_CODE, 200, 200, hints);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap qrcodeencode = encoder.createBitmap(bitMatrix);
            databaseReference = FirebaseDatabase.getInstance().getReference("users" + "/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String avatar = snapshot.child("avatar").getValue(String.class);
                    if (avatar != null && !avatar.isEmpty()) {
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    Bitmap bitmap = Glide.with(getActivity())
                                            .asBitmap()
                                            .load(avatar)
                                            .submit(200, 200)
                                            .get();
                                    Bitmap finalbitmap = mergeBitmaps(bitmap, qrcodeencode);
                                    globalqrcode = finalbitmap;
                                    getActivity().runOnUiThread(() -> {
                                        qrimage.setImageBitmap(finalbitmap);
                                    });
                                    Log.d("MyQrFragment", "onDataChange: " + finalbitmap);
                                } catch (ExecutionException e) {
                                    e.printStackTrace();

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                        }).start();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("MyQrFragment", "onCancelled: " + error.getMessage());
                    qrimage.setImageBitmap(qrcodeencode);
                }


            });

        } catch (WriterException e) {
            e.printStackTrace();
        }


    }



    public Bitmap mergeBitmaps(Bitmap logo, Bitmap qrcode) {

        Bitmap combined = Bitmap.createBitmap(qrcode.getWidth(), qrcode.getHeight(), qrcode.getConfig());
        Canvas canvas = new Canvas(combined);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        canvas.drawBitmap(qrcode, new Matrix(), null);

        Bitmap resizeLogo = Bitmap.createScaledBitmap(logo, canvasWidth / 5, canvasHeight / 5, true);
        int centreX = (canvasWidth - resizeLogo.getWidth()) /2;
        int centreY = (canvasHeight - resizeLogo.getHeight()) / 2;
        canvas.drawBitmap(resizeLogo, centreX, centreY, null);
        return combined;
    }


    private void storeImage(Bitmap image) {
        Log.d("mediaStorage", "storing image");
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            MotionToast.Companion.createToast(getActivity(),
                    "Failed ☹️",
                    "No permission to access storage",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_TOP,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(getActivity(),R.font.opensanlight));

            return;
        }
        try {
            Log.d("mediaStorage", "trying save");
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            MotionToast.Companion.createColorToast(getActivity(), "Saved", "Image saved", MotionToastStyle.SUCCESS,
                    MotionToast.GRAVITY_TOP, MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(getActivity(), www.sanju.motiontoast.R.font.helvetica_regular));

            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("MyQrFragment", "File not found: " + e.getMessage());
            MotionToast.Companion.createToast(getActivity(),
                    "Failed ☹️",
                    "File not found: " + e.getMessage(),
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_TOP,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(getActivity(),R.font.opensanlight));
        } catch (IOException e) {
            MotionToast.Companion.createToast(getActivity(),
                    "Failed ☹️",
                    "Error accessing file: " + e.getMessage(),
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_TOP,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(getActivity(),R.font.opensanlight));
        }
    }

    private  File getOutputMediaFile(){
        Log.d("mediaStorage", "getting output media file");
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getActivity().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                //create a new directory
                mediaStorageDir.mkdirs();


                Log.d("mediaStorage", "no mkdir");
                //return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        Log.d("mediaStorage", "saved");
        return mediaFile;
    }

}