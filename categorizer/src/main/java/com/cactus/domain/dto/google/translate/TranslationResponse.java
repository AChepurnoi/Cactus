package com.cactus.domain.dto.google.translate;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Sasha on 5/13/17.
 */
@Data
@NoArgsConstructor
public class TranslationResponse {
    private String translatedText;
    private String detectedSourceLanguage;

}
