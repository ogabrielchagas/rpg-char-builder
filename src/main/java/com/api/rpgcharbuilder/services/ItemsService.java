package com.api.rpgcharbuilder.services;

import com.api.rpgcharbuilder.domains.*;
import com.api.rpgcharbuilder.repositories.ItemsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
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

    public boolean CharDoesntHaveItem(Char aChar, Items items) {

        List<Items> listItems = aChar.getItems();
        if(listItems == null)
            return true;
        if(!listItems.contains(items))
            return true;
        return false;
    }

    public boolean isMelee(Items items) {
        if(items.getItemType() == CombatType.MELEE)
            return true;
        return false;
    }



}
