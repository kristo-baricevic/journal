package org.launchcode.journal.models;

import org.launchcode.journal.models.Entry;
import org.launchcode.journal.models.WordCloud;
import org.launchcode.journal.models.data.EntryRepository;
import org.launchcode.journal.models.data.WordCloudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WordCloudServices {

    @Autowired
    private WordCloudRepository wordCloudRepository;

    @Autowired
    private EntryRepository entryRepository;

    public List<WordCloud> getAllWordClouds() {
        return (List<WordCloud>) wordCloudRepository.findAll();
    }

    public WordCloud getWordCloudById(int id) {
        Optional<WordCloud> result = wordCloudRepository.findById(id);
        return result.orElse(null);
    }

    public void saveWordCloud(WordCloud wordCloud) {
        wordCloudRepository.save(wordCloud);
    }

    public void deleteWordCloud(WordCloud wordCloud) {
        wordCloudRepository.delete(wordCloud);
    }

    public void addEntryToWordCloud(WordCloud wordCloud, int entryId) {
        Optional<Entry> entryResult = entryRepository.findById(entryId);
        entryResult.ifPresent(wordCloud::addEntry);
        wordCloudRepository.save(wordCloud);
    }

    public void removeEntryFromWordCloud(WordCloud wordCloud, int entryId) {
        Optional<Entry> entryResult = entryRepository.findById(entryId);
        entryResult.ifPresent(wordCloud::removeEntry);
        wordCloudRepository.save(wordCloud);
    }

}

//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
