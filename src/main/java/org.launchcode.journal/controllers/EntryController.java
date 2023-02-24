package org.launchcode.journal.controllers;

import org.launchcode.journal.models.*;
import org.launchcode.journal.models.data.EntryRepository;
import org.launchcode.journal.models.data.MoodRepository;
import org.launchcode.journal.models.data.TopicRepository;
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


@Controller
@RequestMapping("entries")
public class EntryController {
    @Autowired
    private EntryService entryService;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private MoodRepository moodRepository;
    @Autowired
    private EntryRepository entryRepository;

    @GetMapping("add")
    public String addEntry(Model model){
        model.addAttribute("entry", new Entry());
        model.addAttribute("pageTitle","Add entry");
        model.addAttribute("entries", entryRepository.findAll());
        model.addAttribute("topics", topicRepository.findAll());
        model.addAttribute("moods", moodRepository.findAll());
        return "entries/add";
    }


    @GetMapping("list")
    public String listEntry(Model model){
        List<Entry> listEntry = entryService.listEntry();
        model.addAttribute("listEntry",listEntry);
        return "entries/list";
    }

    @PostMapping("add")
    public String processAddEntryForm(@ModelAttribute @Valid Entry newEntry, Model model,
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

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id")Integer id, @ModelAttribute("entry") Entry entry, Model model, RedirectAttributes ra){
        try {
            entry = entryRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("Entry not found with id: " + id));
            System.out.println(entry);
            model.addAttribute("entry", entry);
            model.addAttribute("pageTitle","Edit entry");
            return "entries/edit";
        } catch (UserNotFoundException e) {
            ra.addAttribute("message","entry added successfully");
            return "redirect:/entries/list";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateEntry(@PathVariable("id") Integer id, @ModelAttribute("entry") @Valid Entry entry, BindingResult bindingResult, RedirectAttributes ra) throws UserNotFoundException {
        if(bindingResult.hasErrors()){
            return "entries/edit";
        }
        Entry existingEntry = entryRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Entry not found with id: " + id));
        existingEntry.setTitle(entry.getTitle());
        existingEntry.setJournalEntry(entry.getJournalEntry());
        entryRepository.saveAndFlush(existingEntry);
        ra.addFlashAttribute("message", "Entry updated successfully");
        return "redirect:/entries/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteMood(@PathVariable("id")Integer id, Model model, RedirectAttributes ra){
        try {
            entryService.deleteEntry(id);
        } catch (UserNotFoundException e) {
            ra.addAttribute("message","entry added successfully");

        }
        return "redirect:/entries/list";
    }

    @PostMapping("/delete")
    public String deleteMood(@RequestParam("id")Integer id, RedirectAttributes ra){
        try {
            entryService.deleteEntry(id);
            ra.addFlashAttribute("message", "Entry deleted successfully");
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/entries/list";
    }

    @GetMapping("show/{id}")
    public String displayViewEntry(Model model, @PathVariable Integer id) {

        Optional<Entry> entry = entryService.findByEntryId(id);
        System.out.println(entry);
        if (entry != null) {
            model.addAttribute("title", "Entry: "+ entry.get().getTitle());
            model.addAttribute("entry", entry.get());
            return "entries/show";
        } else {
            return "redirect:/";
        }
    }

}
