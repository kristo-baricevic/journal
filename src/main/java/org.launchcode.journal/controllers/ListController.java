package org.launchcode.journal.controllers;

import org.launchcode.journal.models.Mood;
import org.launchcode.journal.models.Topic;
import org.launchcode.journal.models.Entry;
import org.launchcode.journal.models.data.MoodRepository;
import org.launchcode.journal.models.data.TopicRepository;
import org.launchcode.journal.models.data.EntryRepository;
import org.launchcode.journal.models.EntryData;
//import org.launchcode.journal.models.data.DescriptionRepository;
import org.launchcode.journal.models.data.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "list")
public class ListController {

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private MoodRepository moodRepository;

    static HashMap<String, String> columnChoices = new HashMap<>();


    public ListController (EntryRepository entryRepository, TopicRepository topicRepository, MoodRepository moodRepository) {
        columnChoices.put("all", "All");
        columnChoices.put("topic", "Topic");
        columnChoices.put("mood", "Mood");
        this.entryRepository = entryRepository;
        this.topicRepository = topicRepository;
        this.moodRepository = moodRepository;
    }

    @RequestMapping("")
    public String list(Model model) {
        model.addAttribute("entries", entryRepository.findAll());
        model.addAttribute("topics", topicRepository.findAll());
        model.addAttribute("moods", moodRepository.findAll());

        return "list";
    }

    @RequestMapping(value = "entry/{title}")
    public String listEntriesByColumnAndValue(Model model, @RequestParam String column, @RequestParam String value) {
        Iterable<Entry> entries;
        if (column.equals("all")){
            entries = EntryData.findByColumnAndValue(column, value, entryRepository.findAll());
            model.addAttribute("title", "All Entries");
        } else {
            entries = EntryData.findByColumnAndValue(column, value, entryRepository.findAll());
            model.addAttribute("title", "Entries with " + columnChoices.get(column) + ": " + value);
        }
        List<Entry> entriesList = (List<Entry>) entries;
        System.out.println("Number of entries retrieved: " + entriesList.size());
        model.addAttribute("entries", entries);

        return "journal_entries/list";
    }

    @RequestMapping(value = "topic/{name}", produces = "application/json")
    public String listEntriesByTopic(Model model, @PathVariable String name) {
        Iterable<Topic> entries = entryRepository.findByTopicName(name);
        model.addAttribute("entries", entries);
        model.addAttribute("title", "Entries in topic: " + name);
        return "topics/list";
    }

    @RequestMapping(value = "mood/{name}")
    public String listEntriesByMood(Model model, @PathVariable String name) {
        Iterable<Mood> entries = entryRepository.findByMoodName(name);
        model.addAttribute("entries", entries);
        model.addAttribute("title", "Entries in mood: " + name);
        return "moods/list";
    }


}