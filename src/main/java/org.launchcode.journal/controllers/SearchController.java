package org.launchcode.journal.controllers;

import org.launchcode.journal.models.Entry;
import org.launchcode.journal.models.data.EntryRepository;
import org.launchcode.journal.models.Entry;
import org.launchcode.journal.models.EntryData;
import org.launchcode.journal.models.data.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import static org.launchcode.journal.controllers.ListController.columnChoices;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping("index")
public class SearchController {

    @Autowired
    private EntryRepository entryRepository;

    @RequestMapping("")
    public String search(Model model) {
        model.addAttribute("columns", columnChoices);
        return "index";
    }

    //removed

    @PostMapping("results")
    public String displaySearchResults(Model model,  @RequestParam String searchType, @RequestParam String searchTerm){
        Iterable<Entry> Entries;
        if (searchTerm.toLowerCase().equals("all") || searchTerm.equals("")){
            Entries = entryRepository.findAll();
        } else {

            Entries = EntryData.findByColumnAndValue(searchType, searchTerm, entryRepository.findAll());
        }
        model.addAttribute("columns", columnChoices);
        model.addAttribute("title", "Entry with " + columnChoices.get(searchType) + ": " + searchTerm);
        model.addAttribute("entries", Entries);

        return "index";
        //changed return search to index
    }
}