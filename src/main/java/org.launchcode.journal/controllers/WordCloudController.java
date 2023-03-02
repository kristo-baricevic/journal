package org.launchcode.journal.controllers;


import java.awt.*;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.palette.ColorPalette;

import java.io.IOException;


import org.launchcode.journal.models.Entry;
import org.launchcode.journal.models.EntryIds;
import org.launchcode.journal.models.data.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;

import static java.util.stream.Collectors.joining;

@Controller
public class
WordCloudController {

    @Autowired
    private EntryRepository entryRepository;

    @GetMapping("/wordcloud")
    public String displayWordCloud(Model model) {
        List<Entry> entries = entryRepository.findAll();
        model.addAttribute("entries", entries);
        return "wordcloud";
    }


    @PostMapping("/wordcloud")
    public ResponseEntity<String> generateWordCloud(@RequestBody ArrayList<Integer> entryIds, Model model) throws IOException, FontFormatException {
        System.out.println(entryIds);

        ArrayList<Entry> entries = entryRepository.findAllByIdIn(entryIds);

        // Concatenate journal entries into a single string
        String text = entries.stream().map(Entry::getJournalEntry).collect(joining(" "));


        // Create a FrequencyAnalyzer object
        FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(300);

        // Load stop words from a file
        Set<String> stopWords = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/static/stop_words.rtf"))) {
            String line;
            while ((line = br.readLine()) != null) {
                stopWords.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        frequencyAnalyzer.setStopWords(stopWords);

        System.out.println(entryIds);


        System.out.println("Entries list:");
        for (Entry entry : entries) {
            System.out.println(entry);
        }

        for (Entry entry : entries) {
            System.out.println(entry.getJournalEntry());
        }

        // Generate word frequencies from the text
        ArrayList<WordFrequency> frequencies = (ArrayList<WordFrequency>) frequencyAnalyzer.load(text);

        // Create a WordCloud object
        WordCloud wordCloud = new WordCloud(new Dimension(600, 400), CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new CircleBackground(200));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 60));
        wordCloud.build(frequencies);

        // Get the generated image as a BufferedImage
        BufferedImage bufferedImage = wordCloud.getBufferedImage();

        // Encode the image as a Base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        baos.flush();
        byte[] imageBytes = baos.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        baos.close();

        // Return the Base64-encoded image as a response
        return ResponseEntity.ok().body("{\"image\": \"" + base64Image + "\"}");
    }

//        // Sort frequencies by count in descending order
//        frequencies.sort(Comparator.comparingInt(WordFrequency::getFrequency).reversed());
//
//        // Pass word frequencies to view
//        model.addAttribute("frequencies", frequencies);

//        return "wordcloud-generated";
    }

