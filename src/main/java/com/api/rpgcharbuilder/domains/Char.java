package com.api.rpgcharbuilder.domains;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tb_char")
public class Char {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID gerado para identificação dos Personagens do RPG", required = true)
    private Long id;
    @ApiModelProperty(value = "Nome do Personagem do RPG", required = true)
    private String charName;
    @ApiModelProperty(value = "Level do Personagem(Personagens dos Jogadores criados começam Level 1)", required = true)
    private int level;
    @ApiModelProperty(value = "Pontos de Vida(HP) do Personagem automaticamente configurados quando selecionada uma Raça", required = true)
    private int hp;
    @ApiModelProperty(value = "Indica se o personagem está vivo(true) ou morto(false)", required = true)
    private boolean alive;
    @ApiModelProperty(value = "Representa as moedas(dinheiro) do Personagem (Personagens dos Jogadores criados começam com 10 moedas)", required = true)
    private Long money;
    @ManyToOne
    @ApiModelProperty(value = "Campo que associa uma Raça ao Personagem")
    @JoinColumn(name = "race_id")
    private Race race;
    @ManyToOne
    @JoinColumn(name = "classe_id")
    @ApiModelProperty(value = "Campo que associa uma Classe ao Personagem")
    private Job job;
    @ManyToMany
    @JoinTable(name = "chars_items",
            joinColumns = @JoinColumn(name = "char_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    @ApiModelProperty(value = "Campo que associa os Itens possuidos por um Personagem")
    private List<Items> items;

    public Char(Long id, String charName, int hp) {
        this.id = id;
        this.charName = charName;
        this.hp = hp;
    }

    public Char(List<Items> items) {
        this.items = items;
    }

    public Char(int level, Race race) {
        this.race = race;
        this.level = level;
    }

    public Char(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Char aChar = (Char) o;
        return Objects.equals(id, aChar.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCharName() {
        return charName;
    }

    public void setCharName(String charName) {
        this.charName = charName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public boolean getAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Job getClasse() {
        return job;
    }

    public void setClasse(Job job) {
        this.job = job;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }
}
