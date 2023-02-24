package org.launchcode.journal.models;

import org.launchcode.journal.models.data.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EntryService {
    @Autowired
    private EntryRepository entryRepository;

    public List<Entry> listEntry(){
        return (List<Entry>) entryRepository.findAll();

    }

    public void save(Entry entry) {
        entryRepository.save(entry);
    }

    public Entry get(Integer id) throws UserNotFoundException {
        Optional<Entry> result = entryRepository.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new UserNotFoundException("Could not found user by ID");
    }


    public void deleteEntry(Integer id) throws UserNotFoundException {

        Long count=entryRepository.countById(id);
        if( count == 0){
            throw new UserNotFoundException("Could not found user by ID");
        }
        entryRepository.deleteById(id);
    }

    public Optional<Entry> findByEntryId(Integer id) {
        return entryRepository.findById(id);
    }

    public void update(Entry entry){
        entryRepository.save(entry);
    }
}