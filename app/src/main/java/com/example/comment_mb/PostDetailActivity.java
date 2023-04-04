package com.example.comment_mb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comment_mb.Utills.comment;
import com.example.comment_mb.Utills.posts;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Comment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class PostDetailActivity extends AppCompatActivity {
    ImageView postImage,likeImage,commentsImage, commentSend;
    TextView userEmailcm,timeAgo,postDesc,likeCounter,commentCounter;

    EditText inputComments;
    ImageButton sendBtn;

    ProgressDialog pd;

    String postId, uEmail, pLikes, pDesc, pImg;




    RecyclerView recyclerView;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    DatabaseReference mUserRef,postRef,likeRef, commentRef;
    List<comment> commentList;
    CommentViewHolder adapterComments;

    StorageReference postImageRef;

    ProgressDialog mLoadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Post Detail");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // get id of post with intnet
        Intent intent = getIntent();
        postId = intent.getStringExtra("postID");

        //init view

        postImage = findViewById(R.id.viewImagePostComment);

        commentSend = findViewById(R.id.sendCommentBtn);

        userEmailcm = findViewById(R.id.profileUsernamePost);
        timeAgo = findViewById(R.id.timeAgo);
        postDesc = findViewById(R.id.postDesc);

        recyclerView = findViewById(R.id.recyclerievew);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        inputComments = findViewById(R.id.inputComments);
        sendBtn = findViewById(R.id.sendCommentBtn);


        mAuth = FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");



        postRef = FirebaseDatabase.getInstance().getReference().child("Post");
        postImageRef = FirebaseStorage.getInstance().getReference().child("PostImage");
        likeRef =  FirebaseDatabase.getInstance().getReference().child("Likes");
        commentRef = FirebaseDatabase.getInstance().getReference("Post").child(postId).child("Comments");
        mLoadingBar = new ProgressDialog(this);
        recyclerView= findViewById(R.id.recyclerievew);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        loadPostInfo();
        
        //send cmnt click
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComments();
            }
        });
        
        loadComments();


    }

    private void loadComments() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);

        commentList = new ArrayList<>();

        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    comment modelComment =ds.getValue(comment.class);

                    commentList.add(modelComment);

                    adapterComments = new CommentViewHolder(getApplicationContext(),commentList);

                    recyclerView.setAdapter(adapterComments);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void loadPostInfo() {
        Query query = postRef.orderByChild("postID").equalTo(postId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    String uEmail = ""+ds.child("userEmail").getValue();
                    String pDesc = ""+ds.child("postDesc").getValue();
                    String pImage = ""+ds.child("postImageUrl").getValue();
                    String pID = ""+ds.child("postID").getValue();



                    //set data
                    userEmailcm.setText(uEmail);
                    postDesc.setText(pDesc);
                    Picasso.get().load(pImage).into(postImage);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void addComments() {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        pd = new ProgressDialog(this);
        pd.setMessage("Adding comment .....");
        String userEmail = mUser.getEmail();

        //get data from comment edit text
        String Comments = inputComments.getText().toString().trim();
        //validate
        if(TextUtils.isEmpty(Comments)){
            //no value is entred
            Toast.makeText(this, "Comment is empty....",Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Post").child(postId).child("Comments");

        HashMap<String,Object> hashMap= new HashMap<>();
        hashMap.put("comment", Comments);
        hashMap.put("userEmail",userEmail);
        hashMap.put("postID", postId);
        hashMap.put("cId", timeStamp);


        //put this data in db
        ref.child(timeStamp).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        pd.dismiss();
                        Toast.makeText(PostDetailActivity.this, "Comment Added....", Toast.LENGTH_SHORT).show();
                        inputComments.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(PostDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }






    boolean mProcessComment = false;
    private  void updateCommentCount()
    {
        mProcessComment = true;
        DatabaseReference ref=  FirebaseDatabase.getInstance().getReference("Comments").child(postId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mProcessComment){
                    String comments = ""+ snapshot.child("pComments").getValue();
                    int newCommentVal = Integer.parseInt(comments)+1;
                    ref.child("pComments").setValue(""+newCommentVal);
                    mProcessComment = false;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


   

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}