package ch.bbw.recipe_finder_microservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Column(name = "description", length = 128)
    private String description;

    @Column(name = "recipe_url", length = 256)
    private String recipeUrl;

    // mc-mc with Preferences
    @ManyToMany
    @JoinTable(
        name = "preferences_recipe",
        joinColumns = @JoinColumn(name = "recipe_id"),
        inverseJoinColumns = @JoinColumn(name = "preferences_id")
    )
    private List<Preferences> preferences;

    // mc-mc with Ingredients
    @ManyToMany
    @JoinTable(
        name = "ingredients_recipe",
        joinColumns = @JoinColumn(name = "recipe_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredients_id")
    )
    private List<Ingredient> ingredients;
}