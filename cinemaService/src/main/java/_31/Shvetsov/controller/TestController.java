package _31.Shvetsov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final MongoTemplate mongoTemplate;

    @GetMapping("/test")
    public String test() {
        mongoTemplate.getCollectionNames();
        return "ok";
    }
}