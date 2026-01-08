package ch.bbw.store_items_microservice.delegate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import ch.bbw.store_items_microservice.repository.ItemsRepository;

@RestController
public class StoreItemsDelegate {
    
    @Autowired
    private ItemsRepository itemsRepository;

    @GetMapping("/hello")
    public String hello() {
        return "Hello from store items delegate!";
    }

    @GetMapping("/items")
    public List<ItemResponse> getItems() {
        return itemsRepository.findAll().stream()
                .map(i -> new ItemResponse(
                        i.getName(),
                        i.getPrice(),
                        i.getStock() > 0
                ))
                .toList();
    }

    public record ItemResponse(String name, Double price, boolean inStock) {}

}
