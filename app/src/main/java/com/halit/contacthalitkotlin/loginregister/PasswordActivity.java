package com.halit.contacthalitkotlin.loginregister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.halit.contacthalitkotlin.R;

public class PasswordActivity extends AppCompatActivity {

    EditText username;
    Button reset;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        username = findViewById(R.id.username_reset);
        reset = findViewById(R.id.btnReset);

        DB = new DBHelper(this);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = username.getText().toString();

                Boolean checkUser = DB.checkusername(user);
                if (checkUser == true){

                    Intent intent = new Intent(getApplicationContext(), ResetActivity.class);
                    intent.putExtra("username", user);
                    startActivity(intent);
                    
                }else {

                    Toast.makeText(PasswordActivity.this, "User does not exists", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
