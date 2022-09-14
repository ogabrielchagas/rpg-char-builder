package com.api.rpgcharbuilder.controllers;

import com.api.rpgcharbuilder.domains.Race;
import com.api.rpgcharbuilder.services.RaceService;
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
@RequestMapping("/race")
public class RaceController {
    private final RaceService raceService;

    public RaceController(RaceService raceService) {
        this.raceService = raceService;
    }

    @GetMapping
    public ResponseEntity<Page<Race>> getAll(@PageableDefault(sort = "id",
            direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(raceService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(@PathVariable(value = "id") Long id){
        Optional<Race> raceModelOptional = raceService.findById(id);
        if(raceModelOptional.isEmpty()){
            throw new EntityNotFoundException("Race not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(raceModelOptional.get());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody Race raceModel){
        if(raceService.existsByRaceName(raceModel.getRaceName())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Race already registered");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(raceService.save(raceModel));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") Long id){
        Optional<Race> raceModelOptional = raceService.findById(id);
        if(raceModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Race not found.");
        }
        raceService.delete(raceModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Race deleted successfully");
    }

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
