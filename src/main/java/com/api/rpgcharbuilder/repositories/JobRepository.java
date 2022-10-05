package com.api.rpgcharbuilder.repositories;

import com.api.rpgcharbuilder.domains.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    boolean existsByJobName(String jobName);
}
