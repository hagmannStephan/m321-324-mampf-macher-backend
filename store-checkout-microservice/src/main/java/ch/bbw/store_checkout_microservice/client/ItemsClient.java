package ch.bbw.store_checkout_microservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class ItemsClient {

    private final RestClient rest;

    public ItemsClient(RestClient.Builder builder, @Value("${items.service.base-url}") String baseUrl) {
        this.rest = builder.baseUrl(baseUrl).build();
    }


    public List<ItemDto> getItemsByIds(List<Long> ids) {
        return rest.get()
                .uri(uriBuilder -> {
                    var b = uriBuilder.path("/items/by-ids");
                    ids.forEach(id -> b.queryParam("ids", id));
                    return b.build();
                })
                .retrieve()
                .body(new org.springframework.core.ParameterizedTypeReference<List<ItemDto>>() {});
    }

    public record ItemDto(Long id, String name, Double price, Integer stock, boolean inStock) {}
}
