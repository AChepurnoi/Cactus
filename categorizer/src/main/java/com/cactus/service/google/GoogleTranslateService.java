package com.cactus.service.google;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Sasha on 5/13/17.
 */
public interface GoogleTranslateService {
    public Single<String> translate(String q);
}
