package com.api.rpgcharbuilder.repositories;

import com.api.rpgcharbuilder.domains.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RaceRepository extends JpaRepository<Race, Long> {
    boolean existsByRaceName(String raceName);
}
