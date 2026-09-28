package com.escrowlite.escrowlite.repository;

import com.escrowlite.escrowlite.model.Freelancer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreelancerRepository extends JpaRepository<Freelancer, Long> {
}