package com.example.asus.turkcell;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FriendProfile extends AppCompatActivity {

    private String key;
    private String uid;
    private String username;
    private RecyclerView postList;
    private DatabaseReference ref;
    private DatabaseReference databaseUser;
    private FirebaseAuth auth;
    ImageButton gohome;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        gohome= (ImageButton) findViewById(R.id.imageButton);

        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FriendProfile.this, Home.class);
                startActivity(i);


            }
        });

        key = getIntent().getExtras().get("key").toString();

        postList = (RecyclerView) findViewById(R.id.post_list_friend);
        postList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(FriendProfile.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(mLayoutManager);

        ref = FirebaseDatabase.getInstance().getReference().child("Images").child(key);



        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uid = dataSnapshot.child("uid").getValue().toString();
                username = dataSnapshot.child("username").getValue().toString();
                TextView ppname = (TextView) findViewById(R.id.name);
                ppname.setText(username);
                showPost();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

   public void showPost() {

       databaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("UserImages");

        FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                Post.class,
                R.layout.timeline_row,
                PostViewHolder.class,
                databaseUser
        ) {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, Post model, final int position) {
                final String key = getRef(position).getKey();

                viewHolder.setUniversity(model.getUniversity());
                viewHolder.setDepartment(model.getDepartment());
                viewHolder.setLecture(model.getLecture());
                viewHolder.setSubject(model.getSubject());
                viewHolder.setTerm(model.getTerm());
                viewHolder.setUserName(model.getUsername());
                viewHolder.setImage(getApplicationContext(), model.getImage());

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(FriendProfile.this, ShowPost.class);
                        i.putExtra("key", key);
                        startActivity(i);

                        // Toast.makeText(getActivity(), key, Toast.LENGTH_LONG).show();
                    }
                });

            }
        };

        postList.setAdapter(firebaseRecyclerAdapter);


    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        View view;

        public PostViewHolder(View itemView) {
            super(itemView);

            view = itemView;
        }

        public void setUniversity(String university){

            TextView post_university = (TextView) view.findViewById(R.id.txtCardUni);
            post_university.setText(university);

        }

        public void setDepartment(String department){
            TextView post_department = (TextView) view.findViewById(R.id.txtCardDepartment);
            post_department.setText(department);
        }

        public void setLecture(String lecture){
            TextView post_lecture = (TextView) view.findViewById(R.id.txtCardLecture);
            post_lecture.setText(lecture);
        }

        public void setSubject(String subject){
            TextView post_subject = (TextView) view.findViewById(R.id.txtCardSubject);
            post_subject.setText(subject);
        }

        public void setTerm(String term){
            TextView post_term = (TextView) view.findViewById(R.id.txtCardTerm);
            post_term.setText(term);
        }

        public void setUserName(String name)
        {
            TextView usernameText = (TextView) view.findViewById(R.id.user_name);
            usernameText.setText(name);
        }

        public void setImage(Context ctx, String image){
            ImageView post_image = (ImageView) view.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);

        }


    }
}
