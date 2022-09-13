package com.api.rpgcharbuilder.controllers;

import com.api.rpgcharbuilder.domains.Classe;
import com.api.rpgcharbuilder.services.ClasseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@RestController
@RequestMapping("/classe")
public class ClasseController {
    private final ClasseService classeService;


    public ClasseController(ClasseService classeService) {
        this.classeService = classeService;
    }

    @GetMapping
    public ResponseEntity<Page<Classe>> getAll(@PageableDefault(sort = "id",
            direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(classeService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(@PathVariable(value = "id") Long id){
        Optional<Classe> classeModelOptional = classeService.findById(id);
        if(classeModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Classe not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(classeModelOptional.get());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody Classe classeModel){
        if(classeService.existsByClasseName(classeModel.getClasseName())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Classe already registered");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(classeService.save(classeModel));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") Long id){
        Optional<Classe> roleModelOptional = classeService.findById(id);
        if(roleModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Classe not found.");
        }

        classeService.delete(roleModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Classe deleted successfully");
    }

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
