package com.quochungcyou.proconnect.Activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quochungcyou.proconnect.R;

import java.util.Objects;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class EditProfileActivity extends AppCompatActivity {

    MaterialButton updateFunction;
    private  DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    TextInputEditText editName, editStudentID, editClass, editUsername, editDateofbirth, editPhone, editLocation;
    ImageView gobackArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);

            if (getWindow().getInsetsController() != null) {
                getWindow().getInsetsController().hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                getWindow().getInsetsController().setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        initVar();
        passData();
    }

    private void initVar() {
        updateFunction = findViewById(R.id.updateProfileFunction);
        editName = findViewById(R.id.displayNameEditProfile);
        editStudentID = findViewById(R.id.studentIDEditProfile);
        editClass = findViewById(R.id.classEditProfile);
        editUsername = findViewById(R.id.userNameEditProfile);
        editDateofbirth = findViewById(R.id.dateEditProfile);
        editPhone = findViewById(R.id.phoneEditProfile);
        editLocation = findViewById(R.id.locationEditProfile);


        gobackArrow = findViewById(R.id.gobackArrow);


        gobackArrow.setOnClickListener(v -> {
            onBackPressed();
        });

        updateFunction.setOnClickListener(view -> {
            String useruid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("users" + "/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            databaseReference.child("name").setValue(editName.getText().toString());
            databaseReference.child("studentID").setValue(editStudentID.getText().toString());
            databaseReference.child("class").setValue(editClass.getText().toString());
            databaseReference.child("username").setValue(editUsername.getText().toString());
            databaseReference.child("dateofbirth").setValue(editDateofbirth.getText().toString());
            databaseReference.child("phone").setValue(editPhone.getText().toString());
            databaseReference.child("location").setValue(editLocation.getText().toString());

            MotionToast.Companion.createToast(this,
                    "Success 😊",
                    "Update successfully",
                    MotionToastStyle.SUCCESS,
                    MotionToast.GRAVITY_TOP,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this,R.font.opensanlight));
            passData();
        });
    }


    private void passData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users" + "/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String studentID = snapshot.child("studentID").getValue(String.class);
                String classname = snapshot.child("class").getValue(String.class);
                String username = snapshot.child("username").getValue(String.class);
                String dateofbirth = snapshot.child("dateofbirth").getValue(String.class);
                String phone = snapshot.child("phone").getValue(String.class);
                String location = snapshot.child("location").getValue(String.class);

                editName.setText(name);
                editStudentID.setText(studentID);
                editClass.setText(classname);
                editUsername.setText(username);
                editDateofbirth.setText(dateofbirth);
                editPhone.setText(phone);
                editLocation.setText(location);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}