package ch.bbw.store_items_microservice.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import ch.bbw.store_items_microservice.entity.Items;

import java.util.List;

@Repository
public interface ItemsRepository extends JpaRepository<Items, Long> {

    List<Items> findAll();
    
} 