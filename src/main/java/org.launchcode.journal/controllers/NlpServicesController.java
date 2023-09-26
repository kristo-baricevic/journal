package org.launchcode.journal.controllers;

import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.CoreSentence;
import org.launchcode.journal.models.*;
import org.launchcode.journal.models.AnalysisResult;
import org.launchcode.journal.models.data.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class NlpServicesController {

    @Autowired
    private NlpService nlpService;

    @Autowired
    public EntryRepository entryRepository;

    @Autowired
    public EntryService entryService;

    @GetMapping("/analysis")
    public String displayAnalysis(Model model) {
        List<Entry> entries = entryRepository.findAll();
        model.addAttribute("entries", entries);
        return "analysis";
    }


    @RequestMapping(value = "/analysis", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AnalysisResult>> analyzeEntries(@RequestBody AnalysisRequest analysisRequest) {
        {

            ArrayList<AnalysisResult> analysisResults = new ArrayList<>();

            ArrayList<Integer> entryIds = analysisRequest.getEntryIds();
            String analysisType = analysisRequest.getAnalysisType();

            // retrieve entries based on the selected IDs
            ArrayList<Entry> entries = entryRepository.findAllByIdIn(entryIds);

            System.out.println(entries);

            // perform analysis based on the selected analysis type
            for (Entry entry : entries) {
                String result;
                switch (analysisType) {
                    case "interpretation":
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
                        String requestBody = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"" + "Please interpret the following dream: " + entry + "\"}]}";

                        // Create an HttpEntity with headers and the request body
                        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);

                        // Send the POST request and retrieve the response
                        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, httpEntity, String.class);
                        String responseBody = response.getBody();
                        analysisResults.add(new AnalysisResult(entry.getTitle(), responseBody.toString(), "interpretation"));
                        break;
                    case "sentimentAnalysis":
                        result = nlpService.getSentiment(entry.getJournalEntry());
                        analysisResults.add(new AnalysisResult(entry.getTitle(), result, "sentimentAnalysis"));
                        break;
                    case "namedEntities":
                        Map<String, List<String>> namedEntitiesMap = nlpService.getNamedEntities(entry.getJournalEntry());
                        StringBuilder sb = new StringBuilder();
                        for (Map.Entry<String, List<String>> mappedEntry : namedEntitiesMap.entrySet()) {
                            String category = mappedEntry.getKey();
                            List<String> entities = mappedEntry.getValue();
                            sb.append("<br>").append(category).append(": ");
                            for (String entity : entities) {
                                sb.append(" ").append(entity);
                            }
                        }
                        result = sb.toString();
                        analysisResults.add(new AnalysisResult(entry.getTitle(), result, "namedEntities"));
                        break;

                    case "keyPhrases":
                        List<String> keyPhrases = nlpService.getKeyPhrases(entry.getJournalEntry());
                        System.out.println(keyPhrases);
                        result = String.join(", ", keyPhrases);
                        analysisResults.add(new AnalysisResult(entry.getTitle(), result, "keyPhrases"));
                        break;
                    default:
                        // invalid analysis type
                        result = "Invalid analysis type";
                        analysisResults.add(new AnalysisResult(entry.getTitle(), result, "unknown"));
                        break;
                }
//
//                AnalysisResult analysisResult = new AnalysisResult(entry.getTitle(), result, analysisType);
//                analysisResults.add(analysisResult);
            }
            return ResponseEntity.ok(analysisResults);
    }
}}

