package com.api.rpgcharbuilder.services;

import com.api.rpgcharbuilder.domains.*;
import com.api.rpgcharbuilder.repositories.ClasseRepository;
import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ClasseServiceTest {

    @InjectMocks
    private ClasseService classeService;

    @Mock
    private ClasseRepository classeRepository;


    @Test
    void existsByClasseName() {

    }

    @Test
    void shouldSaveOneClasse() {
        //Prepara
        final var classeToSave = new Classe("Mago", CombatType.RANGED);
        Mockito.when(classeRepository.save(Mockito.any(Classe.class))).thenReturn(classeToSave);

        //Faz
        final var actual = classeService.save(new Classe());

        //Verifica
        assertThat(actual).usingRecursiveComparison().isEqualTo(classeToSave);
        Mockito.verify(classeRepository, Mockito.times(1)).save(Mockito.any(Classe.class));
        Mockito.verifyNoMoreInteractions(classeRepository);
    }

    @Test
    void shouldFindAndReturnAllClasses() {

    }

    @Test
    void shouldFindAndReturnOneClasseById() {
        final var expectedClasse = new Classe("Mago", CombatType.RANGED);
        Mockito.when(classeRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(expectedClasse));

        final var actual = classeService.findById(8L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(Optional.of(expectedClasse));
        Mockito.verify(classeRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(classeRepository);
    }


    @Test
    void shouldDeleteOneClasse() {

    }

    @Test
    void charShouldHaveRace() {
        final var charWithRace = new Char(1, new Race("Gigante", Dice.D20));

        assertFalse(classeService.hasNoRace(charWithRace));
    }

    @Test
    void charShoudNotHaveRace(){
        final var charWithNoRace = new Char();

        assertTrue(classeService.hasNoRace(charWithNoRace));
    }

    @Test
    void classeShouldBeMeleeType() {
        final var classeMelee = new Classe("Bárbaro", CombatType.MELEE);

        assertTrue(classeService.isMelee(classeMelee));
    }

    @Test
    void classeShouldNotBeMeleeType() {
        final var classeMelee = new Classe("Mago", CombatType.RANGED);

        assertFalse(classeService.isMelee(classeMelee));
    }
}