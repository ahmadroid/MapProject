package ir.ahmadandroid.mapproject.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ir.ahmadandroid.mapproject.R;
import ir.ahmadandroid.mapproject.utils.MyActivity;

public class AdminActivity extends MyActivity implements View.OnClickListener {

    private SharedPreferences preferences;
    private String TAG="AdminActivity";
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        btn=findViewById(R.id.btn_register);
        btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.equals(btn)){
            Intent registerIntent=new Intent(AdminActivity.this, InsertPersonActivity.class);
            startActivity(registerIntent);
        }
    }
}