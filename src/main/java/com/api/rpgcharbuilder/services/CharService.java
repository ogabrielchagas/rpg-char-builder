package com.api.rpgcharbuilder.services;

import com.api.rpgcharbuilder.domains.Char;
import com.api.rpgcharbuilder.domains.Items;
import com.api.rpgcharbuilder.dtos.CharDto;
import com.api.rpgcharbuilder.repositories.CharRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CharService {
    private final CharRepository charRepository;

    public CharService(CharRepository charRepository) {
        this.charRepository = charRepository;
    }

    public boolean existsByCharName(String charName){return charRepository.existsByCharName(charName);}

    @Transactional
    public Char save(Char charModel){
        return charRepository.save(charModel);
    }


    public Page<Char> findAll(Pageable pageable){return charRepository.findAll(pageable);}

    public Optional<Char> findById(Long id){return charRepository.findById(id);}

    @Transactional
    public void delete(Char charModel){charRepository.delete(charModel);}

    public void deadOrAlive(Char charModel) {
        if(charModel.getHp() > 0)
            charModel.setAlive(true);
        else
            charModel.setAlive(false);
    }

    public boolean hasNoClasse(Char aChar) {
        if(aChar.getClasse() == null)
            return true;
        return false;
    }

    public boolean charLevelLesserThanItemLevel(Char aChar, Items items) {
        if (aChar.getLevel() < items.getRequiredLevel())
            return true;
        return false;
    }

    public Char addNewItem(Char aChar, Items items) {
        List<Items> itemsModelList = aChar.getItems();
        if(itemsModelList == null){
            List<Items> firstList = new ArrayList<>();
            firstList.add(items);
            aChar.setItems(firstList);
            return aChar;
        }
        itemsModelList.add(items);
        aChar.setItems(itemsModelList);
        return aChar;
    }

    public boolean withoutItems(Char aChar) {
        if(aChar.getItems() == null)
            return true;
        if(aChar.getItems().isEmpty())
            return true;
        return false;
    }

    public boolean isDead(Char aChar) {
        if(aChar.getHp() < 0)
            return true;
        return false;
    }

    public int atkRoll() {
        Random random = new Random();
        int diceRoll = random.nextInt(20) + 1;
        return diceRoll;
    }

    public int enemyDef(Char aChar) {
        return 10 + aChar.getLevel();
    }

    public void dmgTaken(Char aChar, int atkDmg) {
        int hitPoints = aChar.getHp();
        aChar.setHp(hitPoints -= atkDmg);
    }

    public void levelUp(Char aChar) {
        aChar.setLevel(aChar.getLevel() + 1);
    }

    public void loot(Char aChar, Char enemy) {
        aChar.setMoney(aChar.getMoney() + enemy.getMoney());
        enemy.setMoney(0L);

        for (int i = 0; i < enemy.getItems().size(); i++){
            addNewItem(aChar, enemy.getItems().get(i));
            enemy.getItems().remove(i);
        }

    }
}
