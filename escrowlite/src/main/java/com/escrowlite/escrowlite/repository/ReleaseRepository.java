package com.escrowlite.escrowlite.repository;

import com.escrowlite.escrowlite.model.Milestone;
import com.escrowlite.escrowlite.model.Release;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ReleaseRepository extends JpaRepository<Release, Long> {

    boolean existsByMilestone(Milestone milestone);

    @Query("SELECT COALESCE(SUM(r.amountReleased), 0) FROM Release r WHERE r.project.id = :projectId")
    BigDecimal findTotalReleasedAmountByProjectId(@Param("projectId") Long projectId);
}