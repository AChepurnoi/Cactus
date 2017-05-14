package com.cactus.domain.dto.google.vision.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;

/**
 * Created by Sasha on 5/14/17.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VisionResponse {
    private List<VisionLabelResponse> responses;
    private JsonNode error;
}
