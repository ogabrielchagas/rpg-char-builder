package com.api.rpgcharbuilder.domains;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tb_items")
public class Items {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private CombatType itemType;

    private String itemName;

    private int requiredLevel;

    @Enumerated(value = EnumType.STRING)
    private Dice damage;


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
