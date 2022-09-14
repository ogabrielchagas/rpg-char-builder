package com.api.rpgcharbuilder.services;

import com.api.rpgcharbuilder.domains.*;
import com.api.rpgcharbuilder.dtos.CharDto;
import com.api.rpgcharbuilder.repositories.CharRepository;
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
import org.springframework.beans.BeanUtils;
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
class CharServiceTest {

    @InjectMocks
    private CharService charService;

    @Mock
    private CharRepository charRepository;

    @Test
    void nameShouldExists() {

    }

    @Test
    void shouldSaveOneChar() {
        //Prepara
        final var charToSave = new Char("KL", 2);
        Mockito.when(charRepository.save(Mockito.any(Char.class))).thenReturn(charToSave);

        //Faz
        final var actual = charService.save(new Char());

        //Verifica
        assertThat(actual).usingRecursiveComparison().isEqualTo(charToSave);
        Mockito.verify(charRepository, Mockito.times(1)).save(Mockito.any(Char.class));
        Mockito.verifyNoMoreInteractions(charRepository);
    }

    @Test
    void shouldFindAndReturnAllChars() {
        PageRequest pageRequest = PageRequest.of(0,4);
        List<Char> listChars = Arrays.asList(new Char(), new Char(), new Char(), new Char());
        Page<Char> chars = new PageImpl<>(listChars, pageRequest, listChars.size());

        when(charRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(chars);

        final var actual = charService.findAll(pageRequest);

        assertThat(actual.getTotalElements()).isEqualTo(4);
        assertThat(actual.getTotalPages()).isEqualTo(1);
        Mockito.verify(charRepository, Mockito.times(1)).findAll(Mockito.any(PageRequest.class));
        Mockito.verifyNoMoreInteractions(charRepository);
    }

    @Test
    void shouldFindAndReturnOneCharById() {
        final var expectedChar = new Char("KL", 2);
        Mockito.when(charRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(expectedChar));

        final var actual = charService.findById(8L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(Optional.of(expectedChar));
        Mockito.verify(charRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(charRepository);
    }

    @Test
    void shouldDeleteOneRace(){
        final var novoChar = new Char();

        Mockito.doNothing().when(charRepository).delete(novoChar);

        charService.delete(new Char());
        Mockito.verify(charRepository, Mockito.times(1)).delete(novoChar);
        Mockito.verifyNoMoreInteractions(charRepository);
    }

    @Test
    void charShouldBeSetToDead() {
        final var novoChar = new Char("KL", -1);
        charService.deadOrAlive(novoChar);

        assertFalse(novoChar.getAlive());
    }

    @Test
    void charShouldBeSetToAlive() {
        final var novoChar = new Char("KL", 10);
        charService.deadOrAlive(novoChar);

        assertTrue(novoChar.getAlive());
    }

    @Test
    void charShouldNotHaveClasse() {
        final var novoChar = new Char();

        assertTrue(charService.hasNoClasse(novoChar));
    }

    @Test
    void charShouldHaveClasse() {
        final var novoChar = new Char();
        final var novaClasse = new Classe();
        novoChar.setClasse(novaClasse);

        assertFalse(charService.hasNoClasse(novoChar));
    }

    @Test
    void charLevelShouldBeLesserThanItemLevel() {
        final var novoChar = new CharDto();
        final var novoItem = new Items(CombatType.MELEE, "Espada", 2, Dice.D6);

        Char character = new Char();
        BeanUtils.copyProperties(novoChar, character);

        assertTrue(charService.charLevelLesserThanItemLevel(character, novoItem));
    }

    @Test
    void charLevelShouldBeGreaterThanOrEqualToItemLevel() {
        final var novoChar = new CharDto();
        final var novoItem = new Items(CombatType.MELEE, "Espada", 1, Dice.D6);

        Char character = new Char();
        BeanUtils.copyProperties(novoChar, character);

        assertFalse(charService.charLevelLesserThanItemLevel(character, novoItem));
    }

    @Test
    void charShouldHaveNewItem() {
        var novoChar = new Char();
        final var novoItem = new Items(CombatType.MELEE, "Espada", 1, Dice.D6);

        novoChar = charService.addNewItem(novoChar, novoItem);

        assertTrue(novoChar.getItems().contains(novoItem));
    }

    @Test
    void charShouldBeWithoutItems() {
        final var novoChar = new Char();

        assertTrue(charService.withoutItems(novoChar));
    }

    @Test
    void charShouldNotBeWithoutItems() {
        var novoChar = new Char();
        final var novoItem = new Items(CombatType.MELEE, "Espada", 1, Dice.D6);

        novoChar = charService.addNewItem(novoChar, novoItem);

        assertFalse(charService.withoutItems(novoChar));
    }

    @Test
    void isAtkRollInRange() {
        int atk = charService.atkRoll();

        assertThat(atk).isGreaterThan(0);
        assertThat(atk).isLessThanOrEqualTo(20);
    }

    @Test
    void isEnemyDefWorking() {
        final var novoChar = new Char();
        novoChar.setLevel(2);

        assertThat(charService.enemyDef(novoChar)).isEqualTo(12);  //10 + novoChar.getLevel()

    }

    @Test
    void isDmgTakenWorking() {
        final var novoChar = new Char("KL", 10);

        charService.dmgTaken(novoChar, 5);      //hitpoints = hitpoins - dmgTaken

        assertThat(novoChar.getHp()).isEqualTo(5);

    }



    @Test
    void isLevelUpWorking() {
        final var novoChar = new Char();
        novoChar.setLevel(10);

        charService.levelUp(novoChar); //getLevel + 1

        assertThat(novoChar.getLevel()).isEqualTo(11);
    }

    @Test
    void isLootWorking() {
        final var novoChar = new Char();
        final var segChar = new Char();

        final var novoItem = new Items(CombatType.MELEE, "Espada", 1, Dice.D6);
        final var segItem = new Items(CombatType.RANGED, "Arco", 1, Dice.D6);

        charService.addNewItem(novoChar, novoItem);
        charService.addNewItem(segChar, segItem);

        novoChar.setMoney(10L);
        segChar.setMoney(10L);

        charService.loot(novoChar, segChar);

        assertThat(novoChar.getMoney()).isEqualTo(20);
        assertThat(segChar.getMoney()).isEqualTo(0);
        assertTrue(charService.withoutItems(segChar));
        assertTrue(novoChar.getItems().contains(novoItem));
        assertTrue(novoChar.getItems().contains(segItem));
    }

    @Test
    void hpDiceRollShouldBeInRange() {
        final var race = new Race("Gigante", Dice.D20);

        assertThat(charService.dieRoll(race.getHpDice())).isGreaterThan(0);
        assertThat(charService.dieRoll(race.getHpDice())).isLessThanOrEqualTo(100);
    }
}