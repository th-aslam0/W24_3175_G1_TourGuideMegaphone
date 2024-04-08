package com.example.tourguidemegaphone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class PaymentProcessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_process);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find views
        EditText editTextCardName = findViewById(R.id.editTextCardName);
        EditText editTextCardNumber = findViewById(R.id.editTextCardNumber);
        EditText editTextCVV = findViewById(R.id.editTextCVV);
        EditText editDate = findViewById(R.id.editTextDate);
        Button buttonProceed = findViewById(R.id.buttonProceed);

        // Set click listener for Proceed button
        buttonProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardName = editTextCardName.getText().toString();
                String cardNumber = editTextCardNumber.getText().toString();
                String cardCVV = editTextCVV.getText().toString();
                String date = editDate.getText().toString();

                Intent prev_intent = getIntent();
                String price = prev_intent.getStringExtra("price");

                // Collect card details from the user
                Card card = new Card();
                card.setNumber(cardNumber);
                card.setExpiry(date);
                card.setSecurity_code(cardCVV);
                card.setCard_holder_name(cardName);

                // Construct payment request
                PaymentRequest request = new PaymentRequest();
                request.setIntent("CAPTURE");

                PurchaseUnit purchaseUnit = new PurchaseUnit();
                purchaseUnit.setAmount(new Amount());
                purchaseUnit.getAmount().setCurrency_code("USD");
                purchaseUnit.getAmount().setValue(price);
                // Create an instance of Amount
                request.setPurchase_units(new PurchaseUnit[]{purchaseUnit});

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api-m.sandbox.paypal.com") // Replace this with your API base URL
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                PaymentProcessActivity.PayPalService apiService = retrofit.create(PaymentProcessActivity.PayPalService.class);

                // Make payment request
                Call<PaymentResponse> call = apiService.createOrder(request);
                call.enqueue(new Callback<PaymentResponse>() {
                    @Override
                    public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                        if (response.isSuccessful()) {
                            // Handle successful payment response
                            Log.d("DEBUF", "successful: " + response);

                            Toast.makeText(PaymentProcessActivity.this, "Order Placed", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle unsuccessful payment response
                            Log.d("DEBUF", "unsuccessful: " + response);
                        }
                    }

                    @Override
                    public void onFailure(Call<PaymentResponse> call, Throwable t) {
                        // Handle network or unexpected errors
                        Log.d("DEBUF", "error: " +  t.getMessage());
                    }
                });

            }
        });
    }
    public class PaymentRequest {
        private String intent;
        private Payer payer;
        private PurchaseUnit[] purchase_units;

        public String getIntent() {
            return intent;
        }

        public void setIntent(String intent) {
            this.intent = intent;
        }

        public Payer getPayer() {
            return payer;
        }

        public void setPayer(Payer payer) {
            this.payer = payer;
        }

        public PurchaseUnit[] getPurchase_units() {
            return purchase_units;
        }

        public void setPurchase_units(PurchaseUnit[] purchase_units) {
            this.purchase_units = purchase_units;
        }

        // Constructor, getters, and setters
    }

    public class Payer {
        private String name;
        private String email_address;
        private Address address;
        private Card card;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail_address() {
            return email_address;
        }

        public void setEmail_address(String email_address) {
            this.email_address = email_address;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Card getCard() {
            return card;
        }

        public void setCard(Card card) {
            this.card = card;
        }

        // Constructor, getters, and setters
    }

    public class Address {
        private String address_line_1;
        private String address_line_2;
        private String admin_area_2;
        private String admin_area_1;
        private String postal_code;
        private String country_code;

        public String getAddress_line_1() {
            return address_line_1;
        }

        public void setAddress_line_1(String address_line_1) {
            this.address_line_1 = address_line_1;
        }

        public String getAddress_line_2() {
            return address_line_2;
        }

        public void setAddress_line_2(String address_line_2) {
            this.address_line_2 = address_line_2;
        }

        public String getAdmin_area_2() {
            return admin_area_2;
        }

        public void setAdmin_area_2(String admin_area_2) {
            this.admin_area_2 = admin_area_2;
        }

        public String getAdmin_area_1() {
            return admin_area_1;
        }

        public void setAdmin_area_1(String admin_area_1) {
            this.admin_area_1 = admin_area_1;
        }

        public String getPostal_code() {
            return postal_code;
        }

        public void setPostal_code(String postal_code) {
            this.postal_code = postal_code;
        }

        public String getCountry_code() {
            return country_code;
        }

        public void setCountry_code(String country_code) {
            this.country_code = country_code;
        }

        // Constructor, getters, and setters
    }

    public class Card {
        private String number;
        private String expiry;
        private String security_code;
        private String card_holder_name;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getExpiry() {
            return expiry;
        }

        public void setExpiry(String expiry) {
            this.expiry = expiry;
        }

        public String getSecurity_code() {
            return security_code;
        }

        public void setSecurity_code(String security_code) {
            this.security_code = security_code;
        }

        public String getCard_holder_name() {
            return card_holder_name;
        }

        public void setCard_holder_name(String card_holder_name) {
            this.card_holder_name = card_holder_name;
        }

        // Constructor, getters, and setters
    }

    public class PurchaseUnit {
        private Item[] items;
        private Amount amount;

        public Item[] getItems() {
            return items;
        }

        public void setItems(Item[] items) {
            this.items = items;
        }

        public Amount getAmount() {
            return amount;
        }

        public void setAmount(Amount amount) {
            this.amount = amount;
        }
    }

    public class Item {
        private String name;
        private String description;
        private String quantity;
        private UnitAmount unit_amount;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public UnitAmount getUnit_amount() {
            return unit_amount;
        }

        public void setUnit_amount(UnitAmount unit_amount) {
            this.unit_amount = unit_amount;
        }
    }

    public class Amount {
        private String currency_code;
        private String value;
        private Breakdown breakdown;

        public String getCurrency_code() {
            return currency_code;
        }

        public void setCurrency_code(String currency_code) {
            this.currency_code = currency_code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Breakdown getBreakdown() {
            return breakdown;
        }

        public void setBreakdown(Breakdown breakdown) {
            this.breakdown = breakdown;
        }
    }

    public class UnitAmount {
        private String currency_code;
        private String value;

        public String getCurrency_code() {
            return currency_code;
        }

        public void setCurrency_code(String currency_code) {
            this.currency_code = currency_code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public class Breakdown {
        private ItemTotal item_total;

        public ItemTotal getItem_total() {
            return item_total;
        }

        public void setItem_total(ItemTotal item_total) {
            this.item_total = item_total;
        }
    }

    public class ItemTotal {
        private String currency_code;
        private String value;

        public String getCurrency_code() {
            return currency_code;
        }

        public void setCurrency_code(String currency_code) {
            this.currency_code = currency_code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


    public class PaymentResponse {
        // Define properties for the payment response
    }

    public interface PayPalService {
        @Headers({
                "Content-Type: application/json",
                "Authorization: Bearer A21AAIXwkVGqfX3hyIPU11qHZ6lzQq6MKZLKwR5eS2VSZ5LQyJPzkF5pZ2b4bMCxPdzbQIs2he4VVL1PP4qPq-QdlRmI9KMpw"
        })
        @POST("/v2/checkout/orders")
        Call<PaymentResponse> createOrder(@Body PaymentRequest request);
    }
}