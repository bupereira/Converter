package com.bupereira.converterbymurilo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    Spinner spinner;
    Spinner spinner2;
    EditText editText;
    Spinner magSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        magSpinner = (Spinner) findViewById(R.id.magnitudeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.magnitude_array, android.R.layout.simple_spinner_dropdown_item);
        magSpinner.setAdapter(adapter);

        // TODO: Change Spinners values depending on choice (volume/distance)
        // Do I still need to set them here or does it get preset automagically?
        // setValuesToSpinners(R.id.spinner, magSpinner.getSelectedItemId() );
        //setValuesToSpinners(R.id.spinner2);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        editText = (EditText) findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateAndConvert();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Next line could get the chosen item, but not needed right now
                //String str = (String) adapterView.getItemAtPosition(i);
                updateAndConvert();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner2.setOnItemSelectedListener(spinner.getOnItemSelectedListener());
        editText.setSelection(0,1);

        AdapterView.OnItemSelectedListener magListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int arrayID = 0;
                if (magSpinner.getSelectedItem().toString().equals("Distance")) {
                    arrayID = R.array.distance_array;
                }
                else if(magSpinner.getSelectedItem().toString().equals("Volume")) {
                    arrayID = R.array.volume_array;
                }
                else if(magSpinner.getSelectedItem().toString().equals("Quick Conv.")) {
                    arrayID = R.array.quick_conv_array;
                }

                setValuesToSpinners(spinner.getId(),arrayID);
                if(arrayID != R.array.quick_conv_array)
                    setValuesToSpinners(spinner2.getId(),arrayID);

                // Disable this if it's quick conv.
                spinner2.setEnabled(arrayID != R.array.quick_conv_array);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        magSpinner.setOnItemSelectedListener(magListener);
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

    private void setValuesToSpinners(int spinnerId, int arrayID) {
        Spinner spinner = (Spinner) findViewById(spinnerId);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, arrayID, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void updateAndConvert() {

        if(editText.getText().toString().equals("")) {
            editText.setText("0");
            editText.setSelection(0,1);
        }

        // First, convert whatever was input to Meters. (I thought having it in a unique measure would help)

        float baseValue;
        if(editText.getText().toString().equals(".")) {
            baseValue = 0;
        }
        else {
            baseValue = Float.parseFloat(editText.getText().toString());
        }

        double convFactor = 0;
        double result = 0;
        String convertFrom = spinner.getSelectedItem().toString();
        String convertTo = spinner2.getSelectedItem().toString();
        if(magSpinner.getSelectedItem().toString().equals("Distance")) {
            switch (convertFrom) {
                case "m":
                    convFactor = 1;
                    break;
                case "ft":
                    convFactor = 0.3048;
                    break;
                case "mi":
                    convFactor = 1609.34;
                    break;
                case "km":
                    convFactor = 1000;
                    break;
            }

            result = baseValue * convFactor;
            // Now all distances are in meter
            switch (convertTo) {
                case "m":
                    convFactor = 1;
                    break;
                case "ft":
                    convFactor = 0.3048;
                    break;
                case "mi":
                    convFactor = 1609.34;
                    break;
                case "km":
                    convFactor = 1000;
                    break;
            }
            result = result / convFactor;
        }
        else if(magSpinner.getSelectedItem().toString().equals("Volume")) {
            // Again, they're all converted to ml first. Makes it user to keep all possible operations
            switch (convertFrom) {
                case "oz":
                    convFactor = 29.5735;
                    break;
                case "fl.oz":
                    convFactor = 29.5735;
                    break;
                case "gl":
                    convFactor = 3785.41;
                    break;
                case "ml":
                    convFactor = 1;
                    break;
                case  "l":
                    convFactor = 1000;
                    break;
            }

            result = baseValue * convFactor;
            // Now all volumes are in ml. Something something something Dark Side :D
            switch (convertTo) {
                case "oz":
                    convFactor = 29.5735;
                    break;
                case "fl.oz":
                    convFactor = 29.5735;
                    break;
                case "gl":
                    convFactor = 3785.41;
                    break;
                case "ml":
                    convFactor = 1;
                    break;
                case "l":
                    convFactor = 1000;
                    break;
            }
            result = result / convFactor;
        }
        else if(magSpinner.getSelectedItem().toString().equals("Quick Conv.")) {
            switch (convertFrom) {
                case "Barrels to Gallons":
                    result = baseValue * 42;
                    break;
                case "Gallons to Barrels":
                    result = baseValue / 42;
                    break;
                case "Lbs to Metric Tons":
                    result = baseValue / 2204.62;
                    break;
                case "Lbs to Long Tons":
                    result = baseValue / 2240.00;
                    break;
                case "Lbs to Short Tons":
                    result = baseValue / 2000.00;
                    break;
                case "Lbs to Kilos":
                    result = baseValue / 2.20462;
                    break;
                case "m3 to Barrels":
                    result = baseValue * 6.28981;
                    break;
                case "m3 to Gallons":
                    result = baseValue * 264.172;
                    break;
                case "Lbs/Gal to Liter Weight":
                    result = baseValue * 0.1198264;
                    break;
                case "Gallons to m3":
                    result = baseValue * 0.00378;
                    break;
                case "Celsius to Fahrenheit":
                    result = (baseValue * 1.8) + 32;
                    break;
                case "Bar to PSI":
                    result = baseValue * 14.504;
                    break;
                case "Lbs/Gal to Specific Gravity":
                    result = baseValue / 8.32828;
                    break;
                case "Gallons to Liters":
                    result = baseValue * 3.785412;
                    break;
            }

        }

        TextView tvResult = (TextView) findViewById(R.id.textView2);
        tvResult.setText(Double.toString(result));
        // If number was keyed just after 0, remove leading zero from edit
        if((String.valueOf(editText.getText().charAt(0)).equals("0"))&&(editText.getText().length() > 1)&&!(String.valueOf(editText.getText().charAt(0)).equals("."))) {
            editText.setText(String.valueOf(editText.getText().subSequence(1, editText.getText().length())));
            editText.setSelection(editText.getText().length());

        }
    }
}
