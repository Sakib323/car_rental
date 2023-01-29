package com.itsolution.car_owner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class login_car_owner extends AppCompatActivity {

    EditText name,password;
    public CardView user_login;
    public String userEnteredUsername,userEnteredUserPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_car_owner);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar;
        actionBar=getSupportActionBar();
        actionBar.hide();

        user_login=findViewById(R.id.submit);
        name=findViewById(R.id.name);
        password=findViewById(R.id.password);

        user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Dialog dialog_11=new Dialog(login_car_owner.this);
                dialog_11.setContentView(R.layout.location);
                dialog_11.getWindow().setBackgroundDrawableResource(R.drawable.bacground_for_dialog);
                dialog_11.setCancelable(false);
                TextView txt1=dialog_11.findViewById(R.id.textview_text);
                txt1.setText("validating your username and password");
                dialog_11.show();
                CardView ok1=dialog_11.findViewById(R.id.submit_btn);
                ok1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_11.dismiss();
                    }
                });

                userEnteredUsername=name.getText().toString();
                userEnteredUserPassword=password.getText().toString();

                DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("owner");
                Query checkUser=reference.orderByChild("name").equalTo(userEnteredUsername);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){


                            String passwordFromDB=snapshot.child(userEnteredUsername).child("password").getValue(String.class);
                            if (passwordFromDB.equals(userEnteredUserPassword)){

                                String nameFromDB=snapshot.child(userEnteredUsername).child("name").getValue(String.class);
                                String phoneNoFromDB=snapshot.child(userEnteredUsername).child("phone").getValue(String.class);
                                String emailFromDB=snapshot.child(userEnteredUsername).child("email").getValue(String.class);
                                String gender=snapshot.child(userEnteredUsername).child("gender").getValue(String.class);
                                String location=snapshot.child(userEnteredUsername).child("location").getValue(String.class);

                                SharedPreferences sharedPref = getSharedPreferences("user_info", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putBoolean("state",true);
                                editor.putString("name",nameFromDB);
                                editor.putString("email",emailFromDB);
                                editor.putString("phone",phoneNoFromDB);
                                editor.putString("gender",gender);
                                editor.putString("location",location);
                                editor.putString("password",passwordFromDB);
                                editor.apply();

                                editor.apply();


                                Intent intent=new Intent(getApplicationContext(),profile_car_owner.class);
                                startActivity(intent);



                            }
                            else {


                                dialog_11.dismiss();
                                Dialog dialog_1=new Dialog(login_car_owner.this);
                                dialog_1.setContentView(R.layout.location);
                                dialog_1.getWindow().setBackgroundDrawableResource(R.drawable.bacground_for_dialog);
                                dialog_1.setCancelable(false);
                                TextView txt1=dialog_1.findViewById(R.id.textview_text);
                                txt1.setText("invalid username or password");
                                dialog_1.show();
                                CardView ok1=dialog_1.findViewById(R.id.submit_btn);
                                ok1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog_1.dismiss();
                                    }
                                });


                            }

                        }
                        else {
                            dialog_11.dismiss();
                            Dialog dialog_=new Dialog(login_car_owner.this);
                            dialog_.setContentView(R.layout.location);
                            dialog_.getWindow().setBackgroundDrawableResource(R.drawable.bacground_for_dialog);
                            dialog_.setCancelable(false);
                            TextView txt=dialog_.findViewById(R.id.textview_text);
                            txt.setText("invalid username or password");
                            dialog_.show();
                            CardView ok=dialog_.findViewById(R.id.submit_btn);
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog_.dismiss();
                                }
                            });

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





            }
        });
    }









}