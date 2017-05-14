package com.cactus.domain.dto.google.vision.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by Sasha on 5/14/17.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VisionLabelAnnotations {
    private String description;
    private double score;
}
