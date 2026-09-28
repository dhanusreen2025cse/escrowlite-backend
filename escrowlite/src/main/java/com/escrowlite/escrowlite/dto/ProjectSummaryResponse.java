package com.escrowlite.escrowlite.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProjectSummaryResponse(
        Long projectId,
        String title,
        String clientName,
        String freelancerName,
        BigDecimal totalAgreedAmount,
        BigDecimal totalReleasedAmount,
        BigDecimal remainingEscrowBalance,
        List<MilestoneSummaryResponse> milestones
) {
}