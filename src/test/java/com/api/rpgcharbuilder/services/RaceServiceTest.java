package com.api.rpgcharbuilder.services;

import com.api.rpgcharbuilder.domains.*;
import com.api.rpgcharbuilder.repositories.ClasseRepository;
import com.api.rpgcharbuilder.repositories.RaceRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class RaceServiceTest {

    @InjectMocks
    private RaceService raceService;

    @Mock
    private RaceRepository raceRepository;

    @Test
    void existsByRaceName() {
    }

    @Test
    void shouldSaveOneRace() {
        final var raceToSave = new Race("Gigante", Dice.D20);
        Mockito.when(raceRepository.save(Mockito.any(Race.class))).thenReturn(raceToSave);

        //Faz
        final var actual = raceService.save(new Race());

        //Verifica
        assertThat(actual).usingRecursiveComparison().isEqualTo(raceToSave);
        Mockito.verify(raceRepository, Mockito.times(1)).save(Mockito.any(Race.class));
        Mockito.verifyNoMoreInteractions(raceRepository);
    }

    @Test
    void findAll() {
    }

    @Test
    void shouldFindAndReturnOneRaceById() {
        final var expectedRace = new Race("Gigante", Dice.D20);
        Mockito.when(raceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(expectedRace));

        final var actual = raceService.findById(8L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(Optional.of(expectedRace));
        Mockito.verify(raceRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(raceRepository);
    }

    @Test
    void delete() {
    }

    @Test
    void hpD20DiceRollShouldBeInRange() {
        final var race = new Race("Gigante", Dice.D20);
        final var novoChar = new Char(2, race);

        assertThat(raceService.hpDiceRoll(race.getHpDice())).isGreaterThan(0 + novoChar.getLevel());
        assertThat(raceService.hpDiceRoll(race.getHpDice())).isLessThanOrEqualTo(20 + novoChar.getLevel());

    }

    @Test
    void hpConfigure() {
    }
}