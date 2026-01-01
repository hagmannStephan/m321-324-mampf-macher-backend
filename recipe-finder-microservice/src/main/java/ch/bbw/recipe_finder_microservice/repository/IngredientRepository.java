package ch.bbw.recipe_finder_microservice.repository;

import ch.bbw.recipe_finder_microservice.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    List<Ingredient> findAll();
}