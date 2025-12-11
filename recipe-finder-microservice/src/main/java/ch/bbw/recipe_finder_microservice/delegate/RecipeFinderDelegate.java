package ch.bbw.recipe_finder_microservice.delegate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecipeFinderDelegate {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from recipe finder delegate!";
    }
}
