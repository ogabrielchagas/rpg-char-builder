package com.api.rpgcharbuilder.services;

import com.api.rpgcharbuilder.domains.Char;
import com.api.rpgcharbuilder.domains.CombatType;
import com.api.rpgcharbuilder.domains.Dice;
import com.api.rpgcharbuilder.domains.Items;
import com.api.rpgcharbuilder.repositories.ItemsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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

    public boolean charHasItem(Char aChar, Items items) {
        List<Items> listItems = aChar.getItems();
        if(!listItems.contains(items))
            return true;
        return false;
    }

    public boolean isMelee(Items items) {
        if(items.getItemType() == CombatType.MELEE)
            return true;
        return false;
    }

    public int diceDmg(Items items) {
        Random random = new Random();
        int dmgRoll = 0;
        Dice dice = items.getDamage();
        switch (dice){
            case D4:
                dmgRoll = random.nextInt(4) + 1;
                break;
            case D6:
                dmgRoll += random.nextInt(6) + 1;
                break;
            case D8:
                dmgRoll += random.nextInt(8) + 1;
                break;
            case D10:
                dmgRoll += random.nextInt(10) + 1;
                break;
            case D12:
                dmgRoll += random.nextInt(12) + 1;
                break;
            case D20:
                dmgRoll += random.nextInt(20) + 1;
                break;
            case D100:
                dmgRoll += random.nextInt(100) + 1;
                break;
        }
        return dmgRoll;
    }
}
