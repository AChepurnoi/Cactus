package com.cactus.service.google.impl;

import com.cactus.domain.dto.google.vision.request.FeaturesGV;
import com.cactus.domain.dto.google.vision.request.GoogleVisionRequest;
import com.cactus.domain.dto.google.vision.request.ImageGV;
import com.cactus.domain.dto.google.vision.request.VisionRequest;
import com.cactus.domain.dto.google.vision.response.VisionLabelAnnotations;
import com.cactus.domain.dto.google.vision.response.VisionLabelResponse;
import com.cactus.domain.dto.google.vision.response.VisionResponse;
import com.cactus.service.google.GoogleVisionAPI;
import com.cactus.service.google.GoogleVisionService;
import io.reactivex.Observable;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonList;

/**
 * Created by Sasha on 5/14/17.
 */
@Service
public class GoogleVisionServiceImpl implements GoogleVisionService {


    @Value("${google.cloud.key}")
    private String key;

    private Map<String, String> keymap = new HashMap<>();

    private final GoogleVisionAPI api;

    public GoogleVisionServiceImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vision.googleapis.com/")
                .validateEagerly(true)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        api = retrofit.create(GoogleVisionAPI.class);
    }

    @PostConstruct
    public void postConstruct() {
        keymap.put("key", key);
    }

    @Override
    @SneakyThrows
    public Observable<String> labels(MultipartFile image) {
        VisionRequest request = form(image);
        return api.label(keymap, request)
                .map(VisionResponse::getResponses)
                .flatMap(Observable::fromIterable)
                .map(VisionLabelResponse::getLabelAnnotations)
                .flatMap(Observable::fromIterable)
                .map(VisionLabelAnnotations::getDescription);
    }

    @SneakyThrows
    private VisionRequest form(MultipartFile image) {
        VisionRequest request = new VisionRequest();
        GoogleVisionRequest visionRequest = new GoogleVisionRequest();
        visionRequest.setFeatures(singletonList(FeaturesGV.LABELS));
        visionRequest.setImage(new ImageGV((image.getBytes())));
        request.setRequests(singletonList(visionRequest));
        return request;
    }
}
