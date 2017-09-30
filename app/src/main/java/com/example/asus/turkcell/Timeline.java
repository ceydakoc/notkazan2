package com.example.asus.turkcell;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by ASUS on 13.09.2017.
 */

public class Timeline extends Fragment {

    private RecyclerView postList;
    private ImageView add;
    private DatabaseReference database;
    private RecyclerView gotop;
    //private FirebaseAuth auth;
    //private FirebaseAuth.AuthStateListener authStateListener;



    private  static  final String TAG="Timeline";




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timeline,container,false);
        add = (ImageView) view.findViewById(R.id.img_add);



        database = FirebaseDatabase.getInstance().getReference().child("Images");

        postList = (RecyclerView) view.findViewById(R.id.post_list);
        postList.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(mLayoutManager);




        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Storage.class);
                startActivity(intent);

            }
        });

        return  view;
    }

    @Override
    public void onStart() {
        super.onStart();

    //    auth.addAuthStateListener(authStateListener);


        FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                Post.class,
                R.layout.timeline_row,
                PostViewHolder.class,
                database
        ) {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, Post model, int position) {

                final String key = getRef(position).getKey();

                viewHolder.setUniversity(model.getUniversity());
                viewHolder.setDepartment(model.getDepartment());
                viewHolder.setLecture(model.getLecture());
                viewHolder.setSubject(model.getSubject());
                viewHolder.setTerm(model.getTerm());
                viewHolder.setImage(getActivity().getApplicationContext(), model.getImage());
                viewHolder.setUserName(model.getUsername());

               // final String postName = model.getName().toString();

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(getActivity(), ShowPost.class);
                        i.putExtra("key", key);
                        startActivity(i);

                       // Toast.makeText(getActivity(), key, Toast.LENGTH_LONG).show();
                    }
                });

            }
        };

        postList.smoothScrollToPosition(0);
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
            TextView username = (TextView) view.findViewById(R.id.user_name);
            username.setText(name);
        }

        public void setImage(Context ctx, String image){
            ImageView post_image = (ImageView) view.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);

        }


    }
}
