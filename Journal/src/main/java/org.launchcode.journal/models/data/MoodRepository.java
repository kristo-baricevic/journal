package org.launchcode.journal.models.data;

import org.launchcode.journal.models.Mood;
import org.launchcode.journal.models.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MoodRepository extends JpaRepository<Mood,Integer> {
    public long countById(Integer id);
    public Optional<Mood> findById (Integer id);
}