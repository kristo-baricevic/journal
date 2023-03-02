package org.launchcode.journal.models.data;

import org.launchcode.journal.models.Mood;
import org.launchcode.journal.models.Topic;
import org.launchcode.journal.models.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface EntryRepository extends JpaRepository<Entry, Integer> {
//    Iterable<Entry> findAll();
    public long countById(Integer id);
    public Optional<Entry> findById (Integer id);
    public ArrayList<Entry> findAllByIdIn(ArrayList<Integer> ids);
    Iterable<Topic> findByTopicName(String name);
    Iterable<Mood> findByMoodName(String name);
}
