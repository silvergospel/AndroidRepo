package com.cecs453.myassignment_02;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class DetailsActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        Animal detailsAnimal = (Animal)intent.getSerializableExtra("animal");
        ((TextView)findViewById(R.id.details_animal_name)).setText(detailsAnimal.getAnimalName());
        ((TextView)findViewById(R.id.details_animal_description)).setText(detailsAnimal.getAnimalDescription());
        ((ImageView)findViewById(R.id.details_animal_image)).setImageResource(detailsAnimal.getIconId());
    }

}
