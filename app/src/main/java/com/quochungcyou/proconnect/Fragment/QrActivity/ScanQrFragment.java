package com.quochungcyou.proconnect.Fragment.QrActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.quochungcyou.proconnect.Activity.OtherPeopleProfile;
import com.quochungcyou.proconnect.R;

import java.io.IOException;
import java.util.List;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class ScanQrFragment extends Fragment {

    MaterialButton scanQrButton, scanGalleryButton;
    ImageView backButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_qr, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initVar(view);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void initVar(View view) {
        scanGalleryButton = view.findViewById(R.id.scangallery);
        scanQrButton = view.findViewById(R.id.scancamera);
        backButton = view.findViewById(R.id.back_button);

        backButton.setOnClickListener(v -> getActivity().onBackPressed());
        scanGalleryButton.setOnClickListener(v -> selectImage());


        GmsBarcodeScannerOptions options = new GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                        Barcode.FORMAT_QR_CODE)
                .build();
        GmsBarcodeScanner scanner = GmsBarcodeScanning.getClient(getActivity(), options);

        scanQrButton.setOnClickListener(v -> {
            scanner
                    .startScan()
                    .addOnSuccessListener(
                            barcode -> {
                                Log.d("lmao", barcode.getRawValue());
                                String useruid = barcode.getRawValue();
                                String ownid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                if (useruid == ownid) {
                                    MotionToast.Companion.createColorToast(
                                            getActivity(),
                                            "Scan QR Code Failed",
                                            "Same as own id",
                                            MotionToastStyle.ERROR,
                                            MotionToast.GRAVITY_BOTTOM,
                                            MotionToast.SHORT_DURATION,
                                            ResourcesCompat.getFont(getActivity(), R.font.opensanlight));
                                    return;
                                }
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users" + "/" + useruid);
                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            Log.d("lmao", "user found");
                                            Intent intent = new Intent(getActivity(), OtherPeopleProfile.class);
                                            intent.putExtra("useruid", useruid);
                                            startActivity(intent);

                                        } else {
                                            Log.d("lmao", "user not found " + useruid);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.d("lmao", databaseError.getMessage());
                                    }
                                });
                            })
                    .addOnFailureListener(
                            e -> {
                                Log.d("lmao", e.getMessage());
                                MotionToast.Companion.createColorToast(
                                        getActivity(),
                                        "Scan QR Code Failed",
                                        e.getMessage(),
                                        MotionToastStyle.ERROR,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.SHORT_DURATION,
                                        ResourcesCompat.getFont(getActivity(), R.font.opensanlight));
                            });

        });
    }


    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        launcher.launch(intent);
    }
    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri photoUri = result.getData().getData();
                    InputImage image;
                    try {
                        MotionToast.Companion.createColorToast(
                                getActivity(),
                                "Scaning",
                                "",
                                MotionToastStyle.INFO,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.SHORT_DURATION,
                                ResourcesCompat.getFont(getActivity(), R.font.opensanlight));
                        image = InputImage.fromFilePath(getActivity(), photoUri);
                        BarcodeScanner scanner = BarcodeScanning.getClient();
                        Task<List<Barcode>> resultscan = scanner.process(image)
                                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                                    @Override
                                    public void onSuccess(List<Barcode> barcodes) {
                                        MotionToast.Companion.createColorToast(
                                                getActivity(),
                                                "Found " + barcodes.size() + " barcode(s)",
                                                "",
                                                MotionToastStyle.INFO,
                                                MotionToast.GRAVITY_BOTTOM,
                                                MotionToast.SHORT_DURATION,
                                                ResourcesCompat.getFont(getActivity(), R.font.opensanlight));
                                        // Task completed successfully
                                        // ...
                                        for (Barcode barcode: barcodes) {

                                            Rect bounds = barcode.getBoundingBox();
                                            Point[] corners = barcode.getCornerPoints();

                                            String rawValue = barcode.getRawValue();

                                            int valueType = barcode.getValueType();
                                            Log.d("lmao", bounds + " " + corners + " " + rawValue + " " + valueType + " " + barcode.getRawValue());
                                            // See API reference for complete list of supported types
                                                    String useruid = barcode.getRawValue();
                                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users" + "/" + useruid);
                                                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if(dataSnapshot.exists()){
                                                                Log.d("lmao", "user found");
                                                                Intent intent = new Intent(getActivity(), OtherPeopleProfile.class);
                                                                intent.putExtra("useruid", useruid);
                                                                startActivity(intent);

                                                            } else {
                                                                Log.d("lmao", "user not found " + useruid);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                            Log.d("lmao", databaseError.getMessage());
                                                        }
                                                    });
                                            return;
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                        MotionToast.Companion.createToast(getActivity(),
                                                "Failed ☹️",
                                                e.getMessage(),
                                                MotionToastStyle.ERROR,
                                                MotionToast.GRAVITY_BOTTOM,
                                                MotionToast.LONG_DURATION,
                                                ResourcesCompat.getFont(getActivity(),R.font.opensanlight));
                                    }
                                });
                    } catch (IOException e) {

                        MotionToast.Companion.createColorToast(
                                getActivity(),
                                "Image path error",
                                e.getMessage(),
                                MotionToastStyle.ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.SHORT_DURATION,
                                ResourcesCompat.getFont(getActivity(), R.font.opensanlight));
                        e.printStackTrace();
                    }




                }
            }
    );

}