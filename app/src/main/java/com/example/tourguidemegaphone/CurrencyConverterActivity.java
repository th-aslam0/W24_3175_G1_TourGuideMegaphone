package com.example.tourguidemegaphone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tourguidemegaphone.databases.CurrencyRatesDAO;
import com.example.tourguidemegaphone.databases.CurrencyRatesDbHelper;
import com.example.tourguidemegaphone.model.CurrencyRates;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CurrencyConverterActivity extends AppCompatActivity {
    private static final String DEFAULT_CURRENCY = "CAD"; 
    Spinner spinnerCurrencyFrom;
    Spinner spinnerCurrencyTo;
    EditText amountFrom;
    EditText amountTo;
    Button btnConvertCurrency;
    private CurrencyRates currencyRates;
    private CurrencyApiService service;
    private CurrencyRatesDAO ratesDAO;
    Map<String, Double> rates;
    private Call<CurrencyRates> getCurrencyRates(String currency){

        return service.getCurrencyRates(currency);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_converter);

        //Fill in spinners. Get data rates for CAD as default.
        // In your activity or fragment:
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("http://localhost:8000/")
                .baseUrl("https://open.er-api.com/v6/latest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(CurrencyApiService.class);
        Call<CurrencyRates> call = getCurrencyRates(DEFAULT_CURRENCY);

        // Assume you have two Spinners named spinnerCurrencyFrom and spinnerCurrencyTo
        spinnerCurrencyFrom = findViewById(R.id.spinnerCurrencyFrom);
        spinnerCurrencyTo = findViewById(R.id.spinnerCurrencyTo);
        amountFrom = findViewById(R.id.editTextAmountFrom);
        amountTo = findViewById(R.id.editTextAmountTo);
        btnConvertCurrency = findViewById(R.id.btnConvertCurrency);


        call.enqueue(new Callback<CurrencyRates>() {
            @Override
            public void onResponse(Call<CurrencyRates> call, Response<CurrencyRates> response) {
                if (response.isSuccessful()) {
                    // Retrieve currency rates from the CurrencyRates object
                    currencyRates = response.body();
                    //Toast.makeText(CurrencyConverterActivity.this, "RATES:" + currencyRates.getBaseCode().toString(), Toast.LENGTH_SHORT).show();
                    rates = currencyRates.getRates();

                    // Extract currency codes from the Map keys
                    List<String> currencyCodes = new ArrayList<>(rates.keySet());

                    // Create an ArrayAdapter to populate the Spinners
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CurrencyConverterActivity.this, android.R.layout.simple_spinner_item, currencyCodes);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // Set the adapters to the Spinners
                    spinnerCurrencyFrom.setAdapter(adapter);
                    spinnerCurrencyTo.setAdapter(adapter);

                    // Save to db

                    for (Map.Entry<String, Double> entry : rates.entrySet()) {
                        //System.out.println(entry.getKey() + "/" + entry.getValue());
                        ratesDAO.insert(DEFAULT_CURRENCY, entry.getKey(), entry.getValue());
                    }


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

        btnConvertCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double dblAmountFrom = Double.parseDouble(amountFrom.getText().toString());
                //double dlbAmountFrom = Double.parseDouble(amountFrom.getText().toString());
                String currencyTo = spinnerCurrencyTo.getSelectedItem().toString();
                double dblResult = dblAmountFrom * rates.get(currencyTo);
                String strResult = new DecimalFormat("0.00").format(dblResult);
                amountTo.setText(strResult);
            }
        });

        spinnerCurrencyFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("currency-from-changed", "pos: " + position);
                String selectedItem = parent.getItemAtPosition(position).toString();
                //Toast.makeText(CurrencyConverterActivity.this, selectedItem, Toast.LENGTH_SHORT).show();
                Call<CurrencyRates> call = getCurrencyRates(selectedItem);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}