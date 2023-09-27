package org.launchcode.journal.controllers;

import org.launchcode.journal.models.ChatGptResponse;
import org.launchcode.journal.models.data.EntryRepository;
import org.launchcode.journal.models.ChatGptRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.util.Optional;

@Controller
public class ChatGptController {

    @Value("${openai.api.model}")
    private String model;

    @Value("${openai.api.url}")
    private String url;

    @Autowired
    private RestTemplate template;

    @Autowired
    public EntryRepository entryRepository;

    @RequestMapping(value = "/api/v1/ask", method = RequestMethod.POST)
    @ResponseBody
    public String askQuestion(@RequestParam(name = "entryId") Integer entryId) {

        System.out.println("did i get this far");


        Optional entry = entryRepository.findById(entryId);

        template = new RestTemplate();

        String prompt = "Please say hello";

        ChatGptRequest chatGptRequest = new ChatGptRequest(model, prompt);

        System.out.println(chatGptRequest);


        ChatGptResponse chatGPTResponse = template.postForObject(url, chatGptRequest, ChatGptResponse.class);

        System.out.println("did i get THIS far");

        return chatGPTResponse.getChoices().get(0).getMessage().toString();
    }

}
