package com.cecs453.assignment_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final double FIRST_TERM_OPTION = 180;
    private final double SECOND_TERM_OPTION = 240;
    private final double THIRD_TERM_OPTION = 360;
    private SeekBar interestSeekBar;
    private Button calcBtn;
    private EditText principleEditText;
    private CheckBox taxInsuranceCheck;
    private RadioButton firstRadioBtn;
    private RadioButton secondRadioBtn;
    private RadioButton thirdRadioBtn;
    private TextView interestLabel;
    private TextView monthlyLabel;
    private double coarseRate;
    private double interestRate;
    private double taxAndInsurance;
    private double monthlyNote;
    private double termLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeVariables();
       interestLabel.setText(getResources().getString(R.string.interest_rate_label) + formatDouble(coarseRate));
        //interest rate seekbar handling
        interestSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                coarseRate = ((float)progress/100) * 20;
                interestRate = coarseRate/1200;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                interestLabel.setText(getResources().getString(R.string.interest_rate_label) + formatDouble(coarseRate));
            }
        });

        //calculate monthly rate btn handling
        calcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(principleEditText.getText().length() == 0){
                    Toast.makeText(MainActivity.this,"Please enter a valid amount",Toast.LENGTH_SHORT).show();
                    return;
                }
                //get total owed
                float principle = Float.parseFloat(principleEditText.getText().toString());
                //get taxes and insurance
                if(taxInsuranceCheck.isChecked()){
                    taxAndInsurance = principle * .001;
                } else {
                    taxAndInsurance = 0;
                }
                //get term length
                if(firstRadioBtn.isChecked()) termLength = FIRST_TERM_OPTION;
                if(secondRadioBtn.isChecked()) termLength = SECOND_TERM_OPTION;
                if(thirdRadioBtn.isChecked()) termLength = THIRD_TERM_OPTION;
                //calculate monthly payment
                if(interestRate == 0.0) monthlyNote = (principle/termLength) + taxAndInsurance;
                else monthlyNote = principle * (interestRate/(1-(Math.pow(1+interestRate,-termLength)))) + taxAndInsurance;
                //Toast.makeText(MainActivity.this, "Monthly Note = $" + formatDouble(monthlyNote), Toast.LENGTH_SHORT).show();
                monthlyLabel.setText(getResources().getString(R.string.monthly_payment_label) + " $" + formatDouble(monthlyNote));
            }
        });
    }

    private void initializeVariables() {
        coarseRate = 10;
        interestRate = 0;
        taxAndInsurance = 0;
        monthlyNote = 0;
        interestSeekBar = (SeekBar) findViewById(R.id.InterestSeekBar);
        calcBtn = (Button) findViewById(R.id.calcBtn);
        principleEditText = (EditText) findViewById(R.id.PrincipleEditText);
        taxInsuranceCheck = (CheckBox) findViewById(R.id.chkBtn);
        firstRadioBtn = (RadioButton) findViewById(R.id.firstRadioBtn);
        secondRadioBtn = (RadioButton) findViewById(R.id.secondRadioBtn);
        thirdRadioBtn = (RadioButton) findViewById(R.id.thirdRadioBtn);
        interestLabel = (TextView) findViewById(R.id.InterestLabel);
        monthlyLabel = (TextView) findViewById(R.id.MonthlyLabel);
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

    private String formatDouble(double d){
        return String.format("%.2f",d);
    }
}
