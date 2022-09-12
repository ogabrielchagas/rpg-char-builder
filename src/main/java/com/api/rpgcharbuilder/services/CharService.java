package com.api.rpgcharbuilder.services;

import com.api.rpgcharbuilder.domains.Char;
import com.api.rpgcharbuilder.repositories.CharRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class CharService {
    private final CharRepository charRepository;

    public CharService(CharRepository charRepository) {
        this.charRepository = charRepository;
    }

    public boolean existsByCharName(String charName){return charRepository.existsByCharName(charName);}

    @Transactional
    public Char save(Char charModel){
        return charRepository.save(charModel);
    }

    public Page<Char> findAll(Pageable pageable){return charRepository.findAll(pageable);}

    public Optional<Char> findById(Long id){return charRepository.findById(id);}

    @Transactional
    public void delete(Char charModel){charRepository.delete(charModel);}
}
