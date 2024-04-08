package com.example.tourguidemegaphone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourguidemegaphone.databases.CurrencyRatesDAO;
import com.example.tourguidemegaphone.databases.LoginDao;
import com.example.tourguidemegaphone.model.CurrencyRates;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeMap;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CurrencyConverterActivity extends AppCompatActivity {
    private static final String DEFAULT_CURRENCY = "CAD";
    private LoginDao loginDao = LoginDao.getInstance(CurrencyConverterActivity.this);
    private Boolean isUserAction = false;
    Spinner spinnerCurrencyFrom;
    Spinner spinnerCurrencyTo;
    EditText editTextAmountFrom;
    TextView txtViewAmountTo;
    TextView txtConvertedAmount;
    Button btnConvertCurrency;
    ImageButton imbBtnGoBack;
    ImageButton imageBtnLogOut;
    TextView txtViewTitle;
    AutoCompleteTextView autoCompleteTxtViewCurrencyFrom;
    AutoCompleteTextView autoCompleteTxtViewCurrencyTo;

    private CurrencyRates currencyRates;
    private CurrencyApiService service;
    private CurrencyRatesDAO ratesDAO;
    private long nextUpdateUnix;
    private long currentTimeUnix = System.currentTimeMillis() / 1000L;
    Map<String, Double> rates;
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
        imbBtnGoBack = findViewById(R.id.imgBtnGoBack);
        imageBtnLogOut = findViewById(R.id.imgBtnLogOut);
        txtViewTitle = findViewById(R.id.txtViewTitleCurrencyConverter);

        //hide autocomplete until it is fixed (does not select proper value to spinner)
        autoCompleteTxtViewCurrencyFrom.setVisibility(View.INVISIBLE);
        autoCompleteTxtViewCurrencyTo.setVisibility(View.INVISIBLE);

        imbBtnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageBtnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDao.logOut();
                Intent intent = new Intent(CurrencyConverterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ratesDAO = new CurrencyRatesDAO(CurrencyConverterActivity.this);

        setCurrencyRatesFromDbOrAPI(DEFAULT_CURRENCY, false);
        btnConvertCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    double dblAmountFrom = Double.parseDouble(editTextAmountFrom.getText().toString());
                    //double dlbAmountFrom = Double.parseDouble(amountFrom.getText().toString());
                    String currencyTo = spinnerCurrencyTo.getSelectedItem().toString();
                    Log.d("CURRENCYACT", "Currency rate value: " + rates.get(currencyTo).toString());
                    Log.d("CURRENCYACT", "CurrencyTo value: " + currencyTo);
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
                Log.d("CURRENCYACT", "onItemSelected, isUserAction: " + isUserAction);
                if (isUserAction) {
                    Log.d("CURRENCYACT", "currency-from-changed at pos: " + position);
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    Log.d("CURRENCYACT", "new selectedItem: " + selectedItem);
                    setCurrencyRatesFromDbOrAPI(selectedItem, true);
                    isUserAction = false;
                }

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
        //Uncomment this lines after fixing autocomplete:
//        autoCompleteTxtViewCurrencyFrom.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                editTextAmountFrom.setText("");
//                if(!txtConvertedAmount.getText().toString().isEmpty()) {
//                    txtConvertedAmount.setText("");
//                }
//                return false;
//            }
//        });

//        autoCompleteTxtViewCurrencyTo.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                txtConvertedAmount.setText("");
//                return false;
//            }
//        });

    }
    public static String convertUnixTimeToDateTime(long unixTime) {
        // Convert Unix time to milliseconds
        long unixTimeMillis = unixTime * 1000;

        // Create a Date object from Unix time in milliseconds
        Date date = new Date(unixTimeMillis);

        // Define the desired date-time format
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        // Set the time zone to UTC to ensure correct conversion
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        // Format the Date object to human-readable date-time string
        String formattedDateTime = formatter.format(date);

        return formattedDateTime;
    }
    private void setCurrencyRatesFromDbOrAPI(String currency, boolean areSpinnersSet){


        // Get last time we updated the db
        ratesDAO.open();
        //nextUpdateUnix = ratesDAO.queryLastUpdate();
        nextUpdateUnix = ratesDAO.getNextUpdateInUnixTime(currency);
        ratesDAO.close();
        Log.d("CURRENCYACT","setCurrencyRatesFromDbOrAPI()| currency: " + currency + " currentTime: " + currentTimeUnix + " - nextTime: " + nextUpdateUnix);

        // Update from API only on the first run (-1), or when the next update unix value is equal or
        // smaller than current time, or when no rates are saved on the db (-1).
        if(nextUpdateUnix == -1 || nextUpdateUnix <= currentTimeUnix) {
            //Update from API
            Log.d("CURRENCYACT", "Updating from API...");
            //Fill in spinners. Get data rates for CAD as default.
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
                        Log.d("CURRENCYACT", "API successfully replied back. ");
                        // Retrieve currency rates from the CurrencyRates object
                        currencyRates = response.body();

                        rates = currencyRates.getRates();

                        // Extract currency codes from the Map keys
                        // and only set spinners on first run.
                        if(!areSpinnersSet) {
                            Log.d("CURRENCYACT", "Spinners are not set. Setting them up now with data from API.");
                            List<String> currencyCodes = new ArrayList<>(rates.keySet());
                            populateSpinners(currencyCodes, currency);
                        }

                        // Save to db
                        // Save last update
                        ratesDAO.open();

                        // Convert the CurrencyRates object back to a JSON string
                        Gson gson = new Gson();
                        String json = gson.toJson(currencyRates);

                        // Pass the JSON string to the storeJson() method
                        Log.d("CURRENCYACT", "Saving data from API to db now...");
                        ratesDAO.storeCurrencyRates(currencyRates);

                        //Set title with info
                        Long nextUpdate = ratesDAO.getNextUpdateInUnixTime(currency);
                        String strNextUpdate = convertUnixTimeToDateTime(nextUpdate);
                        String currentTitle = txtViewTitle.getText().toString();
                        String newTitle = currentTitle + ". Data valid until: " + strNextUpdate;
                        txtViewTitle.setText(newTitle);

                        ratesDAO.close();


                    } else {
                        // Handle unsuccessful response
                        Log.d("CURRENCYACT", "API call unsuccessful.");
                        Toast.makeText(CurrencyConverterActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<CurrencyRates> call, Throwable t) {
                    // Handle failure
                    Toast.makeText(CurrencyConverterActivity.this, "Fail to get the data.." + call.request() + " Cause: " + t.getCause() + " Msg: " +t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("CURRENCYACT", "Req: " + call.request().headers().toString() + " - Cause: " + t.getCause() + " - Msg: " + t.getMessage());
                }
            });

        } else {
            //Update from db
            Log.d("CURRENCYACT", "Updating data from db now...");
            getCurrencyRatesFromDb(currency);
            if(!areSpinnersSet) {
                Log.d("CURRENCYACT", "Spinners are not set. Setting them now with data from db.");
                Map<String, Object> orderedRates = new TreeMap<String, Object>(rates);
                List<String> currencyCodes = new ArrayList<>(orderedRates.keySet());
                populateSpinners(currencyCodes, currency);
            }

        }
    }
    private Call<CurrencyRates> getCurrencyRatesFromAPI(String currency){
        return service.getCurrencyRates(currency);
    }
    private void getCurrencyRatesFromDb(String currency){
        // Extract currency codes from the currency_to column
        ratesDAO.open();
        rates = ratesDAO.getCurrencyRates(currency);
        ratesDAO.close();
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

//        spinnerCurrencyFrom.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                isUserAction = true;
//                Log.d("CURRENCYACT", "isUserAction: True");
//                return false;
//            }
//
//        });

        int spinnerPositionCurrencyFrom = adapterCurrencyFrom.getPosition(selectedCurrency);
        int spinnerPositionCurrencyTo = adapterCurrencyTo.getPosition(selectedCurrency);

        spinnerCurrencyFrom.setSelection(spinnerPositionCurrencyFrom);
        spinnerCurrencyTo.setSelection(spinnerPositionCurrencyTo);

        autoCompleteTxtViewCurrencyFrom.setAdapter(adapterCurrencyFrom);
        autoCompleteTxtViewCurrencyTo.setAdapter(adapterCurrencyTo);

    }
}