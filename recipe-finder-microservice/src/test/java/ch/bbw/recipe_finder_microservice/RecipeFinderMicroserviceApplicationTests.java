package ch.bbw.recipe_finder_microservice;

import ch.bbw.recipe_finder_microservice.entity.Ingredient;
import ch.bbw.recipe_finder_microservice.entity.Preferences;
import ch.bbw.recipe_finder_microservice.entity.Recipe;
import ch.bbw.recipe_finder_microservice.repository.RecipeRepository;
import ch.bbw.recipe_finder_microservice.service.RecipeService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    RecipeRepository recipeRepository;

    @InjectMocks
    RecipeService recipeService;

	@Test
		void contextLoads() {
	}

    @Test
    void recipes_are_filtered_by_preferences_and_sorted_by_ingredient_matches() {
        // ingredients
        Ingredient flour = new Ingredient();
		flour.setId(1L);
		flour.setName("flour");
        Ingredient sugar = new Ingredient();
		sugar.setId(2L);
		sugar.setName("sugar");
        Ingredient tofu = new Ingredient();
		tofu.setId(3L);
		tofu.setName("tofu");

        // preferences
        Preferences vegetarian = new Preferences(1L, "Vegetarian", List.of());

        // recipes
        Recipe cake = new Recipe(
            1L,
            "Cake",
            "desc",
            "url",
            List.of(vegetarian),
            List.of(flour, sugar)
        );

        Recipe tofuDish = new Recipe(
            2L,
            "Tofu Dish",
            "desc",
            "url",
            List.of(vegetarian),
            List.of(tofu)
        );

        when(recipeRepository.findByAllPreferences(
            List.of("Vegetarian"),
            1L
        )).thenReturn(List.of(cake, tofuDish));

        // execute
        List<Recipe> result = recipeService.findRecipes(
            List.of("flour", "sugar"),
            List.of("Vegetarian")
        );

        // assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Cake");
    }
}
