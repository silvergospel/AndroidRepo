package com.cecs453.lab_03;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    List<Map<String,String>> planetsList = new ArrayList<Map<String,String>>();
    SimpleAdapter simpleAdpt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initList();

        ListView lv = (ListView) findViewById(R.id.listView);
        simpleAdpt = new SimpleAdapter(this, planetsList, android.R.layout.simple_list_item_1, new String[] {"planet"}, new int[] {android.R.id.text1});
        lv.setAdapter(simpleAdpt);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView clickedView = (TextView) view;
                Toast.makeText(MainActivity.this,"Item with id [" +
                        id + "] - Position [" +
                        position + "] - Planet [" +
                        clickedView.getText() + "]", Toast.LENGTH_SHORT).show();
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

    private void initList() {
        planetsList.add(createPlanet("planet","Mercury"));
        planetsList.add(createPlanet("planet","Venus"));
        planetsList.add(createPlanet("planet","Earth"));
        planetsList.add(createPlanet("planet","Mars"));
        planetsList.add(createPlanet("planet","Jupiter"));
        planetsList.add(createPlanet("planet","Saturn"));
        planetsList.add(createPlanet("planet","Uranus"));
        planetsList.add(createPlanet("planet","Neptune"));
    }

    private HashMap<String,String> createPlanet(String key, String name) {
        HashMap<String, String> planet = new HashMap<String,String>();
        planet.put(key,name);
        return planet;
    }
}
