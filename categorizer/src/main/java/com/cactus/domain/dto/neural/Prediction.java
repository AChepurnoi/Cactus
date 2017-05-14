package com.cactus.domain.dto.neural;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Sasha on 5/14/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prediction {
    private String crumbs;
    private String prob;
}
