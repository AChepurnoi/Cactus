package com.cactus.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

/**
 * Created by Sasha on 5/14/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckInput {
    private String title;
    private String description;
    private int price;
    private MultipartFile image;

}
