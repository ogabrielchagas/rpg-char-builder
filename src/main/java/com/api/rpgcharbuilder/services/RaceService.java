package com.api.rpgcharbuilder.services;

import com.api.rpgcharbuilder.domains.Race;
import com.api.rpgcharbuilder.repositories.RaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class RaceService {

    private final RaceRepository raceRepository;

    public RaceService(RaceRepository raceRepository) {
        this.raceRepository = raceRepository;
    }

    public boolean existsByRaceName(String raceName){return raceRepository.existsByRaceName(raceName);}

    @Transactional
    public Race save(Race raceModel){return raceRepository.save(raceModel);}

    public Page<Race> findAll(Pageable pageable){return raceRepository.findAll(pageable);}

    public Optional<Race> findById(Long id){return raceRepository.findById(id);}

    @Transactional
    public void delete(Race raceModel){raceRepository.delete(raceModel);}
}
