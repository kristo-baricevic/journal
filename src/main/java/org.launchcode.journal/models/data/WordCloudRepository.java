package org.launchcode.journal.models.data;

import org.launchcode.journal.models.WordCloud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordCloudRepository extends JpaRepository<WordCloud, Integer> {

    List<WordCloud> findAll();
    Optional<WordCloud> findById(Integer id);

}
