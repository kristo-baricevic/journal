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

            List<AnalysisResult> analysisResults = new ArrayList<>();

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
                             break;
                        case "namedEntities":
                            List<CoreEntityMention> entities = nlpService.getNamedEntities(entry.getJournalEntry());
                            StringBuilder sb = new StringBuilder();
                            for (CoreEntityMention entity : entities) {
                                sb.append(entity.text()).append(", ");
                            }
                            if (sb.length() > 0) {
                                sb.setLength(sb.length() - 2); // remove trailing comma and space
                            }
                            result = sb.toString();
                            break;
                        case "keyPhrases":
                            List<String> keyPhrases = nlpService.getKeyPhrases(entry.getJournalEntry());
                            result = String.join(", ", keyPhrases);
                            break;
                        case "emotionAnalysis":
                            List<CoreSentence> emotions = nlpService.getEmotions(entry.getJournalEntry());
                            StringBuilder sb2 = new StringBuilder();
                            for (CoreSentence emotion : emotions) {
                                sb2.append(emotion.text()).append(", ");
                            }
                            if (sb2.length() > 0) {
                                sb2.setLength(sb2.length() - 2); // remove trailing comma and space
                            }
                            result = sb2.toString();
                            break;
                        default:
                            // invalid analysis type
                            result = "Invalid analysis type";
                            break;
                }

                AnalysisResult analysisResult = new AnalysisResult(entry.getTitle(), result);
                analysisResults.add(analysisResult);
            }
            return ResponseEntity.ok(analysisResults);
    }
}}

