package ch.bbw.recipe_finder_microservice.delegate;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.bbw.recipe_finder_microservice.entity.Ingredient;
import ch.bbw.recipe_finder_microservice.repository.IngredientRepository;

@RestController
public class RecipeFinderDelegate {

    @Autowired
    private IngredientRepository ingredientRepository;

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
}
