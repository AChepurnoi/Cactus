package com.cactus.domain.dto.google.vision.request;

import lombok.Data;

import java.util.List;

/**
 * Created by Sasha on 5/14/17.
 */
@Data
public class VisionRequest {
    private List<GoogleVisionRequest> requests;
}
