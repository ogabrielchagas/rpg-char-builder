package com.api.rpgcharbuilder.services;

import com.api.rpgcharbuilder.domains.Char;
import com.api.rpgcharbuilder.domains.Dice;
import com.api.rpgcharbuilder.domains.Items;
import com.api.rpgcharbuilder.repositories.CharRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    //Combate com bonus quando a classe do personagem e o item possuem o mesmo combat type : RANGED ou MELEE
    public int CombatWithBonus(Char aChar, Char enemy, Items item){
        int diceRoll = atkRoll() + aChar.getLevel();
        int enemyDef = enemyDef(enemy);
        if(diceRoll >= enemyDef){
            int atkDmg = dieRoll(item.getDamage()) + aChar.getLevel();
            dmgTaken(enemy, atkDmg);
            save(enemy);
            return atkDmg;
        }
        else {
            return 0;
        }
    }

    public int CombatWithoutBonus(Char aChar, Char enemy, Items item){
        int diceRoll = atkRoll();
        int enemyDef = enemyDef(enemy);
        if(diceRoll >= enemyDef){
            int atkDmg = dieRoll(item.getDamage());
            dmgTaken(enemy, atkDmg);
            save(enemy);
            return atkDmg;
        }
        else {
            int dmg = 0;
            return dmg;
        }
    }

    public boolean isCombatOver(Char aChar, Char enemy) {
        if(enemy.getHp() < 0){
            enemy.setAlive(false);
            levelUp(aChar);
            aChar.setHp(aChar.getHp() + dieRoll(aChar.getRace().getHpDice()) + aChar.getLevel());
            loot(aChar, enemy);
            save(aChar);
            save(enemy);
            return true;
        }
        return false;
    }
    public int dieRoll(Dice dice) {
        Random random = new Random();
        int diceRoll = 0;
        switch (dice) {
            case D4:
                diceRoll = random.nextInt(4) + 1;
                break;
            case D6:
                diceRoll = random.nextInt(6) + 1;
                break;
            case D8:
                diceRoll = random.nextInt(8) + 1;
                break;
            case D10:
                diceRoll = random.nextInt(10) + 1;
                break;
            case D12:
                diceRoll = random.nextInt(12) + 1;
                break;
            case D20:
                diceRoll = random.nextInt(20) + 1;
                break;
            case D100:
                diceRoll = random.nextInt(100) + 1;
                break;
        }
        return diceRoll;
    }

    public int hpConfigure(Dice hpDice, Char charModel) {
        int hpPoints = 0;
        if (charModel.getLevel() == 1) {
            switch (hpDice) {
                case D4:
                    hpPoints = 4 + charModel.getLevel();
                    break;
                case D6:
                    hpPoints = 6 + charModel.getLevel();
                    break;
                case D8:
                    hpPoints = 8 + charModel.getLevel();
                    break;
                case D10:
                    hpPoints = 10 + charModel.getLevel();
                    break;
                case D12:
                    hpPoints = 12 + charModel.getLevel();
                    break;
                case D20:
                    hpPoints = 20 + charModel.getLevel();
                    break;
                case D100:
                    hpPoints = 100 + charModel.getLevel();
                    break;
            }
            charModel.setAlive(true);
            return hpPoints;
        } else {
            int hitPoints = 0;
            switch (hpDice) {
                case D4:
                    hitPoints = 4 + charModel.getLevel();
                    for (int i = 1; i < charModel.getLevel(); i++) {
                        hitPoints += dieRoll(hpDice) + charModel.getLevel();
                    }
                    break;
                case D6:
                    hitPoints = 6 + charModel.getLevel();
                    for (int i = 1; i < charModel.getLevel(); i++) {
                        hitPoints += dieRoll(hpDice) + charModel.getLevel();
                    }
                    break;
                case D8:
                    hitPoints = 8 + charModel.getLevel();
                    for (int i = 1; i < charModel.getLevel(); i++) {
                        hitPoints += dieRoll(hpDice) + charModel.getLevel();
                    }
                    break;
                case D10:
                    hitPoints = 10 + charModel.getLevel();
                    for (int i = 1; i < charModel.getLevel(); i++) {
                        hitPoints += dieRoll(hpDice) + charModel.getLevel();
                    }
                    break;
                case D12:
                    hitPoints = 12 + charModel.getLevel();
                    for (int i = 1; i < charModel.getLevel(); i++) {
                        hitPoints += dieRoll(hpDice) + charModel.getLevel();
                    }
                    break;
                case D20:
                    hitPoints = 20 + charModel.getLevel();
                    for (int i = 1; i < charModel.getLevel(); i++) {
                        hitPoints += dieRoll(hpDice) + charModel.getLevel();
                    }
                    break;
                case D100:
                    hitPoints = 100 + charModel.getLevel();
                    for (int i = 1; i < charModel.getLevel(); i++) {
                        hitPoints += dieRoll(hpDice) + charModel.getLevel();
                    }
                    break;
            }
            charModel.setAlive(true);
            return hitPoints;
        }
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
