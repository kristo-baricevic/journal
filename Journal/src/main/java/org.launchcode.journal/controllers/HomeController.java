package org.launchcode.journal.controllers;

import org.launchcode.journal.models.*;
import org.launchcode.journal.models.data.MoodRepository;
import org.launchcode.journal.models.data.TopicRepository;
import org.launchcode.journal.models.data.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Created by LaunchCode
 */
@Controller
public class HomeController {

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private MoodRepository moodRepository;

    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("entries", entryRepository.findAll());
        return "index";
    }

    @GetMapping("add")
    public String displayAddEntryForm(Model model) {
        model.addAttribute(new Entry());
        model.addAttribute("entries", entryRepository.findAll());
        model.addAttribute("topics", topicRepository.findAll());
        model.addAttribute("moods", moodRepository.findAll());

        return "add";
    }

    @PostMapping("add")
    public String processAddEntryForm(@ModelAttribute @Valid Entry newEntry,Model model,
                                        Errors errors, @RequestParam int topic_id) {

        if (errors.hasErrors()) {
            model.addAttribute(new Entry());
            model.addAttribute("entries", entryRepository.findAll());
            model.addAttribute("topics", topicRepository.findAll());
            model.addAttribute("moods", moodRepository.findAll());


            return "add";
        }
        Optional<Topic> topic = topicRepository.findById(topic_id);
        if (topic.isPresent()) {
            newEntry.setTopic(topic.get());
        }

        Optional<Mood> mood = moodRepository.findById(topic_id);
        if (mood.isPresent()) {
            newEntry.setMood(mood.get());
        }

        entryRepository.save(newEntry);
        return "redirect:";
    }

    @GetMapping("view/{id}")
    public String displayViewEntry(Model model, @PathVariable Integer id) {

        Optional<Entry> entry = entryRepository.findById(id);
        if (entry != null) {
            model.addAttribute("title", "Entry: "+ entry.get().getTitle());
            model.addAttribute("entry", entry.get());
            return "view";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("list-topics")
    public String displayEntryByTopic(Model model) {
        model.addAttribute(new Entry());
        model.addAttribute("entries", entryRepository.findAll());
        model.addAttribute("topics", topicRepository.findAll());
        model.addAttribute("moods", moodRepository.findAll());
        List<Topic> topics = topicRepository.findAll();
        List<Mood> moods = moodRepository.findAll();

        return "list-topics";
    }

    @GetMapping("list-moods")
    public String displayEntryByMood(Model model) {
        model.addAttribute(new Entry());
        model.addAttribute("entries", entryRepository.findAll());
        model.addAttribute("topics", topicRepository.findAll());
        model.addAttribute("moods", moodRepository.findAll());
        List<Topic> topics = topicRepository.findAll();
        List<Mood> moods = moodRepository.findAll();

        return "list-moods";
    }

}