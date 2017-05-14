package com.cactus.service.google;

import com.cactus.domain.dto.google.vision.request.VisionRequest;
import com.cactus.domain.dto.google.vision.response.VisionResponse;
import com.fasterxml.jackson.databind.JsonNode;
import io.reactivex.Observable;
import okhttp3.internal.http.RealResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

import java.util.Map;

/**
 * Created by Sasha on 5/14/17.
 */
public interface GoogleVisionAPI {
    @POST("v1/images:annotate")
    Observable<VisionResponse> label(@QueryMap Map<String,String> map, @Body VisionRequest request);

}
