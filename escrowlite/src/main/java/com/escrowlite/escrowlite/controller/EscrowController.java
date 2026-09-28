package com.escrowlite.escrowlite.controller;

import com.escrowlite.escrowlite.dto.CreateProjectRequest;
import com.escrowlite.escrowlite.dto.ProjectSummaryResponse;
import com.escrowlite.escrowlite.dto.ReworkRequest;
import com.escrowlite.escrowlite.model.Milestone;
import com.escrowlite.escrowlite.model.Project;
import com.escrowlite.escrowlite.model.Release;
import com.escrowlite.escrowlite.service.EscrowService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EscrowController {

    private final EscrowService escrowService;

    public EscrowController(EscrowService escrowService) {
        this.escrowService = escrowService;
    }

    @PostMapping("/projects")
    @ResponseStatus(HttpStatus.CREATED)
    public Project createProject(@Valid @RequestBody CreateProjectRequest request) {
        return escrowService.createProject(request);
    }

    @PutMapping("/milestones/{id}/deliver")
    public Milestone deliverMilestone(@PathVariable Long id) {
        return escrowService.deliverMilestone(id);
    }

    @PutMapping("/milestones/{id}/approve")
    public Milestone approveMilestone(@PathVariable Long id) {
        return escrowService.approveMilestone(id);
    }

    @PutMapping("/milestones/{id}/rework")
    public Milestone requestRework(@PathVariable Long id, @Valid @RequestBody ReworkRequest request) {
        return escrowService.requestRework(id, request);
    }

    @PostMapping("/milestones/{id}/release")
    @ResponseStatus(HttpStatus.CREATED)
    public Release releaseMilestonePayment(@PathVariable Long id) {
        return escrowService.releaseMilestonePayment(id);
    }

    @GetMapping("/projects/{id}/summary")
    public ProjectSummaryResponse getProjectSummary(@PathVariable Long id) {
        return escrowService.getProjectSummary(id);
    }
}
