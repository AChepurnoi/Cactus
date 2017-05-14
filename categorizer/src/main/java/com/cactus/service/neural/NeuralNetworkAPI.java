package com.cactus.service.neural;

import com.cactus.domain.dto.google.translate.GoogleTranslateResponse;
import com.cactus.domain.dto.neural.Prediction;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

import java.util.List;
import java.util.Map;

/**
 * Created by Sasha on 5/14/17.
 */
public interface NeuralNetworkAPI {

    @Multipart
    @POST("predict")
    Observable<List<Prediction>> predict(@Part("title") String title,
                                         @Part("description")String description,
                                         @Part("price") String price,
                                         @Part MultipartBody.Part image);

}
