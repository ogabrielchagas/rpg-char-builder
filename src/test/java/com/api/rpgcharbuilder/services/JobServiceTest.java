package com.api.rpgcharbuilder.services;

import com.api.rpgcharbuilder.domains.*;
import com.api.rpgcharbuilder.repositories.JobRepository;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JobServiceTest {

    @InjectMocks
    private JobService jobService;

    @Mock
    private JobRepository jobRepository;


    @Test
    void shouldSaveOneClasse() {
        //Prepara
        final var classeToSave = new Job(1L, "Mago", CombatType.RANGED);
        Mockito.when(jobRepository.save(Mockito.any(Job.class))).thenReturn(classeToSave);

        //Faz
        final var actual = jobService.save(new Job());

        //Verifica
        assertThat(actual).usingRecursiveComparison().isEqualTo(classeToSave);
        Mockito.verify(jobRepository, Mockito.times(1)).save(Mockito.any(Job.class));
        Mockito.verifyNoMoreInteractions(jobRepository);
    }

    @Test
    void shouldFindAndReturnAllClasses() {
        PageRequest pageRequest = PageRequest.of(0,4);
        List<Job> listClasses = Arrays.asList(new Job(), new Job(), new Job(), new Job());
        Page<Job> classes = new PageImpl<>(listClasses, pageRequest, listClasses.size());

        when(jobRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(classes);

        final var actual = jobService.findAll(pageRequest);

        assertThat(actual.getTotalElements()).isEqualTo(4);
        assertThat(actual.getTotalPages()).isEqualTo(1);
        Mockito.verify(jobRepository, Mockito.times(1)).findAll(Mockito.any(PageRequest.class));
        Mockito.verifyNoMoreInteractions(jobRepository);
    }

    @Test
    void shouldFindAndReturnOneClasseByName() {
        final var expectedClasse = new Job(1L, "Mago", CombatType.RANGED);
        Mockito.when(jobRepository.findByJobName(Mockito.anyString())).thenReturn(Optional.of(expectedClasse));

        final var actual = jobService.findByClasseName("Guerreiro");

        assertThat(actual).usingRecursiveComparison().isEqualTo(Optional.of(expectedClasse));
        Mockito.verify(jobRepository, Mockito.times(1)).findByJobName(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(jobRepository);
    }

    @Test
    void shouldFindASpecificClasseByName(){
        final var expectedClasse = new Job(1L, "Mago", CombatType.RANGED);
        Mockito.when(jobRepository.findByJobName("Mago")).thenReturn(Optional.of(expectedClasse));

        final var actual = jobService.findByClasseName("Mago");

        assertThat(actual).usingRecursiveComparison().isEqualTo(Optional.of(expectedClasse));
        Mockito.verify(jobRepository, Mockito.times(1)).findByJobName(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(jobRepository);
    }

    @Test
    void shouldNotFindASpecificClasseByName(){
        final var expectedClasse = new Job(1L, "Mago", CombatType.RANGED);
        Mockito.when(jobRepository.findByJobName("Mago")).thenReturn(Optional.of(expectedClasse));

        final var actual = jobService.findByClasseName("Guerreiro");

        assertThat(actual).usingRecursiveComparison().isNotEqualTo(Optional.of(expectedClasse));
        Mockito.verify(jobRepository, Mockito.times(1)).findByJobName(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(jobRepository);
    }



    @Test
    void shouldFindAndReturnOneClasseById() {
        final var expectedClasse = new Job(1L, "Mago", CombatType.RANGED);
        Mockito.when(jobRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(expectedClasse));

        final var actual = jobService.findById(8L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(Optional.of(expectedClasse));
        Mockito.verify(jobRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(jobRepository);
    }

    @Test
    void shouldFindASpecificClasseByID(){
        final var expectedClasse = new Job(1L, "Mago", CombatType.RANGED);
        Mockito.when(jobService.findById(1L)).thenReturn(Optional.of(expectedClasse));

        final var actual = jobService.findById(1L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(Optional.of(expectedClasse));
        Mockito.verify(jobRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(jobRepository);
    }

    @Test
    void shouldNotFindASpecificClasseByID(){
        final var expectedClasse = new Job(1L, "Mago", CombatType.RANGED);
        Mockito.when(jobService.findById(1L)).thenReturn(Optional.of(expectedClasse));

        final var actual = jobService.findById(2L);

        assertThat(actual).usingRecursiveComparison().isNotEqualTo(Optional.of(expectedClasse));
        Mockito.verify(jobRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(jobRepository);
    }



    @Test
    void shouldDeleteOneClasse(){
        final var novaClasse = new Job();

        Mockito.doNothing().when(jobRepository).delete(novaClasse);

        jobService.delete(new Job());
        Mockito.verify(jobRepository, Mockito.times(1)).delete(novaClasse);
        Mockito.verifyNoMoreInteractions(jobRepository);
    }

    @Test
    void charShouldHaveRace() {
        final var charWithRace = new Char(1, new Race(1L, "Gigante", Dice.D20));

        assertFalse(jobService.hasNoRace(charWithRace));
    }

    @Test
    void charShoudNotHaveRace(){
        final var charWithNoRace = new Char();

        assertTrue(jobService.hasNoRace(charWithNoRace));
    }

    @Test
    void classeShouldBeMeleeType() {
        final var classeMelee = new Job(1L, "Bárbaro", CombatType.MELEE);

        assertTrue(jobService.isMelee(classeMelee));
    }

    @Test
    void classeShouldNotBeMeleeType() {
        final var classeMelee = new Job(1L, "Mago", CombatType.RANGED);

        assertFalse(jobService.isMelee(classeMelee));
    }
}