package com.api.rpgcharbuilder.services;

import com.api.rpgcharbuilder.domains.Char;
import com.api.rpgcharbuilder.domains.Classe;
import com.api.rpgcharbuilder.domains.CombatType;
import com.api.rpgcharbuilder.repositories.ClasseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ClasseService {
    private final ClasseRepository classeRepository;

    public ClasseService(ClasseRepository classeRepository) {
        this.classeRepository = classeRepository;
    }

    public boolean existsByClasseName(String classeName){return classeRepository.existsByClasseName(classeName);}

    @Transactional
    public Classe save(Classe classeModel){return classeRepository.save(classeModel);}

    public Page<Classe> findAll(Pageable pageable){return classeRepository.findAll(pageable);}

    public Optional<Classe> findById(Long id){return classeRepository.findById(id);}

    public Optional<Classe> findByClasseName(String classeName){return classeRepository.findByClasseName(classeName);}

    @Transactional
    public void delete(Classe classeModel){classeRepository.delete(classeModel);}

    public boolean hasNoRace(Char aChar) {
        if (aChar.getRace() == null)
                return true;
            return false;
    }

    public boolean isMelee(Classe aClasse) {
        if(aClasse.getCombatType() == CombatType.MELEE)
            return true;
        return false;
    }

}
