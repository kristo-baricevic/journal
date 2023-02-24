package org.launchcode.journal.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.launchcode.journal.models.data.MoodRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MoodService {
    @Autowired
    private MoodRepository moodRepository;
    public List<Mood> listMood(){
        return (List<Mood>) moodRepository.findAll();

    }

    public void save(Mood mood) {
        moodRepository.save(mood);
    }

    public Mood get(Integer id) throws org.launchcode.journal.models.UserNotFoundException {
        Optional<Mood> result = moodRepository.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new org.launchcode.journal.models.UserNotFoundException("Could not found user by ID");
    }


    public void deleteMood(Integer id) throws org.launchcode.journal.models.UserNotFoundException {

        Long count=moodRepository.countById(id);
        if( count == 0){
            throw new org.launchcode.journal.models.UserNotFoundException("Could not found user by ID");
        }
        moodRepository.deleteById(id);
    }

    public Optional<Mood> findByMoodId(Integer id) {
        return moodRepository.findById(id);
    }

    public void update(Mood mood){
        moodRepository.save(mood);
    }
}