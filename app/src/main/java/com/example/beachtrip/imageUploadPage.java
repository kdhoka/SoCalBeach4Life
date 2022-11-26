package com.example.beachtrip;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class imageUploadPage extends AppCompatActivity {

    // One Button
    Button BSelectImage;

    // One Preview Image
    ImageView IVPreviewImage;
    String beachID;

    // constant to compare
    // the activity result code
    int SELECT_PICTURE = Activity.RESULT_OK;
    private FirebaseDatabase root = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        // register the UI widgets with their appropriate IDs
        BSelectImage = findViewById(R.id.BSelectImage);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        beachID = getIntent().getStringExtra("beachID");

        // handle the Choose Image button to trigger
        // the image chooser function
        BSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
    }

    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
//        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);

        launchImageChooserActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchImageChooserActivity
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                // compare the resultCode with the
                // SELECT_PICTURE constant
                if (result.getResultCode()
                        == SELECT_PICTURE) {
                    //get data from intent result, which is an intent itself
                    Intent data = result.getData();
                    if (data != null
                            && data.getData() != null) {
                        Uri selectedImageUri = data.getData();

                        // update the preview image in the layout
                        String imageLink = selectedImageUri.toString();
                        goBack(imageLink);
                    }
                }
            });

    // this function is triggered when user
    // selects the image from the imageChooser
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK) {
//
//            // compare the resultCode with the
//            // SELECT_PICTURE constant
//            if (requestCode == SELECT_PICTURE) {
//                // Get the url of the image from data
//                Uri selectedImageUri = data.getData();
//                if (null != selectedImageUri) {
//                    // update the preview image in the layout
//                    String imagelink = selectedImageUri.toString();
//                    goBack(imagelink);
//                }
//            }
//        }
//    }

    public void goBack(String img){
        Intent intent = new Intent(this, UserReviewPage.class);
        intent.putExtra("image", img);
        intent.putExtra("beachID", beachID);
        startActivity(intent);
    }
}