package com.example.tourguidemegaphone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourguidemegaphone.databases.CurrencyRatesDAO;
import com.example.tourguidemegaphone.model.CurrencyRates;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.TreeMap;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CurrencyConverterActivity extends AppCompatActivity {
    private static final String DEFAULT_CURRENCY = "CAD"; 
    Spinner spinnerCurrencyFrom;
    Spinner spinnerCurrencyTo;
    EditText editTextAmountFrom;
    TextView txtViewAmountTo;
    TextView txtConvertedAmount;
    Button btnConvertCurrency;
    AutoCompleteTextView autoCompleteTxtViewCurrencyFrom;
    AutoCompleteTextView autoCompleteTxtViewCurrencyTo;

    private CurrencyRates currencyRates;
    private CurrencyApiService service;
    private CurrencyRatesDAO ratesDAO;
    private long nextUpdateUnix;
    private long currentTimeUnix = System.currentTimeMillis() / 1000L;
    Map<String, Double> rates;
    private void setCurrencyRatesFromDbOrAPI(String currency, boolean isSetSpinners){


        // Get last time we updated the db
        ratesDAO.open();
        //nextUpdateUnix = ratesDAO.queryLastUpdate();
        nextUpdateUnix = ratesDAO.getLastUpdate(currency);
        ratesDAO.close();
        Log.d("DB-TIME","current: " + currentTimeUnix + " - next: " + nextUpdateUnix);

        // Update from API only on the first run (-1), or when the next update unix value is equal or
        // smaller than current time, or when no rates are saved on the db (-1).
        if(nextUpdateUnix == -1 || nextUpdateUnix <= currentTimeUnix) {
            //Update from API
            //Fill in spinners. Get data rates for CAD as default.
            // In your activity or fragment:
            Retrofit retrofit = new Retrofit.Builder()
                    //.baseUrl("http://localhost:8000/")
                    .baseUrl("https://open.er-api.com/v6/latest/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = retrofit.create(CurrencyApiService.class);
            Call<CurrencyRates> call = getCurrencyRatesFromAPI(currency);

            call.enqueue(new Callback<CurrencyRates>() {
                @Override
                public void onResponse(Call<CurrencyRates> call, Response<CurrencyRates> response) {
                    if (response.isSuccessful()) {
                        // Retrieve currency rates from the CurrencyRates object
                        currencyRates = response.body();
                        //Toast.makeText(CurrencyConverterActivity.this, "RATES:" + currencyRates.getBaseCode().toString(), Toast.LENGTH_SHORT).show();
                        rates = currencyRates.getRates();

                        // Extract currency codes from the Map keys
                        // and only set spinners on first run.
                        if(isSetSpinners) {
                            List<String> currencyCodes = new ArrayList<>(rates.keySet());
                            populateSpinners(currencyCodes, DEFAULT_CURRENCY);
                        }

                        // Save to db
                        // Save last update
                        ratesDAO.open();
                        //ratesDAO.insertInTimesTable(currencyRates.getTime_next_update_unix());

                        // Convert the CurrencyRates object back to a JSON string
                        Gson gson = new Gson();
                        String json = gson.toJson(currencyRates);

                        // Pass the JSON string to the storeJson() method
                        ratesDAO.storeCurrencyRates(currencyRates);

                        ratesDAO.close();
                        // Save rates
//                        for (Map.Entry<String, Double> entry : rates.entrySet()) {
//                            //System.out.println(entry.getKey() + "/" + entry.getValue());
//                            String key = entry.getKey();
//                            Double value = entry.getValue();
//                            Log.d("DB-INSERT: ","key: " + key + " - value: " + value.toString());
//                            ratesDAO.open();
//                            ratesDAO.insertInMainTable(DEFAULT_CURRENCY, entry.getKey(), entry.getValue());
//                            ratesDAO.close();
//                        }


                    } else {
                        // Handle unsuccessful response
                        Toast.makeText(CurrencyConverterActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<CurrencyRates> call, Throwable t) {
                    // Handle failure
                    Toast.makeText(CurrencyConverterActivity.this, "Fail to get the data.." + call.request() + " Cause: " + t.getCause() + " Msg: " +t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("HTTP-ERROR", "Req: " + call.request().headers().toString() + " - Cause: " + t.getCause() + " - Msg: " + t.getMessage());
                }
            });

        } else {
            //Update from db
            getCurrencyRatesFromDb(currency);
            //List<String> currencyCodes = ratesDAO.getAllDifferentCurrencies();
            Map<String, Object> orderedRates = new TreeMap<String, Object>(rates);
            //List<String> currencyCodes = new ArrayList<>(rates.keySet());
            List<String> currencyCodes = new ArrayList<>(orderedRates.keySet());
            populateSpinners(currencyCodes, DEFAULT_CURRENCY);
        }
    }

    private void getCurrencyRatesFromDb(String currency){
        // Extract currency codes from the currency_to column
        ratesDAO.open();

        rates = ratesDAO.getCurrencyRates(currency);

        ratesDAO.close();
    }
    private Call<CurrencyRates> getCurrencyRatesFromAPI(String currency){

        return service.getCurrencyRates(currency);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_converter);

        spinnerCurrencyFrom = findViewById(R.id.spinnerCurrencyFrom);
        spinnerCurrencyTo = findViewById(R.id.spinnerCurrencyTo);
        editTextAmountFrom = findViewById(R.id.editTextAmountFrom);
        txtViewAmountTo = findViewById(R.id.txtViewCurrencyTo);
        btnConvertCurrency = findViewById(R.id.btnConvertCurrency);
        autoCompleteTxtViewCurrencyFrom = findViewById(R.id.autoCompleteTxtViewCurrencyFrom);
        autoCompleteTxtViewCurrencyTo = findViewById(R.id.autoCompleteTxtCurrencyTo);
        txtConvertedAmount = findViewById(R.id.txtViewConvertedAmount);


        ratesDAO = new CurrencyRatesDAO(CurrencyConverterActivity.this);

        setCurrencyRatesFromDbOrAPI(DEFAULT_CURRENCY, true);
        btnConvertCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    double dblAmountFrom = Double.parseDouble(editTextAmountFrom.getText().toString());
                    //double dlbAmountFrom = Double.parseDouble(amountFrom.getText().toString());
                    String currencyTo = spinnerCurrencyTo.getSelectedItem().toString();
                    double dblResult = dblAmountFrom * rates.get(currencyTo);
                    String strResult = new DecimalFormat("0.00").format(dblResult);

                    txtConvertedAmount.setText(strResult);
                } catch (Exception ex) {
                    String message = ex.getMessage();

                    if(ex.getMessage().contains("empty String"))
                        message = "Please enter a value to convert from.";

                    Toast.makeText(CurrencyConverterActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                }


            }
        });

        spinnerCurrencyFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("currency-from-changed", "pos: " + position);
                String selectedItem = parent.getItemAtPosition(position).toString();
                //Toast.makeText(CurrencyConverterActivity.this, selectedItem, Toast.LENGTH_SHORT).show();

                //do we need to call it again? Yes. Get new rates of from db.
                //Call<CurrencyRates> call = getCurrencyRates(selectedItem);
                setCurrencyRatesFromDbOrAPI(selectedItem, false);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerCurrencyTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtConvertedAmount.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerCurrencyFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editTextAmountFrom.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        autoCompleteTxtViewCurrencyFrom.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                editTextAmountFrom.setText("");
                if(!txtConvertedAmount.getText().toString().isEmpty()) {
                    txtConvertedAmount.setText("");
                }
                return false;
            }
        });

        autoCompleteTxtViewCurrencyTo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                txtConvertedAmount.setText("");
                return false;
            }
        });

    }

    protected void populateSpinners(List<String> currencyCodes, String selectedCurrency) {
        // Create an ArrayAdapter to populate the Spinners
        ArrayAdapter<String> adapterCurrencyFrom = new ArrayAdapter<>(CurrencyConverterActivity.this, android.R.layout.simple_spinner_item, currencyCodes);
        adapterCurrencyFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapterCurrencyTo = new ArrayAdapter<>(CurrencyConverterActivity.this, android.R.layout.simple_spinner_item, currencyCodes);
        adapterCurrencyTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapters to the Spinners
        spinnerCurrencyFrom.setAdapter(adapterCurrencyFrom);
        spinnerCurrencyTo.setAdapter(adapterCurrencyTo);

        // on below line we are getting the position of the item by the item name in our adapterCurrencyFrom.
        int spinnerPositionCurrencyFrom = adapterCurrencyFrom.getPosition(selectedCurrency);
        int spinnerPositionCurrencyTo = adapterCurrencyTo.getPosition(selectedCurrency);

        // on below line we are setting selection for our spinner to spinner position.
        spinnerCurrencyFrom.setSelection(spinnerPositionCurrencyFrom);
        spinnerCurrencyTo.setSelection(spinnerPositionCurrencyTo);

        //Now populate the autocomplete txt for currencyTo
        // Initialize the AutoCompleteTextView


        // Create an ArrayAdapter using the string array and a default spinner layout
        //ArrayAdapter<CharSequence> adapterCurrencyFrom = ArrayAdapter.createFromResource(this, R.array.spinner_items, android.R.layout.simple_spinner_dropdown_item);

        // Specify the layout to use when the list of choices appears
        //adapterCurrencyFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapterCurrencyFrom to the AutoCompleteTextView
        autoCompleteTxtViewCurrencyFrom.setAdapter(adapterCurrencyFrom);
        autoCompleteTxtViewCurrencyTo.setAdapter(adapterCurrencyTo);
    }
}