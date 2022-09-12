package com.api.rpgcharbuilder.services;

import com.api.rpgcharbuilder.domains.Items;
import com.api.rpgcharbuilder.repositories.ItemsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ItemsService {
    private final ItemsRepository itemsRepository;

    public ItemsService(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    public boolean existsByItemName(String itemName){return itemsRepository.existsByItemName(itemName);}

    @Transactional
    public Items save(Items itemsModel){return itemsRepository.save(itemsModel);}

    public Page<Items> findAll(Pageable pageable){return itemsRepository.findAll(pageable);}

    public Optional<Items> findById(Long id){return itemsRepository.findById(id);}

    @Transactional
    public void delete(Items itemsModel){itemsRepository.delete(itemsModel);}
}
