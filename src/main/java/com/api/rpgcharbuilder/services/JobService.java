package com.api.rpgcharbuilder.services;

import com.api.rpgcharbuilder.domains.Char;
import com.api.rpgcharbuilder.domains.Job;
import com.api.rpgcharbuilder.domains.CombatType;
import com.api.rpgcharbuilder.repositories.JobRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class JobService {
    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public boolean existsByJobName(String jobName){return jobRepository.existsByJobName(jobName);}

    @Transactional
    public Job save(Job jobModel){return jobRepository.save(jobModel);}

    public Page<Job> findAll(Pageable pageable){return jobRepository.findAll(pageable);}

    public Optional<Job> findById(Long id){return jobRepository.findById(id);}

    @Transactional
    public void delete(Job jobModel){
        jobRepository.delete(jobModel);}

    public boolean hasNoRace(Char aChar) {
        if (aChar.getRace() == null)
                return true;
            return false;
    }

    public boolean isMelee(Job aJob) {
        if(aJob.getCombatType() == CombatType.MELEE)
            return true;
        return false;
    }

}
