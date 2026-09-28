package com.escrowlite.escrowlite.repository;

import com.escrowlite.escrowlite.model.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
}