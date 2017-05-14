package com.cactus.service.neural;

import com.cactus.domain.dto.CheckInput;
import com.cactus.domain.dto.neural.Prediction;
import com.cactus.service.google.GoogleVisionAPI;
import io.reactivex.Observable;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.List;

/**
 * Created by Sasha on 5/14/17.
 */
@Service
public class NeuralNetworkServiceImpl implements NeuralNetworkService {

    private final NeuralNetworkAPI api;

    public NeuralNetworkServiceImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://35.156.4.162/")
                .validateEagerly(true)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        api = retrofit.create(NeuralNetworkAPI.class);
    }

    @Override
    @SneakyThrows
    public Observable<Prediction> predict(String title, String description, int price, MultipartFile image) {
        return api.predict(
                title,
                description,
                String.valueOf(price),
                MultipartBody.Part.createFormData("image","image",
                        RequestBody.create(MediaType.parse(image.getContentType()),image.getBytes())))
                .flatMap(Observable::fromIterable);
    }
}
