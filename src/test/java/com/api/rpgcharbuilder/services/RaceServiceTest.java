package com.api.rpgcharbuilder.services;

import antlr.Utils;
import com.api.rpgcharbuilder.domains.*;
import com.api.rpgcharbuilder.repositories.ClasseRepository;
import com.api.rpgcharbuilder.repositories.RaceRepository;
import org.hamcrest.Matchers;
import org.hibernate.ResourceClosedException;
import org.hibernate.engine.jdbc.Size;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
    void shouldNotSaveBecauseRaceNameAlreadyExists() {

    }

    @Test
    void shouldSaveOneRace() {
        //Prepara
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
        final var expectedRace = new Race("Gigante", Dice.D20);
        Mockito.when(raceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(expectedRace));

        final var actual = raceService.findById(8L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(Optional.of(expectedRace));
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