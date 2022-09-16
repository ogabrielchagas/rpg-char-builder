package com.api.rpgcharbuilder.services;

import com.api.rpgcharbuilder.domains.*;

import com.api.rpgcharbuilder.repositories.RaceRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RaceServiceTest {

    @InjectMocks
    private RaceService raceService;

    @Mock
    private RaceRepository raceRepository;

    @Test
    void shouldFindAndReturnOneRaceByName() {
        final var expectedRace = new Race(1L, "Gigante", Dice.D20);
        Mockito.when(raceRepository.findByRaceName(Mockito.anyString())).thenReturn(Optional.of(expectedRace));

        final var actual = raceService.findByRaceName("Humano");

        assertThat(actual).usingRecursiveComparison().isEqualTo(Optional.of(expectedRace));
        Mockito.verify(raceRepository, Mockito.times(1)).findByRaceName(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(raceRepository);
    }

    @Test
    void shouldFindASpecificRaceByName(){
        final var expectedRace = new Race(1L, "Gigante", Dice.D20);
        Mockito.when(raceRepository.findByRaceName("Gigante")).thenReturn(Optional.of(expectedRace));

        final var actual = raceService.findByRaceName("Gigante");

        assertThat(actual).usingRecursiveComparison().isEqualTo(Optional.of(expectedRace));
        Mockito.verify(raceRepository, Mockito.times(1)).findByRaceName(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(raceRepository);
    }

    @Test
    void shouldNotFindASpecificRaceByName(){
        final var expectedRace = new Race(1L, "Gigante", Dice.D20);
        Mockito.when(raceRepository.findByRaceName("Gigante")).thenReturn(Optional.of(expectedRace));

        final var actual = raceService.findByRaceName("Humano");

        assertThat(actual).usingRecursiveComparison().isNotEqualTo(Optional.of(expectedRace));
        Mockito.verify(raceRepository, Mockito.times(1)).findByRaceName(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(raceRepository);
    }

    @Test
    void shouldSaveOneRace() {
        //Prepara
        final var raceToSave = new Race(1L, "Gigante", Dice.D20);
        Mockito.when(raceRepository.save(Mockito.any(Race.class))).thenReturn(raceToSave);

        //Faz
        final var actual = raceService.save(new Race());

        //Verifica
        assertThat(actual).usingRecursiveComparison().isEqualTo(raceToSave);
        Mockito.verify(raceRepository, Mockito.times(1)).save(Mockito.any(Race.class));
        Mockito.verifyNoMoreInteractions(raceRepository);
    }

    @Test
    void shouldFindAndReturnAllRaces() {
        PageRequest pageRequest = PageRequest.of(0,4);
        List<Race> listRaces = Arrays.asList(new Race(), new Race(), new Race(), new Race());
        Page<Race> races = new PageImpl<>(listRaces, pageRequest, listRaces.size());

        when(raceRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(races);

        final var actual = raceService.findAll(pageRequest);

        assertThat(actual.getTotalElements()).isEqualTo(4);
        assertThat(actual.getTotalPages()).isEqualTo(1);
        Mockito.verify(raceRepository, Mockito.times(1)).findAll(Mockito.any(PageRequest.class));
        Mockito.verifyNoMoreInteractions(raceRepository);
    }

    @Test
    void shouldFindAndReturnOneRaceById() {
        final var expectedRace = new Race(1L, "Gigante", Dice.D20);
        Mockito.when(raceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(expectedRace));

        final var actual = raceService.findById(8L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(Optional.of(expectedRace));
        Mockito.verify(raceRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(raceRepository);
    }

    @Test
    void shouldFindASpecificRaceByID(){
        final var expectedRace = new Race(1L, "Gigante", Dice.D20);
        Mockito.when(raceRepository.findById(1L)).thenReturn(Optional.of(expectedRace));

        final var actual = raceService.findById(1L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(Optional.of(expectedRace));
        Mockito.verify(raceRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(raceRepository);
    }

    @Test
    void shouldNotFindASpecificRaceByID(){
        final var expectedRace = new Race(1L, "Gigante", Dice.D20);
        Mockito.when(raceRepository.findById(1L)).thenReturn(Optional.of(expectedRace));

        final var actual = raceService.findById(2L);

        assertThat(actual).usingRecursiveComparison().isNotEqualTo(Optional.of(expectedRace));
        Mockito.verify(raceRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(raceRepository);
    }

    @Test
    void shouldDeleteOneRace(){
        final var novaRace = new Race();

        Mockito.doNothing().when(raceRepository).delete(novaRace);

        raceService.delete(new Race());
        Mockito.verify(raceRepository, Mockito.times(1)).delete(novaRace);
        Mockito.verifyNoMoreInteractions(raceRepository);
    }


}