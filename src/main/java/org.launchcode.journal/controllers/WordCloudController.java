package org.launchcode.journal.controllers;


import java.awt.*;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.palette.ColorPalette;

import java.io.IOException;


import org.launchcode.journal.models.Entry;
import org.launchcode.journal.models.data.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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


    @RequestMapping(value = "/wordcloud", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
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
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/static/stop_words.txt"))) {
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

        //save the file before passing it to the analyzer
        String filePath = "src/main/resources/static/wordcloud.txt"; // Replace with the path where you want to save the file
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Generate word frequencies from the text
        ArrayList<WordFrequency> frequencies = (ArrayList<WordFrequency>) frequencyAnalyzer.load(filePath);

        System.out.println(frequencies);

        // Create a WordCloud object
        WordCloud wordCloud = new WordCloud(new Dimension(600, 400), CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setKumoFont(new KumoFont(new Font("Arial", Font.PLAIN, 20)));
        wordCloud.setBackground(new RectangleBackground(new Dimension(600, 400)));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 60));
        wordCloud.build(frequencies);

        // Get the generated image as a BufferedImage
        BufferedImage bufferedImage = wordCloud.getBufferedImage();

        File outputFile = new File("src/main/resources/static/wordcloud.png");
        ImageIO.write(bufferedImage, "png", outputFile);

        // Encode the image as a Base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        baos.close();
        baos.flush();
        byte[] imageBytes = baos.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        // Return encoded image as a Response Entity
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity<String>(base64Image, headers, HttpStatus.OK);
    }


    }

