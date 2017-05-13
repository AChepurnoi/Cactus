package com.cactus.web;

import com.cactus.service.GoogleTranslateService;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.*;

@RestController
public class MainController {

    private final GoogleTranslateService googleTranslate;

    @Autowired
    public MainController(GoogleTranslateService googleTranslate) {
        this.googleTranslate = googleTranslate;
    }

    @GetMapping("/")
    public ResponseEntity index(@RequestParam String text) {
        Single<String> translation = googleTranslate.translate(text);
        return ok(translation.blockingGet());
    }
}
