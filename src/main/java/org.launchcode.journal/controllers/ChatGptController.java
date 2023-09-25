package org.launchcode.journal.controllers;

import org.launchcode.journal.models.ChatGptRequest;
import org.launchcode.journal.models.ChatGptResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1")
public class ChatGptController {

    @Value("${chatgpt.model}")
    private String model;

    @Value("${chatgpt.api.url}")
    private String apiUrl;

    @Value("${chatgpt.api.key}")
    private String apiKey;

    private static RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = "/ask", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String ask(@RequestParam String query) {
        ChatGptRequest request = new ChatGptRequest(model, query);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer" +apiKey);
        ChatGptResponse chatGptResponse = restTemplate.postForObject(apiUrl, new HttpEntity<>(request, headers), ChatGptResponse.class);
        return chatGptResponse.getChoices().get(0).getMessage().getContent();
    }
}
