package com.api.rpgcharbuilder.repositories;

import com.api.rpgcharbuilder.domains.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemsRepository extends JpaRepository<Items, Long> {

    boolean existsByItemName(String itemName);
}
