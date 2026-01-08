package ch.bbw.store_items_microservice.delegate;

import ch.bbw.store_items_microservice.entity.Items;
import ch.bbw.store_items_microservice.repository.ItemsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/items")
public class StoreItemsDelegate {

    private final ItemsRepository itemsRepository;

    public StoreItemsDelegate(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    // ---- GET ----

    @GetMapping
    public List<ItemResponse> getAllItems() {
        return itemsRepository.findAll().stream()
                .map(ItemResponse::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public ItemResponse getItemById(@PathVariable Long id) {
        Items item = itemsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found: " + id));
        return ItemResponse.fromEntity(item);
    }

    @GetMapping("/by-ids")
    public List<ItemResponse> getItemsByIds(@RequestParam List<Long> ids) {
        return itemsRepository.findAllById(ids).stream()
                .map(ItemResponse::fromEntity)
                .toList();
    }

    // ---- POST ----

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponse createItem(@RequestBody ItemUpsertRequest req) {
        if (req.name() == null || req.name().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        }
        if (req.price() == null || req.price() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "price must be >= 0");
        }
        if (req.stock() == null || req.stock() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "stock must be >= 0");
        }

        if (itemsRepository.existsByNameIgnoreCase(req.name())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Item already exists: " + req.name());
        }

        Items item = new Items(null, req.name().trim(), req.price(), req.stock());
        return ItemResponse.fromEntity(itemsRepository.save(item));
    }

    /**
     * Accept ingredient names (e.g. from recipe_finder) and create items if missing.
     * This is the "accept ingredients from microservice 3 and put them in store_items_schema".
     */
    @PostMapping("/import")
    public List<ItemResponse> importIngredients(@RequestBody ImportIngredientsRequest req) {
        double defaultPrice = req.defaultPrice() == null ? 0.0 : req.defaultPrice();
        int defaultStock = req.defaultStock() == null ? 0 : req.defaultStock();

        if (req.ingredients() == null || req.ingredients().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ingredients list is required");
        }
        if (defaultPrice < 0 || defaultStock < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "defaultPrice/defaultStock must be >= 0");
        }

        for (String rawName : req.ingredients()) {
            if (rawName == null) continue;
            String name = rawName.trim();
            if (name.isEmpty()) continue;

            String normalized = name.toLowerCase(Locale.ROOT);

            itemsRepository.findByNameIgnoreCase(normalized).orElseGet(() ->
                    itemsRepository.save(new Items(null, normalized, defaultPrice, defaultStock))
            );
        }

        return itemsRepository.findAll().stream()
                .map(ItemResponse::fromEntity)
                .toList();
    }

    // ---- PUT ----

    @PutMapping("/{id}")
    public ItemResponse updateItem(@PathVariable Long id, @RequestBody ItemUpsertRequest req) {
        Items item = itemsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found: " + id));

        if (req.name() != null && !req.name().isBlank()) {
            item.setName(req.name().trim());
        }
        if (req.price() != null) {
            if (req.price() < 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "price must be >= 0");
            item.setPrice(req.price());
        }
        if (req.stock() != null) {
            if (req.stock() < 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "stock must be >= 0");
            item.setStock(req.stock());
        }

        return ItemResponse.fromEntity(itemsRepository.save(item));
    }

    // ---- DELETE ----

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable Long id) {
        if (!itemsRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found: " + id);
        }
        itemsRepository.deleteById(id);
    }

    // ---- DTOs ----

    public record ItemUpsertRequest(String name, Double price, Integer stock) {}

    public record ImportIngredientsRequest(List<String> ingredients, Double defaultPrice, Integer defaultStock) {}

    public record ItemResponse(Long id, String name, Double price, Integer stock, boolean inStock) {
        static ItemResponse fromEntity(Items i) {
            return new ItemResponse(i.getId(), i.getName(), i.getPrice(), i.getStock(), i.getStock() != null && i.getStock() > 0);
        }
    }
}
