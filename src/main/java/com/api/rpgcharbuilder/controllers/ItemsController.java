package com.api.rpgcharbuilder.controllers;

import com.api.rpgcharbuilder.domains.Items;
import com.api.rpgcharbuilder.services.ItemsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Api(tags = {"Items Controller"})
@RequestMapping("/api/v1/items")
public class ItemsController {
    private final ItemsService itemsService;

    public ItemsController(ItemsService itemsService) {
        this.itemsService = itemsService;
    }

    @GetMapping
    @ApiOperation(value = "Retorna uma lista com paginação dos itens do RPG cadastrados", notes = "Jogadores vão escolher itens para o" +
            " personagem dessa lista que deve ser previamente cadastrada.")
    public ResponseEntity<Page<Items>> getAll(@PageableDefault(sort = "id",
            direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(itemsService.findAll(pageable));
    }

    @ApiOperation(value = "Retorna um item dos itens do RPG cadastrados através de uma busca por ID", notes = "Jogadores vão escolher um item para o seu" +
            " personagem através de uma lista que carregará este ID da classe para atribuir ao personagem.")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(@PathVariable(value = "id") Long id){
        Optional<Items> itemsModelOptional = itemsService.findById(id);
        if(itemsModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(itemsModelOptional.get());
    }

    @ApiOperation(value = "Cria um novo Item do RPG", notes = "Endpoint mapeado para o organizador do jogo de RPG (Mestre)" +
            " adicionar novas opções de escolha de itens a seu jogo como Espadas, Arcos e Cajados.")
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody Items itemsModel){
        if(itemsService.existsByItemName(itemsModel.getItemName())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Item already registered");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(itemsService.save(itemsModel));
    }

    @ApiOperation(value = "Deleta um Item do RPG previamente cadastrado", notes = "Endpoint mapeado para o organizador do jogo de RPG (Mestre)" +
            " deletar opções de escolha de itens do seu jogo.")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") Long id){
        Optional<Items> itemsModelOptional = itemsService.findById(id);
        if(itemsModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found.");
        }
        itemsService.delete(itemsModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Item deleted successfully");
    }

    @ApiOperation(value = "Atualiza informações de um Item de RPG previamente cadastrado", notes = "Endpoint mapeado para o organizador do jogo de RPG (Mestre)" +
            " atualizar uma opção de escolha de item do seu jogo.")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") Long id,
                                         @RequestBody Items itemsModel){
        Optional<Items> itemsModelOptional = itemsService.findById(id);
        if(itemsModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(itemsService.save(itemsModel));
    }
}
