package ch.bbw.recipe_finder_microservice.repository;

import ch.bbw.recipe_finder_microservice.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Optional<Recipe> findById(Long id);

    @Query("""
                SELECT DISTINCT r
                FROM Recipe r
                JOIN r.preferences p
                WHERE p.name IN :preferences
                GROUP BY r
                HAVING COUNT(DISTINCT p.name) = :prefCount
            """)
    List<Recipe> findByAllPreferences(
            @Param("preferences") List<String> preferences,
            @Param("prefCount") long prefCount);
}
