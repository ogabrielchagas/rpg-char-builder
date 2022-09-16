package com.api.rpgcharbuilder.domains;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "tb_race")
public class Race {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID gerado para identificação das Raças do RPG", required = true)
    private Long id;

    @ApiModelProperty(value = "Nome da raça do RPG, como Humanos, Anões e Elfos.", required = true)
    private String raceName;

    @ApiModelProperty(value = "Dado de Vida(HP) da raça do RPG, responsável por definir a HP de um Personagem", required = true)
    @Enumerated(value = EnumType.STRING)
    private Dice hpDice;

    @ApiModelProperty(value = "Campo não preenchido por usuários, utilizado para que o mapemanto ManyToOne reconheça quais personagens são dessa raça")
    @OneToMany(mappedBy = "race", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Char> char_id;

    public Race(String raceName, Dice hpDice) {
        this.raceName = raceName;
        this.hpDice = hpDice;
    }

    public Race(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Race race = (Race) o;
        return Objects.equals(id, race.id);
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

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public Dice getHpDice() {
        return hpDice;
    }

    public void setHpDice(Dice hpDice) {
        this.hpDice = hpDice;
    }

    public List<Char> getChar_id() {
        return char_id;
    }

    public void setChar_id(List<Char> char_id) {
        this.char_id = char_id;
    }
}
