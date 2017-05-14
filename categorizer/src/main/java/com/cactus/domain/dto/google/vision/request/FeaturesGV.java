package com.cactus.domain.dto.google.vision.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Sasha on 5/14/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeaturesGV {
    public final static FeaturesGV LABELS = new FeaturesGV("LABEL_DETECTION", 8);
    private String type;
    private int maxResults;
}
