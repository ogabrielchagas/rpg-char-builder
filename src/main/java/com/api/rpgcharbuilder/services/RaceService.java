package com.api.rpgcharbuilder.services;

import com.api.rpgcharbuilder.domains.Char;
import com.api.rpgcharbuilder.domains.Dice;
import com.api.rpgcharbuilder.domains.Race;
import com.api.rpgcharbuilder.repositories.RaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Random;

@Service
public class RaceService {

    private final RaceRepository raceRepository;

    public RaceService(RaceRepository raceRepository) {
        this.raceRepository = raceRepository;
    }

    public boolean existsByRaceName(String raceName){return raceRepository.existsByRaceName(raceName);}

    @Transactional
    public Race save(Race raceModel){return raceRepository.save(raceModel);}

    public Page<Race> findAll(Pageable pageable){return raceRepository.findAll(pageable);}

    public Optional<Race> findById(Long id){return raceRepository.findById(id);}

    @Transactional
    public void delete(Race raceModel){raceRepository.delete(raceModel);}

    public int hpDiceRoll(Dice dice) {
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
                        hitPoints += hpDiceRoll(hpDice) + charModel.getLevel();
                    }
                    break;
                case D6:
                    hitPoints = 6 + charModel.getLevel();
                    for (int i = 1; i < charModel.getLevel(); i++) {
                        hitPoints += hpDiceRoll(hpDice) + charModel.getLevel();
                    }
                    break;
                case D8:
                    hitPoints = 8 + charModel.getLevel();
                    for (int i = 1; i < charModel.getLevel(); i++) {
                        hitPoints += hpDiceRoll(hpDice) + charModel.getLevel();
                    }
                    break;
                case D10:
                    hitPoints = 10 + charModel.getLevel();
                    for (int i = 1; i < charModel.getLevel(); i++) {
                        hitPoints += hpDiceRoll(hpDice) + charModel.getLevel();
                    }
                    break;
                case D12:
                    hitPoints = 12 + charModel.getLevel();
                    for (int i = 1; i < charModel.getLevel(); i++) {
                        hitPoints += hpDiceRoll(hpDice) + charModel.getLevel();
                    }
                    break;
                case D20:
                    hitPoints = 20 + charModel.getLevel();
                    for (int i = 1; i < charModel.getLevel(); i++) {
                        hitPoints += hpDiceRoll(hpDice) + charModel.getLevel();
                    }
                    break;
                case D100:
                    hitPoints = 100 + charModel.getLevel();
                    for (int i = 1; i < charModel.getLevel(); i++) {
                        hitPoints += hpDiceRoll(hpDice) + charModel.getLevel();
                    }
                    break;
            }
            charModel.setAlive(true);
            return hitPoints;
        }
    }
}
