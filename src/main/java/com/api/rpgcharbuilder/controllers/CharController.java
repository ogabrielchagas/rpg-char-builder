package com.api.rpgcharbuilder.controllers;

import com.api.rpgcharbuilder.domains.Char;
import com.api.rpgcharbuilder.domains.Job;
import com.api.rpgcharbuilder.domains.Items;
import com.api.rpgcharbuilder.domains.Race;
import com.api.rpgcharbuilder.dtos.CharDto;
import com.api.rpgcharbuilder.services.CharService;
import com.api.rpgcharbuilder.services.JobService;
import com.api.rpgcharbuilder.services.ItemsService;
import com.api.rpgcharbuilder.services.RaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = {"Characters Controller"})
public class CharController {
    private final CharService charService;
    private final RaceService raceService;

    private final JobService jobService;

    private final ItemsService itemsService;

    public CharController(CharService charService, RaceService raceService, JobService jobService, ItemsService itemsService) {
        this.charService = charService;
        this.raceService = raceService;
        this.jobService = jobService;
        this.itemsService = itemsService;
    }

    @ApiOperation(value = "Retorna uma lista com paginação dos personagens do RPG cadastrados", notes = "Endpoint mapeado para o organizador" +
            " do jogo(Mestre) poder ver todos os seus personagens e os personagens dos jogadores cadastrados.")
    @GetMapping
    public ResponseEntity<Page<Char>> getAll(@PageableDefault(sort = "id",
            direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(charService.findAll(pageable));
    }

    @ApiOperation(value = "Retorna um personagem dos personagens de RPG cadastrados através de uma busca por ID", notes = "Jogadores ou o Mestre" +
            " selecionam de uma lista de personagens pré cadastrados que carregará o ID do personagem ao qual desejam procurar e ver mais informações.")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(@PathVariable(value = "id") Long id){
        Optional<Char> charModelOptional = charService.findById(id);
        if(charModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Character not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(charModelOptional.get());
    }

    @ApiOperation(value = "Retorna uma lista dos itens que um personagem buscado por seu ID possui", notes = "Jogadores ou o Mestre" +
            "selecionam de uma lista de personagens pré cadastrados que carregará o ID e retorna a lista dos itens do personagem associado a esse ID.")
    @GetMapping("/{id}/items")
    ResponseEntity<Object> getItems(@PathVariable(value = "id") Long id){
        Optional<Char> charModelOptional = charService.findById(id);
        if(charModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Character not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(charModelOptional.get().getItems());
    }

    @ApiOperation(value = "Cria um nova Personagem(Char) de RPG", notes = "Endpoint mapeado para o organizador do jogo de RPG (Mestre)" +
            " adicionar novos personagens ao seu jogo (NPCs) que não serão personagens dos jogadores e portanto são criados em um endpoint" +
            " diferente da criação de personagens para Jogadores.")
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody Char charModel){
        if(charService.existsByCharName(charModel.getCharName()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Character already registered");
        return ResponseEntity.status(HttpStatus.CREATED).body(charService.save(charModel));
    }

    @ApiOperation(value = "Deleta um Personagem de RPG previamente cadastrado", notes = "Endpoint mapeado para o organizador do jogo de RPG (Mestre)" +
            " deletar personagens dos jogadores ou NPCs do seu jogo.")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") Long id){
        Optional<Char> charModelOptional = charService.findById(id);
        if(charModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Character not found.");
        }
        charService.delete(charModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Character deleted successfully");
    }

    @ApiOperation(value = "Atualiza informações de um Personagem de RPG do Mestre (NPC) previamente cadastrado", notes = "Endpoint mapeado para o organizador do jogo de RPG (Mestre)" +
            " atualizar seus personagens (NPCs) previamente criados, personagens dos Jogadores não devem ser atualizados neste endpoint")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") Long id,
                                         @RequestBody Char charModel){
        Optional<Char> charModelOptional = charService.findById(id);
        if(charModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Character not found.");
        }


        return ResponseEntity.status(HttpStatus.OK).body(charService.save(charModel));
    }

    @ApiOperation(value = "Cria um novo Personagem do Jogador", notes = "Endpoint mapeado para os jogadores criarem seus" +
            " personagens bastando dar um nome a ele, recebendo automaticamente Level 1 e 10 Moedas.")
    @PostMapping("/newchar")
    public ResponseEntity<Object> save(@RequestBody CharDto charDto){
        if(charService.existsByCharName(charDto.getCharName()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Character already registered");
        var charModel = new Char();
        BeanUtils.copyProperties(charDto, charModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(charService.save(charModel));
    }

    @ApiOperation(value = "Atualiza apenas o nome de um Personagem do Jogador", notes = "Endpoint mapeado para os Jogadores)" +
            " alterarem o nome de seus personagens.")
    @PutMapping(value = "/{id}/changename")
    public ResponseEntity<Object> update(@PathVariable(value = "id") Long id,
                                         @RequestBody CharDto charDto){
        Optional<Char> charModelOptional = charService.findById(id);
        if(charModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Character not found.");
        }
        charModelOptional.get().setCharName(charDto.getCharName());
        return ResponseEntity.status(HttpStatus.OK).body(charService.save(charModelOptional.get()));
    }

    @ApiOperation(value = "Associa uma Raça de RPG previamente cadastrada a um Personagem do Jogador", notes = "Endpoint mapeado para os Jogadores" +
            " escolherem de uma lista de raças previamente cadastradas que associará o ID da opção de raça para o seu personagem através do ID do mesmo.\n\n" +
            "Após escolher a Raça o HP é configurado baseado no Dado de HP da Raça e no Level do Personagem.")
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
        int hpPoints = charService.hpConfigure(raceModelOptional.get().getHpDice(), charModel);
        charModel.setHp(hpPoints);
        return ResponseEntity.status(HttpStatus.CREATED).body(charService.save(charModel));
    }

    @ApiOperation(value = "Associa uma Classe de RPG previamente cadastrada a um Personagem do Jogador", notes = "Endpoint mapeado para os Jogadores" +
            " escolherem de uma lista de classes previamente cadastradas que associará o ID da opção de classe para o seu personagem através do ID do mesmo.\n\n" +
            " Para escolher uma Classe deve-se primeiro escolher a Raça.")
    @PutMapping(value = "/{charid}/addclasse/{classeid}")
    ResponseEntity<Object> addExistingRoleToChar(@PathVariable(value = "charid")Long charId,
                                                 @PathVariable(value = "classeid")Long classeId){
        Optional<Char> charModelOptional = charService.findById(charId);
        Optional<Job> classeModelOptional = jobService.findById(classeId);
        if(charModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Character not found.");
        }
        if(classeModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Classe not found.");
        }
        if(jobService.hasNoRace(charModelOptional.get()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Char must have a Race before choosing a Classe");

        Char charModel = charModelOptional.get();
        charModel.setClasse(classeModelOptional.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(charService.save(charModel));
    }

    @ApiOperation(value = "Associa um Item do RPG previamente cadastrado a um Personagem do Jogador", notes = "Endpoint mapeado para os Jogadores" +
            " escolherem de uma lista de itens previamente cadastrados que associará o ID da opção de item para o seu personagem através do ID do mesmo.\n\n" +
            " Para escolher um Item deve-se primeiro escolher a Classe.\n\n" +
            " O Personagem deve ter o Level necessário para equipar.")

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

    @ApiOperation(value = "Combate entre dois Personagens(NPCs ou Personagens dos Jogadores)", notes = "Endpoint mapeado para um combate" +
            " entre dois personagens(NPCs ou Personagens dos Jogadores), O jogador que seleciona a opção de ataque carregá seu ID para o endpoint" +
            " seleciona um de seus itens para atacar que carrega este ID do item para o endpoint e por fim seleciona quem atacará carregando o ID desse inimigo" +
            "(enemy) para o endpoint.\n\n" +
            " O Vencedor sobe 1 Level e fica com alguns itens e todo as moedas do Inimigo derrotado.")
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
        if(charService.isCombatOver(charModelOptional.get(), enemyModelOptional.get()))
            return ResponseEntity.status(HttpStatus.OK).body("You Won!!\n Now you are Level " + charModelOptional.get().getLevel()
                    + "\nYou get the money and items from your enemy");

        //SE O ITEM ESCOLHIDO FOR DO TIPO MELEE
        if(itemsService.isMelee(itemsModelOptional.get())) {
            if (jobService.isMelee(charModelOptional.get().getClasse())){
                int dmg = charService.CombatWithBonus(charModelOptional.get(), enemyModelOptional.get(), itemsModelOptional.get());
                if (dmg == 0)
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Attack does not hit.");
                else
                    return ResponseEntity.status(HttpStatus.OK).body("Dano do ataque: " + dmg + "\nVida do Inimigo: " +
                            enemyModelOptional.get().getHp());
            }
            else{
                int dmg = charService.CombatWithoutBonus(charModelOptional.get(), enemyModelOptional.get(), itemsModelOptional.get());
                if (dmg == 0)
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Attack does not hit.");
                else
                    return ResponseEntity.status(HttpStatus.OK).body("Dano do ataque: " + dmg + "\nVida do Inimigo: "
                            + enemyModelOptional.get().getHp());
            }
        }
        //SE O ITEM ESCOLHIDO FOR RANGED
        else {
            if (!jobService.isMelee(charModelOptional.get().getClasse())) {
                int dmg = charService.CombatWithBonus(charModelOptional.get(), enemyModelOptional.get(), itemsModelOptional.get());
                if (dmg == 0)
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Attack does not hit.");
                else
                    return ResponseEntity.status(HttpStatus.OK).body("Dano do ataque: " + dmg + "\nVida do Inimigo: "
                            + enemyModelOptional.get().getHp());
            } else {
                int dmg = charService.CombatWithoutBonus(charModelOptional.get(), enemyModelOptional.get(), itemsModelOptional.get());
                if (dmg == 0)
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Attack does not hit.");
                else
                    return ResponseEntity.status(HttpStatus.OK).body("Dano do ataque: " + dmg + "\nVida do Inimigo: "
                            + enemyModelOptional.get().getHp());
            }
        }
    }

    @ApiOperation(value = "Reconfigura (Revive) a Vida de um Personagem baseado no seu Nível", notes = "Endpoint mapeado para os Jogadores ou Mestre" +
            " reviverem personagens que estiverem com HP negativa, o personagem será escolhido de uma lista dos personagens com HP negativa" +
            " e ao ser escolhido carregará o seu ID para o endpoint.")
    @PutMapping("/{id}/revive")
    ResponseEntity<Object> revive(@PathVariable(value = "id") Long id){
        Optional<Char> charModelOptional = charService.findById(id);
        if(charModelOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Character not found.");
        if(jobService.hasNoRace(charModelOptional.get()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Char must have a Race to be revived");

        int hpPoints = charService.hpConfigure(charModelOptional.get().getRace().getHpDice(), charModelOptional.get());
        charModelOptional.get().setHp(hpPoints);
        return ResponseEntity.status(HttpStatus.OK).body(charService.save(charModelOptional.get()));
    }
}

