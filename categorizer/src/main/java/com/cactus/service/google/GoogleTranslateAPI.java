package com.cactus.service.google;

import com.cactus.domain.dto.google.translate.GoogleTranslateResponse;
import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

import java.util.Map;

/**
 * Created by Sasha on 5/13/17.
 */
public interface GoogleTranslateAPI {
    @POST("translate/v2")
    Observable<GoogleTranslateResponse> translate(@QueryMap Map<String,String> map);

}
