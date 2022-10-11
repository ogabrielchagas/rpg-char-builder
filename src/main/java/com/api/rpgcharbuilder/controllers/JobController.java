package com.api.rpgcharbuilder.controllers;

import com.api.rpgcharbuilder.domains.Job;
import com.api.rpgcharbuilder.services.JobService;
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
@RequestMapping("/jobs")
@CrossOrigin(origins = "http://localhost:4200")
@Api(tags = {"Job Controller"})
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @ApiOperation(value = "Retorna uma lista com paginação das classes/jobs de RPG cadastradas", notes = "Jogadores vão escolher a classe/job para o seu" +
            " personagem dessa lista que deve ser previamente cadastrada")
    @GetMapping
    public ResponseEntity<Page<Job>> getAll(@PageableDefault(sort = "id",
            direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(jobService.findAll(pageable));
    }

    @ApiOperation(value = "Retorna uma classe/job das classes/jobs de RPG cadastradas através de uma busca por ID", notes = "Jogadores vão escolher a classe/job para o seu" +
            " personagem através de uma lista que carregará este ID da classe/job para atribuir ao personagem.")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(@PathVariable(value = "id") Long id){
        Optional<Job> jobModelOptional = jobService.findById(id);

        if(jobModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(jobModelOptional.get());
    }

    @ApiOperation(value = "Cria uma nova Classe/Job de RPG", notes = "Endpoint mapeado para o organizador do jogo de RPG (Mestre)" +
            " adicionar novas opções de escolha de classes/jobs a seu jogo como Guerreiros, Magos e Arqueiros.")
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody Job jobModel){
        if(jobService.existsByJobName(jobModel.getJobName())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Job already registered");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.save(jobModel));
    }

    @ApiOperation(value = "Deleta uma Classe/Job de RPG previamente cadastrada", notes = "Endpoint mapeado para o organizador do jogo de RPG (Mestre)" +
            " deletar opções de escolha de classes/job do seu jogo.")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") Long id){
        Optional<Job> jobModelOptional = jobService.findById(id);

        if(jobModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found.");
        }

        jobService.delete(jobModelOptional.get());
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Atualiza informações de uma Classe/Job de RPG previamente cadastrada", notes = "Endpoint mapeado para o organizador do jogo de RPG (Mestre)" +
            " atualizar uma opção de escolha de classe/job do seu jogo.")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") Long id,
                                         @RequestBody Job jobModel){
        Optional<Job> jobModelOptional = jobService.findById(id);
        if(jobModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(jobService.save(jobModel));
    }
}
