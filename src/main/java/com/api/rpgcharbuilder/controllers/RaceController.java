package com.api.rpgcharbuilder.controllers;

import com.api.rpgcharbuilder.domains.Race;
import com.api.rpgcharbuilder.services.RaceService;
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

@Api(tags = {"Race Controller"})
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/races")
public class RaceController {
    private final RaceService raceService;

    public RaceController(RaceService raceService) {
        this.raceService = raceService;
    }

    @ApiOperation(value = "Retorna uma lista com paginação das raças de RPG cadastradas", notes = "Jogadores vão escolher a raça para o seu" +
            " personagem dessa lista que deve ser previamente cadastrada")
    @GetMapping
    public ResponseEntity<Page<Race>> getAll(@PageableDefault(sort = "id",
            direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(raceService.findAll(pageable));
    }

    @ApiOperation(value = "Retorna uma raça das raças de RPG cadastradas através de uma busca por ID", notes = "Jogadores vão escolher a raça para o seu" +
            " personagem através de uma lista que carregará este ID da raça para atribuir ao personagem.")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(@PathVariable(value = "id") Long id){
        Optional<Race> raceModelOptional = raceService.findById(id);
        if(raceModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Race not Found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(raceModelOptional.get());
    }

    @ApiOperation(value = "Cria uma nova Raça de RPG", notes = "Endpoint mapeado para o organizador do jogo de RPG (Mestre)" +
            " adicionar novas opções de escolha de raças a seu jogo como Humanos, Anões e Elfos.")
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody Race raceModel){
        if(raceService.existsByRaceName(raceModel.getRaceName())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Race already registered");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(raceService.save(raceModel));
    }
    @ApiOperation(value = "Deleta uma Raça de RPG previamente cadastrada", notes = "Endpoint mapeado para o organizador do jogo de RPG (Mestre)" +
            " deletar opções de escolha de raças do seu jogo")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") Long id){
        Optional<Race> raceModelOptional = raceService.findById(id);
        if(raceModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Race not found.");
        }
        raceService.delete(raceModelOptional.get());
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Atualiza informações de uma Raça de RPG previamente cadastrada", notes = "Endpoint mapeado para o organizador do jogo de RPG (Mestre)" +
            " atualizar uma opção de escolha de raça do seu jogo")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") Long id,
                                         @RequestBody Race raceModel){
        Optional<Race> raceModelOptional = raceService.findById(id);
        if(raceModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Race not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(raceService.save(raceModel));
    }
}
