package com.api.rpgcharbuilder.controllers;

import com.api.rpgcharbuilder.domains.Classe;
import com.api.rpgcharbuilder.services.ClasseService;
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
@RequestMapping("/api/v1/classe")
@Api(tags = {"Classe Controller"})
public class ClasseController {
    private final ClasseService classeService;


    public ClasseController(ClasseService classeService) {
        this.classeService = classeService;
    }

    @ApiOperation(value = "Retorna uma lista com paginação das classes de RPG cadastradas", notes = "Jogadores vão escolher a classe para o seu" +
            " personagem dessa lista que deve ser previamente cadastrada")
    @GetMapping
    public ResponseEntity<Page<Classe>> getAll(@PageableDefault(sort = "id",
            direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(classeService.findAll(pageable));
    }

    @ApiOperation(value = "Retorna uma classe das classes de RPG cadastradas através de uma busca por ID", notes = "Jogadores vão escolher a classe para o seu" +
            " personagem através de uma lista que carregará este ID da classe para atribuir ao personagem.")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(@PathVariable(value = "id") Long id){
        Optional<Classe> classeModelOptional = classeService.findById(id);

        if(classeModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Classe not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(classeModelOptional.get());
    }

    @ApiOperation(value = "Retorna uma classe das clases de RPG cadastradas através de uma busca por Nome", notes = "Endpoint mapeado como opção" +
            " de filtragem das classes por nome.")
    @GetMapping("/findbyname/{name}")
    public ResponseEntity<Object> getOneByName(@PathVariable(value = "name") String classeName){
        Optional<Classe> classeModelOptional = classeService.findByClasseName(classeName);
        if(classeModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Classe not Found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(classeModelOptional.get());
    }

    @ApiOperation(value = "Cria uma nova Classe de RPG", notes = "Endpoint mapeado para o organizador do jogo de RPG (Mestre)" +
            " adicionar novas opções de escolha de classes a seu jogo como Guerreiros, Magos e Arqueiros.")
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody Classe classeModel){
        if(classeService.existsByClasseName(classeModel.getClasseName())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Classe already registered");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(classeService.save(classeModel));
    }

    @ApiOperation(value = "Deleta uma Classe de RPG previamente cadastrada", notes = "Endpoint mapeado para o organizador do jogo de RPG (Mestre)" +
            " deletar opções de escolha de classes do seu jogo.")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") Long id){
        Optional<Classe> roleModelOptional = classeService.findById(id);

        if(roleModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Classe not found.");
        }

        classeService.delete(roleModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Classe deleted successfully");
    }

    @ApiOperation(value = "Atualiza informações de uma Classe de RPG previamente cadastrada", notes = "Endpoint mapeado para o organizador do jogo de RPG (Mestre)" +
            " atualizar uma opção de escolha de classe do seu jogo.")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") Long id,
                                         @RequestBody Classe classeModel){
        Optional<Classe> classeModelOptional = classeService.findById(id);
        if(classeModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Classe not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(classeService.save(classeModel));
    }
}
