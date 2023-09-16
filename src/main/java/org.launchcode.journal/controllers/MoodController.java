package org.launchcode.journal.controllers;

import org.launchcode.journal.models.*;
import org.launchcode.journal.models.data.MoodRepository;
import org.launchcode.journal.models.data.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("moods")

public class MoodController {
    @Autowired
    private MoodService moodService;
    @Autowired
    private MoodRepository moodRepository;
    @Autowired
    private EntryRepository productRepository;

    @GetMapping("add")
    public String addMood(Model model){
        model.addAttribute("mood", new Mood());
        model.addAttribute("pageTitle","Add mood");
        model.addAttribute("name","Add mood");
        model.addAttribute("meaning","Add mood");
        return "moods/add";
    }

    @GetMapping("list")
    public String listMood(Model model){
        List<Mood> listMood = moodService.listMood();
        model.addAttribute("listMood",listMood);
        return "moods/list";
    }

    @PostMapping("add")
    public String saveMood(Mood mood){
        System.out.println(mood);
        moodService.save(mood);
        return "redirect:/moods/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id")Integer id, @ModelAttribute("mood") Mood mood, Model model, RedirectAttributes ra){
        try {
            mood = moodRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("Mood not found with id: " + id));
            model.addAttribute("mood", mood);
            model.addAttribute("pageTitle","Edit mood");
            return "moods/edit";
        } catch (UserNotFoundException e) {
            ra.addAttribute("message","mood added successfully");
            return "redirect:/moods/list";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateMood(@PathVariable("id") Integer id, @ModelAttribute("mood") @Valid Mood mood, BindingResult bindingResult, RedirectAttributes ra) throws UserNotFoundException {
        if(bindingResult.hasErrors()){
            return "moods/edit";
        }
        Mood existingMood = moodRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Mood not found with id: " + id));
        existingMood.setName(mood.getName());
        moodRepository.saveAndFlush(existingMood);
        ra.addFlashAttribute("message", "Mood updated successfully");
        return "redirect:/moods/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteMood(@PathVariable("id")Integer id, Model model, RedirectAttributes ra){
        try {
            moodService.deleteMood(id);
        } catch (UserNotFoundException e) {
            ra.addAttribute("message","mood added successfully");

        }
        return "redirect:/moods/list";
    }

    @PostMapping("/delete")
    public String deleteMood(@RequestParam("id")Integer id, RedirectAttributes ra){
        try {
            moodService.deleteMood(id);
            ra.addFlashAttribute("message", "Mood deleted successfully");
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/moods/list";
    }

    @GetMapping("show/{id}")
    public String displayViewEntry(Model model, @PathVariable Integer id) {

        Optional<Mood> mood = moodService.findByMoodId(id);
        if (mood != null) {
            model.addAttribute("name", "Mood: "+ mood.get().getName());
            model.addAttribute("meaning", "Mood: "+ mood.get().getMeaning());
            model.addAttribute("mood", mood.get());
            return "moods/show";
        } else {
            return "redirect:/";
        }
    }
}