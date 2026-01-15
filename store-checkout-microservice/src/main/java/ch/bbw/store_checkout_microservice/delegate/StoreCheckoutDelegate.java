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
        System.out.println("Checkout requested for " + req.fullName() + " (" + req.email() + ")" + "(" + req.items() + ")");
        return buildPreview(req.items()); // allow null
    }

private CheckoutPreviewResponse buildPreview(List<CartLine> cart) {
    if (cart == null || cart.isEmpty()) {
        return new CheckoutPreviewResponse(List.of(), 0.0);
    }

    // keep only valid lines
    List<CartLine> valid = cart.stream()
            .filter(c -> c != null && c.id() != null && c.quantity() != null)
            .toList();

    if (valid.isEmpty()) {
        return new CheckoutPreviewResponse(List.of(), 0.0);
    }

    List<Long> ids = valid.stream().map(CartLine::id).distinct().toList();
    List<ItemsClient.ItemDto> items = itemsClient.getItemsByIds(ids);

    Map<Long, ItemsClient.ItemDto> byId = items.stream()
            .collect(Collectors.toMap(ItemsClient.ItemDto::id, x -> x));

    List<CheckoutLine> lines = new ArrayList<>();
    double total = 0.0;

    for (CartLine c : valid) {
        if (c.quantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "quantity must be > 0");
        }

        ItemsClient.ItemDto item = byId.get(c.id());
        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found: " + c.id());
        }

        double lineTotal = item.price() * c.quantity();
        total += lineTotal;
        lines.add(new CheckoutLine(item.id(), item.name(), item.price(), c.quantity(), lineTotal));
    }

    return new CheckoutPreviewResponse(lines, total);
}

    public record CartLine(Long id, Integer quantity) {}
    public record CheckoutRequest(String fullName, String email, String address, List<CartLine> items) {}

    public record CheckoutLine(Long id, String name, Double unitPrice, int quantity, double lineTotal) {}
    public record CheckoutPreviewResponse(List<CheckoutLine> lines, double total) {}
}
