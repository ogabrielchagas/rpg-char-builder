package com.api.rpgcharbuilder.repositories;

import com.api.rpgcharbuilder.domains.Char;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharRepository extends JpaRepository<Char, Long> {

    boolean existsByCharName(String charName);

    Optional<Char> findByCharName(String charName);
}
