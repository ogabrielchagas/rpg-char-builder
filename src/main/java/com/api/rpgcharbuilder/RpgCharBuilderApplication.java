package com.api.rpgcharbuilder;

import com.api.rpgcharbuilder.domains.*;
import com.api.rpgcharbuilder.repositories.ItemsRepository;
import com.api.rpgcharbuilder.repositories.JobRepository;
import com.api.rpgcharbuilder.repositories.RaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class RpgCharBuilderApplication implements CommandLineRunner {


    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private ItemsRepository itemsRepository;
    public static void main(String[] args) {
        SpringApplication.run(RpgCharBuilderApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Job job1 = new Job(null, "Guerreiro", CombatType.MELEE);
        Job job2 = new Job(null, "Arqueiro", CombatType.RANGED);
        Job job3 = new Job(null, "Mago", CombatType.RANGED);

        Race race1 = new Race(null, "Humano", Dice.D10);
        Race race2 = new Race(null, "Anão", Dice.D12);
        Race race3 = new Race(null, "Elfo", Dice.D8);

        Items item1 = new Items(null, CombatType.MELEE, "Espada", 1, Dice.D8);
        Items item2 = new Items(null, CombatType.RANGED, "Arco", 1, Dice.D10);
        Items item3 = new Items(null, CombatType.MELEE, "Varinha", 2, Dice.D12);

        this.jobRepository.saveAll(Arrays.asList(job1, job2, job3));
        this.raceRepository.saveAll(Arrays.asList(race1, race2, race3));
        this.itemsRepository.saveAll(Arrays.asList(item1, item2, item3));
    }
}
