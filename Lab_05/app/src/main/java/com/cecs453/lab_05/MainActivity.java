package com.cecs453.lab_05;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button phoneButton;
    private Button browserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get views
        initializeVariables();
        //set listeners
        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numberString = getString(R.string.phone_number);
                Uri number = Uri.parse("tel:" + numberString);
                Intent dialerIntent = new Intent(Intent.ACTION_DIAL,number);
                startActivity(dialerIntent);
            }
        });

        browserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlString = getString(R.string.web_uri);
                Uri url = Uri.parse(urlString);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,url);
                Intent chooser = Intent.createChooser(browserIntent, "Choose Browser" );
                if(browserIntent.resolveActivity(getPackageManager()) != null) startActivity(chooser);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeVariables(){
        phoneButton = (Button) findViewById(R.id.phoneDialerBtn);
        browserButton = (Button) findViewById(R.id.webBrowserBtn);
    }
}
