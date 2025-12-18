package ch.bbw.recipe_finder_microservice.repository;

import ch.bbw.recipe_finder_microservice.entity.Preferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferencesRepository extends JpaRepository<Preferences, Long> {

    List<Preferences> findAll();
}