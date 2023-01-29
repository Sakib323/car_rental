package com.itsolution.car_owner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    CardView register,login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar;
        actionBar=getSupportActionBar();
        actionBar.hide();


        SharedPreferences sharedPreferences=getSharedPreferences("user_info",MODE_PRIVATE);
        Boolean state=sharedPreferences.getBoolean("state",false);
        if(state==true){
            Intent intent=new Intent(MainActivity.this,profile_car_owner.class);
            startActivity(intent);
        }


        register=findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,register_car_owner.class);
                startActivity(intent);
            }
        });


        login=findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,login_car_owner.class);
                startActivity(intent);
            }
        });

    }
}