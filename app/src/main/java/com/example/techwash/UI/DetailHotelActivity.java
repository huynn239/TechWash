//package com.example.techwash.UI;
//
//import android.Manifest;
//import android.content.ActivityNotFoundException;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.drawable.Drawable;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.app.ActivityCompat;
//
//import com.example.techwash.Model.Booked;
//import com.example.techwash.Model.Auto;
//import com.example.techwash.R;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.squareup.picasso.Picasso;
//
//import java.io.IOException;
//import java.text.NumberFormat;
//import java.util.List;
//import java.util.Locale;
//
//import kr.co.prnd.readmore.ReadMoreTextView;
//
//public class DetailHotelActivity extends AppCompatActivity implements View.OnClickListener {
//
//    Auto auto;
//    Booked booked;
//    TextView txttenks, txtdiachi, txtgia, toolbartenks;
//    ReadMoreTextView txtmota;
//    ImageView img, img2, img3, img4;
//    Button btndatphong;
//    FirebaseDatabase database;
//    FirebaseAuth auth;
//    FusedLocationProviderClient fusedLocationProviderClient;
//    Boolean isInMyFavorite = false;
//    Toolbar toolbar;
//    MenuItem menuItem;
//    DatabaseReference reference;
//    List<Auto> list;
//    FirebaseUser user;
//    String[] theloai = {"Phòng đơn", "Phòng đôi"};
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_detailhotel);
//
//        //toolbar
//        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_detail);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("");
//        toolbartenks = findViewById(R.id.toolbar_tenks);
//        //end toolbar
//
//        auth = FirebaseAuth.getInstance();
//        txttenks = findViewById(R.id.txtTenks);
//
//        txtdiachi = findViewById(R.id.txtDiachiCT);
//
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        txtdiachi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getLocation();
//            }
//        });
//
//        txtgia = findViewById(R.id.txtGia);
//        txtmota = findViewById(R.id.txtMoto);
//        img = findViewById(R.id.img);
//        img2 = findViewById(R.id.img2);
//        img3 = findViewById(R.id.img3);
//        img4 = findViewById(R.id.img4);
//        btndatphong = findViewById(R.id.btndatphong);
//        btndatphong.setOnClickListener(this);
//
//        Intent intent = getIntent();
//        auto = (Auto) intent.getSerializableExtra("clickdetail");
//
//        txttenks.setText(auto.getTenks());
//        txtdiachi.setText(auto.getDiachiCT());
//
//        Locale localeVN = new Locale("vi", "VN");
//        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
//        String get_gia = currencyVN.format(Integer.parseInt(auto.getGia()));
//
//        txtgia.setText(get_gia);
//        txtmota.setText(auto.getMota());
//
//        Picasso.get().load(auto.getHinh()).fit().centerCrop().into(img);
//        Picasso.get().load(auto.getHinh2()).fit().centerCrop().into(img2);
//        Picasso.get().load(auto.getHinh3()).fit().centerCrop().into(img3);
//        Picasso.get().load(auto.getHinh4()).fit().centerCrop().into(img4);
//        showname();
//    }
//
//    private void getLocation() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        } else {
//            ActivityCompat.requestPermissions(DetailHotelActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
//        }
//        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//            @Override
//            public void onComplete(@NonNull Task<Location> task) {
//                Location location = task.getResult();
//                if (location != null) {
//                    try {
//                        Geocoder geocoder = new Geocoder(DetailHotelActivity.this, Locale.getDefault());
//                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//                        try {
//                            Uri uri;
//                            uri = Uri.parse("https://www.google.com/maps/dir/" + location.getLatitude() + ' ' + location.getLongitude() + '/' + auto.getDiachiCT());
//                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                            intent.setPackage("com.google.android.apps.maps");
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                        } catch (ActivityNotFoundException e) {
//                            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
//                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//    }
//
//    private void addFavorite() {
//        String url1 = auto.getHinh();
//        String url2 = auto.getHinh2();
//        String url3 = auto.getHinh3();
//        String url4 = auto.getHinh4();
//        String tenDiaDiem = txttenks.getText().toString();
//        String diaChiCT = txtdiachi.getText().toString();
//        String moTa = txtmota.getText().toString();
//        String gia = auto.getGia();
//        String gia2 = auto.getGia2();
//        String slp = auto.getSlphongdon();
//        String dc = auto.getDiachi();
//        String sdt = auto.getSdtks();
//        Boolean trangthai = auto.isTrangthai();
//
//        Auto diaDiem1 = new Auto(url1, tenDiaDiem, dc, gia, url2, url3, url4, diaChiCT, moTa, slp, sdt, trangthai, gia2);
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
//        reference.child(auth.getUid()).child("Favorites").child(tenDiaDiem)
//                .setValue(diaDiem1)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        showToast("Đã thêm vào danh sách yêu thích!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        showToast("Thêm vào danh sách yêu thích thất bại !");
//                    }
//                });
//    }
//
//    private void removeFavorite() {
//        String tenDiaDiem = txttenks.getText().toString();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
//        reference.child(auth.getUid()).child("Favorites").child(tenDiaDiem)
//                .removeValue()
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        showToast("Đã xóa khỏi danh sách yêu thích!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        showToast("Xóa khỏi danh sách yêu thích thất bại !");
//                    }
//                });
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//            case R.id.menu_favorite:
//                if (isInMyFavorite) {
//                    removeFavorite();
//                } else {
//                    addFavorite();
//                }
//                break;
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_favorite, menu);
//        menuItem = menu.findItem(R.id.menu_favorite);
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
//        String tenDiaDiem = txttenks.getText().toString();
//        reference.child(auth.getUid()).child("Favorites").child(tenDiaDiem)
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        isInMyFavorite = snapshot.exists(); //true if exists, false nguoc lai
//                        if (isInMyFavorite) {
//                            Drawable myDrawable = getResources().getDrawable(R.drawable.ic_baseline_favorite_24_white);
//                            menuItem.setIcon(myDrawable);
//                        } else {
//                            Drawable myDrawable = getResources().getDrawable(R.drawable.ic_baseline_favorite_border_24);
//                            menuItem.setIcon(myDrawable);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    public void showname() {
//        String tenks = auto.getTenks();
//        reference = FirebaseDatabase.getInstance().getReference("TPHCM");
//        reference.child(tenks).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                toolbartenks.setText(tenks);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                toolbartenks.setText("");
//            }
//        });
//    }
//
//    private void showToast(String mess) {
//        Toast.makeText(this, mess, Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onClick(View view) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(DetailHotelActivity.this);
//        builder.setTitle("Chọn loại phòng")
//                .setItems(theloai, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case 0:
//                                Intent intentdp = new Intent(view.getContext(), PaymentActivity.class);
//                                intentdp.putExtra("clickdp", auto);
//                                intentdp.putExtra("loai1", 1);
//                                view.getContext().startActivity(intentdp);
//                                break;
//                            case 1:
//                                Intent intent = new Intent(view.getContext(), PaymentActivity.class);
//                                intent.putExtra("clickdp", auto);
//                                intent.putExtra("loai1", 2);
//                                view.getContext().startActivity(intent);
//                                break;
//                        }
//                    }
//                });
//        builder.create();
//        builder.show();
//    }
//}