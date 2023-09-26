package org.launchcode.journal.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class ChatGptController {

    @RequestMapping(value = "/api/v1/ask", method = RequestMethod.POST)
    @ResponseBody
    public String askQuestion(@RequestParam String userQuestion) {
        // Define the API URL
        String apiUrl = "https://api.openai.com/v1/chat/completions";

        // Set your OpenAI API key
        String apiKey = "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";

        // Create an instance of RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Create headers with the Content-Type and Authorization
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        // Create the request body with the user's question
        String requestBody = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"" + userQuestion + "\"}]}";

        // Create an HttpEntity with headers and the request body
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // Send the POST request and retrieve the response
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);

        // Extract and return the response body
        String responseBody = response.getBody();

        return responseBody;
    }
}
