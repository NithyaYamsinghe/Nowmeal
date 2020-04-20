package com.example.nowmeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import com.example.nowmeal.admin.MainAdminActivity;
import com.example.nowmeal.client.MainClientActivity;

public class MainActivity extends AppCompatActivity {

    Button buttonClient, buttonAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
//        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_main);

        // hooks
        buttonClient = findViewById(R.id.clientButton);
        buttonAdmin = findViewById(R.id.adminButton);
        buttonClient.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainClientActivity.class);
            startActivity(intent);

        });

        buttonAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainAdminActivity.class);
            startActivity(intent);

        });

    }

}