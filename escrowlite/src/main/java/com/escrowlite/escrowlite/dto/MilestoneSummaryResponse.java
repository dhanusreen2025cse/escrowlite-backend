package com.escrowlite.escrowlite.dto;

import com.escrowlite.escrowlite.model.MilestoneStatus;

import java.math.BigDecimal;

public record MilestoneSummaryResponse(
        Long milestoneId,
        String title,
        BigDecimal amount,
        MilestoneStatus status
) {
}