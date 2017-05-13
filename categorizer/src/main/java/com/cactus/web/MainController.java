package com.cactus.web;

import com.cactus.domain.dto.CheckInput;
import com.cactus.service.GoogleTranslateService;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static java.util.Arrays.asList;
import static org.springframework.http.ResponseEntity.*;

@RestController
@EnableWebMvc
public class MainController {

    private final GoogleTranslateService googleTranslate;

    @Autowired
    public MainController(GoogleTranslateService googleTranslate) {
        this.googleTranslate = googleTranslate;
    }

    @PostMapping(value = "/check")
    public ResponseEntity index(CheckInput input) {

        String title = googleTranslate.translate(input.getTitle()).blockingGet();
        String desc = googleTranslate.translate(input.getDescription()).blockingGet();


        return ok(asList(title, desc));
    }
}
