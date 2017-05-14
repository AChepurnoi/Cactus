package com.cactus.service.google;

import io.reactivex.Observable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Sasha on 5/14/17.
 */
public interface GoogleVisionService {
    public Observable<String> labels(MultipartFile image);
}
