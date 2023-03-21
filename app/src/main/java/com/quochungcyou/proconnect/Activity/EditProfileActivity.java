package com.quochungcyou.proconnect.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.app.progresviews.ProgressWheel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.quochungcyou.proconnect.R;

import java.util.Objects;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class EditProfileActivity extends AppCompatActivity {

    MaterialButton updateFunction;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    ProgressWheel progressWheel;
    ImageView profileImage;
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
        profileImage = findViewById(R.id.profile_image);
        progressWheel = findViewById(R.id.wheelprogress);


        gobackArrow = findViewById(R.id.gobackArrow);


        gobackArrow.setOnClickListener(v -> onBackPressed());

        profileImage.setOnClickListener(v -> selectImage());

        updateFunction.setOnClickListener(view -> {
            databaseReference = FirebaseDatabase.getInstance().getReference("users" + "/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            databaseReference.child("name").setValue(Objects.requireNonNull(editName.getText()).toString());
            databaseReference.child("studentID").setValue(Objects.requireNonNull(editStudentID.getText()).toString());
            databaseReference.child("class").setValue(Objects.requireNonNull(editClass.getText()).toString());
            databaseReference.child("username").setValue(Objects.requireNonNull(editUsername.getText()).toString());
            databaseReference.child("dateofbirth").setValue(Objects.requireNonNull(editDateofbirth.getText()).toString());
            databaseReference.child("phone").setValue(Objects.requireNonNull(editPhone.getText()).toString());
            databaseReference.child("location").setValue(Objects.requireNonNull(editLocation.getText()).toString());

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
                String avatar = snapshot.child("avatar").getValue(String.class);

                editName.setText(name);
                editStudentID.setText(studentID);
                editClass.setText(classname);
                editUsername.setText(username);
                editDateofbirth.setText(dateofbirth);
                editPhone.setText(phone);
                editLocation.setText(location);

                if (avatar != null) {
                    Glide.with(EditProfileActivity.this)
                            .load(avatar)
                            .placeholder(R.drawable.loadinganim).fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH)
                            .into(profileImage);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        launcher.launch(intent);
    }


    //done pick image, upload to server
    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri photoUri = result.getData().getData();
                    //upload photo to firebase cloud storage
                    String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                    StorageReference storage = firebaseStorage.getReference().child("users/" + uid + "/avatar/" + uid);
                    progressWheel.setPercentage(0);
                    progressWheel.setVisibility(View.VISIBLE);

                    storage.putFile(photoUri).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            progressWheel.setPercentage(99);
                            storage.getDownloadUrl().addOnSuccessListener(uri -> {

                                String url = uri.toString();
                                databaseReference = FirebaseDatabase.getInstance().getReference("users" + "/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                                databaseReference.child("avatar").setValue(url);

                                MotionToast.Companion.createToast(this, "Success 😊",
                                        "Upload successfully",
                                        MotionToastStyle.SUCCESS,
                                        MotionToast.GRAVITY_TOP,
                                        MotionToast.LONG_DURATION,
                                        ResourcesCompat.getFont(this,R.font.opensanlight));
                                Glide.with(EditProfileActivity.this)
                                        .load(url)
                                        .placeholder(R.drawable.loadinganim).fitCenter()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .priority(Priority.HIGH)
                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                progressWheel.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                progressWheel.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(profileImage);

                            });
                        } else {
                            MotionToast.Companion.createToast(this,
                                    "Error 😥",
                                    "Upload failed",
                                    MotionToastStyle.ERROR,
                                    MotionToast.GRAVITY_TOP,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(this,R.font.opensanlight));
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            Log.d("progrss", "onProgress: " + progress);
                            progressWheel.setPercentage((int) progress);
                        }
                    });
                }
            }
    );



}