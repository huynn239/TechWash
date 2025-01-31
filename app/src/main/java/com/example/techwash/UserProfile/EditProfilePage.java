package com.example.techwash.UserProfile;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.techwash.Model.User;
import com.example.techwash.R;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfilePage extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private static final int STORAGE_REQUEST = 200;
    private static final int IMAGEPICK_GALLERY_REQUEST = 0;
    private static final int IMAGE_PICKCAMERA_REQUEST = 1;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String storagepath = "Users_Profile_Cover_image/";
    CircleImageView circleImageView;
    Button btn_changeImage, btn_Back;
    ProgressDialog pd;
    String[] cameraPermission;
    String[] storagePermission;
    Uri imageuri;
    String profileOrCoverPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_page);
        initUI();
        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = firebaseDatabase.getReference("User");
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getImage() == null) {
                        Drawable drawable = getResources().getDrawable(R.drawable.avatar);
                        circleImageView.setImageDrawable(drawable);
                    } else {
                        Picasso.get().load(user.getImage()).into(circleImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Updating Profile Picture");
                profileOrCoverPhoto = "image";
                showImagePicDialog();
            }
        });

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getImage() == null) {
                        Drawable drawable = getResources().getDrawable(R.drawable.avatar);
                        circleImageView.setImageDrawable(drawable);
                    } else {
                        Picasso.get().load(user.getImage()).into(circleImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getImage() == null) {
                        Drawable drawable = getResources().getDrawable(R.drawable.avatar);
                        circleImageView.setImageDrawable(drawable);
                    } else {
                        Picasso.get().load(user.getImage()).into(circleImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfilePage.this, "Failed to load profile image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // checking storage permission ,if given then we can add something in our storage
    private boolean checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
            return result && result1;
        }
    }

    // requesting for storage permission
    private void requestStoragePermission() {
        //requestPermissions(storagePermission, STORAGE_REQUEST);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                startActivity(intent);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            }
        } else {
            //below android 11
            requestPermissions(storagePermission, STORAGE_REQUEST);
        }
    }

    // checking camera permission ,if given then we can click image using our camera
    private Boolean checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
            boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
            return result && result1;
        }
    }

    // requesting for camera permission if not given
    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                startActivity(intent);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            }
        } else {
            //below android 11
            requestPermissions(cameraPermission, CAMERA_REQUEST);
        }
    }

    // Here we are showing image pic dialog where we will select
    // and image either from camera or gallery
    private void showImagePicDialog() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // if access is not given then we will request for permission
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                } else if (which == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGEPICK_GALLERY_REQUEST && data != null) {
                imageuri = data.getData();  // Lấy ảnh từ thư viện
                // Hiển thị ảnh trực tiếp
                Picasso.get().load(imageuri).into(circleImageView);
                // Tải ảnh lên Firebase Storage
                uploadProfileCoverPhoto(imageuri);
            }
            if (requestCode == IMAGE_PICKCAMERA_REQUEST) {
                // Hiển thị ảnh trực tiếp từ camera
                Picasso.get().load(imageuri).into(circleImageView);
                // Tải ảnh lên Firebase Storage
                uploadProfileCoverPhoto(imageuri);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accepted && writeStorageaccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean writeStorageaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageaccepted && readStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }
    }

    // Here we will click a photo and then go to startactivityforresult for updating data
    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp Image");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        imageuri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);  // Đặt ảnh đã chụp vào Uri tạm thời
        startActivityForResult(cameraIntent, IMAGE_PICKCAMERA_REQUEST);  // Mã yêu cầu camera
    }


    // We will select an image from gallery
    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGEPICK_GALLERY_REQUEST);
    }

    // We will upload the image from here.
    private void uploadProfileCoverPhoto(final Uri uri) {
        Log.d("FirebaseUID", "User ID: " + firebaseUser.getUid());
        pd.show();

        // Đường dẫn ảnh trên Firebase Storage
        String filepathname = "avatars/" + firebaseUser.getUid() + ".png";  // Lưu avatar theo UID của người dùng
        StorageReference storageReference1 = storageReference.child(filepathname);

        storageReference1.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());

                // Nhận URL tải xuống của ảnh
                final Uri downloadUri = uriTask.getResult();
                if (uriTask.isSuccessful()) {
                    // Cập nhật URL ảnh vào Firestore
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put(profileOrCoverPhoto, downloadUri.toString());

                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    firestore.collection("User").document(firebaseUser.getUid())
                            .update(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                    Toast.makeText(EditProfilePage.this, "Profile image updated successfully", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(EditProfilePage.this, "Failed to update image in Firestore: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                } else {
                    pd.dismiss();
                    Toast.makeText(EditProfilePage.this, "Failed to get download URL", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                // Hiển thị thông báo lỗi chi tiết
                Toast.makeText(EditProfilePage.this, "Error uploading image: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }




    private void initUI() {
        circleImageView = findViewById(R.id.profile_image_edit);
        btn_changeImage = findViewById(R.id.btn_changeImage);
        btn_Back = findViewById(R.id.btn_Back);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}