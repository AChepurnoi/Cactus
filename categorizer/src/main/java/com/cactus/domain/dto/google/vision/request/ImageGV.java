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
public class ImageGV {
    private byte[] content;
}
