package com.example.asus.turkcell;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Storage extends AppCompatActivity {

    private Button selectImage;
    private ImageView image;
    private EditText imageName;
    private EditText imageDescription;
    private Button submitBtn;
    private static final int GALLERY_REQUEST = 1;
    private Uri imageUri = null;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener authListener;

    private StorageReference storage;
    private ProgressDialog progress;

    private DatabaseReference database;
    private DatabaseReference databaseUser;
    private DatabaseReference databaseUserImages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();


        storage = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance().getReference().child("Images");
        databaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());
        databaseUserImages = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid()).child("UserImages");

        selectImage = (Button) findViewById(R.id.btn_select_image);
        image = (ImageView) findViewById(R.id.img_selected);
        imageName = (EditText) findViewById(R.id.txtImageName);
        imageDescription = (EditText) findViewById(R.id.txtDescription);
        submitBtn = (Button) findViewById(R.id.btn_submit);

        progress = new ProgressDialog(this);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent (Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/x");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });
    }

    private void startPosting() {

        progress.setMessage("Paylaşılıyor...");
        progress.show();

        final String name_value = imageName.getText().toString().trim();
        final String desc_value = imageDescription.getText().toString().trim();

        if(!TextUtils.isEmpty(name_value) && !TextUtils.isEmpty(desc_value) && imageUri != null) {

            StorageReference filepath = storage.child("Images").child(imageUri.getLastPathSegment());

            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    final DatabaseReference newPost = database.push();

                    databaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            databaseUserImages.child(newPost.getKey()).child("name").setValue(name_value);
                            databaseUserImages.child(newPost.getKey()).child("description").setValue(desc_value);
                            databaseUserImages.child(newPost.getKey()).child("image").setValue(downloadUrl.toString());
                            databaseUserImages.child(newPost.getKey()).child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>(){
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(Storage.this, Home.class);
                                        startActivity(intent);
                                    }
                                }
                            });


                            newPost.child("name").setValue(name_value);
                            newPost.child("description").setValue(desc_value);
                            newPost.child("image").setValue(downloadUrl.toString());
                            newPost.child("uid").setValue(currentUser.getUid());
                            newPost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>(){
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(Storage.this, Home.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                    progress.dismiss();
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            imageUri = data.getData();

            image.setImageURI(imageUri);
        }
    }
}
