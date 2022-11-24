package com.example.beachtrip;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class UserReviewPage extends AppCompatActivity {
    FirebaseDatabase root;
    DatabaseReference reviewRef;
    ValueEventListener reviewCredentialListener;

    FirebaseAuth mAuth;
    FirebaseUser user;
    String beachID;
    String userID;
    Boolean isAnon = false;
    Review review = null;

    private Button uploadBtn, showallbtn;
    private ImageView imageView;
    private ProgressBar progressBar;

    private DatabaseReference imageDBRef = FirebaseDatabase.getInstance().getReference("Image");
    private com.google.firebase.storage.StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private Uri imageUri;
    private Model model;

    private static final String TAG = "Create Review.";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_review);

        //initialize constants and references to DB and storage
        beachID = this.getIntent().getStringExtra("beachID");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userID = user.getUid();

        root = FirebaseDatabase.getInstance();
        DatabaseReference beachRef = root.getReference("beaches");
        ValueEventListener beachCredentialListener = new ValueEventListener() {
            private static final String TAG = "Beach read.";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get value of each attribute of a User ob
                int size = 0;
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String id = dsp.getKey().toString();
                    if(id.equals(beachID)){
                        String beachName = dsp.child("name").getValue().toString();
                        TextView beachName_tv = findViewById(R.id.beach_name_val);
                        beachName_tv.setText(beachName);
                        getReviewFromDB();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        beachRef.addValueEventListener(beachCredentialListener);

        //initialization of data members for image uploading
        uploadBtn = findViewById(R.id.upload_btn);
        showallbtn = findViewById(R.id.show_all_btn);
        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.review_image_upload_view);
        progressBar.setVisibility(View.INVISIBLE);
        model = null;

        View.OnClickListener imageOnClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                diyActivityResultLauncher.launch(galleryIntent);
            }
        };

        imageView.setOnClickListener(imageOnClickListener);


        View.OnClickListener uploadOnClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (imageUri != null){
                    uploadToFirebase(imageUri);
                } else {
                    Toast.makeText(UserReviewPage.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        };
        uploadBtn.setOnClickListener(uploadOnClickListener);

//        testPicasso();
//        testPicassoWithProgressBar();
    }

    private void testPicassoWithProgressBar() {
        ImageView image_v = findViewById(R.id.review_image_upload_view);
        String link = "https://firebasestorage.googleapis.com/v0/b/cs310-beach4life.appspot.com/o/1668464376752.jpg?alt=media&token=253e3d79-2318-41b7-b62f-a22e5581d563";

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        // Hide progress bar on successful load
        Picasso.get().load(link)
                .into(image_v, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        System.out.println("Picasso load image failed");
                    }
                });
    }

    private void testPicasso() {
        ImageView image_v = findViewById(R.id.review_image_upload_view);
        String link = "https://firebasestorage.googleapis.com/v0/b/cs310-beach4life.appspot.com/o/1668464376752.jpg?alt=media&token=253e3d79-2318-41b7-b62f-a22e5581d563";
        Picasso.get().load(link).into(image_v);
    }

    private void uploadToFirebase(Uri uri) {
        StorageReference fileRef =  storageReference.child(System.currentTimeMillis()+"."+getFileExtension(uri));

        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                //store imageUri to realtime
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        model = new Model(uri.toString(), fileRef.getPath());//model always contains the lastly upload image URL in fire storage
                        Toast.makeText(UserReviewPage.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(UserReviewPage.this, "Image uploading failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        //return the
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> diyActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        imageUri = data.getData();
                        imageView.setImageURI(imageUri);
                    }
                }
            });

    private void getReviewFromDB() {
        root = FirebaseDatabase.getInstance();
        reviewRef = root.getReference("Reviews"); //pointer to the Review tree

        reviewCredentialListener = new ValueEventListener() {
            private static final String TAG = "Review read.";
            //attempt to find a review whose ID is "current_beach current_user"
            String target_review_id = beachID + " " + userID;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String beachKey = dsp.child("beach").getValue().toString();
                    String userKey = dsp.child("uID").getValue().toString();
                    //if found a review for selected beach by current user, display it.
                    if (dsp.getKey().equals(target_review_id)) {
                        String reviewID = dsp.getKey();//used to identify if this is a new review
                        String content = dsp.child("content").getValue().toString();
                        Double rate = Double.parseDouble(dsp.child("rate").getValue().toString());
                        Boolean isAnonymous = (Boolean)dsp.child("isAnonymous").getValue();
                        String imageURL = dsp.child("image").getValue().toString();
                        String imagePath = dsp.child("imagePath").getValue().toString();

                        //TODO: update Review class constructor to support image
                        review = new Review(reviewID, userKey, beachKey, isAnonymous, rate, content, imageURL, imagePath);
                        break;
                    }
                }
                onFinishLoading();
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error){
                DatabaseError databaseError = null;
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }

        };

        reviewRef.addValueEventListener(reviewCredentialListener);
    }

    private void onFinishLoading() {
        reviewRef.removeEventListener(reviewCredentialListener);

        String rating = "";
        String isAnonStr = "false";
        isAnon = false;
        String content = "";

        TextView rating_view = findViewById(R.id.rating);
        TextView isAnon_btn = findViewById(R.id.anon_btn);
        TextView content_view = findViewById(R.id.content_tv);
        TextView delete_btn = findViewById(R.id.delete_btn);
        if (review != null){
            rating = String.valueOf(review.getRating());
            isAnon = review.getIs_anonymous();
            if (isAnon){
                isAnonStr = "true";
            }
            content = review.getContent();

            //load image from old review if exists
            String link = review.getImageURL();
            if (!link.equals("nullURL")){
                ImageView image_v = findViewById(R.id.review_image_upload_view);
                ProgressBar progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
                // Hide progress bar on successful load
                Picasso.get().load(link)
                        .into(image_v, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                if (progressBar != null) {
                                    progressBar.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                System.out.println("Picasso load image failed");
                            }
                        });
            }

            //hide delete button
            delete_btn.setVisibility(View.VISIBLE);
        } else {
            delete_btn.setVisibility(View.INVISIBLE);
        }

        if (!rating.isEmpty()){//only load rating is there is one, else display hint
            rating_view.setText(rating);
        }
        isAnon_btn.setText(isAnonStr);
        if(!content.isEmpty()){//only load content is there is one, else display hint
            content_view.setText(content);
        }
    }

    public void onClickBackFromMyReview(View view) {
        //TODO: clear all text fields
        Intent intent = new Intent(this, BeachInfoActivity.class);
        intent.putExtra("id", beachID);
        startActivity(intent);
    }

    public void onClickConfirm(View view) {
        root = FirebaseDatabase.getInstance();
        reviewRef = root.getReference("Reviews");

        TextView rating_view = findViewById(R.id.rating);
        TextView isAnon_btn = findViewById(R.id.anon_btn);
        TextView content_view = findViewById(R.id.content_tv);

        String rating = rating_view.getText().toString();
        String content = content_view.getText().toString();
        Double rate_double;

        try {
            rate_double = Double.parseDouble(rating);

            if (!(0 <= rate_double) && (rate_double <= 5)) {
                rating_view.setError("Rating must be a double in range 0-5");
                return;
            }
        } catch (NullPointerException e){
            //catch empty rating string input error
            rating_view.setError("Rating cannot be empty!");
            throw e;
        } catch (NumberFormatException e){
            rating_view.setError("Rating must be a number!");
            throw e;
        }

        if (review == null){//create new review. Update review object, update child count
            reviewRef = root.getReference("Reviews");

            String reviewID = beachID + " " + userID;
            String imageURL = "nullURL";
            String imagePath = "nullPath";
            if (model != null){
                imageURL = model.getImageUri();
                imagePath = model.getImagePath();
            }

            review = new Review(reviewID, userID, beachID, isAnon, rate_double, content, imageURL, imagePath);
            System.out.println("onClickConfirm, new review obj contains imageURL: " + review.getImageURL());

            reviewRef.child(reviewID).push();
            DatabaseReference newReview = reviewRef.child(reviewID);

            newReview.child("beach").push();
            newReview.child("beach").setValue(review.getBeach_name());

            newReview.child("uID").push();
            newReview.child("uID").setValue(review.getUser_ID());

            newReview.child("rate").push();
            newReview.child("rate").setValue(review.getRating());

            newReview.child("content").push();
            newReview.child("content").setValue(review.getContent());

            newReview.child("isAnonymous").push();
            newReview.child("isAnonymous").setValue(review.isAnonymous);

            newReview.child("image").push();
            newReview.child("image").setValue(review.getImageURL());

            newReview.child("imagePath").push();
            newReview.child("imagePath").setValue(review.getImagePath());

            Log.d(TAG, "onClick database listener finished.");
            Toast.makeText(UserReviewPage.this, "Review created!", Toast.LENGTH_SHORT).show();

            //TODO: update review obj, and deletion button visibility to allow immediate deletion after creation
            TextView delete_btn = findViewById(R.id.delete_btn);
            delete_btn.setVisibility(View.VISIBLE);
        } else {
            //else update existing review's data in DB.
            String reviewID = review.getReviewID();
            DatabaseReference currReview = reviewRef.child(review.getReviewID());
            currReview.child("rate").setValue(rating);
            currReview.child("content").setValue(content);
            currReview.child("isAnonymous").setValue(isAnon);

            if (model != null){//update imageURL in DB if new image is uploaded.\
                System.out.println("onClickConfirm, model is not null, update curr_review image to:  " + model.getImageUri());
                currReview.child("image").setValue(model.getImageUri());
                currReview.child("imagePath").setValue(model.getImagePath());
            } else {
                System.out.println("onClickConfirm, model is null, keep curr_review image and imagePath as it is: " + review.getImageURL());
            }

            Toast.makeText(UserReviewPage.this, "Review updated!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickAnon(View view) {
        TextView isAnonymous_tv = findViewById(R.id.anon_btn);
        String isAnonStr = isAnonymous_tv.getText().toString();
        if (isAnonStr.equals("true")){
            //change button text and state to false;
            isAnonymous_tv.setText("false");
            isAnon = false;
        } else {
            isAnonymous_tv.setText("true");
            isAnon = true;
        }
    }

    public void onClickDelete(View view) {
        reviewRef = root.getReference("Reviews");
        Query query = reviewRef.child(review.getReviewID());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
                review = null;
                Toast.makeText(UserReviewPage.this, "Deleted!", Toast.LENGTH_SHORT).show();
                TextView delete_btn = findViewById(R.id.delete_btn);
                delete_btn.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("delete failed");
            }
        });

        String imagePath = review.getImagePath();
        if (!imagePath.equals("nullPath")){
            // Create a reference to the file to delete
            StorageReference desertRef = storageReference.child(imagePath);

            // Delete the file
            desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                   Toast.makeText(UserReviewPage.this, "image deletion in storage sucesses", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(UserReviewPage.this, "image deletion in storage failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void onClickImageUpload(View view){
        Intent intent = new Intent(this, imageUploadPage.class);
        intent.putExtra("beachID", beachID);
        startActivity(intent);
    }
}