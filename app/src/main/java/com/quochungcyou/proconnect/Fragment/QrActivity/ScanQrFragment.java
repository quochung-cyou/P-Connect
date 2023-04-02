package com.quochungcyou.proconnect.Fragment.QrActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;
import com.quochungcyou.proconnect.Activity.OtherPeopleProfile;
import com.quochungcyou.proconnect.R;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class ScanQrFragment extends Fragment {

    MaterialButton scanQrButton, scanGalleryButton;


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
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users" + "/" + useruid);
                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            Log.d("lmao", "user found");
                                            Intent intent = new Intent(getActivity(), OtherPeopleProfile.class);
                                            intent.putExtra("useruid", useruid);
                                            //zoom animation
                                            getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
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
}