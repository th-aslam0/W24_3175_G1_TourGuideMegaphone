package com.example.tourguidemegaphone;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import com.example.tourguidemegaphone.model.CurrencyRates;

public interface CurrencyApiService {
    @GET("{baseCurrency}")
    Call<CurrencyRates> getCurrencyRates(@Path("baseCurrency") String baseCurrency);


}

