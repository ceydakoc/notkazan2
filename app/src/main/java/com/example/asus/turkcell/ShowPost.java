package com.example.asus.turkcell;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ShowPost extends AppCompatActivity {

    private String key;
    private String adres;
    private String username;
    private TextView usrnameText;
    private Button gotoprof;
    private FirebaseUser currentUser;
    private FirebaseAuth auth;

    private DatabaseReference ref ;
    ZoomableImageView imageView;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);
        gotoprof= (Button)findViewById(R.id.gotoprof);
        imageView = (ZoomableImageView) findViewById(R.id.img_post);
        imageView.setImageBitmap(bitmap);

        key = getIntent().getExtras().getString("key");
        final TextView usrnameText= (TextView) findViewById(R.id.user_name);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        ref = FirebaseDatabase.getInstance().getReference().child("Images").child(key);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                username = dataSnapshot.child("username").getValue().toString();
                usrnameText.setText(username);
                adres = dataSnapshot.child("image").getValue().toString();
                final String uid = dataSnapshot.child("uid").getValue().toString();
                loadImageFromUrl(adres);

                gotoprof.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(currentUser.getUid().equals(uid)){
                            Intent i=new Intent(ShowPost.this, Home.class);
                            startActivity(i);
                        }
                        else{
                            Intent i=new Intent(ShowPost.this, FriendProfile.class);
                            i.putExtra("key", key);
                            startActivity(i);
                        }

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void loadImageFromUrl(String s) {


        Picasso.with(this).load(s)
                .error(R.mipmap.ic_launcher)
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    private  void loadUserName(String s)
    {




    }
}
