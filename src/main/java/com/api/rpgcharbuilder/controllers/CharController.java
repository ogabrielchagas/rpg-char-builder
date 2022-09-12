package com.api.rpgcharbuilder.controllers;

import com.api.rpgcharbuilder.domains.Char;
import com.api.rpgcharbuilder.services.CharService;
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

    public CharController(CharService charService) {
        this.charService = charService;
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

    @GetMapping("/{id}/items")
    ResponseEntity<Object> getItems(@PathVariable(value = "id") Long id){
        Optional<Char> charModelOptional = charService.findById(id);
        if(charModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Character not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(charModelOptional.get().getItems());
    }

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


}
