package ch.bbw.store_checkout_microservice.delegate;

import ch.bbw.store_checkout_microservice.client.ItemsClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/checkout")
public class StoreCheckoutDelegate {

    private final ItemsClient itemsClient;

    public StoreCheckoutDelegate(ItemsClient itemsClient) {
        this.itemsClient = itemsClient;
    }

    // GET: simple health / test
    @GetMapping
    public String hello() {
        return "Hello from storeCheckout!";
    }

    // GET: optional "preview items in cart" endpoint (pulls from store-items)
    @GetMapping("/preview")
    public CheckoutPreviewResponse preview(@RequestParam List<Long> ids,
                                          @RequestParam List<Integer> qty) {
        if (ids.size() != qty.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ids and qty must have same length");
        }

        List<CartLine> cart = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            cart.add(new CartLine(ids.get(i), qty.get(i)));
        }
        return buildPreview(cart);
    }

    // POST: accept cart, return a preview receipt (no real ordering/persistence)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CheckoutPreviewResponse checkout(@RequestBody CheckoutRequest req) {
        if (req.items() == null || req.items().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "items is required");
        }
        return buildPreview(req.items());
    }

    private CheckoutPreviewResponse buildPreview(List<CartLine> cart) {
        List<Long> ids = cart.stream().map(CartLine::itemId).toList();
        List<ItemsClient.ItemDto> items = itemsClient.getItemsByIds(ids);

        Map<Long, ItemsClient.ItemDto> byId = items.stream()
                .collect(Collectors.toMap(ItemsClient.ItemDto::id, x -> x));

        List<CheckoutLine> lines = new ArrayList<>();
        double total = 0.0;

        for (CartLine c : cart) {
            if (c.quantity() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "quantity must be > 0");
            }

            ItemsClient.ItemDto item = byId.get(c.itemId());
            if (item == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found: " + c.itemId());
            }
            if (item.stock() == null || item.stock() < c.quantity()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Not enough stock for " + item.name() + " (have " + item.stock() + ")");
            }

            double lineTotal = item.price() * c.quantity();
            total += lineTotal;

            lines.add(new CheckoutLine(item.id(), item.name(), item.price(), c.quantity(), lineTotal));
        }

        return new CheckoutPreviewResponse(lines, total);
    }

    public record CartLine(Long itemId, int quantity) {}
    public record CheckoutRequest(String fullName, String email, String address, List<CartLine> items) {}

    public record CheckoutLine(Long itemId, String name, Double unitPrice, int quantity, double lineTotal) {}
    public record CheckoutPreviewResponse(List<CheckoutLine> lines, double total) {}
}
