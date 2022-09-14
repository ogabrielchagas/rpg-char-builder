package com.api.rpgcharbuilder.services;

import com.api.rpgcharbuilder.domains.*;
import com.api.rpgcharbuilder.repositories.ItemsRepository;
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
class ItemsServiceTest {

    @InjectMocks
    private ItemsService itemsService;

    @Mock
    private ItemsRepository itemsRepository;

    @Test
    void existsByItemName() {
    }

    @Test
    void shouldSaveOneClasse() {
        //Prepara
        final var itemToSave = new Items(CombatType.MELEE, "Espada", 2, Dice.D6);
        Mockito.when(itemsRepository.save(Mockito.any(Items.class))).thenReturn(itemToSave);

        //Faz
        final var actual = itemsService.save(new Items());

        //Verifica
        assertThat(actual).usingRecursiveComparison().isEqualTo(itemToSave);
        Mockito.verify(itemsRepository, Mockito.times(1)).save(Mockito.any(Items.class));
        Mockito.verifyNoMoreInteractions(itemsRepository);
    }

    @Test
    void shouldFindAndReturnAllItems() {
        PageRequest pageRequest = PageRequest.of(0,4);
        List<Items> listItems = Arrays.asList(new Items(), new Items(), new Items(), new Items());
        Page<Items> items = new PageImpl<>(listItems, pageRequest, listItems.size());

        when(itemsRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(items);

        final var actual = itemsService.findAll(pageRequest);

        assertThat(actual.getTotalElements()).isEqualTo(4);
        assertThat(actual.getTotalPages()).isEqualTo(1);
        Mockito.verify(itemsRepository, Mockito.times(1)).findAll(Mockito.any(PageRequest.class));
        Mockito.verifyNoMoreInteractions(itemsRepository);
    }

    @Test
    void shouldFindAndReturnOneItemById() {
        final var expectedItem = new Items(CombatType.MELEE, "Espada", 2, Dice.D6);
        Mockito.when(itemsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(expectedItem));

        final var actual = itemsService.findById(8L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(Optional.of(expectedItem));
        Mockito.verify(itemsRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(itemsRepository);
    }

    @Test
    void shouldDeleteOneItem() {
        final var novoItem = new Items();

        Mockito.doNothing().when(itemsRepository).delete(novoItem);

        itemsService.delete(new Items());
        Mockito.verify(itemsRepository, Mockito.times(1)).delete(novoItem);
        Mockito.verifyNoMoreInteractions(itemsRepository);
    }

    @Test
    void charShouldHaveItem() {
        final var novoItem = new Items(CombatType.MELEE, "Espada", 2, Dice.D6);
        final var novoChar = new Char(List.of(novoItem));

        assertFalse(itemsService.CharDoesntHaveItem(novoChar, novoItem));
    }

    @Test
    void charShouldNotHaveItem() {
        final var novoItem = new Items(CombatType.MELEE, "Espada", 2, Dice.D6);
        final var novoChar = new Char();

        assertTrue(itemsService.CharDoesntHaveItem(novoChar, novoItem));
    }

    @Test
    void itemShouldBeMeleeType() {
        final var itemMelee =  new Items(CombatType.MELEE, "Espada", 2, Dice.D6);

        assertTrue(itemsService.isMelee(itemMelee));
    }

    @Test
    void itemShouldNotBeMeleeType() {
        final var itemMelee =  new Items(CombatType.RANGED, "Arco", 2, Dice.D6);

        assertFalse(itemsService.isMelee(itemMelee));
    }

    @Test
    void isDiceDmgInRange() {
        final var novoItem =  new Items(CombatType.RANGED, "Arco", 2, Dice.D6);

        //GARANTIR QUE INDEPENDENTE DO DADO A SER ROLADO O NÚMERO SEJA POSSÍVEL DENTRO DO RANGE DOS DADOS
        assertThat(itemsService.diceDmg(novoItem)).isGreaterThan(0);
        assertThat(itemsService.diceDmg(novoItem)).isLessThanOrEqualTo(100);
    }
}