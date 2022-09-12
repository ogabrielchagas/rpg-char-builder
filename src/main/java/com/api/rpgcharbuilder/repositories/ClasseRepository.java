package com.api.rpgcharbuilder.repositories;

import com.api.rpgcharbuilder.domains.Classe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long> {

    boolean existsByClasseName(String classeName);
}
