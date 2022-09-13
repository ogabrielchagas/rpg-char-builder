package com.api.rpgcharbuilder.controllers;

import com.api.rpgcharbuilder.domains.Items;
import com.api.rpgcharbuilder.services.ItemsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/items")
public class ItemsController {
    private final ItemsService itemsService;

    public ItemsController(ItemsService itemsService) {
        this.itemsService = itemsService;
    }

    @GetMapping
    public ResponseEntity<Page<Items>> getAll(@PageableDefault(sort = "id",
            direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(itemsService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(@PathVariable(value = "id") Long id){
        Optional<Items> itemsModelOptional = itemsService.findById(id);
        if(itemsModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(itemsModelOptional.get());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody Items itemsModel){
        if(itemsService.existsByItemName(itemsModel.getItemName())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Item already registered");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(itemsService.save(itemsModel));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") Long id){
        Optional<Items> itemsModelOptional = itemsService.findById(id);
        if(itemsModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found.");
        }
        itemsService.delete(itemsModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Item deleted successfully");
    }

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
