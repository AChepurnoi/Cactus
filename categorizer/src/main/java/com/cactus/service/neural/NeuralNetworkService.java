package com.cactus.service.neural;

import com.cactus.domain.dto.CheckInput;
import com.cactus.domain.dto.neural.Prediction;
import io.reactivex.Observable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by Sasha on 5/14/17.
 */
public interface NeuralNetworkService {
    Observable<Prediction> predict(String title, String description, int price, MultipartFile image);
}
