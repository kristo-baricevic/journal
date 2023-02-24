package org.launchcode.journal.controllers;

import org.launchcode.journal.models.Topic;
import org.launchcode.journal.models.TopicService;
import org.launchcode.journal.models.UserNotFoundException;
import org.launchcode.journal.models.data.TopicRepository;
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
@RequestMapping("topics")

public class TopicController {
    @Autowired
    private TopicService topicService;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private EntryRepository entryRepository;

    @GetMapping("add")
    public String addTopic(Model model){
        model.addAttribute("topic", new Topic());
        model.addAttribute("pageTitle","Add topic");
        model.addAttribute("name","Add topic");
        return "topics/add";

    }
    @GetMapping("list")
    public String listTopic(Model model){
        List<Topic> listTopic = topicService.listTopic();
        model.addAttribute("listTopic",listTopic);

        return "topics/list";
    }

    @PostMapping("add")
    public String saveTopic(Topic topic){
        System.out.println(topic);
        topicService.save(topic);
        return "redirect:/topics/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id")Integer id, @ModelAttribute("topic") Topic topic, Model model, RedirectAttributes ra){
        try {
            topic = topicRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("Topic not found with id: " + id));
            model.addAttribute("topic", topic);
            model.addAttribute("pageTitle","Edit topic");
            return "topics/edit";
        } catch (UserNotFoundException e) {
            ra.addAttribute("message","topic added successfully");
            return "redirect:/topics/list";
        }
    }



    @PostMapping("/edit/{id}")
    public String updateTopic(@PathVariable("id") Integer id, @ModelAttribute("topic") @Valid Topic topic, BindingResult bindingResult, RedirectAttributes ra) throws UserNotFoundException {
        if(bindingResult.hasErrors()){
            return "topics/edit";
        }
        Topic existingTopic = topicRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Topic not found with id: " + id));
        existingTopic.setName(topic.getName());
        topicRepository.saveAndFlush(existingTopic);
        ra.addFlashAttribute("message", "Topic updated successfully");
        return "redirect:/topics/list";
    }


    @GetMapping("/delete/{id}")
    public String deleteTopic(@PathVariable("id")Integer id, Model model, RedirectAttributes ra){
        try {
            topicService.deleteTopic(id);
        } catch (UserNotFoundException e) {
            ra.addAttribute("message","topic added successfully");

        }
        return "redirect:/topics/list";
    }

    @PostMapping("/delete")
    public String deleteTopic(@RequestParam("id")Integer id, RedirectAttributes ra){
        try {
            topicService.deleteTopic(id);
            ra.addFlashAttribute("message", "Topic deleted successfully");
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/topics/list";
    }

    @GetMapping("show/{id}")
    public String displayViewEntry(Model model, @PathVariable Integer id) {

        Optional<Topic> topic = topicService.findByTopicId(id);
        if (topic != null) {
            model.addAttribute("name", "Topic: "+ topic.get().getName());
            model.addAttribute("topic", topic.get());
            return "topics/show";
        } else {
            return "redirect:/";
        }
    }
}