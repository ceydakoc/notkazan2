package com.example.asus.turkcell;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class Profile extends Fragment {

    private RecyclerView postList;
    private DatabaseReference databaseUser;
    private FirebaseUser currentUser;
    private FirebaseAuth auth;
    private DatabaseReference ref;
    private ImageView power;



    //private EditText ppname;
    private  static  final String TAG="Profile";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.profile,container,false);

        power = (ImageView) view.findViewById(R.id.power);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Çıkış Yap");
                builder.setMessage("NOT | KAZAN'dan çıkış yap?");

                builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        startActivity(i);

                        dialog.dismiss();
                    }

                });

                builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });


        databaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid()).child("UserImages");


        ref=FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid()).child("name");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView ppname = (TextView) view.findViewById(R.id.name);
                ppname.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        postList = (RecyclerView) view.findViewById(R.id.post_list2);
        postList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(mLayoutManager);



        return  view;


    }


    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Post, Profile.PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, Profile.PostViewHolder>(
                Post.class,
                R.layout.timeline_row,
                Profile.PostViewHolder.class,
                databaseUser
        ) {
            @Override
            protected void populateViewHolder(Profile.PostViewHolder viewHolder, Post model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setUserName(model.getUsername());
                viewHolder.setImage(getActivity().getApplicationContext(), model.getImage());

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

        public void setName(String name){

            TextView post_name = (TextView) view.findViewById(R.id.post_name);
            post_name.setText(name);

        }

        public void setUserName(String name)
        {
            TextView usernameText = (TextView) view.findViewById(R.id.user_name);
            usernameText.setText(name);
        }

        public void setDesc(String desc){
            TextView post_desc = (TextView) view.findViewById(R.id.post_description);
            post_desc.setText(desc);
        }

        public void setImage(Context ctx, String image){
            ImageView post_image = (ImageView) view.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);

        }


    }

}