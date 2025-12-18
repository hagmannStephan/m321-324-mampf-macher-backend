package ch.bbw.recipe_finder_microservice.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.bbw.recipe_finder_microservice.entity.Ingredient;
import ch.bbw.recipe_finder_microservice.entity.Recipe;
import ch.bbw.recipe_finder_microservice.repository.RecipeRepository;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    public List<Recipe> findRecipes(
            List<String> ingredientNames,
            List<String> preferenceNames
    ) {

        List<Recipe> candidates =
            preferenceNames.isEmpty()
                ? recipeRepository.findAll()
                : recipeRepository.findByAllPreferences(
                    preferenceNames,
                    preferenceNames.size()
                );

        return candidates.stream()
            .map(recipe -> Map.entry(
                recipe,
                countMatchingIngredients(recipe, ingredientNames)
            ))
            // at least one ingredient match
            .filter(entry -> entry.getValue() > 0)
            .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
            .map(Map.Entry::getKey)
            .toList();
    }

    private int countMatchingIngredients(Recipe recipe, List<String> ingredientNames) {
        return (int) recipe.getIngredients().stream()
            .map(Ingredient::getName)
            .filter(ingredientNames::contains)
            .count();
    }
}