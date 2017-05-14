package com.cactus.domain.dto.google.vision.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Created by Sasha on 5/14/17.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VisionLabelResponse {
    private List<VisionLabelAnnotations> labelAnnotations;
}
