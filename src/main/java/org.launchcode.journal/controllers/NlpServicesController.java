package org.launchcode.journal.controllers;

import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.CoreSentence;
import org.launchcode.journal.models.*;
import org.launchcode.journal.models.AnalysisResult;
import org.launchcode.journal.models.data.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
                    case "getPhrases":
                        Map<String, List<String>> phraseMap = nlpService.getPhrases(entry.getJournalEntry());
                        List<String> verbPhrases = phraseMap.get("verbPhrases");
                        List<String> nounPhrases = phraseMap.get("nounPhrases");

                        System.out.println("Verb Phrases: " + verbPhrases);
                        System.out.println("Noun Phrases: " + nounPhrases);

                        analysisResults.add(new AnalysisResult(entry.getTitle(), "Verb Phrases: " + String.join(", ", verbPhrases) + "\nNoun Phrases: " + String.join(", ", nounPhrases), "getPhrases"));
                        break;
                    case "emotionAnalysis":
                        List<SentenceInfo> emotions = nlpService.getEmotions(entry.getJournalEntry());
                        StringBuilder sb2 = new StringBuilder();
                        for (SentenceInfo emotion : emotions) {
                            sb2.append(emotion.getText()).append(", ");
                        }
                        if (sb2.length() > 0) {
                            sb2.setLength(sb2.length() - 2); // remove trailing comma and space
                        }
                        result = sb2.toString();
                        analysisResults.add(new AnalysisResult(entry.getTitle(), result, "emotionAnalysis"));
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

