package ch.bbw.store_items_microservice.repository;

import ch.bbw.store_items_microservice.entity.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemsRepository extends JpaRepository<Items, Long> {
    Optional<Items> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
