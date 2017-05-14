package com.cactus.service.google.impl;

import com.cactus.domain.dto.google.translate.GoogleTranslateResponse;
import com.cactus.domain.dto.google.translate.TranslationResponse;
import com.cactus.service.google.GoogleTranslateAPI;
import com.cactus.service.google.GoogleTranslateService;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sasha on 5/13/17.
 */
@Service
public class GoogleTranslateServiceImpl implements GoogleTranslateService {

    @Value("${google.cloud.key}")
    private String key;


    private GoogleTranslateAPI api;

    public GoogleTranslateServiceImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://translation.googleapis.com/language/")
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        api = retrofit.create(GoogleTranslateAPI.class);


    }

    @Override
    public Single<TranslationResponse> translate(String q, String lang) {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        params.put("target", lang);
        params.put("key", key);

        return api.translate(params)
                .map(GoogleTranslateResponse::getData)
                .flatMap(data -> Observable.fromIterable(data.getTranslations()))
                .singleOrError();

    }
}
