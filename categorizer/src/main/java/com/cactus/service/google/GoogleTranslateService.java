package com.cactus.service.google;

import com.cactus.domain.dto.google.translate.TranslationResponse;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Sasha on 5/13/17.
 */
public interface GoogleTranslateService {
    public Single<TranslationResponse> translate(String q, String lang);
}
