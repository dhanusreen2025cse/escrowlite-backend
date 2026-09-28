package com.escrowlite.escrowlite.repository;

import com.escrowlite.escrowlite.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}