package org.launchcode.journal.controllers;

import lombok.RequiredArgsConstructor;
import org.launchcode.journal.models.ChatGptResponse;
import org.launchcode.journal.models.data.EntryRepository;
import org.launchcode.journal.models.ChatGptRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;




@Controller
@RequestMapping("/api/v1/bot")
@RequiredArgsConstructor
public class ChatGptController {

    @Value("${openai.api.model}")
    private String model;

    @Value("${openai.api.url}")
    private String url;

    @Autowired
    private RestTemplate template;

    @Autowired
    public EntryRepository entryRepository;

    @RequestMapping(value = "/ask", method = RequestMethod.POST)
    @ResponseBody
    public String askQuestion(@RequestBody ChatGptRequest chatGptRequest) {
        System.out.println("Received request with prompt: " + chatGptRequest.getPrompt());


//        Optional entry = entryRepository.findById(entryId);

        template = new RestTemplate();


        ChatGptResponse chatGPTResponse = template.postForObject(url, chatGptRequest, ChatGptResponse.class);

        System.out.println("response received");

        return chatGPTResponse.getChoices().get(0).getMessage().toString();
    }

}
