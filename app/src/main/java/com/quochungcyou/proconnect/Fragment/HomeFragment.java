package com.quochungcyou.proconnect.Fragment;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mikepenz.itemanimators.SlideLeftAlphaAnimator;
import com.quochungcyou.proconnect.APIUtils.APIHelper;
import com.quochungcyou.proconnect.APIUtils.APIInterface;
import com.quochungcyou.proconnect.Adapter.PostAdapter;
import com.quochungcyou.proconnect.Model.ArticleModel;
import com.quochungcyou.proconnect.Model.ResultModel;
import com.quochungcyou.proconnect.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    List<ArticleModel> postlist;
    private DatabaseReference databaseReference;
    RoundedImageView avatarImage;
    TextView welcomeMessage;
    PostAdapter adapter;
    LottieAnimationView loadPost;
    ImageButton techcate, newcate, sportcate, financecate, foodcate;
    Call<ResultModel> call;
    APIInterface apiInterface;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initVar(view);
        updateData();
        getPostList();
    }

    @Override
    public void onResume() {

        super.onResume();
        updateData();

    }

    public void initVar(View view) {
        recyclerView = view.findViewById(R.id.recycleViewNew);
        welcomeMessage = view.findViewById(R.id.welcomeMessage);
        avatarImage = view.findViewById(R.id.thumbnailavatar);
        loadPost = view.findViewById(R.id.loadPost);
        techcate = view.findViewById(R.id.techcate);
        newcate = view.findViewById(R.id.newcate);
        sportcate = view.findViewById(R.id.sportcate);
        financecate = view.findViewById(R.id.financecate);
        foodcate = view.findViewById(R.id.foodcate);
        apiInterface  = APIHelper.getApiClient("https://api.newscatcherapi.com/", getContext()).create(APIInterface.class);



        postlist = new ArrayList<>();

        techcate.setOnClickListener(v -> {
            call = apiInterface.getLastestHeadline("3d", "VN", "tech", "10");
            callData();
        });
        newcate.setOnClickListener(v -> {
            call = apiInterface.getLastestHeadline("3d", "VN", "news", "10");
            callData();
        });
        sportcate.setOnClickListener(v -> {
            call = apiInterface.getLastestHeadline("3d", "VN", "sport", "10");
            callData();
        });
        financecate.setOnClickListener(v -> {
            call = apiInterface.getLastestHeadline("3d", "VN", "finance", "10");
            callData();
        });
        foodcate.setOnClickListener(v -> {
            call = apiInterface.getLastestHeadline("3d", "VN", "food", "10");
            callData();
        });


    }

    private void getPostList() {

        call = apiInterface.getLastestHeadline("7d", "VN", "tech", "15");
        Log.d("HomeFragment", "Call to get post list");
        callData();
    }

    private void callData() {
        call.enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

                if (response.isSuccessful() && Objects.requireNonNull(response.body()).getArticle() != null) {
                    postlist = response.body().getArticle();
                    if (!postlist.isEmpty()) {
                        loadPost.setVisibility(View.GONE);
                        Log.d("HomeFragment", "done call " + postlist.size());

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        adapter = new PostAdapter(getActivity() , postlist);
                        recyclerView.setItemAnimator(new SlideLeftAlphaAnimator());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Log.d("HomeFragment", "Empty postlist");
                        MotionToast.Companion.createToast(getActivity(),
                                "Failed ☹️",
                                "No post available",
                                MotionToastStyle.ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(getActivity(),R.font.opensanlight));
                    }

                } else {
                    Log.d("HomeFragment", "null reponse " + response.isSuccessful() + " " + response.toString());
                    MotionToast.Companion.createToast(getActivity(),
                            "Failed ☹️",
                            "No post available",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(getActivity(),R.font.opensanlight));
                }
            }

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {

                Log.d("EROR", t.getMessage());
                MotionToast.Companion.createToast(getActivity(),
                        "Failed ☹️",
                        "No post available",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(getActivity(),R.font.opensanlight));
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
                String text = "<font color=#7A7A7A>Welcome Back, </font> <font color=#B12341><u>" + name + "</u></font> " + "\uD83D\uDC4B";
                welcomeMessage.setText(Html.fromHtml(text));


                String avatar = snapshot.child("avatar").getValue(String.class);
                if (avatar != null && !avatar.isEmpty()) {
                    Glide.with(getActivity())
                            .load(avatar)
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH)
                            .placeholder(R.drawable.loadinganim).into(avatarImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ProfileFragment", "onCancelled: " + error.getMessage());
            }
        });
    }
}