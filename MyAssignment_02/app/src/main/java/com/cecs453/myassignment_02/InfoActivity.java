package com.cecs453.myassignment_02;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class InfoActivity extends MainActivity {
    private Button telephoneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        initializeVariables();
    }

    private void initializeVariables() {
        telephoneButton = (Button) findViewById(R.id.telephone_button);
        telephoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getString(R.string.phone_number))));
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getString(R.string.phone_number))));
                }
            }
        });
    }

}
