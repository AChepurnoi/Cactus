package com.cactus.service;

import com.cactus.domain.dto.google.GoogleTranslateResponse;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Sasha on 5/13/17.
 */
interface GoogleTranslateAPI {
    @POST("translate/v2")
    Observable<GoogleTranslateResponse> translate(@QueryMap Map<String,String> map);
}
