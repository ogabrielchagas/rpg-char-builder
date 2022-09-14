package com.api.rpgcharbuilder.services;

import com.api.rpgcharbuilder.domains.*;
import com.api.rpgcharbuilder.repositories.ClasseRepository;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
        PageRequest pageRequest = PageRequest.of(0,4);
        List<Classe> listClasses = Arrays.asList(new Classe(), new Classe(), new Classe(), new Classe());
        Page<Classe> classes = new PageImpl<>(listClasses, pageRequest, listClasses.size());

        when(classeRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(classes);

        final var actual = classeService.findAll(pageRequest);

        assertThat(actual.getTotalElements()).isEqualTo(4);
        assertThat(actual.getTotalPages()).isEqualTo(1);
        Mockito.verify(classeRepository, Mockito.times(1)).findAll(Mockito.any(PageRequest.class));
        Mockito.verifyNoMoreInteractions(classeRepository);
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
    void shouldDeleteOneClasse(){
        final var novaClasse = new Classe();

        Mockito.doNothing().when(classeRepository).delete(novaClasse);

        classeService.delete(new Classe());
        Mockito.verify(classeRepository, Mockito.times(1)).delete(novaClasse);
        Mockito.verifyNoMoreInteractions(classeRepository);
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