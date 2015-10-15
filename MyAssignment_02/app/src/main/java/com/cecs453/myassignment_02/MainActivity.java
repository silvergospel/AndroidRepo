package com.cecs453.myassignment_02;

import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LastAnimalDialogFragment.DialogInputListener{
    private ArrayList<Animal> animalArrayList;
    private ListView mainList;
    private LastAnimalDialogFragment dialogFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeVariables();
        populateListView();
        registerListViewClick();

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
        if (id == R.id.action_info) {
            startActivity(new Intent("com.cecs453.myassignment_02.InfoActivity"));
            return true;
        }
        if (id == R.id.action_uninstall) {
            //Toast.makeText(MainActivity.this,getApplicationContext().getPackageName(),Toast.LENGTH_SHORT).show();
            Uri packageUri = Uri.parse("package:" + getApplicationContext().getPackageName());
            startActivity(new Intent(Intent.ACTION_DELETE, packageUri));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeVariables(){
        mainList = (ListView) findViewById(R.id.main_list);
        animalArrayList = new ArrayList();
        populateAnimalArrayList(animalArrayList);
        dialogFragment = new LastAnimalDialogFragment();
    }

    private void populateAnimalArrayList(ArrayList<Animal> animalArrayList){
        animalArrayList.add(new Animal("Penguin","Flightless bird.",R.drawable.penguin));
        animalArrayList.add(new Animal("Gorilla", "Big ape.",R.drawable.gorilla));
        animalArrayList.add(new Animal("Elephant", "Picky pachyderm.", R.drawable.elephant));
        animalArrayList.add(new Animal("Sheep", "Woolly wonder.", R.drawable.sheep));
        animalArrayList.add(new Animal("Panda", "Vegetarian bear.", R.drawable.panda));
    }

    private void populateListView(){
        ArrayAdapter<Animal> animalArrayAdapter = new AnimalArrayAdapter();
        mainList.setAdapter(animalArrayAdapter);

    }

    @Override
    public void onPositive(DialogFragment dialogFragment) {
        //Toast.makeText(MainActivity.this, "Positive Clicked", Toast.LENGTH_SHORT).show();
        Intent detailsIntent = new Intent("com.cecs453.myassignment_02.DetailsActivity");
        detailsIntent.putExtra("animal", (Serializable) animalArrayList.get(animalArrayList.size()-1));
        startActivity(detailsIntent);
    }

    @Override
    public void onNegative(DialogFragment dialogFragment) {
        //Toast.makeText(MainActivity.this, "Negative Clicked", Toast.LENGTH_SHORT).show();
        return;
    }

    private class AnimalArrayAdapter extends ArrayAdapter<Animal> {
        public AnimalArrayAdapter(){
            super(MainActivity.this, R.layout.item_view, animalArrayList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            if(itemView == null) itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
            Animal currentAnimal = animalArrayList.get(position);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.item_view_image);
            imageView.setImageResource(currentAnimal.getIconId());
            TextView textView = (TextView) itemView.findViewById(R.id.item_view_text);
            textView.setText(currentAnimal.getAnimalName());
            return itemView;
        }
    }

    private void registerListViewClick(){
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Animal animalClicked = animalArrayList.get(position);
                if(position > animalArrayList.size() - 2){

                    dialogFragment.show(getFragmentManager(),"WARNING!!!");

                    return;
                }
                Intent detailsIntent = new Intent("com.cecs453.myassignment_02.DetailsActivity");
                detailsIntent.putExtra("animal", (Serializable) animalClicked);
                startActivity(detailsIntent);

            }
        });
    }
}

