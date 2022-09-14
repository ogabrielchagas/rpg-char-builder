package com.api.rpgcharbuilder.controllers;

import com.api.rpgcharbuilder.domains.Char;
import com.api.rpgcharbuilder.domains.Classe;
import com.api.rpgcharbuilder.domains.Items;
import com.api.rpgcharbuilder.domains.Race;
import com.api.rpgcharbuilder.dtos.CharDto;
import com.api.rpgcharbuilder.services.CharService;
import com.api.rpgcharbuilder.services.ClasseService;
import com.api.rpgcharbuilder.services.ItemsService;
import com.api.rpgcharbuilder.services.RaceService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/chars")
public class CharController {
    private final CharService charService;
    private final RaceService raceService;

    private final ClasseService classeService;

    private final ItemsService itemsService;

    public CharController(CharService charService, RaceService raceService, ClasseService classeService, ItemsService itemsService) {
        this.charService = charService;
        this.raceService = raceService;
        this.classeService = classeService;
        this.itemsService = itemsService;
    }

    @GetMapping
    public ResponseEntity<Page<Char>> getAll(@PageableDefault(sort = "id",
            direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(charService.findAll(pageable));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(@PathVariable(value = "id") Long id){
        Optional<Char> charModelOptional = charService.findById(id);
        if(charModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Character not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(charModelOptional.get());
    }

    //GET DE TODOS OS ITEMS PERTENCESTES A UM CHAR
    @GetMapping("/{id}/items")
    ResponseEntity<Object> getItems(@PathVariable(value = "id") Long id){
        Optional<Char> charModelOptional = charService.findById(id);
        if(charModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Character not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(charModelOptional.get().getItems());
    }

    //CHAR COMPLETO (NPC)
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody Char charModel){
        if(charService.existsByCharName(charModel.getCharName()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Character already registered");
        return ResponseEntity.status(HttpStatus.CREATED).body(charService.save(charModel));

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") Long id){
        Optional<Char> charModelOptional = charService.findById(id);
        if(charModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Character not found.");
        }
        charService.delete(charModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Character deleted successfully");
    }

    //PUT CHAR COMPLETO (NPC)
    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") Long id,
                                         @RequestBody Char charModel){
        Optional<Char> charModelOptional = charService.findById(id);
        if(charModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Character not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(charService.save(charModel));
    }

    //APENAS TROCAR  NOME DE UM CHAR
    @PutMapping(value = "/changename/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") Long id,
                                         @RequestBody CharDto charDto){
        Optional<Char> charModelOptional = charService.findById(id);
        if(charModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Character not found.");
        }
        charModelOptional.get().setCharName(charDto.getCharName());
        return ResponseEntity.status(HttpStatus.OK).body(charService.save(charModelOptional.get()));
    }

    //CRIAÇÃO DE CHAR POR PARTES
    @PostMapping("/newchar")
    public ResponseEntity<Object> save(@RequestBody CharDto charDto){
        if(charService.existsByCharName(charDto.getCharName()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Character already registered");
        var charModel = new Char();
        BeanUtils.copyProperties(charDto, charModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(charService.save(charModel));
    }

    //ADD EXISTING RACE TO CHAR
    @PutMapping(value = "/{charid}/addrace/{raceid}")
    ResponseEntity<Object> addExistingRaceToChar(@PathVariable(value = "charid")Long charId,
                                                 @PathVariable(value = "raceid")Long raceId){
        Optional<Char> charModelOptional = charService.findById(charId);
        Optional<Race> raceModelOptional = raceService.findById(raceId);
        if(charModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Character not found.");
        }
        if(raceModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Race not found.");
        }
        Char charModel = charModelOptional.get();
        charModel.setRace(raceModelOptional.get());
        int hpPoints = raceService.hpConfigure(raceModelOptional.get().getHpDice(), charModel);
        charModel.setHp(hpPoints);
        return ResponseEntity.status(HttpStatus.CREATED).body(charService.save(charModel));
    }

    //ADD EXISTING CLASSE TO CHAR
    @PutMapping(value = "/{charid}/addclasse/{classeid}")
    ResponseEntity<Object> addExistingRoleToChar(@PathVariable(value = "charid")Long charId,
                                                 @PathVariable(value = "classeid")Long classeId){
        Optional<Char> charModelOptional = charService.findById(charId);
        Optional<Classe> classeModelOptional = classeService.findById(classeId);
        if(charModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Character not found.");
        }
        if(classeModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Classe not found.");
        }
        if(classeService.hasNoRace(charModelOptional.get()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Char must have a Race before choosing a Classe");

        Char charModel = charModelOptional.get();
        charModel.setClasse(classeModelOptional.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(charService.save(charModel));
    }

    //ADD EXISTING ITEM TO CHAR
    @PutMapping(value = "/{charid}/additem/{itemid}")
    ResponseEntity<Object> addExistingItemToChar(@PathVariable(value = "charid") Long charId,
                                                 @PathVariable(value = "itemid") Long itemId) {
        Optional<Char> charModelOptional = charService.findById(charId);
        Optional<Items> itemsModelOptional = itemsService.findById(itemId);
        if(charModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Character not found.");
        }
        if(charService.hasNoClasse(charModelOptional.get())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Character must have a Classe to add a item.");
        }
        if(itemsModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found.");
        }
        if(charService.charLevelLesserThanItemLevel(charModelOptional.get(), itemsModelOptional.get())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Item needs more level to equip.");
        }
        Char charModel = charService.addNewItem(charModelOptional.get(), itemsModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body(charService.save(charModel));
    }

    @PutMapping(value = "/{charid}/equip/{itemid}/atk/{enemy}")
    ResponseEntity<Object> attacking(@PathVariable(value = "charid") Long charId,
                                     @PathVariable(value = "itemid") Long itemId,
                                     @PathVariable(value = "enemy") Long enemyId){
        Optional<Char> charModelOptional = charService.findById(charId);
        Optional<Char> enemyModelOptional = charService.findById(enemyId);
        Optional<Items> itemsModelOptional = itemsService.findById(itemId);

        //SE FOR ESCOLHIDO UM ID DE CHAR QUE NÃO EXISTE NO DB
        if(charModelOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Character not found.");
        //SE FOR ESCOLHIDO UM ID DE ITEM QUE NÃO EXISTE NO DB
        if(itemsModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found.");
        }
        //SE FOR ESCOLHIDO UM ID DE ENEMY(IGUAL A UM ID DE CHAR) QUE NÃO EXISTE NO DB
        if(enemyModelOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Enemy not found.");
        //SE O CHAR NÃO POSSUIR NENHUM ITEM
        if(charService.withoutItems(charModelOptional.get())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Character does not have items to attack.");
        }
        //SE O ID DO ITEM ESCOLHIDO EXISTIR NO DB, MAS O CHAR NÃO O POSSUIR
        if(itemsService.CharDoesntHaveItem(charModelOptional.get(), itemsModelOptional.get())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Character does not have that item to attack. \n" +
                    "Please select another item.");
        }
        //SE O ENEMY ESTIVER SEM HP = LOOT DO COMBATE
        if(charService.isDead(enemyModelOptional.get())){
            enemyModelOptional.get().setAlive(false);
            charService.levelUp(charModelOptional.get());
            charModelOptional.get().setHp(charModelOptional.get().getHp() + raceService.hpDiceRoll(charModelOptional.get().getRace().getHpDice()) + charModelOptional.get().getLevel());
            charService.loot(charModelOptional.get(), enemyModelOptional.get());

            charService.save(charModelOptional.get());
            charService.save(enemyModelOptional.get());

            return ResponseEntity.status(HttpStatus.OK).body("You Won!!\n Now you are Level " + charModelOptional.get().getLevel()
                        + "\nYou get the money and items from your enemy");
        }

        //SE O ITEM ESCOLHIDO FOR DO TIPO MELEE
        if(itemsService.isMelee(itemsModelOptional.get())) {
            if (classeService.isMelee(charModelOptional.get().getClasse())) {
                int diceRoll = charService.atkRoll() + charModelOptional.get().getLevel();
                int enemyDef = charService.enemyDef(enemyModelOptional.get());
                if (diceRoll >= enemyDef) {
                    int atkDmg = itemsService.diceDmg(itemsModelOptional.get()) + charModelOptional.get().getLevel();
                    charService.dmgTaken(enemyModelOptional.get(), atkDmg);
                    ResponseEntity.status(HttpStatus.OK).body(charService.save(enemyModelOptional.get()));
                    return ResponseEntity.status(HttpStatus.OK).body("Teste de Ataque: " + diceRoll + "\nDefesa do Inimigo: " +
                            enemyDef + "\nDano do ataque: " + atkDmg + "\nVida do Inimigo: " + enemyModelOptional.get().getHp());
                } else {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Teste de Ataque: " + diceRoll + "\nDefesa do Inimigo: " +
                            enemyDef + "\nAttack does not hit.");
                }
            } else {
                int diceRoll = charService.atkRoll();
                int enemyDef = charService.enemyDef(enemyModelOptional.get());
                if (diceRoll >= enemyDef) {
                    int atkDmg = itemsService.diceDmg(itemsModelOptional.get());
                    charService.dmgTaken(enemyModelOptional.get(), atkDmg);
                    ResponseEntity.status(HttpStatus.OK).body(charService.save(enemyModelOptional.get()));
                    return ResponseEntity.status(HttpStatus.OK).body("Teste de Ataque: " + diceRoll + "\nDefesa do Inimigo: " +
                            enemyDef + "\nDano do ataque: " + atkDmg + "\nVida do Inimigo: " + enemyModelOptional.get().getHp());
                }
                else {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Teste de Ataque: " + diceRoll + "\nDefesa do Inimigo: " +
                            enemyDef + "\nAttack does not hit.");
                }
            }
        }
        //SE O ITEM ESCOLHIDO FOR RANGED
        else {
            if (!classeService.isMelee(charModelOptional.get().getClasse())){
                int diceRoll = charService.atkRoll() + charModelOptional.get().getLevel();
                int enemyDef = charService.enemyDef(enemyModelOptional.get());
                if (diceRoll >= enemyDef) {
                    int atkDmg = itemsService.diceDmg(itemsModelOptional.get()) + charModelOptional.get().getLevel();
                    charService.dmgTaken(enemyModelOptional.get(), atkDmg);
                    ResponseEntity.status(HttpStatus.OK).body(charService.save(enemyModelOptional.get()));
                    return ResponseEntity.status(HttpStatus.OK).body("Teste de Ataque: " + diceRoll + "\nDefesa do Inimigo: " +
                            enemyDef + "\nDano do ataque: " + atkDmg + "\nVida do Inimigo: " + enemyModelOptional.get().getHp());
                } else {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Teste de Ataque: " + diceRoll + "\nDefesa do Inimigo: " +
                            enemyDef + "\nAttack does not hit.");
                }
            }
            else{
                int diceRoll = charService.atkRoll();
                int enemyDef = charService.enemyDef(enemyModelOptional.get());
                if (diceRoll >= enemyDef) {
                    int atkDmg = itemsService.diceDmg(itemsModelOptional.get());
                    charService.dmgTaken(enemyModelOptional.get(), atkDmg);
                    ResponseEntity.status(HttpStatus.OK).body(charService.save(enemyModelOptional.get()));
                    return ResponseEntity.status(HttpStatus.OK).body("Teste de Ataque: " + diceRoll + "\nDefesa do Inimigo: " +
                            enemyDef + "\nDano do ataque: " + atkDmg + "\nVida do Inimigo: " + enemyModelOptional.get().getHp());
                }
                else {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Teste de Ataque: " + diceRoll + "\nDefesa do Inimigo: " +
                            enemyDef + "\nAttack does not hit.");
                }
            }
        }
    }

    @PutMapping("/{id}/revive")
    ResponseEntity<Object> revive(@PathVariable(value = "id") Long id){
        Optional<Char> charModelOptional = charService.findById(id);
        if(charModelOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Character not found.");
        if(classeService.hasNoRace(charModelOptional.get()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Char must have a Race to be revived");

        int hpPoints = raceService.hpConfigure(charModelOptional.get().getRace().getHpDice(), charModelOptional.get());
        charModelOptional.get().setHp(hpPoints);
        return ResponseEntity.status(HttpStatus.OK).body(charService.save(charModelOptional.get()));
    }
}

