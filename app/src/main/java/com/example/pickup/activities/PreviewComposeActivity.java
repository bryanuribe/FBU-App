package com.example.pickup.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pickup.R;

public class PreviewComposeActivity extends AppCompatActivity {

    private Button btnSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_compose);

        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goMainActivity();
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
