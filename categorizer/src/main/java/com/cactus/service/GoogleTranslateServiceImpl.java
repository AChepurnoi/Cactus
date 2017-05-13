package com.cactus.service;

import com.cactus.domain.dto.google.GoogleTranslateResponse;
import com.cactus.domain.dto.google.GoogleTranslateResponseData;
import com.cactus.domain.dto.google.TranslationResponse;
import io.reactivex.Observable;
import io.reactivex.Single;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Sasha on 5/13/17.
 */
@Service
public class GoogleTranslateServiceImpl implements GoogleTranslateService {

    @Value("${google.cloud.key}")
    private String key;

    private final String TARGET_LANG = "en";

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
    public Single<String> translate(String q) {
        Map<String, String> params = new HashMap<>();
        params.put("q", q);
        params.put("target", TARGET_LANG);
        params.put("key", key);

        return api.translate(params)
                .map(GoogleTranslateResponse::getData)
                .flatMap(data -> Observable.fromIterable(data.getTranslations()))
                .map(TranslationResponse::getTranslatedText)
                .singleOrError();

    }
}
