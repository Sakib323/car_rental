package com.itsolution.car_owner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile_car_owner extends AppCompatActivity {

    public String name,email,phone,gender,location,password,verified,balance,profile_url;
    TextView name_,email_,address_,balance_;
    CircleImageView prfl_img;
    public String number="";
    Uri image_uri=null,vehicle_img_1=null,vehicle_img_2=null,vehicle_img_3=null;
    String child_name;
    public Dialog dialog;
    public int a=0;
    private static final int Gallery_Code=1;
    public int request_for_vehicle=0;
    public Dialog upload_car_img;
    RecyclerView recyclerView;
    adapter_to_get_data_OwnerDashboard adapter;
    List<model_for_owner_dash> model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_car_owner);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar;
        actionBar=getSupportActionBar();
        actionBar.hide();




        SharedPreferences sharedPreferences=getSharedPreferences("user_info",MODE_PRIVATE);
        name=sharedPreferences.getString("name","");
        email=sharedPreferences.getString("email","");
        phone=sharedPreferences.getString("phone","");
        gender=sharedPreferences.getString("gender","");
        location=sharedPreferences.getString("location","");
        password=sharedPreferences.getString("password","");

        name_=findViewById(R.id.name);
        email_=findViewById(R.id.email);
        address_=findViewById(R.id.address);
        balance_=findViewById(R.id.balance);
        name_.setText(name);
        email_.setText(email);
        address_.setText(location);
        prfl_img=findViewById(R.id.profile_image);

        DatabaseReference firebaseDatabase_for_list_data=FirebaseDatabase.getInstance().getReference().child("owner").child(name).child("car");
        recyclerView=findViewById(R.id.list_of_car);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        model=new ArrayList<model_for_owner_dash>();
        adapter=new adapter_to_get_data_OwnerDashboard(profile_car_owner.this,model);
        recyclerView.setAdapter(adapter);
        firebaseDatabase_for_list_data.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                model_for_owner_dash student_model=snapshot.getValue(model_for_owner_dash.class);
                model.add(student_model);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference db= FirebaseDatabase.getInstance().getReference().child("owner");
        db.child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                verified=snapshot.child("verified").getValue(String.class);
                Log.e("verified",verified);
                balance=snapshot.child("balance").getValue(String.class);
                balance_.setText(balance);
                profile_url=snapshot.child("profile_img").getValue(String.class);
                Picasso.get().load(profile_url).into(prfl_img);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        LinearLayout add_car=findViewById(R.id.add_car);
        add_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verified.contains("false")){
                    //verify first
                    Dialog dialog_1=new Dialog(profile_car_owner.this);
                    dialog_1.setContentView(R.layout.verify);
                    dialog_1.getWindow().setBackgroundDrawableResource(R.drawable.bacground_for_dialog);
                    dialog_1.setCancelable(false);
                    dialog_1.show();
                    CardView ok=dialog_1.findViewById(R.id.yes);
                    CardView no=dialog_1.findViewById(R.id.no);

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            call_dialog("Enter nid info","nid card");
                            dialog_1.dismiss();
                            //call_dialog("Enter driving licence info","driving licence");
                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog_1.dismiss();
                        }
                    });
                }
                else{


                    LocationstatusCheck();
                    Dialog add_car=new Dialog(profile_car_owner.this);
                    add_car.setContentView(R.layout.add_car);
                    add_car.getWindow().setBackgroundDrawableResource(R.color.transparent);
                    add_car.setCancelable(false);
                    add_car.show();

                    EditText location1,price_asking;
                    AutoCompleteTextView car_company,car_model,vehicle_type;
                    car_company=add_car.findViewById(R.id.car_company);
                    String[] car_comapny_name={"Toyota","Honda","Nissan","Suzuki","Hyundai","Audi","BMW","Subaru","TATA","Mercedes-Benz","CNG","others"};
                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(profile_car_owner.this, com.airbnb.lottie.R.layout.support_simple_spinner_dropdown_item,car_comapny_name);
                    car_company.setAdapter(arrayAdapter);
                    car_company.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            car_company.showDropDown();
                            car_company.requestFocus();
                            return false;
                        }
                    });


                    car_model=add_car.findViewById(R.id.car_model);
                    String[] car_model_name={"Toyota Corolla","Toyota Noah","Mitsubishi Pajero","Toyota Allion","Toyota Axio","Toyota Probox","Toyota Fielder","Hiace","Sedan Car","CNG","others"};
                    ArrayAdapter<String> array_model_Adapter=new ArrayAdapter<String>(profile_car_owner.this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,car_model_name);
                    car_model.setAdapter(array_model_Adapter);
                    car_model.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            car_model.showDropDown();
                            car_model.requestFocus();
                            return false;
                        }
                    });


                    vehicle_type=add_car.findViewById(R.id.vehicle_type);
                    String[] type_of_vehicle={"MICROBUS","SEDAN","CUV","SUV","COUPE","STATION WAGON","HATCHBACK","MINIVAN","JEEP","Multi-Purpose Vehicle(MPV)","MINIBUS","others"};
                    ArrayAdapter<String> array_type_Adapter=new ArrayAdapter<String>(profile_car_owner.this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,type_of_vehicle);
                    vehicle_type.setAdapter(array_type_Adapter);
                    vehicle_type.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            vehicle_type.showDropDown();
                            vehicle_type.requestFocus();
                            return false;
                        }
                    });
                    location1=add_car.findViewById(R.id.location);
                    location1.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            location1.setText(location);
                            return false;
                        }
                    });

                    price_asking=add_car.findViewById(R.id.price);

                    CardView next_btn=add_car.findViewById(R.id.next_btn);
                    next_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String location_,price,car_company_,car_model_,vehicle_type_;
                            location_=location1.getText().toString();
                            price=price_asking.getText().toString();
                            car_company_=car_company.getText().toString();
                            car_model_=car_model.getText().toString();
                            vehicle_type_=vehicle_type.getText().toString();
                            if(!location_.isEmpty()&&!price.isEmpty()&&!car_company_.isEmpty()&&!car_model_.isEmpty()&&!vehicle_type_.isEmpty()){



                                upload_car_img=new Dialog(profile_car_owner.this);
                                upload_car_img.setContentView(R.layout.upload_car_img);
                                upload_car_img.getWindow().setBackgroundDrawableResource(R.color.transparent);
                                upload_car_img.show();
                                CardView card_1,card_2,card_3,finish;
                                card_1=upload_car_img.findViewById(R.id.first_img);
                                card_2=upload_car_img.findViewById(R.id.second_img);
                                card_3=upload_car_img.findViewById(R.id.third_img);
                                finish=upload_car_img.findViewById(R.id.next_btn);

                                card_1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        request_for_vehicle=1;
                                        open_gallery();
                                    }
                                });
                                card_2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        request_for_vehicle=2;
                                        open_gallery();
                                    }
                                });
                                card_3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        request_for_vehicle=3;
                                        open_gallery();
                                    }
                                });

                                finish.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {



                                        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
                                        Random rnd = new Random();
                                        StringBuilder sb = new StringBuilder(10);
                                        for (int i = 0; i < 10; i++)
                                            sb.append(chars.charAt(rnd.nextInt(chars.length())));
                                        String random_= sb.toString();


                                        FirebaseStorage fs=FirebaseStorage.getInstance();
                                        StorageReference filepath = fs.getReference().child("owner's_car").child(name).child("car_"+random_);


                                        if(vehicle_img_1!=null&&vehicle_img_2!=null&&vehicle_img_3!=null){

                                            filepath.child("1").putFile(vehicle_img_1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                        Task<Uri> downloadUri=taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Uri> task) {
                                                                DatabaseReference db=FirebaseDatabase.getInstance().getReference();
                                                                db.child("owner").child(name).child("car").child("car_"+random_).child("img_1").setValue(task.getResult().toString());
                                                                return;
                                                            }
                                                        });
                                                }
                                            });
                                            //StorageReference filepath1 = fs.getReference().child("owner's_car").child(name).child("car_"+random_).child(vehicle_img_2.getLastPathSegment());

                                            filepath.child("2").putFile(vehicle_img_2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                    Task<Uri> downloadUri=taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Uri> task) {
                                                            DatabaseReference db=FirebaseDatabase.getInstance().getReference();
                                                            db.child("owner").child(name).child("car").child("car_"+random_).child("img_2").setValue(task.getResult().toString());
                                                            return;
                                                        }
                                                    });
                                                }
                                            });

                                            //StorageReference filepath2 = fs.getReference().child("owner's_car").child(name).child("car_"+random_).child(vehicle_img_1.getLastPathSegment());

                                            filepath.child("3").putFile(vehicle_img_3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                    Task<Uri> downloadUri=taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Uri> task) {
                                                            DatabaseReference db=FirebaseDatabase.getInstance().getReference();
                                                            db.child("owner").child(name).child("car").child("car_"+random_).child("img_3").setValue(task.getResult().toString());
                                                            return;
                                                        }
                                                    });
                                                }
                                            });

                                            DatabaseReference db=FirebaseDatabase.getInstance().getReference();
                                            HashMap<String,String> map=new HashMap<>();
                                            map.put("location",location_);
                                            map.put("price",price);
                                            map.put("car_company",car_company_);
                                            map.put("car_model",car_model_);
                                            map.put("vehicle_type",vehicle_type_);
                                            db.child("owner").child(name).child("car").child("car_"+random_).setValue(map);
                                            db.child("car_by_location").child(location_).child("car_"+random_).setValue(map);


                                            Dialog dialog=new Dialog(profile_car_owner.this);
                                            dialog.setContentView(R.layout.verify);
                                            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bacground_for_dialog);
                                            dialog.show();
                                            TextView txt=dialog.findViewById(R.id.textview_text);
                                            txt.setText("Car is uploaded successfully");
                                            CardView btn=dialog.findViewById(R.id.yes);
                                            btn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {


                                                    vehicle_img_1=null;
                                                    vehicle_img_3=null;
                                                    vehicle_img_2=null;
                                                    upload_car_img.dismiss();
                                                    dialog.dismiss();
                                                    add_car.dismiss();
                                                }
                                            });

                                        }


                                    }
                                });



                            }

                        }
                    });
                    //add car
                }

            }
        });

        BottomNavigationView bottomNavigationView=findViewById(R.id.nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.home1){

                }
                return false;
            }
        });

    }

    public void call_dialog(String txt,String child){
        dialog=new Dialog(profile_car_owner.this);
        dialog.setContentView(R.layout.document);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.setCancelable(false);
        dialog.show();
        //TextView txt_tv=dialog.findViewById(R.id.textview_text);
        //txt_tv.setText(txt);

        CardView cardView=dialog.findViewById(R.id.add_img);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    open_gallery();
            }
        });
    }
    private void open_gallery(){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,Gallery_Code);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Gallery_Code && resultCode==RESULT_OK && request_for_vehicle==0){
            ImageView img=dialog.findViewById(R.id.img);
            image_uri=data.getData();
            img.setImageURI(image_uri);
            upload_to_server();
        }

        if(requestCode==Gallery_Code && resultCode==RESULT_OK && request_for_vehicle==1){
            ImageView img=upload_car_img.findViewById(R.id.first_img_view);
            vehicle_img_1=data.getData();
            img.setImageURI(image_uri);
        }
        if(requestCode==Gallery_Code && resultCode==RESULT_OK && request_for_vehicle==2){
            ImageView img=upload_car_img.findViewById(R.id.second_img_view);
            vehicle_img_2=data.getData();
            img.setImageURI(image_uri);
        }
        if(requestCode==Gallery_Code && resultCode==RESULT_OK && request_for_vehicle==3){
            ImageView img=upload_car_img.findViewById(R.id.third_img_view);
            vehicle_img_3=data.getData();
            img.setImageURI(image_uri);
        }

    }

    private void upload_to_server() {
        FirebaseStorage fs=FirebaseStorage.getInstance();
        StorageReference filepath = fs.getReference().child("owner's_document").child(name).child("child_name").child(image_uri.getLastPathSegment());
        filepath.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> downloadUri=taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        EditText editText=dialog.findViewById(R.id.nid_card_nmbr);
                        String nmbr=editText.getText().toString();
                        DatabaseReference db=FirebaseDatabase.getInstance().getReference();
                        db.child("owner").child(name).child("document info").child("child_name").setValue(nmbr);
                        db.child("owner").child(name).child("document img url").child("child_name").setValue(task.getResult().toString());
                        dialog.dismiss();
                        return;
                    }
                });
            }
        });

    }

    public void LocationstatusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }



}