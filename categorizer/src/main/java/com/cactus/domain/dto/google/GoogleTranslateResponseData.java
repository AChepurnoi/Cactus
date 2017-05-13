package com.cactus.domain.dto.google;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Sasha on 5/13/17.
 */
@Data
@NoArgsConstructor
public class GoogleTranslateResponseData {
    private List<TranslationResponse> translations;


}
