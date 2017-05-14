package com.cactus.web;

import com.cactus.domain.dto.CheckInput;
import com.cactus.domain.dto.PredictorResponse;
import com.cactus.domain.dto.neural.Prediction;
import com.cactus.service.google.GoogleTranslateService;
import com.cactus.service.google.GoogleVisionService;
import com.cactus.service.neural.NeuralNetworkService;
import io.reactivex.Observable;
import io.reactivex.Single;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
@EnableWebMvc
public class MainController {

    private final GoogleTranslateService googleTranslate;
    private final GoogleVisionService googleVision;
    private final NeuralNetworkService neuralNet;

    @Autowired
    public MainController(GoogleTranslateService googleTranslate, GoogleVisionService googleVision, NeuralNetworkService neuralNet) {
        this.googleTranslate = googleTranslate;
        this.googleVision = googleVision;
        this.neuralNet = neuralNet;
    }

    @SneakyThrows
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public ResponseEntity index(CheckInput input) {
        String title = googleTranslate.translate(input.getTitle()).blockingGet();
        String desc = googleTranslate.translate(input.getDescription()).blockingGet();

        Single<List<Prediction>> predicitons = neuralNet
                .predict(title, desc, input.getPrice(), input.getImage()).toList();

        Single<List<String>> labels = googleVision.labels(input.getImage()).toList();

        return ok(new PredictorResponse(predicitons.blockingGet(), labels.blockingGet()));
    }
}
