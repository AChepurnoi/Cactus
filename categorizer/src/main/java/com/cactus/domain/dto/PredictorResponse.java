package com.cactus.domain.dto;

import com.cactus.domain.dto.neural.Prediction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Sasha on 5/14/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictorResponse {
    List<Prediction> predictions;
    List<String> labels;
}
