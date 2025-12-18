package ch.bbw.recipe_finder_microservice.delegate;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.bbw.recipe_finder_microservice.entity.*;
import ch.bbw.recipe_finder_microservice.repository.*;


@RestController
public class RecipeFinderDelegate {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private PreferencesRepository preferencesRepository;

    @GetMapping("/hello")
    public String hello() {
        return "Hello from recipe finder delegate!";
    }

    @GetMapping("/ingredients")
    public List<String> getIngredients() {
        return ingredientRepository.findAll().stream()
                .map(Ingredient::getName)
                .collect(Collectors.toList());
    }

    @GetMapping("/preferences")
    public List<String> getPreferences() {
        return preferencesRepository.findAll().stream()
                .map(Preferences::getName)
                .collect(Collectors.toList());
    }
}
