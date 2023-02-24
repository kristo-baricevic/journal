package org.launchcode.journal.models;

import org.launchcode.journal.models.data.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicService {
    @Autowired
    private TopicRepository topicRepository;
    public List<Topic> listTopic(){
        return (List<Topic>) topicRepository.findAll();

    }

    public void save(Topic topic) {
        topicRepository.save(topic);
    }

    public Topic get(Integer id) throws org.launchcode.journal.models.UserNotFoundException {
        Optional<Topic> result = topicRepository.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new org.launchcode.journal.models.UserNotFoundException("Could not found user by ID");
    }


    public void deleteTopic(Integer id) throws org.launchcode.journal.models.UserNotFoundException {

        Long count=topicRepository.countById(id);
        if( count == 0){
            throw new org.launchcode.journal.models.UserNotFoundException("Could not found user by ID");
        }
        topicRepository.deleteById(id);
    }

    public Optional<Topic> findByTopicId(Integer id) {
        return topicRepository.findById(id);
    }

    public void update(Topic topic){
        topicRepository.save(topic);
    }
}