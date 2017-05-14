package com.cactus.web;

import com.cactus.domain.dto.CheckInput;
import com.cactus.domain.dto.PredictorResponse;
import com.cactus.domain.dto.google.translate.TranslationResponse;
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
import java.util.Optional;
import java.util.stream.Stream;

import static org.springframework.http.ResponseEntity.*;

@RestController
@EnableWebMvc
public class MainController {

    private final GoogleTranslateService googleTranslate;
    private final GoogleVisionService googleVision;
    private final NeuralNetworkService neuralNet;
    private final String LANG_ENG = "en";


    @Autowired
    public MainController(GoogleTranslateService googleTranslate, GoogleVisionService googleVision, NeuralNetworkService neuralNet) {
        this.googleTranslate = googleTranslate;
        this.googleVision = googleVision;
        this.neuralNet = neuralNet;
    }

    @SneakyThrows
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public ResponseEntity index(CheckInput input) {

        TranslationResponse title = googleTranslate.translate(input.getTitle(), LANG_ENG).blockingGet();
        TranslationResponse desc = googleTranslate.translate(input.getDescription(), LANG_ENG).blockingGet();

        final String sourceLang = Stream.of(title, desc)
                .filter(TranslationResponse::notEnglish)
                .map(TranslationResponse::getDetectedSourceLanguage)
                .findAny().orElse(LANG_ENG);


        Single<List<Prediction>> predicitons = neuralNet
                .predict(title.getTranslatedText(), desc.getTranslatedText(),
                        input.getPrice(), input.getImage())
                .map(data -> translate(data, sourceLang)).toList();

        Single<List<String>> labels = googleVision.labels(input.getImage())
                .map(label -> googleTranslate.translate(label, sourceLang))
                .map(Single::blockingGet)
                .map(TranslationResponse::getTranslatedText)
                .toList();

        return ok(new PredictorResponse(predicitons.blockingGet(), labels.blockingGet()));
    }

    private Prediction translate(Prediction prediction, String target) {
        String translated = googleTranslate.translate(prediction.getCrumbs(), target)
                .blockingGet().getTranslatedText().replaceAll("&gt;",">");
        return new Prediction(translated, prediction.getProb());

    }
}
