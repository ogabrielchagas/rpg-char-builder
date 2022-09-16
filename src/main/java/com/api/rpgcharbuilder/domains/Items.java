package com.api.rpgcharbuilder.domains;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tb_items")
public class Items {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID gerado para identificação dos Itens do RPG", required = true)
    private Long id;

    @ApiModelProperty(value = "Tipo de combate do Item, MELEE (Corpo a Corpo) ou RANGED (A Distancia)", required = true)
    @Enumerated(value = EnumType.STRING)
    private CombatType itemType;

    @ApiModelProperty(value = "Nome do Item de RPG como Espadas, Arcos e Cajados", required = true)
    private String itemName;

    @ApiModelProperty(value = "Level requerido para um personagem usar esse item", required = true)
    private int requiredLevel;

    @ApiModelProperty(value = "Dado que representa o Range de Dano que um item causa", required = true)
    @Enumerated(value = EnumType.STRING)
    private Dice damage;

    @ApiModelProperty(value = "Campo não preenchido por usuários, utilizado para que o mapemanto ManyToMany reconheça quais personagens possuem esses itens")
    @ManyToMany(mappedBy = "items")
    @JsonBackReference
    private List<Char> char_id;

    public Items(){}

    public Items(CombatType itemType, String itemName, int requiredLevel, Dice damage) {
        this.itemType = itemType;
        this.itemName = itemName;
        this.requiredLevel = requiredLevel;
        this.damage = damage;
    }

    @PreRemove
    public void removeItemsFromChars(){
        for (Char chars : char_id){
            chars.getItems().remove(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Items items = (Items) o;
        return Objects.equals(id, items.id);
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

    public CombatType getItemType() {
        return itemType;
    }

    public void setItemType(CombatType itemType) {
        this.itemType = itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(int requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public Dice getDamage() {
        return damage;
    }

    public void setDamage(Dice damage) {
        this.damage = damage;
    }

    public List<Char> getChar_id() {
        return char_id;
    }

    public void setChar_id(List<Char> char_id) {
        this.char_id = char_id;
    }
}
