package org.launchcode.journal.models.data;

import org.launchcode.journal.models.Topic;
import org.launchcode.journal.models.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic,Integer> {
    public long countById(Integer id);
    public Optional<Topic> findById (Integer id);
}